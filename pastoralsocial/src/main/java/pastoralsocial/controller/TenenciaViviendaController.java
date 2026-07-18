package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.TenenciaViviendaDAO;
import pastoralsocial.modelo.TenenciaVivienda;

public class TenenciaViviendaController {

	private final TenenciaViviendaDAO tenenciaViviendaDAO;

	public TenenciaViviendaController() {
		tenenciaViviendaDAO = new TenenciaViviendaDAO();
	}

	public List<TenenciaVivienda> listar() {
		return tenenciaViviendaDAO.getAll();
	}

	public TenenciaVivienda buscarPorId(int idTenencia) {

		if (idTenencia <= 0) {
			throw new IllegalArgumentException("El identificador de la tenencia de vivienda es inválido.");
		}

		return tenenciaViviendaDAO.getById(idTenencia);
	}

	public void registrar(TenenciaVivienda tenenciaVivienda) {

		validar(tenenciaVivienda);

		tenenciaViviendaDAO.save(tenenciaVivienda);
	}

	public void actualizar(TenenciaVivienda tenenciaVivienda) {

		validar(tenenciaVivienda);

		tenenciaViviendaDAO.update(tenenciaVivienda);
	}

	public void eliminar(TenenciaVivienda tenenciaVivienda) {

		if (tenenciaVivienda == null) {
			throw new IllegalArgumentException("Debe seleccionar una tenencia de vivienda.");
		}

		tenenciaViviendaDAO.delete(tenenciaVivienda);
	}

	private void validar(TenenciaVivienda tenenciaVivienda) {

		if (tenenciaVivienda == null) {
			throw new IllegalArgumentException("La información de la tenencia de vivienda es obligatoria.");
		}

		if (tenenciaVivienda.getDescripcion() == null || tenenciaVivienda.getDescripcion().trim().isEmpty()) {
			throw new IllegalArgumentException("La descripción es obligatoria.");
		}
	}

}
