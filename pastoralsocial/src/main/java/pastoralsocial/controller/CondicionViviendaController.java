package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.CondicionViviendaDAO;
import pastoralsocial.modelo.CondicionVivienda;

public class CondicionViviendaController {

	private final CondicionViviendaDAO condicionViviendaDAO;

	public CondicionViviendaController() {
		condicionViviendaDAO = new CondicionViviendaDAO();
	}

	public List<CondicionVivienda> listar() {
		return condicionViviendaDAO.getAll();
	}

	public CondicionVivienda buscarPorId(int idCondicion) {

		if (idCondicion <= 0) {
			throw new IllegalArgumentException("El identificador de la condición de vivienda es inválido.");
		}

		return condicionViviendaDAO.getById(idCondicion);
	}

	public void registrar(CondicionVivienda condicionVivienda) {

		validar(condicionVivienda);

		condicionViviendaDAO.save(condicionVivienda);
	}

	public void actualizar(CondicionVivienda condicionVivienda) {

		validar(condicionVivienda);

		condicionViviendaDAO.update(condicionVivienda);
	}

	public void eliminar(CondicionVivienda condicionVivienda) {

		if (condicionVivienda == null) {
			throw new IllegalArgumentException("Debe seleccionar una condición de vivienda.");
		}

		condicionViviendaDAO.delete(condicionVivienda);
	}

	private void validar(CondicionVivienda condicionVivienda) {

		if (condicionVivienda == null) {
			throw new IllegalArgumentException("La información de la condición de vivienda es obligatoria.");
		}

		if (condicionVivienda.getDescripcion() == null || condicionVivienda.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción es obligatoria.");
		}
	}

}
