package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.util.JPAUtil;

public class AsistenciaDAO implements DAO<Asistencia> {

	@Override
	public List<Asistencia> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Asistencia> query = em.createQuery("SELECT a FROM Asistencia a", Asistencia.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener las asistencias.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public Asistencia getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(Asistencia.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar la asistencia.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(Asistencia asistencia) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(asistencia);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar la asistencia.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(Asistencia asistencia) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(asistencia);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar la asistencia.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(Asistencia asistencia) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			asistencia = em.merge(asistencia);
			em.remove(asistencia);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar la asistencia.", e);

		} finally {
			em.close();

		}

	}

	public List<Asistencia> buscarPorCasoSocial(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Asistencia> query = em.createQuery(
					"SELECT a FROM Asistencia a " + "WHERE a.casoSocial = :casoSocial " + "ORDER BY a.fecha DESC",
					Asistencia.class);

			query.setParameter("casoSocial", casoSocial);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar las asistencias del caso social.", e);

		} finally {
			em.close();

		}

	}

}
