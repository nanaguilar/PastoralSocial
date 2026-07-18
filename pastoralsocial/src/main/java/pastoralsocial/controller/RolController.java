package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.RolDAO;
import pastoralsocial.modelo.Rol;

public class RolController {

	private final RolDAO rolDAO;

	public RolController() {
		rolDAO = new RolDAO();
	}

	public List<Rol> listar() {
		return rolDAO.getAll();
	}

	public Rol buscarPorId(int idRol) {

		if (idRol <= 0) {
			throw new IllegalArgumentException("El identificador del rol es inválido.");
		}

		return rolDAO.getById(idRol);
	}

	public void registrar(Rol rol) {

		validar(rol);

		rolDAO.save(rol);
	}

	public void actualizar(Rol rol) {

		validar(rol);

		rolDAO.update(rol);
	}

	public void eliminar(Rol rol) {

		if (rol == null) {
			throw new IllegalArgumentException("Debe seleccionar un rol.");
		}

		rolDAO.delete(rol);
	}

	private void validar(Rol rol) {

		if (rol == null) {
			throw new IllegalArgumentException("La información del rol es obligatoria.");
		}

		if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del rol es obligatorio.");
		}
	}

}
