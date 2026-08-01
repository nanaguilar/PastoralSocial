package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.Parroquia;
import model.SectorFilial;
import util.JPAUtil;

// este DAO maneja el maestro (parroquia) y el detalle (sector/filial) juntos,
// porque siempre se usan de la mano y no vale la pena partirlo en dos archivos.
public class ParroquiaDAO {

    public List<Parroquia> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Parroquia> query = em.createQuery(
                "SELECT p FROM Parroquia p ORDER BY p.nombre", Parroquia.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<SectorFilial> listarSectores(Parroquia parroquia) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SectorFilial> query = em.createQuery(
                "SELECT s FROM SectorFilial s WHERE s.parroquia.id = :id ORDER BY s.nombre",
                SectorFilial.class);
            query.setParameter("id", parroquia.getId());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ---------------------------------------------------------------- maestro

    public void guardarParroquia(Parroquia parroquia) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (parroquia.getId() == null) {
                em.persist(parroquia);
            } else {
                em.merge(parroquia);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // al borrar la parroquia me llevo sus sectores por delante, porque un sector
    // sin parroquia no tiene sentido. antes de llamar esto la pantalla revisa
    // que nadie lo esté usando.
    public void eliminarParroquia(Integer parroquiaId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Parroquia p = em.find(Parroquia.class, parroquiaId);
            if (p != null) {
                em.createQuery("DELETE FROM SectorFilial s WHERE s.parroquia.id = :id")
                  .setParameter("id", parroquiaId)
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

    public boolean existeParroquia(String nombre, Integer idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Parroquia p "
                        + "WHERE LOWER(p.nombre) = LOWER(:nombre)";
            if (idExcluir != null) {
                jpql += " AND p.id <> :id";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("nombre", nombre);
            if (idExcluir != null) {
                query.setParameter("id", idExcluir);
            }
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    // ---------------------------------------------------------------- detalle

    public void guardarSector(SectorFilial sector) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (sector.getId() == null) {
                em.persist(sector);
            } else {
                em.merge(sector);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

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

    // ------------------------------------------------------------ uso en casos

    // cuento cuántos expedientes usan esta parroquia (contando los que la usan a
    // través de uno de sus sectores), para no dejar borrar algo que está en uso
    public long contarUsoParroquia(Integer parroquiaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Expediente e "
              + "WHERE e.parroquia.id = :id OR e.sectorFilial.parroquia.id = :id",
                Long.class);
            query.setParameter("id", parroquiaId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public long contarUsoSector(Integer sectorId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Expediente e WHERE e.sectorFilial.id = :id",
                Long.class);
            query.setParameter("id", sectorId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
