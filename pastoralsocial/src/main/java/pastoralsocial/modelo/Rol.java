package pastoralsocial.modelo;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Rol
 *
 */
@Entity
@Table(name = "rol")
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idRol")
	private int idRol;

	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;

//	@OneToMany(mappedBy = "rol")
//	private List<Usuario> usuarios = new ArrayList<>();

	public Rol() {
	}

	public Rol(String nombre) {
		this.nombre = nombre;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

//	public List<Usuario> getUsuarios() {
//		return usuarios;
//	}
//
//	public void setUsuarios(List<Usuario> usuarios) {
//		this.usuarios = usuarios;
//	}

	@Override
	public String toString() {
		return nombre;
	}

}
