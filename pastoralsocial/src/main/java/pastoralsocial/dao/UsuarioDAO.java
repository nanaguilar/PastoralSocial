package pastoralsocial.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.JPAUtil;

public class UsuarioDAO implements DAO<Usuario> {

	@Override
	public List<Usuario> getAll() {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);

			return query.getResultList();

		} catch (Exception e) {
			throw new RuntimeException("Error al obtener los usuarios.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public Usuario getById(int id) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			return em.find(Usuario.class, id);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el usuario.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void save(Usuario usuario) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(usuario);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al guardar el usuario.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void update(Usuario usuario) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(usuario);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al actualizar el usuario.", e);

		} finally {
			em.close();

		}

	}

	@Override
	public void delete(Usuario usuario) {

		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			usuario = em.merge(usuario);
			em.remove(usuario);
			tx.commit();

		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();

			}
			throw new RuntimeException("Error al eliminar el usuario.", e);

		} finally {
			em.close();

		}

	}

	public Usuario login(String username, String password) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u " + "WHERE u.username = :username "
					+ "AND u.password = :password " + "AND u.estado = true", Usuario.class);
			query.setParameter("username", username);
			query.setParameter("password", password);

			List<Usuario> resultado = query.getResultList();

			return resultado.isEmpty() ? null : resultado.get(0);

		} catch (Exception e) {
			throw new RuntimeException("Error al iniciar sesión.", e);

		} finally {
			em.close();

		}

	}

	public Usuario buscarPorUsername(String username) {

		EntityManager em = JPAUtil.getEntityManager();

		try {
			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username",
					Usuario.class);

			query.setParameter("username", username);

			List<Usuario> resultado = query.getResultList();

			return resultado.isEmpty() ? null : resultado.get(0);

		} catch (Exception e) {
			throw new RuntimeException("Error al buscar el usuario por nombre de usuario.", e);

		} finally {
			em.close();

		}

	}

}