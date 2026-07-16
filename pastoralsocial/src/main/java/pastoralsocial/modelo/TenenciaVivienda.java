package pastoralsocial.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TenenciaVivienda
 *
 */
@Entity
@Table(name = "tenenciaVivienda")
public class TenenciaVivienda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idTenencia")
	private int idTenencia;

	@Column(name = "descripcion", nullable = false, unique = true)
	private String descripcion;

	@OneToMany(mappedBy = "tenenciaVivienda")
	private List<CasoSocial> casosSociales = new ArrayList<>();

	public TenenciaVivienda() {
	}

	public TenenciaVivienda(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdTenencia() {
		return this.idTenencia;
	}

	public void setIdTenencia(int idTenencia) {
		this.idTenencia = idTenencia;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<CasoSocial> getCasosSociales() {
		return casosSociales;
	}

	public void setCasosSociales(List<CasoSocial> casosSociales) {
		this.casosSociales = casosSociales;
	}

	@Override
	public String toString() {
		return descripcion;
	}

}
