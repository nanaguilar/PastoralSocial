package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

// esto antes era un enum adentro de Expediente. lo paso a tabla porque el profe
// pidió que los valores se puedan agregar y quitar desde una pantalla.
@Entity
@Table(name = "tipo_vivienda")
public class TipoVivienda implements CatalogoSimple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public TipoVivienda() {
    }

    public TipoVivienda(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nombre;
    }
}
