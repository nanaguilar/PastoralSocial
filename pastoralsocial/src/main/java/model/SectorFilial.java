package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

// el sector/filial es el DETALLE: siempre pertenece a una parroquia.
// ojo: el nombre no lo puse unique porque dos parroquias distintas sí podrían
// tener un sector que se llame igual (por ejemplo "Centro").
@Entity
@Table(name = "sector_filial")
public class SectorFilial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "parroquia_id", nullable = false)
    private Parroquia parroquia;

    public SectorFilial() {
    }

    public SectorFilial(String nombre, Parroquia parroquia) {
        this.nombre = nombre;
        this.parroquia = parroquia;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Parroquia getParroquia() { return parroquia; }
    public void setParroquia(Parroquia parroquia) { this.parroquia = parroquia; }

    @Override
    public String toString() {
        return nombre;
    }
}
