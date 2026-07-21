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

		return casoSocialDAO.getById(idCasoSocial);
	}

	public void registrar(CasoSocial casoSocial) {

		if (casoSocial == null) {

			throw new IllegalArgumentException("La información del caso social es obligatoria.");
		}

		String numeroFicha = casoSocialDAO.generarSiguienteNumeroFicha();

		casoSocial.setNumeroFicha(numeroFicha);

		validar(casoSocial);

		casoSocialDAO.save(casoSocial);
	}

	public void actualizar(CasoSocial casoSocial) {

		if (casoSocial == null) {

			throw new IllegalArgumentException("La información del caso social es obligatoria.");
		}

		CasoSocial casoOriginal = casoSocialDAO.getById(casoSocial.getIdCasoSocial());

		if (casoOriginal == null) {

			throw new IllegalArgumentException("El caso social no existe.");
		}

		casoSocial.setNumeroFicha(casoOriginal.getNumeroFicha());

		validar(casoSocial);

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

	public List<CasoSocial> buscar(String texto) {

		if (texto == null || texto.trim().isEmpty()) {
			return casoSocialDAO.getAll();
		}

		return casoSocialDAO.buscar(texto.trim());
	}

	private void validar(CasoSocial casoSocial) {

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
