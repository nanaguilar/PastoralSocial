package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Adendum;
import pastoralsocial.util.JPAUtil;

public class AdendumDAO implements DAO<Adendum> {

	@Override
	public List<Adendum> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			TypedQuery<Adendum> query = em.createQuery("SELECT a FROM Adendum a ORDER BY a.fechaRegistro DESC",
					Adendum.class);

			return query.getResultList();

		} catch (Exception e) {

			throw new RuntimeException("Error al buscar los adendum.", e);

		} finally {

			em.close();
		}
	}

	@Override
	public Adendum getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			return em.find(Adendum.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el adendum.", e);

		} finally {

			em.close();
		}
	}

	@Override
	public void save(Adendum adendum) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {

			tx.begin();
			em.persist(adendum);
			tx.commit();

		} catch (Exception e) {

			if (tx.isActive()) {
				tx.rollback();
			}

			throw new RuntimeException("Error al guardar el adendum.", e);

		} finally {

			em.close();
		}
	}

	@Override
	public void update(Adendum adendum) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {

			tx.begin();
			em.merge(adendum);
			tx.commit();

		} catch (Exception e) {

			if (tx.isActive()) {
				tx.rollback();
			}

			throw new RuntimeException("Error al actualizar el adendum.", e);

		} finally {

			em.close();
		}
	}

	@Override
	public void delete(Adendum adendum) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {

			tx.begin();
			adendum = em.merge(adendum);
			em.remove(adendum);
			tx.commit();

		} catch (Exception e) {

			if (tx.isActive()) {
				tx.rollback();
			}

			throw new RuntimeException("Error al eliminar el adendum.", e);

		} finally {

			em.close();
		}
	}

	public List<Adendum> listarPorCaso(int idCasoSocial) {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			TypedQuery<Adendum> query = em.createQuery("SELECT a FROM Adendum a "
					+ "WHERE a.casoSocial.idCasoSocial = :idCaso " + "ORDER BY a.fechaRegistro DESC, a.idAdendum DESC",
					Adendum.class);

			query.setParameter("idCaso", idCasoSocial);

			return query.getResultList();

		} catch (Exception e) {

			throw new RuntimeException("Error al buscar los adendum del caso social.", e);

		} finally {

			em.close();
		}
	}
}