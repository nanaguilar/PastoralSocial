package pastoralsocial.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Maestro
 *
 * Representa un catalogo del sistema
 */
@Entity
@Table(name = "maestro")
public class Maestro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idMaestro")
	private int idMaestro;

	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;

	@OneToMany(mappedBy = "maestro")
	private List<Detalle> detalles = new ArrayList<>();

	public Maestro() {
	}

	public Maestro(String nombre) {
		this.nombre = nombre;
	}

	public int getIdMaestro() {
		return idMaestro;
	}

	public void setIdMaestro(int idMaestro) {
		this.idMaestro = idMaestro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	@Override
	public String toString() {
		return nombre;
	}

}