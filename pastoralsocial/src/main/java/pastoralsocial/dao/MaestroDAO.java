package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Maestro;
import pastoralsocial.util.JPAUtil;

public class MaestroDAO implements DAO<Maestro> {

	@Override
	public List<Maestro> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Maestro> query = em.createQuery("SELECT m FROM Maestro m", Maestro.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los catalogos.", e);

		} finally {
			em.close();

		}

	}

	/**
	 * Busca un catalogo (Maestro) por su nombre, por ejemplo "Tipo de Asistencia".
	 * Es la forma mas comoda de obtener el idMaestro sin tener que recordarlo
	 * "quemado" en el codigo.
	 */
	public Maestro buscarPorNombre(String nombre) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Maestro> query = em.createQuery(
					"SELECT m FROM Maestro m WHERE m.nombre = :nombre", Maestro.class);

			query.setParameter("nombre", nombre);

			return query.getSingleResult();

		} catch (NoResultException e) {
			return null;

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el catalogo.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public Maestro getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(Maestro.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el catalogo.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(Maestro maestro) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(maestro);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el catalogo.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(Maestro maestro) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(maestro);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el catalogo.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(Maestro maestro) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			maestro = em.merge(maestro);
			em.remove(maestro);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar el catalogo.", e);

		} finally {
			em.close();

		}

	}

}