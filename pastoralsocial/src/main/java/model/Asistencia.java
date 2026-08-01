package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import java.util.Date;

// esta es la sección de "Asistencia Solicitada y Brindada" que pidió el profe.
// hice una sola tabla en vez de dos: la diferencia entre solicitada y brindada
// la llevo en el campo estado, que también me sirve para la anulación.
// el tipo sale de tipo_asistencia, que YA EXISTÍA en el proyecto y no toqué.
@Entity
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "expediente_id", nullable = false)
    private Expediente expediente;

    @ManyToOne
    @JoinColumn(name = "tipo_asistencia_id", nullable = false)
    private TipoAsistencia tipoAsistencia;

    private String modalidad;
    private String frecuencia;
    private String duracion;
    private Double valor;

    @Column(name = "fecha_solicitud")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaSolicitud;

    @Column(name = "fecha_entrega")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaEntrega;

    // mismo criterio que en Expediente: texto y no enum, y tampoco tabla porque
    // los estados están amarrados a la lógica, no son algo que el usuario cree
    @Column(name = "estado")
    private String estado;

    @Column(name = "motivo_anulacion")
    private String motivoAnulacion;

    @Column(name = "registrado_por")
    private String registradoPor;

    public static final String ESTADO_SOLICITADA = "SOLICITADA";
    public static final String ESTADO_BRINDADA = "BRINDADA";
    public static final String ESTADO_ANULADA = "ANULADA";

    public Asistencia() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Expediente getExpediente() { return expediente; }
    public void setExpediente(Expediente expediente) { this.expediente = expediente; }

    public TipoAsistencia getTipoAsistencia() { return tipoAsistencia; }
    public void setTipoAsistencia(TipoAsistencia tipoAsistencia) { this.tipoAsistencia = tipoAsistencia; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Date getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public Date getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(Date fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivoAnulacion() { return motivoAnulacion; }
    public void setMotivoAnulacion(String motivoAnulacion) { this.motivoAnulacion = motivoAnulacion; }

    public String getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(String registradoPor) { this.registradoPor = registradoPor; }

    public boolean estaAnulada() {
        return ESTADO_ANULADA.equals(estado);
    }

    @Override
    public String toString() {
        String nombreTipo = (tipoAsistencia == null) ? "sin tipo" : tipoAsistencia.getNombre();
        return nombreTipo + " (" + estado + ")";
    }
}
