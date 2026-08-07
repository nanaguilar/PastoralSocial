package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Detalle;
import pastoralsocial.util.JPAUtil;

public class DetalleDAO implements DAO<Detalle> {

	@Override
	public List<Detalle> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Detalle> query = em.createQuery("SELECT d FROM Detalle d", Detalle.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los detalles.", e);

		} finally {
			em.close();

		}

	}

	/**
	 * Devuelve unicamente los detalles que pertenecen a un catalogo (Maestro)
	 * especifico. Este es el metodo que se usa para llenar los ComboBox,
	 * por ejemplo listarPorMaestro(idMaestroTipoAsistencia).
	 */
	public List<Detalle> listarPorMaestro(int idMaestro) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Detalle> query = em.createQuery(
					"SELECT d FROM Detalle d WHERE d.maestro.idMaestro = :idMaestro", Detalle.class);

			query.setParameter("idMaestro", idMaestro);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los detalles del catalogo.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public Detalle getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(Detalle.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el detalle.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(Detalle detalle) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(detalle);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el detalle.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(Detalle detalle) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(detalle);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el detalle.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(Detalle detalle) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			detalle = em.merge(detalle);
			em.remove(detalle);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar el detalle.", e);

		} finally {
			em.close();

		}

	}

}