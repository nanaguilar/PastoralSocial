package pastoralsocial.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TipoVivienda
 *
 */
@Entity
@Table(name = "tipoVivienda")
public class TipoVivienda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idTipoVivienda")
	private int idTipoVivienda;

	@Column(name = "descripcion", nullable = false, unique = true)
	private String descripcion;

	@OneToMany(mappedBy = "tipoVivienda")
	private List<CasoSocial> casosSociales = new ArrayList<>();

	public TipoVivienda() {
	}

	public TipoVivienda(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdTipoVivienda() {
		return this.idTipoVivienda;
	}

	public void setIdTipoVivienda(int idTipoVivienda) {
		this.idTipoVivienda = idTipoVivienda;
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
