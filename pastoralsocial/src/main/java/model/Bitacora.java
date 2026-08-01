package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Temporal;
import java.util.Date;

// tabla simple para dejar rastro de las acciones importantes: quién, cuándo,
// qué hizo, sobre qué y sobre cuál registro.
// no hice nada automático ni interceptores: el registro se llama a mano desde
// los puntos donde de verdad importa.
@Entity
@Table(name = "bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String usuario;

    // acá sí uso TIMESTAMP y no DATE, porque para auditar necesito la hora
    @Column(name = "fecha_hora")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHora;

    private String accion;

    // sobre qué se hizo: Usuario, Caso social, Asistencia...
    private String entidad;

    // cómo identifico el registro afectado (el username, el número de ficha...)
    private String referencia;

    private String detalle;

    // los dejo como constantes para no andar escribiendo el texto a mano en cada
    // llamada y que después no me cuadren los filtros por typos
    public static final String CREAR = "CREAR";
    public static final String EDITAR = "EDITAR";
    public static final String CERRAR = "CERRAR";
    public static final String REABRIR = "REABRIR";
    public static final String ANULAR = "ANULAR";
    public static final String ACTIVAR = "ACTIVAR";
    public static final String DESACTIVAR = "DESACTIVAR";
    public static final String RESTABLECER_CLAVE = "RESTABLECER_CLAVE";

    public Bitacora() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}
