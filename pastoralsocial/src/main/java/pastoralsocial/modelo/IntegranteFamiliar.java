package pastoralsocial.modelo;

import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: IntegranteFamiliar
 *
 */
@Entity
@Table(name = "integranteFamiliar")
public class IntegranteFamiliar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idIntegrante")
	private int idIntegrante;

	@Column(name = "identificacion", nullable = false)
	private String identificacion;

	@Column(name = "tipoIdentificacion", nullable = false)
	private String tipoIdentificacion;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "apellidos", nullable = false)
	private String apellidos;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaNacimiento")
	private Date fechaNacimiento;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "correoElectronico")
	private String correoElectronico;

	@ManyToOne
	@JoinColumn(name = "idDetalleGenero", nullable = false)
	private Detalle detalleGenero;

	@Column(name = "jefeHogar", nullable = false)
	private boolean jefeHogar;

	@ManyToOne
	@JoinColumn(name = "idDetalleParentesco", nullable = false)
	private Detalle detalleParentesco;

	@ManyToOne
	@JoinColumn(name = "idDetalleNivelEducativo", nullable = false)
	private Detalle detalleNivelEducativo;

	@ManyToOne
	@JoinColumn(name = "idDetalleSeguro")
	private Detalle detalleSeguro;

	@Column(name = "salud")
	private String salud;

	@Column(name = "migracion")
	private String migracion;

	@Column(name = "pais")
	private String pais;

	@Column(name = "estado", nullable = false)
	private boolean estado;

	@ManyToOne
	@JoinColumn(name = "idCasoSocial", nullable = false)
	private CasoSocial casoSocial;

	public IntegranteFamiliar() {
	}

	public IntegranteFamiliar(String identificacion, String tipoIdentificacion, String nombre, String apellidos,
			Date fechaNacimiento, String telefono, String correoElectronico, Detalle detalleGenero, boolean jefeHogar,
			Detalle detalleParentesco, Detalle detalleNivelEducativo, Detalle detalleSeguro, String salud,
			String migracion, String pais, boolean estado, CasoSocial casoSocial) {

		this.identificacion = identificacion;
		this.tipoIdentificacion = tipoIdentificacion;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.correoElectronico = correoElectronico;
		this.detalleGenero = detalleGenero;
		this.jefeHogar = jefeHogar;

		this.detalleParentesco = detalleParentesco;
		this.detalleNivelEducativo = detalleNivelEducativo;
		this.detalleSeguro = detalleSeguro;

		this.salud = salud;
		this.migracion = migracion;
		this.pais = pais;
		this.estado = estado;

		this.casoSocial = casoSocial;
	}

	public int getIdIntegrante() {
		return idIntegrante;
	}

	public void setIdIntegrante(int idIntegrante) {
		this.idIntegrante = idIntegrante;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public Detalle getDetalleGenero() {
		return detalleGenero;
	}

	public void setDetalleGenero(Detalle detalleGenero) {
		this.detalleGenero = detalleGenero;
	}

	public boolean isJefeHogar() {
		return jefeHogar;
	}

	public void setJefeHogar(boolean jefeHogar) {
		this.jefeHogar = jefeHogar;
	}

	public Detalle getDetalleParentesco() {
		return detalleParentesco;
	}

	public void setDetalleParentesco(Detalle detalleParentesco) {
		this.detalleParentesco = detalleParentesco;
	}

	public Detalle getDetalleNivelEducativo() {
		return detalleNivelEducativo;
	}

	public void setDetalleNivelEducativo(Detalle detalleNivelEducativo) {
		this.detalleNivelEducativo = detalleNivelEducativo;
	}

	public Detalle getDetalleSeguro() {
		return detalleSeguro;
	}

	public void setDetalleSeguro(Detalle detalleSeguro) {
		this.detalleSeguro = detalleSeguro;
	}

	public String getSalud() {
		return salud;
	}

	public void setSalud(String salud) {
		this.salud = salud;
	}

	public String getMigracion() {
		return migracion;
	}

	public void setMigracion(String migracion) {
		this.migracion = migracion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public CasoSocial getCasoSocial() {
		return casoSocial;
	}

	public void setCasoSocial(CasoSocial casoSocial) {
		this.casoSocial = casoSocial;
	}

	@Override
	public String toString() {
		return nombre + " " + apellidos;
	}

}
