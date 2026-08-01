package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

// la parroquia es el MAESTRO del catálogo maestro-detalle: cada parroquia tiene
// sus sectores/filiales. antes esto era texto libre en el expediente y se prestaba
// para que cada quien lo escribiera distinto.
@Entity
@Table(name = "parroquia")
public class Parroquia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Parroquia() {
    }

    public Parroquia(String nombre) {
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
