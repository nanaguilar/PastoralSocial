package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.CondicionVivienda;
import pastoralsocial.util.JPAUtil;

public class CondicionViviendaDAO implements DAO<CondicionVivienda> {

	@Override
	public List<CondicionVivienda> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<CondicionVivienda> query = em.createQuery("SELECT cv FROM CondicionVivienda cv",
					CondicionVivienda.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener las condiciones de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public CondicionVivienda getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(CondicionVivienda.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar la condición de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(CondicionVivienda condicionVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(condicionVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al guardar la condición de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(CondicionVivienda condicionVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(condicionVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al actualizar la condición de vivienda.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(CondicionVivienda condicionVivienda) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			condicionVivienda = em.merge(condicionVivienda);
			em.remove(condicionVivienda);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
				
			}
			throw new RuntimeException("Error al eliminar la condición de vivienda.", e);

		} finally {
			em.close();

		}

	}

}
