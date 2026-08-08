package pastoralsocial.modelo;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: CondicionVivienda
 *
 */
@Entity
@Table(name = "condicionVivienda")
public class CondicionVivienda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idCondicion")
	private int idCondicion;

	@Column(name = "descripcion", nullable = false, unique = true)
	private String descripcion;

//	@OneToMany(mappedBy = "condicionVivienda")
//	private List<CasoSocial> casosSociales = new ArrayList<>();

	public CondicionVivienda() {
	}

	public CondicionVivienda(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdCondicion() {
		return this.idCondicion;
	}

	public void setIdCondicion(int idCondicion) {
		this.idCondicion = idCondicion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

//	public List<CasoSocial> getCasosSociales() {
//		return casosSociales;
//	}
//
//	public void setCasosSociales(List<CasoSocial> casosSociales) {
//		this.casosSociales = casosSociales;
//	}

	@Override
	public String toString() {
		return descripcion;
	}

}
