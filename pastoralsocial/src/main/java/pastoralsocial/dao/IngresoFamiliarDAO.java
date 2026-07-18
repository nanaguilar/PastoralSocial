package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IngresoFamiliar;
import pastoralsocial.util.JPAUtil;

public class IngresoFamiliarDAO implements DAO<IngresoFamiliar> {

	@Override
	public List<IngresoFamiliar> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<IngresoFamiliar> query = em.createQuery("SELECT i FROM IngresoFamiliar i",
					IngresoFamiliar.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los ingresos familiares.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public IngresoFamiliar getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(IngresoFamiliar.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el ingreso familiar.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(IngresoFamiliar ingreso) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(ingreso);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}

			throw new RuntimeException("Error al guardar el ingreso familiar.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(IngresoFamiliar ingreso) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(ingreso);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el ingreso familiar.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(IngresoFamiliar ingreso) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			ingreso = em.merge(ingreso);
			em.remove(ingreso);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar el ingreso familiar.", e);

		} finally {
			em.close();

		}

	}

	public List<IngresoFamiliar> buscarPorCasoSocial(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<IngresoFamiliar> query = em.createQuery(
					"SELECT i FROM IngresoFamiliar i " + "WHERE i.casoSocial = :casoSocial", IngresoFamiliar.class);

			query.setParameter("casoSocial", casoSocial);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar los ingresos familiares del caso social.", e);

		} finally {
			em.close();

		}

	}

}
