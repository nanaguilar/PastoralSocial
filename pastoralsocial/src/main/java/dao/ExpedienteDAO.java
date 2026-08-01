package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Asistencia;
import model.Expediente;
import model.Persona;
import model.ProlongacionAyuda;
import util.JPAUtil;

public class ExpedienteDAO {

    // ------------------------------------------------- observación B del profe

    // el número de ficha se arma solo: año actual + consecutivo de 4 dígitos
    // (2026-0001, 2026-0002...). el usuario nunca lo escribe.
    // ojo: esto lo calculo DENTRO de la transacción del guardado, no antes, para
    // que pase el menor tiempo posible entre que pido el número y lo grabo.
    private String siguienteNumeroFicha(EntityManager em) {
        int anio = Calendar.getInstance().get(Calendar.YEAR);
        String prefijo = anio + "-";

        // en vez de MAX() ordeno al revés y me quedo con el primero, que en
        // Hibernate 3 me da menos problemas con columnas de texto
        List<String> ultimas = em.createQuery(
                "SELECT e.numeroFicha FROM Expediente e "
              + "WHERE e.numeroFicha LIKE :prefijo ORDER BY e.numeroFicha DESC",
                String.class)
            .setParameter("prefijo", prefijo + "%")
            .setMaxResults(1)
            .getResultList();

        int consecutivo = 1;
        if (!ultimas.isEmpty()) {
            consecutivo = numeroDeLaFicha(em, ultimas.get(0), prefijo) + 1;
        }

        // el %04d es para que quede 0001 y no 1
        return prefijo + String.format("%04d", consecutivo);
    }

    // saco el consecutivo de una ficha tipo "2026-0007".
    // si la base trae una ficha vieja escrita a mano que no sigue el formato,
    // el parseInt truena, así que en ese caso me voy por la cantidad de fichas
    // del año como respaldo y dejo que el UNIQUE me avise si choca.
    private int numeroDeLaFicha(EntityManager em, String ficha, String prefijo) {
        try {
            return Integer.parseInt(ficha.substring(prefijo.length()));
        } catch (RuntimeException e) {
            Long cuantas = em.createQuery(
                    "SELECT COUNT(e) FROM Expediente e WHERE e.numeroFicha LIKE :prefijo",
                    Long.class)
                .setParameter("prefijo", prefijo + "%")
                .getSingleResult();
            return cuantas.intValue();
        }
    }

    // ------------------------------------------------------------- guardar

    public void guardarNuevo(Expediente expediente, String usuario) {
        try {
            intentarGuardar(expediente, usuario);
        } catch (RuntimeException e) {
            // si dos personas guardan casi al mismo tiempo pueden pedir el mismo
            // número; el UNIQUE de numero_ficha lo rechaza. reintento una sola vez
            // pidiendo el número de nuevo. limpio el id porque después del rollback
            // puede haber quedado a medias.
            expediente.setId(null);
            expediente.setNumeroFicha(null);
            limpiarIdsDeHijos(expediente);
            intentarGuardar(expediente, usuario);
        }
    }

    // si el primer intento falló a medias, los hijos que iban en la cascada
    // pueden haber quedado con id asignado. si los dejo así, el persist del
    // reintento se queja de "detached entity", por eso los devuelvo a null.
    private void limpiarIdsDeHijos(Expediente expediente) {
        for (Persona p : expediente.getPersonas()) {
            p.setId(null);
        }
        for (Asistencia a : expediente.getAsistencias()) {
            a.setId(null);
        }
        for (ProlongacionAyuda pr : expediente.getProlongaciones()) {
            pr.setId(null);
        }
    }

    private void intentarGuardar(Expediente expediente, String usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            expediente.setNumeroFicha(siguienteNumeroFicha(em));
            expediente.setEstado(Expediente.ESTADO_ACTIVO);
            expediente.setCreadoPor(usuario);

            em.persist(expediente);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // al editar nunca toco el número de ficha ni el estado, solo los datos
    public void actualizar(Expediente expediente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(expediente);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // los casos que ya estaban en la base quedaron con estado en NULL cuando
    // Hibernate agregó la columna. si los dejo así, estaActivo() devuelve false
    // y salen todos como cerrados, sin poder editarlos ni darles asistencias.
    // los paso a ACTIVO una sola vez al arrancar.
    public void normalizarRegistrosViejos() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("UPDATE Expediente e SET e.estado = :activo WHERE e.estado IS NULL")
              .setParameter("activo", Expediente.ESTADO_ACTIVO)
              .executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------------- consultas

    public Expediente buscarPorId(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Expediente.class, id);
        } finally {
            em.close();
        }
    }

    // busca por ficha, parroquia o entrevistador y filtra por estado.
    // el texto y el estado pueden venir vacíos, ahí no filtro por eso.
    // uso LEFT JOIN en la parroquia porque si no, los casos que todavía no
    // tienen parroquia no saldrían nunca en la lista.
    public List<Expediente> buscar(String texto, String estado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                "SELECT e FROM Expediente e LEFT JOIN e.parroquia p");

            List<String> condiciones = new ArrayList<String>();
            boolean hayTexto = (texto != null && !texto.trim().isEmpty());
            boolean hayEstado = (estado != null && !estado.trim().isEmpty());

            if (hayTexto) {
                condiciones.add("(LOWER(e.numeroFicha) LIKE :texto "
                              + "OR LOWER(p.nombre) LIKE :texto "
                              + "OR LOWER(e.entrevistador) LIKE :texto)");
            }
            if (hayEstado) {
                condiciones.add("e.estado = :estado");
            }

            for (int i = 0; i < condiciones.size(); i++) {
                jpql.append(i == 0 ? " WHERE " : " AND ").append(condiciones.get(i));
            }
            jpql.append(" ORDER BY e.numeroFicha DESC");

            TypedQuery<Expediente> query = em.createQuery(jpql.toString(), Expediente.class);
            if (hayTexto) {
                query.setParameter("texto", "%" + texto.trim().toLowerCase() + "%");
            }
            if (hayEstado) {
                query.setParameter("estado", estado);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------- cerrar / reabrir

    // el cierre no borra nada, solo cambia el estado y deja constancia del motivo
    public void cerrar(Integer expedienteId, String motivo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Expediente e = em.find(Expediente.class, expedienteId);
            if (e != null) {
                e.setEstado(Expediente.ESTADO_CERRADO);
                e.setMotivoCierre(motivo);
                e.setFechaCierre(new Date());
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    // reabrir es el mismo caso de siempre, no uno nuevo, así no se duplica el
    // historial. guardo la justificación pero no borro el motivo de cierre.
    public void reabrir(Integer expedienteId, String justificacion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Expediente e = em.find(Expediente.class, expedienteId);
            if (e != null) {
                e.setEstado(Expediente.ESTADO_ACTIVO);
                e.setMotivoReapertura(justificacion);
                e.setFechaCierre(null);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
