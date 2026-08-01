package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

import model.CatalogoSimple;
import model.CondicionVivienda;
import model.Tenencia;
import model.TipoVivienda;
import util.JPAUtil;

// hice un solo DAO para las tres tablas de vivienda en vez de tres DAOs calcados.
// le paso la clase (TipoVivienda.class, Tenencia.class...) y con getSimpleName()
// armo el JPQL, que en JPA usa el nombre de la entidad y no el de la tabla.
public class CatalogoViviendaDAO {

    public <T> List<T> listar(Class<T> clase) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery(
                "SELECT c FROM " + clase.getSimpleName() + " c ORDER BY c.nombre", clase);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // como los tres constructores son iguales pero las clases distintas, prefiero
    // este if explícito antes que meterme con reflection, que se lee peor
    public CatalogoSimple crear(Class<? extends CatalogoSimple> clase, String nombre) {
        if (clase == TipoVivienda.class) {
            return new TipoVivienda(nombre);
        }
        if (clase == Tenencia.class) {
            return new Tenencia(nombre);
        }
        return new CondicionVivienda(nombre);
    }

    public void guardar(CatalogoSimple entidad) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (entidad.getId() == null) {
                em.persist(entidad);
            } else {
                em.merge(entidad);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminar(Class<?> clase, Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Object fila = em.find(clase, id);
            if (fila != null) {
                em.remove(fila);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean existeNombre(Class<?> clase, String nombre, Integer idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(c) FROM " + clase.getSimpleName() + " c "
                        + "WHERE LOWER(c.nombre) = LOWER(:nombre)";
            if (idExcluir != null) {
                jpql += " AND c.id <> :id";
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

    // antes de borrar un valor reviso que ningún expediente lo esté usando,
    // si no me quedarían casos apuntando a una fila que ya no existe
    public long contarUsoEnExpedientes(Class<?> clase, Integer id) {
        String campo;
        if (clase == TipoVivienda.class) {
            campo = "tipoVivienda";
        } else if (clase == Tenencia.class) {
            campo = "tenencia";
        } else {
            campo = "condicionVivienda";
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Expediente e WHERE e." + campo + ".id = :id",
                Long.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Hibernate me crea las tres tablas pero vacías, así que las siembro con los
    // mismos valores que antes estaban en los enums. es idempotente: cada tabla
    // se siembra solo si está en cero, así puedo llamarlo en cada arranque.
    public void sembrarSiVacio() {
        sembrar(TipoVivienda.class, new String[] {
            "Casa", "Apartamento", "Cuarto", "Tugurio", "Albergue / refugio", "Ninguna" });

        sembrar(Tenencia.class, new String[] {
            "Propia", "Paga a plazos", "Alquilada", "Prestada", "Precario" });

        sembrar(CondicionVivienda.class, new String[] {
            "Buena", "Regular", "Mala", "No aplica" });
    }

    private void sembrar(Class<? extends CatalogoSimple> clase, String[] nombres) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            long cuantos = em.createQuery(
                "SELECT COUNT(c) FROM " + clase.getSimpleName() + " c",
                Long.class).getSingleResult();

            if (cuantos > 0) {
                return;
            }

            tx.begin();
            for (String nombre : nombres) {
                em.persist(crear(clase, nombre));
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
