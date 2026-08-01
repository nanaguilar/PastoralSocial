package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import model.Asistencia;
import model.TipoAsistencia;
import util.JPAUtil;

public class AsistenciaDAO {

    // los tipos salen de la tabla tipo_asistencia que ya venía en el proyecto.
    // le puse el método acá en vez de hacer un DAO aparte solo para listar.
    public List<TipoAsistencia> listarTipos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<TipoAsistencia> query = em.createQuery(
                "SELECT t FROM TipoAsistencia t ORDER BY t.nombre", TipoAsistencia.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // muestro también las anuladas, porque la idea es que quede la evidencia.
    // las pongo de últimas para que no estorben arriba.
    public List<Asistencia> listarPorExpediente(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Asistencia> query = em.createQuery(
                "SELECT a FROM Asistencia a WHERE a.expediente.id = :exp "
              + "ORDER BY a.fechaEntrega DESC, a.id DESC", Asistencia.class);
            query.setParameter("exp", expedienteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void guardar(Asistencia asistencia) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (asistencia.getId() == null) {
                em.persist(asistencia);
            } else {
                em.merge(asistencia);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // la anulación es lógica: cambia el estado y guarda el motivo, pero el
    // registro nunca se borra para que quede la evidencia (HU-17)
    public void anular(Integer asistenciaId, String motivo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Asistencia a = em.find(Asistencia.class, asistenciaId);
            if (a != null) {
                a.setEstado(Asistencia.ESTADO_ANULADA);
                a.setMotivoAnulacion(motivo);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // totales por tipo para el historial (HU-16). dejo por fuera las anuladas
    // porque si no, los totales quedarían inflados con ayudas que no se dieron.
    // cada fila del resultado viene como {nombre del tipo, cantidad, suma}.
    // uso Query sin tipar en vez de TypedQuery<Object[]> porque Hibernate 3 se
    // queja cuando la consulta trae varias columnas y le paso Object[].class
    @SuppressWarnings("unchecked")
    public List<Object[]> totalesPorTipo(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createQuery(
                "SELECT a.tipoAsistencia.nombre, COUNT(a), SUM(a.valor) "
              + "FROM Asistencia a "
              + "WHERE a.expediente.id = :exp AND a.estado <> :anulada "
              + "GROUP BY a.tipoAsistencia.nombre "
              + "ORDER BY a.tipoAsistencia.nombre");
            query.setParameter("exp", expedienteId);
            query.setParameter("anulada", Asistencia.ESTADO_ANULADA);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
