package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.UsuarioDAO;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.PasswordUtil;

public class UsuarioController {

	private final UsuarioDAO usuarioDAO;

	public UsuarioController() {
		this.usuarioDAO = new UsuarioDAO();
	}

	public List<Usuario> listar() {
		return usuarioDAO.getAll();
	}

	public Usuario buscarPorId(int idUsuario) {
		if (idUsuario <= 0) {
			throw new IllegalArgumentException("El identificador del usuario es inválido.");
		}

		return usuarioDAO.getById(idUsuario);
	}

	public void registrar(Usuario usuario) {

		validarUsuario(usuario);
		validarIdentificacionDuplicada(usuario);
		validarCorreoDuplicado(usuario);

		if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("La contraseña es obligatoria.");
		}

		if (usuario.getPassword().length() != 8) {
			throw new IllegalArgumentException("La contraseña debe tener exactamente 8 caracteres.");
		}

		if (usuarioDAO.buscarPorUsername(usuario.getUsername()) != null) {
			throw new IllegalArgumentException("El nombre de usuario ya se encuentra registrado.");
		}

		usuario.setPassword(PasswordUtil.hash(usuario.getPassword()));

		usuarioDAO.save(usuario);
	}

	public boolean actualizar(Usuario usuario) {

		if (usuario == null) {
			throw new IllegalArgumentException("La información del usuario es obligatoria.");
		}

		Usuario usuarioExistente = usuarioDAO.getById(usuario.getIdUsuario());

		if (usuarioExistente == null) {
			throw new IllegalArgumentException("El usuario no fue encontrado.");
		}

		// Validar identificación solo si cambió
		if (usuario.getIdentificacion() != null
				&& !usuario.getIdentificacion().equalsIgnoreCase(usuarioExistente.getIdentificacion())) {

			Usuario usuarioPorIdentificacion = usuarioDAO.buscarPorIdentificacion(usuario.getIdentificacion());

			if (usuarioPorIdentificacion != null
					&& usuarioPorIdentificacion.getIdUsuario() != usuarioExistente.getIdUsuario()) {

				throw new IllegalArgumentException("Ya existe un usuario registrado con esta identificación.");
			}
		}

		// Validar correo solo si cambió
		String correoAnterior = usuarioExistente.getCorreoElectronico();
		String correoNuevo = usuario.getCorreoElectronico();

		boolean correoCambio = false;

		if (correoNuevo == null || correoNuevo.trim().isEmpty()) {
			throw new IllegalArgumentException("El correo electrónico es obligatorio.");
		}

		if (correoAnterior == null || !correoAnterior.equalsIgnoreCase(correoNuevo.trim())) {

			Usuario usuarioPorCorreo = usuarioDAO.buscarPorCorreoTodos(correoNuevo);

			if (usuarioPorCorreo != null && usuarioPorCorreo.getIdUsuario() != usuarioExistente.getIdUsuario()) {

				throw new IllegalArgumentException("Ya existe un usuario registrado con este correo electrónico.");
			}

			correoCambio = true;
		}

		// Campos que NO pueden modificarse
		usuario.setNombre(usuarioExistente.getNombre());
		usuario.setApellidos(usuarioExistente.getApellidos());
		usuario.setTipoIdentificacion(usuarioExistente.getTipoIdentificacion());
		usuario.setIdentificacion(usuarioExistente.getIdentificacion());

		// Validar username
		if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
		}

		Usuario usuarioPorUsername = usuarioDAO.buscarPorUsername(usuario.getUsername());

		if (usuarioPorUsername != null && usuarioPorUsername.getIdUsuario() != usuarioExistente.getIdUsuario()) {

			throw new IllegalArgumentException("El nombre de usuario ya se encuentra registrado.");
		}

		// Validar rol
		if (usuario.getDetalleRol() == null) {
			throw new IllegalArgumentException("Debe seleccionar un rol.");
		}

		// Contraseña
		if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
			usuario.setPassword(PasswordUtil.hash(usuario.getPassword()));

		} else {

			// Mantener la contraseña actual
			usuario.setPassword(usuarioExistente.getPassword());
		}

		usuarioDAO.update(usuario);

		return correoCambio;
	}

	public void eliminar(Usuario usuario) {

		if (usuario == null) {
			throw new IllegalArgumentException("Debe seleccionar un usuario.");
		}

		usuarioDAO.delete(usuario);
	}

	public Usuario login(String username, String password) {

		if (username == null || username.trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar el nombre de usuario.");
		}

		if (password == null || password.trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar la contraseña.");
		}

		Usuario usuario = usuarioDAO.login(username.trim(), password);

		if (usuario == null) {

			throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
		}

		return usuario;
	}

	public Usuario buscarPorUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
		}

		return usuarioDAO.buscarPorUsername(username.trim());
	}

	private void validarUsuario(Usuario usuario) {

		if (usuario == null) {
			throw new IllegalArgumentException("La información del usuario es obligatoria.");
		}

		if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
		}

		if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
			throw new IllegalArgumentException("La contraseña es obligatoria.");
		}

		if (usuario.getDetalleRol() == null) {
			throw new IllegalArgumentException("Debe seleccionar un rol.");
		}
	}

	public void cambiarEstado(Usuario usuario, Usuario usuarioActual) {

		if (usuario == null) {

			throw new IllegalArgumentException("Debe seleccionar un usuario.");
		}

		if (usuarioActual == null) {

			throw new IllegalArgumentException("No se pudo identificar al usuario autenticado.");
		}

		if (usuario.getIdUsuario() == usuarioActual.getIdUsuario() && usuario.getEstado()) {

			throw new IllegalArgumentException(
					"No puede desactivar su propio usuario mientras mantiene una sesión activa.");
		}

		usuario.setEstado(!usuario.getEstado());

		usuarioDAO.update(usuario);
	}

	private void validarIdentificacionDuplicada(Usuario usuario) {

		Usuario existente = usuarioDAO.buscarPorIdentificacion(usuario.getIdentificacion());

		if (existente != null && existente.getIdUsuario() != usuario.getIdUsuario()) {

			throw new IllegalArgumentException("Ya existe un usuario registrado con esta identificación.");
		}
	}

	private void validarCorreoDuplicado(Usuario usuario) {

		if (usuario.getCorreoElectronico() == null || usuario.getCorreoElectronico().trim().isEmpty()) {

			throw new IllegalArgumentException("El correo electrónico es obligatorio.");
		}

		Usuario existente = usuarioDAO.buscarPorCorreoTodos(usuario.getCorreoElectronico());

		if (existente != null && existente.getIdUsuario() != usuario.getIdUsuario()) {

			throw new IllegalArgumentException("Ya existe un usuario registrado con este correo electrónico.");
		}
	}

	public Usuario buscarPorCorreo(String correoElectronico) {

		if (correoElectronico == null || correoElectronico.trim().isEmpty()) {

			throw new IllegalArgumentException("El correo electrónico es obligatorio.");
		}

		Usuario usuario = usuarioDAO.buscarPorCorreo(correoElectronico.trim());

		if (usuario == null) {

			throw new IllegalArgumentException("No existe una cuenta asociada a este correo electrónico.");
		}

		if (!usuario.getEstado()) {

			throw new IllegalArgumentException("La cuenta asociada a este correo se encuentra inactiva.");
		}

		return usuario;
	}

	public void restablecerContrasena(Usuario usuario, String nuevaContrasena, String confirmarContrasena) {

		if (usuario == null) {

			throw new IllegalArgumentException("No se pudo identificar al usuario.");
		}

		if (!usuario.getEstado()) {

			throw new IllegalArgumentException("No es posible restablecer la contraseña de un usuario inactivo.");
		}

		if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {

			throw new IllegalArgumentException("La nueva contraseña es obligatoria.");
		}

		if (confirmarContrasena == null || confirmarContrasena.trim().isEmpty()) {

			throw new IllegalArgumentException("Debe confirmar la nueva contraseña.");
		}

		if (!nuevaContrasena.equals(confirmarContrasena)) {

			throw new IllegalArgumentException("Las contraseñas no coinciden.");
		}

		usuario.setPassword(PasswordUtil.hash(nuevaContrasena));
		usuarioDAO.update(usuario);
	}
}
