package pastoralsocial.modelo;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Detalle
 *
 * Representa un valor especifico dentro de un catalogo (Maestro)
 */
@Entity
@Table(name = "detalle")
public class Detalle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idDetalle")
	private int idDetalle;

	@Column(name = "descripcion", nullable = false)
	private String descripcion;

	@ManyToOne
	@JoinColumn(name = "idMaestro", nullable = false)
	private Maestro maestro;

	public Detalle() {
	}

	public Detalle(String descripcion, Maestro maestro) {
		this.descripcion = descripcion;
		this.maestro = maestro;
	}

	public int getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(int idDetalle) {
		this.idDetalle = idDetalle;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Maestro getMaestro() {
		return maestro;
	}

	public void setMaestro(Maestro maestro) {
		this.maestro = maestro;
	}

	@Override
	public String toString() {
		return descripcion;
	}

}