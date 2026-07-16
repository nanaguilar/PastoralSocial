package pastoralsocial.modelo;

import java.math.BigDecimal;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: IngresoFamiliar
 *
 */
@Entity
@Table(name = "ingresoFamiliar")
public class IngresoFamiliar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idIngreso")
	private int idIngreso;

	@Column(name = "integrante", nullable = false)
	private String integrante;

	@Column(name = "ocupacion", nullable = false)
	private String ocupacion;

	@Column(name = "trabaja", nullable = false)
	private boolean trabaja;

	@Column(name = "ingresoMensual", precision = 10, scale = 2)
	private BigDecimal ingresoMensual;

	@ManyToOne
	@JoinColumn(name = "idCasoSocial", nullable = false)
	private CasoSocial casoSocial;

	public IngresoFamiliar() {
	}

	public IngresoFamiliar(String integrante, String ocupacion, boolean trabaja, BigDecimal ingresoMensual,
			CasoSocial casoSocial) {

		this.integrante = integrante;
		this.ocupacion = ocupacion;
		this.trabaja = trabaja;
		this.ingresoMensual = ingresoMensual;

		this.casoSocial = casoSocial;
	}

	public int getIdIngreso() {
		return idIngreso;
	}

	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	public String getIntegrante() {
		return integrante;
	}

	public void setIntegrante(String integrante) {
		this.integrante = integrante;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public boolean isTrabaja() {
		return trabaja;
	}

	public void setTrabaja(boolean trabaja) {
		this.trabaja = trabaja;
	}

	public BigDecimal getIngresoMensual() {
		return ingresoMensual;
	}

	public void setIngresoMensual(BigDecimal ingresoMensual) {
		this.ingresoMensual = ingresoMensual;
	}

	public CasoSocial getCasoSocial() {
		return casoSocial;
	}

	public void setCasoSocial(CasoSocial casoSocial) {
		this.casoSocial = casoSocial;
	}

	@Override
	public String toString() {
		return integrante;
	}

}
