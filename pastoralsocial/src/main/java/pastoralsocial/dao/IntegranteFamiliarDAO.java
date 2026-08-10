package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IntegranteFamiliar;
import pastoralsocial.util.JPAUtil;

public class IntegranteFamiliarDAO implements DAO<IntegranteFamiliar> {

	@Override
	public List<IntegranteFamiliar> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<IntegranteFamiliar> query = em.createQuery("SELECT i FROM IntegranteFamiliar i",
					IntegranteFamiliar.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los integrantes familiares.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public IntegranteFamiliar getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(IntegranteFamiliar.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el integrante familiar.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(IntegranteFamiliar integrante) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(integrante);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el integrante familiar.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(IntegranteFamiliar integrante) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(integrante);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el integrante familiar.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(IntegranteFamiliar integrante) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			integrante = em.merge(integrante);
			em.remove(integrante);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar el integrante familiar.", e);

		} finally {
			em.close();

		}

	}

	public List<IntegranteFamiliar> buscarPorCasoSocial(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<IntegranteFamiliar> query = em.createQuery("SELECT i FROM IntegranteFamiliar i "
					+ "WHERE i.casoSocial = :casoSocial " + "ORDER BY i.nombre, i.apellidos", IntegranteFamiliar.class);

			query.setParameter("casoSocial", casoSocial);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar los integrantes del caso social.", e);

		} finally {
			em.close();

		}

	}

	public IntegranteFamiliar buscarIntegranteEnOtroCaso(String identificacion, int idCasoActual) {

		EntityManager em = null;

		try {

			em = JPAUtil.getEntityManager();

			List<IntegranteFamiliar> resultados = em
					.createQuery("SELECT i FROM IntegranteFamiliar i " + "WHERE i.identificacion = :identificacion "
							+ "AND i.casoSocial.idCasoSocial <> :idCasoActual "
							+ "AND (i.casoSocial.detalleEstado.descripcion = 'Activo' "
							+ "OR i.casoSocial.detalleEstado.descripcion = 'En proceso') " + "AND i.estado = true",
							IntegranteFamiliar.class)
					.setParameter("identificacion", identificacion).setParameter("idCasoActual", idCasoActual)
					.setMaxResults(1).getResultList();

			if (resultados.isEmpty()) {
				return null;
			}

			return resultados.get(0);

		} finally {

			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	public IntegranteFamiliar buscarPorIdentificacionEnCaso(String identificacion, int idCasoSocial, int idIntegrante) {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			TypedQuery<IntegranteFamiliar> query = em.createQuery(
					"SELECT i FROM IntegranteFamiliar i " + "WHERE LOWER(i.identificacion) = LOWER(:identificacion) "
							+ "AND i.casoSocial.idCasoSocial = :idCasoSocial " + "AND i.idIntegrante <> :idIntegrante",
					IntegranteFamiliar.class);

			query.setParameter("identificacion", identificacion.trim());
			query.setParameter("idCasoSocial", idCasoSocial);
			query.setParameter("idIntegrante", idIntegrante);

			List<IntegranteFamiliar> resultados = query.getResultList();

			if (resultados.isEmpty()) {
				return null;
			}

			return resultados.get(0);

		} finally {

			em.close();
		}
	}

	public IntegranteFamiliar buscarPorIdentificacion(String identificacion) {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			TypedQuery<IntegranteFamiliar> query = em.createQuery(
					"SELECT i FROM IntegranteFamiliar i WHERE LOWER(i.identificacion) = LOWER(:identificacion)",
					IntegranteFamiliar.class);

			query.setParameter("identificacion", identificacion.trim());

			List<IntegranteFamiliar> resultados = query.getResultList();

			return resultados.isEmpty() ? null : resultados.get(0);

		} finally {

			em.close();
		}
	}

	public IntegranteFamiliar buscarJefeHogarActivo(CasoSocial casoSocial, int idIntegrante) {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			TypedQuery<IntegranteFamiliar> query = em.createQuery("SELECT i FROM IntegranteFamiliar i "
					+ "WHERE i.casoSocial = :casoSocial " + "AND i.jefeHogar = true " + "AND i.estado = true "
					+ "AND i.idIntegrante <> :idIntegrante", IntegranteFamiliar.class);

			query.setParameter("casoSocial", casoSocial);
			query.setParameter("idIntegrante", idIntegrante);

			List<IntegranteFamiliar> resultados = query.getResultList();

			if (resultados.isEmpty()) {
				return null;
			}

			return resultados.get(0);

		} finally {

			em.close();
		}
	}

}