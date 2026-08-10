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

		if (integrante == null) {
			throw new IllegalArgumentException("La información del integrante es obligatoria.");
		}

		validar(integrante);

		int idCaso = integrante.getCasoSocial().getIdCasoSocial();

		List<IntegranteFamiliar> integrantes = integranteFamiliarDAO.buscarPorCasoSocial(integrante.getCasoSocial());

		if (integrantes.isEmpty()) {

			if (!integrante.isJefeHogar() || !integrante.isEstado()) {

				throw new IllegalArgumentException(
						"El primer integrante del caso social debe ser un jefe de hogar activo.");
			}
		}

		IntegranteFamiliar duplicado = integranteFamiliarDAO
				.buscarPorIdentificacionEnCaso(integrante.getIdentificacion(), idCaso, 0);

		if (duplicado != null) {

			throw new IllegalArgumentException(
					"No se puede registrar el integrante. Ya existe una persona con esta identificación "
							+ "dentro del caso social " + integrante.getCasoSocial().getNumeroFicha() + ".");
		}

		IntegranteFamiliar existente = integranteFamiliarDAO.buscarIntegranteEnOtroCaso(integrante.getIdentificacion(),
				idCaso);

		if (existente != null) {

			String numeroFicha = existente.getCasoSocial().getNumeroFicha();
			String filial = existente.getCasoSocial().getSectorFilial();

			throw new IllegalArgumentException("La persona con identificación " + integrante.getIdentificacion()
					+ " ya pertenece al caso social " + numeroFicha + " en la filial " + filial
					+ ". No puede pertenecer simultáneamente a otro caso social activo.");
		}

		validarEstadoJefeHogar(integrante);
		validarJefeHogar(integrante);

		integranteFamiliarDAO.save(integrante);
	}

	public void actualizar(IntegranteFamiliar integrante) {

		if (integrante == null) {
			throw new IllegalArgumentException("La información del integrante es obligatoria.");
		}

		validar(integrante);
		validarEstadoJefeHogar(integrante);

		int idCaso = integrante.getCasoSocial().getIdCasoSocial();

		int idIntegrante = integrante.getIdIntegrante();

		IntegranteFamiliar duplicado = integranteFamiliarDAO
				.buscarPorIdentificacionEnCaso(integrante.getIdentificacion(), idCaso, idIntegrante);

		if (duplicado != null) {
			throw new IllegalArgumentException(
					"No se puede actualizar el integrante. " + "Ya existe otra persona con esta identificación "
							+ "dentro del caso social " + integrante.getCasoSocial().getNumeroFicha() + ".");
		}

		IntegranteFamiliar existente = integranteFamiliarDAO.buscarIntegranteEnOtroCaso(integrante.getIdentificacion(),
				idCaso);

		if (existente != null && existente.getIdIntegrante() != idIntegrante) {

			String numeroFicha = existente.getCasoSocial().getNumeroFicha();

			String filial = existente.getCasoSocial().getSectorFilial();

			throw new IllegalArgumentException("La persona con identificación " + integrante.getIdentificacion()
					+ " ya pertenece al caso social " + numeroFicha + " en la filial " + filial
					+ ". No puede pertenecer simultáneamente " + "a otro caso social activo.");
		}

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

	private void validarEstadoJefeHogar(IntegranteFamiliar integrante) {

		if (integrante.isJefeHogar() && !integrante.isEstado()) {

			throw new IllegalArgumentException("No se puede desactivar un jefe de hogar. ");
		}
	}

	private void validarJefeHogar(IntegranteFamiliar integrante) {

		if (!integrante.isJefeHogar() || !integrante.isEstado()) {
			return;
		}

		List<IntegranteFamiliar> integrantes = integranteFamiliarDAO.buscarPorCasoSocial(integrante.getCasoSocial());

		for (IntegranteFamiliar existente : integrantes) {

			if (existente.isJefeHogar() && existente.isEstado()
					&& existente.getIdIntegrante() != integrante.getIdIntegrante()) {

				throw new IllegalArgumentException(
						"El caso " + integrante.getCasoSocial().getNumeroFicha() + " ya posee un jefe de hogar activo: "
								+ existente.getNombre() + " " + existente.getApellidos() + ".");
			}
		}
	}

	public long contarFamiliasRegistradas() {

		return integranteFamiliarDAO.contarFamiliasRegistradas();
	}
}