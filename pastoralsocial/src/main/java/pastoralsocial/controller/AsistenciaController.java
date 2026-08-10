package pastoralsocial.controller;

import java.math.BigDecimal;
import java.util.List;

import pastoralsocial.dao.AsistenciaDAO;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.CasoSocial;

public class AsistenciaController {

	private final AsistenciaDAO asistenciaDAO;

	public AsistenciaController() {
		asistenciaDAO = new AsistenciaDAO();
	}

	public List<Asistencia> listar() {
		return asistenciaDAO.getAll();
	}

	public Asistencia buscarPorId(int idAsistencia) {

		if (idAsistencia <= 0) {
			throw new IllegalArgumentException("El identificador de la asistencia es inválido.");
		}

		return asistenciaDAO.getById(idAsistencia);
	}

	public void registrar(Asistencia asistencia) {

		validar(asistencia);

		asistenciaDAO.save(asistencia);
	}

	public void actualizar(Asistencia asistencia) {

		validar(asistencia);

		asistenciaDAO.update(asistencia);
	}

	public void eliminar(Asistencia asistencia) {

		if (asistencia == null) {
			throw new IllegalArgumentException("Debe seleccionar una asistencia.");
		}

		asistenciaDAO.delete(asistencia);
	}

	public List<Asistencia> buscarPorCasoSocial(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		return asistenciaDAO.buscarPorCasoSocial(casoSocial);
	}

	private void validar(Asistencia asistencia) {

		if (asistencia == null) {
			throw new IllegalArgumentException("La información de la asistencia es obligatoria.");
		}

		if (asistencia.getFecha() == null) {
			throw new IllegalArgumentException("La fecha es obligatoria.");
		}

		if (asistencia.getModalidad() == null || asistencia.getModalidad().trim().isEmpty()) {
			throw new IllegalArgumentException("La modalidad es obligatoria.");
		}

		if (asistencia.getCasoSocial() == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		if (asistencia.getDetalleTipoAsistencia() == null) {
			throw new IllegalArgumentException("Debe seleccionar un tipo de asistencia.");
		}

		if (asistencia.getValor() != null && asistencia.getValor().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("El valor no puede ser negativo.");
		}
	}

	public long contarAsistencias() {

		return asistenciaDAO.contarAsistencias();
	}
}