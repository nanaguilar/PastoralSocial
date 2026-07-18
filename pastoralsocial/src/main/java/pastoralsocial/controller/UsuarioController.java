package pastoralsocial.controller;

import java.util.List;

import pastoralsocial.dao.UsuarioDAO;
import pastoralsocial.modelo.Usuario;

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

		if (usuarioDAO.buscarPorUsername(usuario.getUsername()) != null) {
			throw new IllegalArgumentException("El nombre de usuario ya se encuentra registrado.");
		}

		usuarioDAO.save(usuario);
	}

	public void actualizar(Usuario usuario) {

		validarUsuario(usuario);

		Usuario usuarioExistente = usuarioDAO.buscarPorUsername(usuario.getUsername());

		if (usuarioExistente != null && usuarioExistente.getIdUsuario() != usuario.getIdUsuario()) {

			throw new IllegalArgumentException("El nombre de usuario ya se encuentra registrado.");
		}

		usuarioDAO.update(usuario);
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

		if (usuario.getRol() == null) {
			throw new IllegalArgumentException("Debe seleccionar un rol.");
		}
	}

}
