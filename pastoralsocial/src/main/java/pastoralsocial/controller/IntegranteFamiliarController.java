package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.IntegranteFamiliarDAO;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IntegranteFamiliar;

public class IntegranteFamiliarController {

	private final IntegranteFamiliarDAO integranteFamiliarDAO;

	public IntegranteFamiliarController() {
		integranteFamiliarDAO = new IntegranteFamiliarDAO();
	}

	public List<IntegranteFamiliar> listar() {
		return integranteFamiliarDAO.getAll();
	}

	public IntegranteFamiliar buscarPorId(int idIntegrante) {

		if (idIntegrante <= 0) {
			throw new IllegalArgumentException("El identificador del integrante es inválido.");
		}

		return integranteFamiliarDAO.getById(idIntegrante);
	}

	public void registrar(IntegranteFamiliar integrante) {

		validar(integrante);
		validarIdentificacionDuplicada(integrante);
		validarJefeHogar(integrante);

		integranteFamiliarDAO.save(integrante);
	}

	public void actualizar(IntegranteFamiliar integrante) {

		validar(integrante);
		validarIdentificacionDuplicada(integrante);
		validarJefeHogar(integrante);

		integranteFamiliarDAO.update(integrante);
	}

	public void eliminar(IntegranteFamiliar integrante) {

		if (integrante == null) {
			throw new IllegalArgumentException("Debe seleccionar un integrante familiar.");
		}

		integranteFamiliarDAO.delete(integrante);
	}

	public List<IntegranteFamiliar> buscarPorCasoSocial(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		return integranteFamiliarDAO.buscarPorCasoSocial(casoSocial);
	}

	private void validar(IntegranteFamiliar integrante) {

		if (integrante == null) {
			throw new IllegalArgumentException("La información del integrante es obligatoria.");
		}

		if (integrante.getIdentificacion() == null || integrante.getIdentificacion().trim().isEmpty()) {
			throw new IllegalArgumentException("La identificación es obligatoria.");
		}

		if (integrante.getTipoIdentificacion() == null || integrante.getTipoIdentificacion().trim().isEmpty()) {
			throw new IllegalArgumentException("El tipo de identificación es obligatorio.");
		}

		if (integrante.getNombre() == null || integrante.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre es obligatorio.");
		}

		if (integrante.getApellidos() == null || integrante.getApellidos().trim().isEmpty()) {
			throw new IllegalArgumentException("Los apellidos son obligatorios.");
		}

		if (integrante.getCasoSocial() == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}
	}

	private void validarIdentificacionDuplicada(IntegranteFamiliar integrante) {

		IntegranteFamiliar existente = integranteFamiliarDAO.buscarPorIdentificacion(integrante.getIdentificacion());

		if (existente != null && existente.getIdIntegrante() != integrante.getIdIntegrante()) {

			throw new IllegalArgumentException("Ya existe un integrante registrado con esta identificación.");
		}
	}

	private void validarJefeHogar(IntegranteFamiliar integrante) {

		if (!integrante.isJefeHogar() || !integrante.isEstado()) {

			return;
		}

		IntegranteFamiliar jefeActual = integranteFamiliarDAO.buscarJefeHogarActivo(integrante.getCasoSocial());

		if (jefeActual != null && jefeActual.getIdIntegrante() != integrante.getIdIntegrante()) {

			throw new IllegalArgumentException(
					"El caso " + integrante.getCasoSocial().getNumeroFicha() + " ya posee un jefe de hogar activo: "
							+ jefeActual.getNombre() + " " + jefeActual.getApellidos() + ".");
		}
	}

}
