package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.DocumentoCaso;
import pastoralsocial.util.JPAUtil;

public class DocumentoCasoDAO implements DAO<DocumentoCaso> {

	@Override
	public List<DocumentoCaso> getAll() {
		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<DocumentoCaso> query = em.createQuery("SELECT d FROM DocumentoCaso d", DocumentoCaso.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los documentos del caso social", e);

		} finally {
			em.close();

		}
	}

	@Override
	public DocumentoCaso getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(DocumentoCaso.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el documento del caso social.", e);

		} finally {
			em.close();
		}
	}

	@Override
	public void save(DocumentoCaso documentoCaso) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(documentoCaso);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el documento del caso social.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(DocumentoCaso documentoCaso) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(documentoCaso);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el caso social.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(DocumentoCaso documentoCaso) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {

			tx.begin();

			DocumentoCaso documento = em.find(DocumentoCaso.class, documentoCaso.getIdDocumento());

			if (documento == null) {
				throw new IllegalArgumentException("El documento no existe.");
			}

			em.remove(documento);

			tx.commit();

		} catch (Exception e) {

			if (tx.isActive()) {
				tx.rollback();
			}

			throw new RuntimeException("Error al eliminar el documento del caso social.", e);

		} finally {

			em.close();
		}
	}

	public List<DocumentoCaso> listarPorCaso(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<DocumentoCaso> query = em.createQuery(
					"SELECT d FROM DocumentoCaso d " + "WHERE d.casoSocial = :caso " + "ORDER BY d.fechaRegistro DESC",
					DocumentoCaso.class);

			query.setParameter("caso", casoSocial);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el caso por número de ficha.", e);

		} finally {
			em.close();

		}
	}

}
