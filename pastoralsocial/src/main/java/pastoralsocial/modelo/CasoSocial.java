package pastoralsocial.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: CasoSocial
 *
 */
@Entity
@Table(name = "casoSocial")
public class CasoSocial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idCasoSocial")
	private int idCasoSocial;

	@Column(name = "numeroFicha", nullable = false, unique = true)
	private String numeroFicha;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaInicio", nullable = false)
	private Date fechaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "fechaPrevistaFin")
	private Date fechaPrevistaFin;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false)
	private EstadoCaso estado;

	@Column(name = "parroquia")
	private String parroquia;

	@Column(name = "sectorFilial")
	private String sectorFilial;

	@Column(name = "provincia")
	private String provincia;

	@Column(name = "canton")
	private String canton;

	@Column(name = "distrito")
	private String distrito;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "observaciones", columnDefinition = "TEXT")
	private String observaciones;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idTipoVivienda", nullable = false)
	private TipoVivienda tipoVivienda;

	@ManyToOne
	@JoinColumn(name = "idTenencia", nullable = false)
	private TenenciaVivienda tenenciaVivienda;

	@ManyToOne
	@JoinColumn(name = "idCondicion", nullable = false)
	private CondicionVivienda condicionVivienda;

	@OneToMany(mappedBy = "casoSocial")
	private List<IntegranteFamiliar> integrantesFamiliares = new ArrayList<>();

	@OneToMany(mappedBy = "casoSocial")
	private List<Asistencia> asistencias = new ArrayList<>();

	@OneToMany(mappedBy = "casoSocial")
	private List<IngresoFamiliar> ingresosFamiliares = new ArrayList<>();

	public CasoSocial() {
	}

	public CasoSocial(String numeroFicha, Date fechaInicio, Date fechaPrevistaFin, EstadoCaso estado, String parroquia,
			String sectorFilial, String provincia, String canton, String distrito, String direccion,
			String observaciones, Usuario usuario, TipoVivienda tipoVivienda, TenenciaVivienda tenenciaVivienda,
			CondicionVivienda condicionVivienda) {

		this.numeroFicha = numeroFicha;
		this.fechaInicio = fechaInicio;
		this.fechaPrevistaFin = fechaPrevistaFin;
		this.estado = estado;
		this.parroquia = parroquia;
		this.sectorFilial = sectorFilial;
		this.provincia = provincia;
		this.canton = canton;
		this.distrito = distrito;
		this.direccion = direccion;
		this.observaciones = observaciones;

		this.usuario = usuario;
		this.tipoVivienda = tipoVivienda;
		this.tenenciaVivienda = tenenciaVivienda;
		this.condicionVivienda = condicionVivienda;
	}

	public void agregarIntegrante(IntegranteFamiliar integrante) {
		integrantesFamiliares.add(integrante);
		integrante.setCasoSocial(this);
	}

	public void agregarAsistencia(Asistencia asistencia) {
		asistencias.add(asistencia);
		asistencia.setCasoSocial(this);
	}

	public void agregarIngreso(IngresoFamiliar ingreso) {
		ingresosFamiliares.add(ingreso);
		ingreso.setCasoSocial(this);
	}

	public int getIdCasoSocial() {
		return idCasoSocial;
	}

	public void setIdCasoSocial(int idCasoSocial) {
		this.idCasoSocial = idCasoSocial;
	}

	public String getNumeroFicha() {
		return numeroFicha;
	}

	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaPrevistaFin() {
		return fechaPrevistaFin;
	}

	public void setFechaPrevistaFin(Date fechaPrevistaFin) {
		this.fechaPrevistaFin = fechaPrevistaFin;
	}

	public EstadoCaso getEstado() {
		return estado;
	}

	public void setEstado(EstadoCaso estado) {
		this.estado = estado;
	}

	public String getParroquia() {
		return parroquia;
	}

	public void setParroquia(String parroquia) {
		this.parroquia = parroquia;
	}

	public String getSectorFilial() {
		return sectorFilial;
	}

	public void setSectorFilial(String sectorFilial) {
		this.sectorFilial = sectorFilial;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCanton() {
		return canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TipoVivienda getTipoVivienda() {
		return tipoVivienda;
	}

	public void setTipoVivienda(TipoVivienda tipoVivienda) {
		this.tipoVivienda = tipoVivienda;
	}

	public TenenciaVivienda getTenenciaVivienda() {
		return tenenciaVivienda;
	}

	public void setTenenciaVivienda(TenenciaVivienda tenenciaVivienda) {
		this.tenenciaVivienda = tenenciaVivienda;
	}

	public CondicionVivienda getCondicionVivienda() {
		return condicionVivienda;
	}

	public void setCondicionVivienda(CondicionVivienda condicionVivienda) {
		this.condicionVivienda = condicionVivienda;
	}

	public List<IntegranteFamiliar> getIntegrantesFamiliares() {
		return integrantesFamiliares;
	}

	public void setIntegrantesFamiliares(List<IntegranteFamiliar> integrantesFamiliares) {
		this.integrantesFamiliares = integrantesFamiliares;
	}

	public List<Asistencia> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	public List<IngresoFamiliar> getIngresosFamiliares() {
		return ingresosFamiliares;
	}

	public void setIngresosFamiliares(List<IngresoFamiliar> ingresosFamiliares) {
		this.ingresosFamiliares = ingresosFamiliares;
	}

	@Override
	public String toString() {
		return numeroFicha;
	}

}
