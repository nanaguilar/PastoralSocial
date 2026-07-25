package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.Parroquia;
import model.SectorFilial;
import util.JPAUtil;

/**
 * Acceso a datos del catálogo de parroquias y sus sectores.
 *
 * <p>Incluye una siembra mínima ({@link #sembrarSiVacio()}) para que los combos
 * siempre tengan opciones aunque la base de datos se haya creado solo con el
 * esquema. La operación es idempotente: no duplica datos si ya existen.
 */
public class ParroquiaDAO {

    /** Devuelve todas las parroquias ordenadas por nombre. */
    public List<Parroquia> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Parroquia> q = em.createQuery(
                "SELECT p FROM Parroquia p ORDER BY p.nombre", Parroquia.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /** Devuelve los sectores/filiales de una parroquia (detalle del catálogo). */
    public List<SectorFilial> listarSectores(Parroquia parroquia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SectorFilial> q = em.createQuery(
                "SELECT s FROM SectorFilial s WHERE s.parroquia = :p ORDER BY s.nombre",
                SectorFilial.class);
            q.setParameter("p", parroquia);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // ------------------------------------------------------------- CRUD parroquia

    /** Crea o actualiza una parroquia (maestro del catálogo). */
    public Parroquia guardarParroquia(Parroquia parroquia) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Parroquia resultado = (parroquia.getId() == null)
                ? persistir(em, parroquia) : em.merge(parroquia);
            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private Parroquia persistir(EntityManager em, Parroquia p) {
        em.persist(p);
        return p;
    }

    /**
     * Elimina una parroquia y sus sectores. Si la parroquia o alguno de sus
     * sectores está en uso por un expediente, la operación falla y se informa.
     */
    public void eliminarParroquia(Integer parroquiaId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Parroquia p = em.find(Parroquia.class, parroquiaId);
            if (p != null) {
                em.createQuery("DELETE FROM SectorFilial s WHERE s.parroquia = :p")
                  .setParameter("p", p)
                  .executeUpdate();
                em.remove(p);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /** Indica si el nombre de parroquia ya existe (evita duplicados). */
    public boolean existeParroquia(String nombre, Integer idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Parroquia p WHERE LOWER(p.nombre) = LOWER(:n)";
            if (idExcluir != null) jpql += " AND p.id <> :id";
            TypedQuery<Long> q = em.createQuery(jpql, Long.class);
            q.setParameter("n", nombre);
            if (idExcluir != null) q.setParameter("id", idExcluir);
            return q.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    // --------------------------------------------------------------- CRUD sector

    /** Crea o actualiza un sector/filial (detalle del catálogo). */
    public SectorFilial guardarSector(SectorFilial sector) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SectorFilial resultado;
            if (sector.getId() == null) {
                em.persist(sector);
                resultado = sector;
            } else {
                resultado = em.merge(sector);
            }
            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /** Elimina un sector/filial. Falla si está en uso por un expediente. */
    public void eliminarSector(Integer sectorId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SectorFilial s = em.find(SectorFilial.class, sectorId);
            if (s != null) {
                em.remove(s);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private long contar(EntityManager em) {
        return em.createQuery("SELECT COUNT(p) FROM Parroquia p", Long.class)
                 .getSingleResult();
    }

    /**
     * Siembra un conjunto básico de parroquias y sectores solo si el catálogo
     * está vacío. Pensado para llamarse una vez al abrir el módulo.
     */
    public void sembrarSiVacio() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            if (contar(em) > 0) {
                return;
            }
            tx.begin();

            String[][] datos = {
                {"Parroquia Catedral",                 "Centro",       "Barrio Norte",     "Barrio Sur"},
                {"Parroquia San José",                 "San José este","San José oeste",   "La Colina"},
                {"Parroquia Nuestra Señora del Carmen","El Carmen",    "Los Ángeles",      "La Trinidad"},
                {"Parroquia San Antonio",              "San Antonio",  "El Progreso",      "Las Palmas"},
                {"Parroquia Inmaculada Concepción",    "El Rosario",   "San Rafael",       "La Esperanza"}
            };

            for (String[] fila : datos) {
                Parroquia p = new Parroquia(fila[0]);
                em.persist(p);
                for (int i = 1; i < fila.length; i++) {
                    em.persist(new SectorFilial(fila[i], p));
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
