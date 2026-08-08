package pastoralsocial.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Usuario
 *
 */
@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idUsuario")
	private int idUsuario;

	@Column(name = "identificacion", nullable = false, unique = true)
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

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@ManyToOne
	@JoinColumn(name = "idDetalleGenero", nullable = false)
	private Detalle detalleGenero;

	@Column(name = "estado")
	private boolean estado;

	@Column(name = "reset_token")
	private String resetToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "token_expiration")
	private Date tokenExpiration;

	@ManyToOne
	@JoinColumn(name = "idDetalleRol", nullable = false)
	private Detalle detalleRol;

	@OneToMany(mappedBy = "usuario")
	private List<CasoSocial> casosSociales = new ArrayList<>();

	public Usuario() {
	}

	public Usuario(String identificacion, String tipoIdentificacion, String nombre, String apellidos,
			Date fechaNacimiento, String telefono, String correoElectronico, String username, String password,
			Detalle detalleGenero, boolean estado, Detalle detalleRol) {

		this.identificacion = identificacion;
		this.tipoIdentificacion = tipoIdentificacion;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.correoElectronico = correoElectronico;
		this.username = username;
		this.password = password;
		this.detalleGenero = detalleGenero;
		this.estado = estado;
		this.detalleRol = detalleRol;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Detalle getDetalleGenero() {
		return detalleGenero;
	}

	public void setDetalleGenero(Detalle detalleGenero) {
		this.detalleGenero = detalleGenero;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Detalle getDetalleRol() {
		return detalleRol;
	}

	public void setDetalleRol(Detalle detalleRol) {
		this.detalleRol = detalleRol;
	}

	public List<CasoSocial> getCasosSociales() {
		return casosSociales;
	}

	public void setCasosSociales(List<CasoSocial> casosSociales) {
		this.casosSociales = casosSociales;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Date getTokenExpiration() {
		return tokenExpiration;
	}

	public void setTokenExpiration(Date tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

	@Override
	public String toString() {
		return nombre + " " + apellidos;
	}

}
