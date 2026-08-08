package pastoralsocial.modelo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Asistencia
 *
 */
@Entity
@Table(name = "asistencia")
public class Asistencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idAsistencia")
	private int idAsistencia;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha", nullable = false)
	private Date fecha;

	@Column(name = "modalidad", nullable = false)
	private String modalidad;

	@Column(name = "frecuencia")
	private String frecuencia;

	@Column(name = "duracion")
	private String duracion;

	@Column(name = "valor", precision = 10, scale = 2)
	private BigDecimal valor;

	@Column(name = "observaciones", columnDefinition = "TEXT")
	private String observaciones;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaProlongacion1")
	private Date fechaProlongacion1;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaProlongacion2")
	private Date fechaProlongacion2;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaProlongacion3")
	private Date fechaProlongacion3;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaProlongacion4")
	private Date fechaProlongacion4;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaProlongacion5")
	private Date fechaProlongacion5;

	@ManyToOne
	@JoinColumn(name = "idCasoSocial", nullable = false)
	private CasoSocial casoSocial;

	// Antes era: @ManyToOne TipoAsistencia tipoAsistencia;
	// Ahora apunta a Detalle (el valor concreto dentro del catalogo "Tipo de
	// Asistencia")
	@ManyToOne
	@JoinColumn(name = "idDetalleTipoAsistencia", nullable = false)
	private Detalle detalleTipoAsistencia;

	public Asistencia() {
	}

	public Asistencia(Date fecha, String modalidad, String frecuencia, String duracion, BigDecimal valor,
			String observaciones, CasoSocial casoSocial, Detalle detalleTipoAsistencia) {

		this.fecha = fecha;
		this.modalidad = modalidad;
		this.frecuencia = frecuencia;
		this.duracion = duracion;
		this.valor = valor;
		this.observaciones = observaciones;

		this.casoSocial = casoSocial;
		this.detalleTipoAsistencia = detalleTipoAsistencia;
	}

	public int getIdAsistencia() {
		return idAsistencia;
	}

	public void setIdAsistencia(int idAsistencia) {
		this.idAsistencia = idAsistencia;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaProlongacion1() {
		return fechaProlongacion1;
	}

	public void setFechaProlongacion1(Date fechaProlongacion1) {
		this.fechaProlongacion1 = fechaProlongacion1;
	}

	public Date getFechaProlongacion2() {
		return fechaProlongacion2;
	}

	public void setFechaProlongacion2(Date fechaProlongacion2) {
		this.fechaProlongacion2 = fechaProlongacion2;
	}

	public Date getFechaProlongacion3() {
		return fechaProlongacion3;
	}

	public void setFechaProlongacion3(Date fechaProlongacion3) {
		this.fechaProlongacion3 = fechaProlongacion3;
	}

	public Date getFechaProlongacion4() {
		return fechaProlongacion4;
	}

	public void setFechaProlongacion4(Date fechaProlongacion4) {
		this.fechaProlongacion4 = fechaProlongacion4;
	}

	public Date getFechaProlongacion5() {
		return fechaProlongacion5;
	}

	public void setFechaProlongacion5(Date fechaProlongacion5) {
		this.fechaProlongacion5 = fechaProlongacion5;
	}

	public CasoSocial getCasoSocial() {
		return casoSocial;
	}

	public void setCasoSocial(CasoSocial casoSocial) {
		this.casoSocial = casoSocial;
	}

	public Detalle getDetalleTipoAsistencia() {
		return detalleTipoAsistencia;
	}

	public void setDetalleTipoAsistencia(Detalle detalleTipoAsistencia) {
		this.detalleTipoAsistencia = detalleTipoAsistencia;
	}

	@Override
	public String toString() {
		return detalleTipoAsistencia.getDescripcion() + " - " + fecha;
	}

}