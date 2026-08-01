package model;

// las tres tablas de vivienda son idénticas (id + nombre), así que les puse esta
// interfaz para poder reutilizar el mismo panel y el mismo DAO en las tres.
// si no, tendría que escribir el mismo código tres veces cambiando solo el tipo.
public interface CatalogoSimple {

    Integer getId();

    String getNombre();

    void setNombre(String nombre);
}
