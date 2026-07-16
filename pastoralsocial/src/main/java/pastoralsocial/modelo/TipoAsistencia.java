package pastoralsocial.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TipoAsistencia
 *
 */
@Entity
@Table(name = "tipoAsistencia")
public class TipoAsistencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idTipoAsistencia")
	private int idTipoAsistencia;

	@Column(name = "nombre", nullable = false, unique = true)
	private String nombre;

	@OneToMany(mappedBy = "tipoAsistencia")
	private List<Asistencia> asistencias = new ArrayList<>();

	public TipoAsistencia() {
	}

	public TipoAsistencia(String nombre) {
		this.nombre = nombre;
	}

	public int getIdTipoAsistencia() {
		return idTipoAsistencia;
	}

	public void setIdTipoAsistencia(int idTipoAsistencia) {
		this.idTipoAsistencia = idTipoAsistencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Asistencia> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
