package pastoralsocial.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pastoralsocial.modelo.Bitacora;
import pastoralsocial.util.JPAUtil;
import pastoralsocial.util.Sesion;

public class BitacoraDAO {

	public void registrar(String accion, String entidad, String referencia, String detalle) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			Bitacora b = new Bitacora();
			b.setUsuario(Sesion.nombreUsuario());
			b.setFechaHora(new Date());
			b.setAccion(accion);
			b.setEntidad(entidad);
			b.setReferencia(referencia);
			b.setDetalle(detalle);

			tx.begin();
			em.persist(b);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx.isActive())
				tx.rollback();
		} finally {
			em.close();
		}
	}

	public List<Bitacora> buscar(Date desde, Date hasta, String usuario, String accion) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			StringBuilder jpql = new StringBuilder("SELECT b FROM Bitacora b");

			List<String> condiciones = new ArrayList<String>();
			boolean hayUsuario = (usuario != null && !usuario.trim().isEmpty());
			boolean hayAccion = (accion != null && !accion.trim().isEmpty());

			if (desde != null) {
				condiciones.add("b.fechaHora >= :desde");
			}
			if (hasta != null) {
				condiciones.add("b.fechaHora <= :hasta");
			}
			if (hayUsuario) {
				condiciones.add("LOWER(b.usuario) LIKE :usuario");
			}
			if (hayAccion) {
				condiciones.add("b.accion = :accion");
			}

			for (int i = 0; i < condiciones.size(); i++) {
				jpql.append(i == 0 ? " WHERE " : " AND ").append(condiciones.get(i));
			}
			jpql.append(" ORDER BY b.fechaHora DESC");

			TypedQuery<Bitacora> query = em.createQuery(jpql.toString(), Bitacora.class);

			if (desde != null) {
				query.setParameter("desde", desde);
			}
			if (hasta != null) {
				query.setParameter("hasta", finDelDia(hasta));
			}
			if (hayUsuario) {
				query.setParameter("usuario", "%" + usuario.trim().toLowerCase() + "%");
			}
			if (hayAccion) {
				query.setParameter("accion", accion);
			}

			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> listarAcciones() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			Query query = em.createQuery("SELECT DISTINCT b.accion FROM Bitacora b ORDER BY b.accion");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	private Date finDelDia(Date fecha) {
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
}