package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.util.JPAUtil;

public class CasoSocialDAO implements DAO<CasoSocial> {

	@Override
	public List<CasoSocial> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<CasoSocial> query = em.createQuery("SELECT c FROM CasoSocial c", CasoSocial.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los casos sociales.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public CasoSocial getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(CasoSocial.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el caso social.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(casoSocial);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el caso social.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(casoSocial);
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
	public void delete(CasoSocial casoSocial) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			casoSocial = em.merge(casoSocial);
			em.remove(casoSocial);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar el caso social.", e);

		} finally {
			em.close();

		}

	}

	public CasoSocial buscarPorNumeroFicha(String numeroFicha) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<CasoSocial> query = em
					.createQuery("SELECT c FROM CasoSocial c WHERE c.numeroFicha = :numeroFicha", CasoSocial.class);

			query.setParameter("numeroFicha", numeroFicha);

			List<CasoSocial> resultado = query.getResultList();

			return resultado.isEmpty() ? null : resultado.get(0);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el caso por número de ficha.", e);

		} finally {
			em.close();

		}

	}

	public List<CasoSocial> buscarPorUsuario(Usuario usuario) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<CasoSocial> query = em.createQuery(
					"SELECT c FROM CasoSocial c " + "WHERE c.usuario = :usuario " + "ORDER BY c.fechaInicio DESC",
					CasoSocial.class);

			query.setParameter("usuario", usuario);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar los casos del usuario.", e);

		} finally {
			em.close();

		}

	}

}