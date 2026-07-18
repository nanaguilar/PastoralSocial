package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.TenenciaVivienda;
import pastoralsocial.util.JPAUtil;

public class TenenciaViviendaDAO implements DAO<TenenciaVivienda> {

	@Override
	public List<TenenciaVivienda> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<TenenciaVivienda> query = em.createQuery("SELECT tv FROM TenenciaVivienda tv",
					TenenciaVivienda.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener las tenencias de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public TenenciaVivienda getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(TenenciaVivienda.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar la tenencia de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(TenenciaVivienda tenenciaVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(tenenciaVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al guardar la tenencia de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(TenenciaVivienda tenenciaVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(tenenciaVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al actualizar la tenencia de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(TenenciaVivienda tenenciaVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			tenenciaVivienda = em.merge(tenenciaVivienda);
			em.remove(tenenciaVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al eliminar la tenencia de vivienda.", e);

		} finally {
			em.close();

		}

	}

}
