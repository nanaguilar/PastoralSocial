package pastoralsocial.controller;

import java.math.BigDecimal;
import java.util.List;

import pastoralsocial.dao.IngresoFamiliarDAO;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IngresoFamiliar;

public class IngresoFamiliarController {

	private final IngresoFamiliarDAO ingresoFamiliarDAO;

	public IngresoFamiliarController() {
		ingresoFamiliarDAO = new IngresoFamiliarDAO();
	}

	public List<IngresoFamiliar> listar() {
		return ingresoFamiliarDAO.getAll();
	}

	public IngresoFamiliar buscarPorId(int idIngreso) {

		if (idIngreso <= 0) {
			throw new IllegalArgumentException("El identificador del ingreso es inválido.");
		}

		return ingresoFamiliarDAO.getById(idIngreso);
	}

	public List<IngresoFamiliar> buscarPorCasoSocial(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		return ingresoFamiliarDAO.buscarPorCasoSocial(casoSocial);
	}

	public void registrar(IngresoFamiliar ingreso) {

		validar(ingreso);

		ingresoFamiliarDAO.save(ingreso);
	}

	public void actualizar(IngresoFamiliar ingreso) {

		validar(ingreso);

		ingresoFamiliarDAO.update(ingreso);
	}

	public void eliminar(IngresoFamiliar ingreso) {

		if (ingreso == null) {
			throw new IllegalArgumentException("Debe seleccionar un ingreso familiar.");
		}

		ingresoFamiliarDAO.delete(ingreso);
	}

	private void validar(IngresoFamiliar ingreso) {

		if (ingreso == null) {
			throw new IllegalArgumentException("La información del ingreso familiar es obligatoria.");
		}

		if (ingreso.getIntegrante() == null || ingreso.getIntegrante().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del integrante es obligatorio.");
		}

		if (ingreso.getOcupacion() == null || ingreso.getOcupacion().trim().isEmpty()) {
			throw new IllegalArgumentException("La ocupación es obligatoria.");
		}

		if (ingreso.getCasoSocial() == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		if (ingreso.isTrabaja()) {

			if (ingreso.getIngresoMensual() == null) {
				throw new IllegalArgumentException("Debe indicar el ingreso mensual.");
			}

			if (ingreso.getIngresoMensual().compareTo(BigDecimal.ZERO) < 0) {
				throw new IllegalArgumentException("El ingreso mensual no puede ser negativo.");
			}
		}
	}

}
