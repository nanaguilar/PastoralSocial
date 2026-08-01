package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.Persona;
import util.JPAUtil;

public class PersonaDAO {

    // traigo activos e inactivos, pero pongo primero a los activos para que los
    // retirados queden al final de la tabla y no estorben
    public List<Persona> listarPorExpediente(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Persona> query = em.createQuery(
                "SELECT p FROM Persona p WHERE p.expediente.id = :exp "
              + "ORDER BY p.activo DESC, p.jefatura DESC, p.apellidosNombres",
                Persona.class);
            query.setParameter("exp", expedienteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // guardar y actualizar en el mismo método, según si ya tiene id o no.
    // acá también resuelvo la regla del jefe de hogar único.
    public void guardar(Persona persona) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Persona guardada;
            if (persona.getId() == null) {
                em.persist(persona);
                // necesito el flush para que la base le asigne el id, si no
                // el UPDATE de abajo no sabría a quién excluir
                em.flush();
                guardada = persona;
            } else {
                guardada = em.merge(persona);
            }

            // si este quedó como jefe de hogar, le quito la jefatura a todos los
            // demás del mismo caso. así nunca hay dos jefes al mismo tiempo.
            if (persona.isJefatura()) {
                em.createQuery(
                    "UPDATE Persona p SET p.jefatura = false "
                  + "WHERE p.expediente.id = :exp AND p.id <> :id")
                  .setParameter("exp", persona.getExpediente().getId())
                  .setParameter("id", guardada.getId())
                  .executeUpdate();
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // retirar o reactivar. es lógico, nunca borro la fila (HU-14).
    public void cambiarEstado(Integer personaId, boolean activo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Persona p = em.find(Persona.class, personaId);
            if (p != null) {
                p.setActivo(activo);
                if (!activo) {
                    // si lo retiro, deja de ser jefe de hogar; el caso queda sin
                    // jefe hasta que le asignen otro, y la pantalla avisa de eso
                    p.setJefatura(false);
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // los integrantes que ya estaban en la base quedaron con activo en NULL cuando
    // Hibernate agregó esa columna. como en Java es un boolean primitivo, leer un
    // NULL ahí truena con PropertyAccessException y no podría ni abrir el caso.
    // por eso los paso a true una vez al arrancar. es idempotente: la segunda vez
    // ya no hay ninguno en NULL y no hace nada.
    public void normalizarRegistrosViejos() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("UPDATE Persona p SET p.activo = true WHERE p.activo IS NULL")
              .executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public long contarActivos(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Persona p "
              + "WHERE p.expediente.id = :exp AND p.activo = true", Long.class);
            query.setParameter("exp", expedienteId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public long contarJefesActivos(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Persona p WHERE p.expediente.id = :exp "
              + "AND p.activo = true AND p.jefatura = true", Long.class);
            query.setParameter("exp", expedienteId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // sumo el ingreso de los integrantes activos para mostrar el total del
    // grupo familiar. si nadie tiene ingreso el SUM viene en null, por eso el 0.
    public double ingresoTotal(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                "SELECT SUM(p.ingresoMensual) FROM Persona p "
              + "WHERE p.expediente.id = :exp AND p.activo = true", Double.class);
            query.setParameter("exp", expedienteId);
            Double total = query.getSingleResult();
            return (total == null) ? 0 : total.doubleValue();
        } finally {
            em.close();
        }
    }
}
