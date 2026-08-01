package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.Rol;
import util.JPAUtil;

// sigo el mismo patrón de UsuarioDAO: abro EntityManager, uso la transacción
// solo cuando escribo, y siempre cierro en el finally.
public class RolDAO {

    public List<Rol> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Rol> query = em.createQuery(
                "SELECT r FROM Rol r ORDER BY r.nombre", Rol.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // lo uso para buscar "ADMINISTRADOR" sin depender del id que le tocó en la tabla
    public Rol buscarPorNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Rol> query = em.createQuery(
                "SELECT r FROM Rol r WHERE r.nombre = :nombre", Rol.class);
            query.setParameter("nombre", nombre);
            List<Rol> resultado = query.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    // Hibernate me crea la tabla pero vacía, así que los 3 roles los siembro yo.
    // es idempotente: si ya hay roles no vuelve a insertar nada, así que puedo
    // llamarlo cada vez que arranca la app sin miedo a duplicar.
    public void sembrarSiVacio() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            long cuantos = em.createQuery(
                "SELECT COUNT(r) FROM Rol r", Long.class).getSingleResult();
            if (cuantos > 0) {
                return;
            }

            tx.begin();
            em.persist(new Rol("ADMINISTRADOR"));
            em.persist(new Rol("TRABAJADOR_SOCIAL"));
            em.persist(new Rol("COORDINADOR"));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
