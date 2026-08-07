package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.MaestroDAO;
import pastoralsocial.modelo.Maestro;

public class MaestroController {

	private final MaestroDAO maestroDAO;

	public MaestroController() {
		maestroDAO = new MaestroDAO();
	}

	public List<Maestro> listar() {
		return maestroDAO.getAll();
	}

	/**
	 * Busca el catalogo por su nombre, por ejemplo "Tipo de Asistencia".
	 * Util para obtener el idMaestro sin tenerlo quemado en el codigo.
	 */
	public Maestro buscarPorNombre(String nombre) {
		return maestroDAO.buscarPorNombre(nombre);
	}

	public Maestro buscarPorId(int idMaestro) {

		if (idMaestro <= 0) {
			throw new IllegalArgumentException("El identificador del catalogo es invalido.");
		}

		return maestroDAO.getById(idMaestro);
	}

	public void registrar(Maestro maestro) {

		validar(maestro);

		maestroDAO.save(maestro);
	}

	public void actualizar(Maestro maestro) {

		validar(maestro);

		maestroDAO.update(maestro);
	}

	public void eliminar(Maestro maestro) {

		if (maestro == null) {
			throw new IllegalArgumentException("Debe seleccionar un catalogo.");
		}

		maestroDAO.delete(maestro);
	}

	private void validar(Maestro maestro) {

		if (maestro == null) {
			throw new IllegalArgumentException("La informacion del catalogo es obligatoria.");
		}

		if (maestro.getNombre() == null || maestro.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del catalogo es obligatorio.");
		}
	}

}