package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.AdendumDAO;
import pastoralsocial.modelo.Adendum;
import pastoralsocial.modelo.CasoSocial;

public class AdendumController {

	private final AdendumDAO adendumDAO;

	public AdendumController() {
		this.adendumDAO = new AdendumDAO();
	}

	public List<Adendum> listar() {
		return adendumDAO.getAll();
	}

	public Adendum buscarPorId(int id) {

		return adendumDAO.getById(id);
	}

	public void registrar(Adendum adendum) {

		if (adendum == null) {
			throw new IllegalArgumentException("La información del adendum es obligatoria.");
		}

		validar(adendum);

		adendumDAO.save(adendum);
	}

	public void actualizar(Adendum adendum) {

		if (adendum == null) {
			throw new IllegalArgumentException("La información del adendum es obligatoria.");
		}

		Adendum original = adendumDAO.getById(adendum.getIdAdendum());

		if (original == null) {
			throw new IllegalArgumentException("El adendum no existe.");
		}

		validar(adendum);

		adendumDAO.update(adendum);
	}

	public void eliminar(Adendum adendum) {

		if (adendum == null) {
			throw new IllegalArgumentException("Debe seleccionar un adendum.");
		}

		adendumDAO.delete(adendum);
	}

	public List<Adendum> listarPorCaso(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		return adendumDAO.listarPorCaso(casoSocial.getIdCasoSocial());
	}

	private void validar(Adendum adendum) {

		if (adendum.getFechaRegistro() == null) {
			throw new IllegalArgumentException("La fecha del adendum es obligatoria.");
		}

		if (adendum.getCasoSocial() == null) {
			throw new IllegalArgumentException("El adendum debe estar asociado a un caso social.");
		}
	}
}