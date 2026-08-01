package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

// el rol lo hago tabla y no enum, igual que los catálogos de vivienda, para no
// repetir el error de los enums fijos que el profe rechazó.
// los nombres los dejo tal cual los escribió él: ADMINISTRADOR, TRABAJADOR_SOCIAL, COORDINADOR.
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Rol() {
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // esto es para que el combo de la pantalla de usuarios muestre el nombre
    // y no algo como model.Rol@1a2b3c
    @Override
    public String toString() {
        return nombre;
    }
}
