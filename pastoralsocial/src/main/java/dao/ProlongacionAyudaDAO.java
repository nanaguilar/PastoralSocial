package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.ProlongacionAyuda;
import util.JPAUtil;

public class ProlongacionAyudaDAO {

    public List<ProlongacionAyuda> listarPorExpediente(Integer expedienteId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<ProlongacionAyuda> query = em.createQuery(
                "SELECT p FROM ProlongacionAyuda p WHERE p.expediente.id = :exp "
              + "ORDER BY p.fecha", ProlongacionAyuda.class);
            query.setParameter("exp", expedienteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void guardar(ProlongacionAyuda prolongacion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(prolongacion);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // acá sí borro de verdad la fila, porque una fecha de prolongación mal
    // digitada no es historial que valga la pena conservar
    public void eliminar(Integer prolongacionId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProlongacionAyuda p = em.find(ProlongacionAyuda.class, prolongacionId);
            if (p != null) {
                em.remove(p);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
