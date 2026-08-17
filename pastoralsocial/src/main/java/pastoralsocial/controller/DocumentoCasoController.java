package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.DocumentoCasoDAO;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.DocumentoCaso;

public class DocumentoCasoController {

	private final DocumentoCasoDAO documentoCasoDAO;

	public DocumentoCasoController() {
		this.documentoCasoDAO = new DocumentoCasoDAO();
	}

	public List<DocumentoCaso> listar() {

		return documentoCasoDAO.getAll();
	}

	public DocumentoCaso buscarPorId(int id) {

		return documentoCasoDAO.getById(id);
	}

	public void registrar(DocumentoCaso documentoCaso) {

		if (documentoCaso == null) {
			throw new IllegalArgumentException("La información del documento es obligatoria.");
		}

		validar(documentoCaso);

		documentoCasoDAO.save(documentoCaso);
	}

	public void actualizar(DocumentoCaso documentoCaso) {

		if (documentoCaso == null) {
			throw new IllegalArgumentException("La información del documento es obligatoria.");
		}

		if (documentoCaso.getIdDocumento() <= 0) {
			throw new IllegalArgumentException("El documento no tiene un identificador válido.");
		}

		DocumentoCaso original = documentoCasoDAO.getById(documentoCaso.getIdDocumento());

		if (original == null) {
			throw new IllegalArgumentException("El documento no existe.");
		}

		validar(documentoCaso);

		documentoCasoDAO.update(documentoCaso);
	}

	public void eliminar(DocumentoCaso documentoCaso) {

		if (documentoCaso == null) {
			throw new IllegalArgumentException("Debe seleccionar un documento.");
		}

		if (documentoCaso.getIdDocumento() <= 0) {
			throw new IllegalArgumentException("El documento no tiene un identificador válido.");
		}

		documentoCasoDAO.delete(documentoCaso);
	}

	public List<DocumentoCaso> listarPorCaso(CasoSocial casoSocial) {

		if (casoSocial == null) {
			throw new IllegalArgumentException("Debe seleccionar un caso social.");
		}

		return documentoCasoDAO.listarPorCaso(casoSocial);
	}

	private void validar(DocumentoCaso documentoCaso) {

		if (documentoCaso.getCasoSocial() == null) {
			throw new IllegalArgumentException("El documento debe estar asociado a un caso social.");
		}

		if (documentoCaso.getDetalleTipoDocumento() == null
				|| documentoCaso.getDetalleTipoDocumento().toString().isEmpty()) {

			throw new IllegalArgumentException("El tipo de documento es obligatorio.");
		}

		if (documentoCaso.getNombreArchivo() == null || documentoCaso.getNombreArchivo().trim().isEmpty()) {

			throw new IllegalArgumentException("El nombre del archivo es obligatorio.");
		}

		if (documentoCaso.getRutaArchivo() == null || documentoCaso.getRutaArchivo().trim().isEmpty()) {

			throw new IllegalArgumentException("La ruta del archivo es obligatoria.");
		}

		if (documentoCaso.getFechaRegistro() == null) {
			throw new IllegalArgumentException("La fecha de registro es obligatoria.");
		}
	}
}