package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.TipoAsistenciaDAO;
import pastoralsocial.modelo.TipoAsistencia;

public class TipoAsistenciaController {

	private final TipoAsistenciaDAO tipoAsistenciaDAO;

	public TipoAsistenciaController() {
		tipoAsistenciaDAO = new TipoAsistenciaDAO();
	}

	public List<TipoAsistencia> listar() {
		return tipoAsistenciaDAO.getAll();
	}

	public TipoAsistencia buscarPorId(int idTipoAsistencia) {

		if (idTipoAsistencia <= 0) {
			throw new IllegalArgumentException("El identificador del tipo de asistencia es inválido.");
		}

		return tipoAsistenciaDAO.getById(idTipoAsistencia);
	}

	public void registrar(TipoAsistencia tipoAsistencia) {

		validar(tipoAsistencia);

		tipoAsistenciaDAO.save(tipoAsistencia);
	}

	public void actualizar(TipoAsistencia tipoAsistencia) {

		validar(tipoAsistencia);

		tipoAsistenciaDAO.update(tipoAsistencia);
	}

	public void eliminar(TipoAsistencia tipoAsistencia) {

		if (tipoAsistencia == null) {
			throw new IllegalArgumentException("Debe seleccionar un tipo de asistencia.");
		}

		tipoAsistenciaDAO.delete(tipoAsistencia);
	}

	private void validar(TipoAsistencia tipoAsistencia) {

		if (tipoAsistencia == null) {
			throw new IllegalArgumentException("La información del tipo de asistencia es obligatoria.");
		}

		if (tipoAsistencia.getNombre() == null || tipoAsistencia.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre es obligatorio.");
		}
	}

}
