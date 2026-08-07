package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.DetalleDAO;
import pastoralsocial.modelo.Detalle;

public class DetalleController {

	private final DetalleDAO detalleDAO;

	public DetalleController() {
		detalleDAO = new DetalleDAO();
	}

	public List<Detalle> listar() {
		return detalleDAO.getAll();
	}

	/**
	 * Metodo principal para llenar un JComboBox: trae solo los valores
	 * que pertenecen al catalogo indicado (por ejemplo, solo los tipos
	 * de asistencia, sin mezclar con los de vivienda).
	 */
	public List<Detalle> listarPorMaestro(int idMaestro) {

		if (idMaestro <= 0) {
			throw new IllegalArgumentException("El identificador del catalogo es invalido.");
		}

		return detalleDAO.listarPorMaestro(idMaestro);
	}

	public Detalle buscarPorId(int idDetalle) {

		if (idDetalle <= 0) {
			throw new IllegalArgumentException("El identificador del detalle es invalido.");
		}

		return detalleDAO.getById(idDetalle);
	}

	public void registrar(Detalle detalle) {

		validar(detalle);

		detalleDAO.save(detalle);
	}

	public void actualizar(Detalle detalle) {

		validar(detalle);

		detalleDAO.update(detalle);
	}

	public void eliminar(Detalle detalle) {

		if (detalle == null) {
			throw new IllegalArgumentException("Debe seleccionar un detalle.");
		}

		detalleDAO.delete(detalle);
	}

	private void validar(Detalle detalle) {

		if (detalle == null) {
			throw new IllegalArgumentException("La informacion del detalle es obligatoria.");
		}

		if (detalle.getDescripcion() == null || detalle.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripcion es obligatoria.");
		}

		if (detalle.getMaestro() == null) {
			throw new IllegalArgumentException("El detalle debe pertenecer a un catalogo (Maestro).");
		}
	}

}