package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.Expediente;
import util.JPAUtil;

/**
 * Acceso a datos del maestro-detalle "Caso Social".
 *
 * <p>El expediente se guarda con sus familiares en una sola operación gracias a
 * la cascada definida en {@link Expediente}. Los métodos de carga traen también
 * los familiares para poder <b>reutilizar</b> los datos ya registrados sin
 * pedirlos de nuevo.
 */
public class ExpedienteDAO {

    /** Guarda un expediente nuevo junto con todos sus familiares (cascada). */
    public void guardar(Expediente expediente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(expediente);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un expediente existente y su detalle de familiares.
     * Los familiares agregados, modificados o quitados se sincronizan por la
     * cascada y {@code orphanRemoval}, sin duplicar registros.
     */
    public Expediente actualizar(Expediente expediente) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Expediente fusionado = em.merge(expediente);
            tx.commit();
            return fusionado;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los casos sociales (para reabrir uno existente, no recrearlo).
     * Trae también los familiares con {@code LEFT JOIN FETCH} para poder mostrar
     * su cantidad en la lista sin errores de inicialización perezosa.
     */
    public List<Expediente> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Expediente> q = em.createQuery(
                "SELECT DISTINCT e FROM Expediente e LEFT JOIN FETCH e.personas "
                + "ORDER BY e.numeroFicha", Expediente.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Carga un expediente con sus familiares, parroquia y sector ya inicializados,
     * de modo que la pantalla pueda mostrar toda la información existente sin
     * volver a solicitarla al usuario.
     */
    public Expediente buscarConDetalle(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Expediente e = em.find(Expediente.class, id);
            if (e != null) {
                // Inicializa las asociaciones perezosas dentro de la sesión.
                e.getPersonas().size();
                if (e.getParroquia() != null) e.getParroquia().getNombre();
                if (e.getSectorFilial() != null) e.getSectorFilial().getNombre();
            }
            return e;
        } finally {
            em.close();
        }
    }

    /** Indica si ya existe un expediente con ese número de ficha (evita duplicados). */
    public boolean existeNumeroFicha(String numeroFicha, Integer idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(e) FROM Expediente e WHERE e.numeroFicha = :nf";
            if (idExcluir != null) {
                jpql += " AND e.id <> :id";
            }
            TypedQuery<Long> q = em.createQuery(jpql, Long.class);
            q.setParameter("nf", numeroFicha);
            if (idExcluir != null) {
                q.setParameter("id", idExcluir);
            }
            return q.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    /** Elimina un caso social y, por cascada, sus familiares. */
    public void eliminar(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Expediente e = em.find(Expediente.class, id);
            if (e != null) {
                em.remove(e);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
