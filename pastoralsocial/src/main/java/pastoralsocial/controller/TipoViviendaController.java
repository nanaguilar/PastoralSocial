package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.TipoViviendaDAO;
import pastoralsocial.modelo.TipoVivienda;

public class TipoViviendaController {

	private final TipoViviendaDAO tipoViviendaDAO;

	public TipoViviendaController() {
		tipoViviendaDAO = new TipoViviendaDAO();
	}

	public List<TipoVivienda> listar() {
		return tipoViviendaDAO.getAll();
	}

	public TipoVivienda buscarPorId(int idTipoVivienda) {

		if (idTipoVivienda <= 0) {
			throw new IllegalArgumentException("El identificador del tipo de vivienda es inválido.");
		}

		return tipoViviendaDAO.getById(idTipoVivienda);
	}

	public void registrar(TipoVivienda tipoVivienda) {

		validar(tipoVivienda);

		tipoViviendaDAO.save(tipoVivienda);
	}

	public void actualizar(TipoVivienda tipoVivienda) {

		validar(tipoVivienda);

		tipoViviendaDAO.update(tipoVivienda);
	}

	public void eliminar(TipoVivienda tipoVivienda) {

		if (tipoVivienda == null) {
			throw new IllegalArgumentException("Debe seleccionar un tipo de vivienda.");
		}

		tipoViviendaDAO.delete(tipoVivienda);
	}

	private void validar(TipoVivienda tipoVivienda) {

		if (tipoVivienda == null) {
			throw new IllegalArgumentException("La información del tipo de vivienda es obligatoria.");
		}

		if (tipoVivienda.getDescripcion() == null || tipoVivienda.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción es obligatoria.");
		}
	}

}
