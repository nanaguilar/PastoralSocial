package pastoralsocial.modelo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Temporal;
import java.util.Date;

@Entity
@Table(name = "bitacora")
public class Bitacora {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String usuario;

	@Column(name = "fecha_hora")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fechaHora;

	private String accion;

	private String entidad;

	private String referencia;

	private String detalle;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
}
