package pastoralsocial.util;

import pastoralsocial.modelo.Usuario;

public class Sesion {

	private static Usuario usuarioActivo;

	public static void iniciar(Usuario usuario) {
		usuarioActivo = usuario;
	}

	public static void cerrar() {
		usuarioActivo = null;
	}

	public static Usuario getUsuarioActivo() {
		return usuarioActivo;
	}

	public static boolean esAdmin() {
		return tieneRol("ADMINISTRADOR");
	}

	public static boolean puedeEditar() {
		return tieneRol("ADMINISTRADOR") || tieneRol("TRABAJADORSOCIAL");
	}

	private static boolean tieneRol(String nombre) {
		return usuarioActivo != null && usuarioActivo.getDetalleRol() != null
				&& nombre.equalsIgnoreCase(usuarioActivo.getDetalleRol().getDescripcion());
	}

	public static String nombreUsuario() {
		return usuarioActivo == null ? "desconocido" : usuarioActivo.getUsername();
	}
}
