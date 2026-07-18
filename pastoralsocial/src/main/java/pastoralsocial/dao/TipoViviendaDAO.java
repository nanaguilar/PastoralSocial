package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.TipoVivienda;
import pastoralsocial.util.JPAUtil;

public class TipoViviendaDAO implements DAO<TipoVivienda> {

	@Override
	public List<TipoVivienda> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<TipoVivienda> query = em.createQuery("SELECT tv FROM TipoVivienda tv", TipoVivienda.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los tipos de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public TipoVivienda getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(TipoVivienda.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el tipo de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(TipoVivienda tipoVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(tipoVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al guardar el tipo de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(TipoVivienda tipoVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {

			tx.begin();
			em.merge(tipoVivienda);
			tx.commit();

		} catch (Exception e) {

			if (tx.isActive()) {
				tx.rollback();
				
			}

			throw new RuntimeException("Error al actualizar el tipo de vivienda.", e);

		} finally {

			em.close();

		}

	}

	@Override
	public void delete(TipoVivienda tipoVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			tipoVivienda = em.merge(tipoVivienda);
			em.remove(tipoVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al eliminar el tipo de vivienda.", e);

		} finally {
			em.close();

		}

	}

}
