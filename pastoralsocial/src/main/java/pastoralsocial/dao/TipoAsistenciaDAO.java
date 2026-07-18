package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.TipoAsistencia;
import pastoralsocial.util.JPAUtil;

public class TipoAsistenciaDAO implements DAO<TipoAsistencia> {

	@Override
	public List<TipoAsistencia> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<TipoAsistencia> query = em.createQuery("SELECT ta FROM TipoAsistencia ta", TipoAsistencia.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los tipos de asistencia.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public TipoAsistencia getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(TipoAsistencia.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el tipo de asistencia.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(TipoAsistencia tipoAsistencia) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(tipoAsistencia);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el tipo de asistencia.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(TipoAsistencia tipoAsistencia) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(tipoAsistencia);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el tipo de asistencia.", e);

		} finally {
			em.close();

		}

	}

	public void delete(TipoAsistencia tipoAsistencia) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			tipoAsistencia = em.merge(tipoAsistencia);
			em.remove(tipoAsistencia);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al eliminar el tipo de asistencia.", e);

		} finally {
			em.close();

		}

	}

}