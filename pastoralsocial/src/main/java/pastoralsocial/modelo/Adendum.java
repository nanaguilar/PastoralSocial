package pastoralsocial.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "adendum")
public class Adendum implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idAdendum;

	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;

	@ManyToOne
	@JoinColumn(name = "id_caso_social", nullable = false)
	private CasoSocial casoSocial;

	// SERVICIOS PUBLICOS
	@Temporal(TemporalType.DATE)
	private Date electricidadFecha1;

	@Temporal(TemporalType.DATE)
	private Date electricidadFecha2;

	@Temporal(TemporalType.DATE)
	private Date electricidadFecha3;

	@Temporal(TemporalType.DATE)
	private Date aguaFecha1;

	@Temporal(TemporalType.DATE)
	private Date aguaFecha2;

	@Temporal(TemporalType.DATE)
	private Date aguaFecha3;

	@Temporal(TemporalType.DATE)
	private Date telefonoFecha1;

	@Temporal(TemporalType.DATE)
	private Date telefonoFecha2;

	@Temporal(TemporalType.DATE)
	private Date telefonoFecha3;

	@Temporal(TemporalType.DATE)
	private Date internetFecha1;

	@Temporal(TemporalType.DATE)
	private Date internetFecha2;

	@Temporal(TemporalType.DATE)
	private Date internetFecha3;

	@Temporal(TemporalType.DATE)
	private Date cableFecha1;

	@Temporal(TemporalType.DATE)
	private Date cableFecha2;

	@Temporal(TemporalType.DATE)
	private Date cableFecha3;

	// GASTOS DE VIVIENDA
	@Temporal(TemporalType.DATE)
	private Date alquilerPrestamoFecha1;

	@Temporal(TemporalType.DATE)
	private Date alquilerPrestamoFecha2;

	@Temporal(TemporalType.DATE)
	private Date alquilerPrestamoFecha3;

	@Temporal(TemporalType.DATE)
	private Date alimentacionFecha1;

	@Temporal(TemporalType.DATE)
	private Date alimentacionFecha2;

	@Temporal(TemporalType.DATE)
	private Date alimentacionFecha3;

	// GASTO DE SALUD
	@Temporal(TemporalType.DATE)
	private Date medicamentoFecha1;

	@Temporal(TemporalType.DATE)
	private Date medicamentoFecha2;

	@Temporal(TemporalType.DATE)
	private Date medicamentoFecha3;

	// OTROS GASTOS
	private String otroGasto1;

	@Temporal(TemporalType.DATE)
	private Date otroGasto1Fecha1;

	@Temporal(TemporalType.DATE)
	private Date otroGasto1Fecha2;

	@Temporal(TemporalType.DATE)
	private Date otroGasto1Fecha3;

	private String otroGasto2;

	@Temporal(TemporalType.DATE)
	private Date otroGasto2Fecha1;

	@Temporal(TemporalType.DATE)
	private Date otroGasto2Fecha2;

	@Temporal(TemporalType.DATE)
	private Date otroGasto2Fecha3;

	// OBSERVACIONES
	@Lob
	private String observaciones;

	// RECOMENDACIONES DE AYUDA
	private Boolean recomendacion1;

	@Temporal(TemporalType.DATE)
	private Date recomendacion1Fecha;

	private Boolean recomendacion2;

	@Temporal(TemporalType.DATE)
	private Date recomendacion2Fecha;

	private Boolean recomendacion3;

	@Temporal(TemporalType.DATE)
	private Date recomendacion3Fecha;

	public Adendum() {
	}

	public int getIdAdendum() {
		return idAdendum;
	}

	public void setIdAdendum(int idAdendum) {
		this.idAdendum = idAdendum;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public CasoSocial getCasoSocial() {
		return casoSocial;
	}

	public void setCasoSocial(CasoSocial casoSocial) {
		this.casoSocial = casoSocial;
	}

	public Date getElectricidadFecha1() {
		return electricidadFecha1;
	}

	public void setElectricidadFecha1(Date electricidadFecha1) {
		this.electricidadFecha1 = electricidadFecha1;
	}

	public Date getElectricidadFecha2() {
		return electricidadFecha2;
	}

	public void setElectricidadFecha2(Date electricidadFecha2) {
		this.electricidadFecha2 = electricidadFecha2;
	}

	public Date getElectricidadFecha3() {
		return electricidadFecha3;
	}

	public void setElectricidadFecha3(Date electricidadFecha3) {
		this.electricidadFecha3 = electricidadFecha3;
	}

	public Date getAguaFecha1() {
		return aguaFecha1;
	}

	public void setAguaFecha1(Date aguaFecha1) {
		this.aguaFecha1 = aguaFecha1;
	}

	public Date getAguaFecha2() {
		return aguaFecha2;
	}

	public void setAguaFecha2(Date aguaFecha2) {
		this.aguaFecha2 = aguaFecha2;
	}

	public Date getAguaFecha3() {
		return aguaFecha3;
	}

	public void setAguaFecha3(Date aguaFecha3) {
		this.aguaFecha3 = aguaFecha3;
	}

	public Date getTelefonoFecha1() {
		return telefonoFecha1;
	}

	public void setTelefonoFecha1(Date telefonoFecha1) {
		this.telefonoFecha1 = telefonoFecha1;
	}

	public Date getTelefonoFecha2() {
		return telefonoFecha2;
	}

	public void setTelefonoFecha2(Date telefonoFecha2) {
		this.telefonoFecha2 = telefonoFecha2;
	}

	public Date getTelefonoFecha3() {
		return telefonoFecha3;
	}

	public void setTelefonoFecha3(Date telefonoFecha3) {
		this.telefonoFecha3 = telefonoFecha3;
	}

	public Date getInternetFecha1() {
		return internetFecha1;
	}

	public void setInternetFecha1(Date internetFecha1) {
		this.internetFecha1 = internetFecha1;
	}

	public Date getInternetFecha2() {
		return internetFecha2;
	}

	public void setInternetFecha2(Date internetFecha2) {
		this.internetFecha2 = internetFecha2;
	}

	public Date getInternetFecha3() {
		return internetFecha3;
	}

	public void setInternetFecha3(Date internetFecha3) {
		this.internetFecha3 = internetFecha3;
	}

	public Date getCableFecha1() {
		return cableFecha1;
	}

	public void setCableFecha1(Date cableFecha1) {
		this.cableFecha1 = cableFecha1;
	}

	public Date getCableFecha2() {
		return cableFecha2;
	}

	public void setCableFecha2(Date cableFecha2) {
		this.cableFecha2 = cableFecha2;
	}

	public Date getCableFecha3() {
		return cableFecha3;
	}

	public void setCableFecha3(Date cableFecha3) {
		this.cableFecha3 = cableFecha3;
	}

	public Date getAlquilerPrestamoFecha1() {
		return alquilerPrestamoFecha1;
	}

	public void setAlquilerPrestamoFecha1(Date alquilerPrestamoFecha1) {
		this.alquilerPrestamoFecha1 = alquilerPrestamoFecha1;
	}

	public Date getAlquilerPrestamoFecha2() {
		return alquilerPrestamoFecha2;
	}

	public void setAlquilerPrestamoFecha2(Date alquilerPrestamoFecha2) {
		this.alquilerPrestamoFecha2 = alquilerPrestamoFecha2;
	}

	public Date getAlquilerPrestamoFecha3() {
		return alquilerPrestamoFecha3;
	}

	public void setAlquilerPrestamoFecha3(Date alquilerPrestamoFecha3) {
		this.alquilerPrestamoFecha3 = alquilerPrestamoFecha3;
	}

	public Date getAlimentacionFecha1() {
		return alimentacionFecha1;
	}

	public void setAlimentacionFecha1(Date alimentacionFecha1) {
		this.alimentacionFecha1 = alimentacionFecha1;
	}

	public Date getAlimentacionFecha2() {
		return alimentacionFecha2;
	}

	public void setAlimentacionFecha2(Date alimentacionFecha2) {
		this.alimentacionFecha2 = alimentacionFecha2;
	}

	public Date getAlimentacionFecha3() {
		return alimentacionFecha3;
	}

	public void setAlimentacionFecha3(Date alimentacionFecha3) {
		this.alimentacionFecha3 = alimentacionFecha3;
	}

	public Date getMedicamentoFecha1() {
		return medicamentoFecha1;
	}

	public void setMedicamentoFecha1(Date medicamentoFecha1) {
		this.medicamentoFecha1 = medicamentoFecha1;
	}

	public Date getMedicamentoFecha2() {
		return medicamentoFecha2;
	}

	public void setMedicamentoFecha2(Date medicamentoFecha2) {
		this.medicamentoFecha2 = medicamentoFecha2;
	}

	public Date getMedicamentoFecha3() {
		return medicamentoFecha3;
	}

	public void setMedicamentoFecha3(Date medicamentoFecha3) {
		this.medicamentoFecha3 = medicamentoFecha3;
	}

	public String getOtroGasto1() {
		return otroGasto1;
	}

	public void setOtroGasto1(String otroGasto1) {
		this.otroGasto1 = otroGasto1;
	}

	public Date getOtroGasto1Fecha1() {
		return otroGasto1Fecha1;
	}

	public void setOtroGasto1Fecha1(Date otroGasto1Fecha1) {
		this.otroGasto1Fecha1 = otroGasto1Fecha1;
	}

	public Date getOtroGasto1Fecha2() {
		return otroGasto1Fecha2;
	}

	public void setOtroGasto1Fecha2(Date otroGasto1Fecha2) {
		this.otroGasto1Fecha2 = otroGasto1Fecha2;
	}

	public Date getOtroGasto1Fecha3() {
		return otroGasto1Fecha3;
	}

	public void setOtroGasto1Fecha3(Date otroGasto1Fecha3) {
		this.otroGasto1Fecha3 = otroGasto1Fecha3;
	}

	public String getOtroGasto2() {
		return otroGasto2;
	}

	public void setOtroGasto2(String otroGasto2) {
		this.otroGasto2 = otroGasto2;
	}

	public Date getOtroGasto2Fecha1() {
		return otroGasto2Fecha1;
	}

	public void setOtroGasto2Fecha1(Date otroGasto2Fecha1) {
		this.otroGasto2Fecha1 = otroGasto2Fecha1;
	}

	public Date getOtroGasto2Fecha2() {
		return otroGasto2Fecha2;
	}

	public void setOtroGasto2Fecha2(Date otroGasto2Fecha2) {
		this.otroGasto2Fecha2 = otroGasto2Fecha2;
	}

	public Date getOtroGasto2Fecha3() {
		return otroGasto2Fecha3;
	}

	public void setOtroGasto2Fecha3(Date otroGasto2Fecha3) {
		this.otroGasto2Fecha3 = otroGasto2Fecha3;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getRecomendacion1() {
		return recomendacion1;
	}

	public void setRecomendacion1(Boolean recomendacion1) {
		this.recomendacion1 = recomendacion1;
	}

	public Date getRecomendacion1Fecha() {
		return recomendacion1Fecha;
	}

	public void setRecomendacion1Fecha(Date recomendacion1Fecha) {
		this.recomendacion1Fecha = recomendacion1Fecha;
	}

	public Boolean getRecomendacion2() {
		return recomendacion2;
	}

	public void setRecomendacion2(Boolean recomendacion2) {
		this.recomendacion2 = recomendacion2;
	}

	public Date getRecomendacion2Fecha() {
		return recomendacion2Fecha;
	}

	public void setRecomendacion2Fecha(Date recomendacion2Fecha) {
		this.recomendacion2Fecha = recomendacion2Fecha;
	}

	public Boolean getRecomendacion3() {
		return recomendacion3;
	}

	public void setRecomendacion3(Boolean recomendacion3) {
		this.recomendacion3 = recomendacion3;
	}

	public Date getRecomendacion3Fecha() {
		return recomendacion3Fecha;
	}

	public void setRecomendacion3Fecha(Date recomendacion3Fecha) {
		this.recomendacion3Fecha = recomendacion3Fecha;
	}
}