package pastoralsocial.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.JPAUtil;
import pastoralsocial.util.PasswordUtil;

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

			String passwordHash = PasswordUtil.hash(password);

			TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u " + "WHERE u.username = :username "
					+ "AND u.password = :password " + "AND u.estado = true", Usuario.class);

			query.setParameter("username", username);
			query.setParameter("password", passwordHash);

			List<Usuario> resultado = query.getResultList();

			return resultado.isEmpty() ? null : resultado.get(0);

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

	public Usuario buscarPorIdentificacion(String identificacion) {

		EntityManager em = JPAUtil.getEntityManager();

		try {

			TypedQuery<Usuario> query = em.createQuery(
					"SELECT u FROM Usuario u WHERE LOWER(u.identificacion) = LOWER(:identificacion)", Usuario.class);

			query.setParameter("identificacion", identificacion.trim());

			List<Usuario> resultados = query.getResultList();

			return resultados.isEmpty() ? null : resultados.get(0);

		} finally {

			em.close();
		}
	}

	// password methods
	public Usuario buscarPorCorreo(String correoElectronico) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Usuario> query = em.createQuery(
					"SELECT u FROM Usuario u WHERE LOWER(u.correoElectronico) = :correoElectronico AND u.estado = true",
					Usuario.class);
			query.setParameter("correoElectronico", correoElectronico.trim().toLowerCase());
			List<Usuario> resultado = query.getResultList();
			return resultado.isEmpty() ? null : resultado.get(0);
		} finally {
			em.close();
		}
	}

	public boolean guardarTokenRecuperacion(String correoElectronico, String token, Date expiracion) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			List<Usuario> encontrados = em.createQuery(
					"SELECT u FROM Usuario u WHERE LOWER(u.correoElectronico) = :correoElectronico AND u.estado = true",
					Usuario.class).setParameter("correoElectronico", correoElectronico.trim().toLowerCase())
					.getResultList();

			if (encontrados.isEmpty()) {
				tx.rollback();
				return false;
			}

			Usuario usuario = encontrados.get(0);
			usuario.setResetToken(token);
			usuario.setTokenExpiration(expiracion);

			tx.commit();
			return true;
		} catch (RuntimeException e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public Usuario validarToken(String correoElectronico, String token) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Usuario> query = em.createQuery(
					"SELECT u FROM Usuario u WHERE LOWER(u.correoElectronico) = :correoElectronico "
							+ "AND u.resetToken = :token AND u.tokenExpiration > :ahora AND u.estado = true",
					Usuario.class);
			query.setParameter("correoElectronico", correoElectronico.trim().toLowerCase());
			query.setParameter("token", token);
			query.setParameter("ahora", new Date());
			List<Usuario> resultado = query.getResultList();
			return resultado.isEmpty() ? null : resultado.get(0);
		} finally {
			em.close();
		}
	}

	public boolean restablecerConToken(Integer usuarioId, String passwordPlano) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Usuario u = em.find(Usuario.class, usuarioId);
			if (u == null) {
				tx.rollback();
				return false;
			}
			u.setPassword(PasswordUtil.hash(passwordPlano));
			u.setResetToken(null);
			u.setTokenExpiration(null);
			tx.commit();
			return true;
		} catch (RuntimeException e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}
}