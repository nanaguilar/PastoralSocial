package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import model.Rol;
import model.Usuario;
import util.JPAUtil;
import util.PasswordUtil;

public class UsuarioDAO {

    public void guardar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(usuario);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario login(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username " +
                "AND u.password = :password AND u.activo = true",
                Usuario.class);
            query.setParameter("username", username);
            // ojo: en la base guardo el hash, no la contraseña. por eso tengo que
            // hashear lo que el usuario escribió antes de compararlo.
            query.setParameter("password", PasswordUtil.hash(password));
            java.util.List<Usuario> resultado = query.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    public boolean existeUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username",
                Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    // ===================== lo que agregué para la gestión de usuarios =====================
    // de aquí para abajo es todo nuevo, no toqué nada de los métodos de arriba.

    public List<Usuario> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u ORDER BY u.nombre", Usuario.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // el existeUsername de arriba no me sirve cuando estoy editando, porque el
    // usuario se encuentra a sí mismo y cree que está repetido. por eso este
    // recibe el id a excluir de la búsqueda.
    public boolean existeUsername(String username, Integer idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username";
            if (idExcluir != null) {
                jpql += " AND u.id <> :id";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("username", username);
            if (idExcluir != null) {
                query.setParameter("id", idExcluir);
            }
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    // uso este en vez del guardar() de arriba porque acá le aplico el hash antes
    // de persistir. no modifiqué el guardar() original para no romper nada.
    public void guardarConHash(Usuario usuario, String passwordPlano) {
        usuario.setPassword(PasswordUtil.hash(passwordPlano));
        guardar(usuario);
    }

    public void actualizar(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuario);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // el admin escribe la contraseña nueva directamente, sin flujo de token temporal
    public void cambiarPassword(Integer usuarioId, String passwordPlano) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuario u = em.find(Usuario.class, usuarioId);
            if (u != null) {
                u.setPassword(PasswordUtil.hash(passwordPlano));
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // activar / desactivar sin borrar nada, para no perder el historial
    public void cambiarEstado(Integer usuarioId, boolean activo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuario u = em.find(Usuario.class, usuarioId);
            if (u != null) {
                u.setActivo(activo);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // esto lo consulto ANTES de desactivar a alguien o de quitarle el rol de admin.
    // el profe pide que siempre quede al menos un administrador activo, así que si
    // esto devuelve 0 significa que ese usuario es el último y no lo puedo tocar.
    public long contarOtrosAdminsActivos(Integer usuarioId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM Usuario u "
                        + "WHERE u.rol.nombre = 'ADMINISTRADOR' AND u.activo = true";
            if (usuarioId != null) {
                jpql += " AND u.id <> :id";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            if (usuarioId != null) {
                query.setParameter("id", usuarioId);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // el correo también tiene que ser único, si no dos usuarios pedirían el código
    // al mismo buzón. mismo truco que arriba: al editar me excluyo a mí mismo.
    public boolean existeCorreo(String correo, Integer idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.correo) = :correo";
            if (idExcluir != null) {
                jpql += " AND u.id <> :id";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("correo", correo.trim().toLowerCase());
            if (idExcluir != null) {
                query.setParameter("id", idExcluir);
            }
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    // ===================== recuperación de contraseña por correo =====================

    // solo busco entre los activos: un usuario desactivado no debería poder
    // recuperar la clave y volver a entrar por la puerta de atrás.
    public Usuario buscarPorCorreo(String correo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.correo) = :correo AND u.activo = true",
                Usuario.class);
            query.setParameter("correo", correo.trim().toLowerCase());
            List<Usuario> resultado = query.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    // guarda el código y hasta cuándo vale. si el usuario pide otro código, este
    // se sobreescribe y el anterior deja de servir.
    public boolean guardarTokenRecuperacion(String correo, String token, Date expiracion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // busco dentro de este mismo EntityManager para que el usuario quede
            // administrado y Hibernate detecte los cambios sin tener que hacer merge
            List<Usuario> encontrados = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.correo) = :correo AND u.activo = true",
                Usuario.class)
                .setParameter("correo", correo.trim().toLowerCase())
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
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // valido el código contra el correo que lo pidió, no suelto por toda la tabla:
    // son solo 6 dígitos y dos usuarios podrían tener el mismo generado al mismo
    // tiempo. devuelve null si el código no coincide o si ya se venció.
    public Usuario validarToken(String correo, String token) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.correo) = :correo "
              + "AND u.resetToken = :token AND u.tokenExpiration > :ahora AND u.activo = true",
                Usuario.class);
            query.setParameter("correo", correo.trim().toLowerCase());
            query.setParameter("token", token);
            query.setParameter("ahora", new Date());
            List<Usuario> resultado = query.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    // este es el que usa la pantalla de recuperación. ojo con dos cosas:
    // 1) la contraseña va HASHEADA, si la guardo en texto plano el login (que
    //    compara contra el hash) nunca más la va a aceptar.
    // 2) limpio el token para que el mismo código no sirva una segunda vez.
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
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // esto lo llamo una vez al arrancar la app y me arregla dos cosas de golpe:
    // 1) los usuarios que ya existían tenían la contraseña en texto plano, se la hasheo
    // 2) al agregar la columna rol_id todas las filas viejas quedaron en NULL, así que
    //    si no hay ningún administrador activo le asigno el rol al primer usuario.
    // sin esto quedaría sin poder entrar a la pantalla de gestión de usuarios.
    // es idempotente: la segunda vez que corre ya no hace nada.
    public void asegurarAdminInicial() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            List<Usuario> usuarios = em.createQuery(
                "SELECT u FROM Usuario u ORDER BY u.id", Usuario.class).getResultList();

            if (usuarios.isEmpty()) {
                tx.commit();
                return;
            }

            // migro las contraseñas viejas de texto plano a hash
            for (Usuario u : usuarios) {
                if (!PasswordUtil.pareceHash(u.getPassword())) {
                    u.setPassword(PasswordUtil.hash(u.getPassword()));
                }
            }

            long admins = em.createQuery(
                "SELECT COUNT(u) FROM Usuario u "
              + "WHERE u.rol.nombre = 'ADMINISTRADOR' AND u.activo = true",
                Long.class).getSingleResult();

            if (admins == 0) {
                // busco el rol dentro de este mismo EntityManager para que quede
                // administrado y Hibernate me guarde bien la relación
                List<Rol> roles = em.createQuery(
                    "SELECT r FROM Rol r WHERE r.nombre = 'ADMINISTRADOR'", Rol.class)
                    .getResultList();

                if (!roles.isEmpty()) {
                    Usuario primero = usuarios.get(0);
                    primero.setRol(roles.get(0));
                    primero.setActivo(true);
                }
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}