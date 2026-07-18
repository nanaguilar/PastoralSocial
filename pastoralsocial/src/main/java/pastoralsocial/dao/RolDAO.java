package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Rol;
import pastoralsocial.util.JPAUtil;

public class RolDAO implements DAO<Rol> {

	@Override
	public List<Rol> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Rol> query = em.createQuery("SELECT r FROM Rol r ORDER BY r.idRol", Rol.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los roles.", e);

		} finally {
			em.close();

		}
	}

	@Override
	public Rol getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(Rol.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el rol.", e);

		} finally {
			em.close();

		}
	}

	@Override
	public void save(Rol rol) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(rol);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el rol.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(Rol rol) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(rol);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el rol.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(Rol rol) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			rol = em.merge(rol);
			em.remove(rol);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al eliminar el rol.", e);

		} finally {
			em.close();

		}

	}

}
