package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.CasoSocialDAO;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Usuario;

public class CasoSocialController {

	private final CasoSocialDAO casoSocialDAO;

	public CasoSocialController() {
		this.casoSocialDAO = new CasoSocialDAO();
	}

	public List<CasoSocial> listar() {
		return casoSocialDAO.getAll();
	}

	public CasoSocial buscarPorId(int idCasoSocial) {

		if (idCasoSocial <= 0) {
			throw new IllegalArgumentException("El identificador del caso social es inválido.");
		}

		return casoSocialDAO.getById(idCasoSocial);
	}

	public void registrar(CasoSocial casoSocial) {
		validar(casoSocial);

		if (casoSocialDAO.buscarPorNumeroFicha(casoSocial.getNumeroFicha()) != null) {
			throw new IllegalArgumentException("El número de ficha ya se encuentra registrado.");
		}

		casoSocialDAO.save(casoSocial);
	}

	public void actualizar(CasoSocial casoSocial) {

		validar(casoSocial);

		CasoSocial casoExistente = casoSocialDAO.buscarPorNumeroFicha(casoSocial.getNumeroFicha());

		if (casoExistente != null && casoExistente.getIdCasoSocial() != casoSocial.getIdCasoSocial()) {

			throw new IllegalArgumentException("El número de ficha ya se encuentra registrado.");
		}

		casoSocialDAO.update(casoSocial);
	}

	public void eliminar(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		casoSocialDAO.delete(casoSocial);
	}

	public CasoSocial buscarPorNumeroFicha(String numeroFicha) {

		if (numeroFicha == null || numeroFicha.trim().isEmpty()) {
			throw new IllegalArgumentException("El número de ficha es obligatorio.");
		}

		return casoSocialDAO.buscarPorNumeroFicha(numeroFicha.trim());
	}

	public List<CasoSocial> buscarPorUsuario(Usuario usuario) {

		if (usuario == null) {
			throw new IllegalArgumentException("Debe seleccionar un usuario.");
		}

		return casoSocialDAO.buscarPorUsuario(usuario);
	}

	private void validar(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("La información del caso social es obligatoria.");
		}

		if (casoSocial.getNumeroFicha() == null || casoSocial.getNumeroFicha().trim().isEmpty()) {
			throw new IllegalArgumentException("El número de ficha es obligatorio.");
		}

		if (casoSocial.getFechaInicio() == null) {
			throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
		}

		if (casoSocial.getEstado() == null) {
			throw new IllegalArgumentException("Debe seleccionar el estado del caso.");
		}

		if (casoSocial.getUsuario() == null) {
			throw new IllegalArgumentException("Debe seleccionar un usuario responsable.");
		}

		if (casoSocial.getTipoVivienda() == null) {
			throw new IllegalArgumentException("Debe seleccionar el tipo de vivienda.");
		}

		if (casoSocial.getTenenciaVivienda() == null) {
			throw new IllegalArgumentException("Debe seleccionar la tenencia de la vivienda.");
		}

		if (casoSocial.getCondicionVivienda() == null) {
			throw new IllegalArgumentException("Debe seleccionar la condición de la vivienda.");
		}
	}

}
