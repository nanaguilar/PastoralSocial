package util;

import model.Usuario;

// acá guardo quién está usando el sistema en este momento.
// lo hice todo estático porque en una app de escritorio solo hay una sesión
// abierta a la vez, así no tengo que andar pasando el usuario por todos los frames.
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

    // esta es la que uso para ocultar el botón de gestión de usuarios en el menú
    public static boolean esAdmin() {
        return tieneRol("ADMINISTRADOR");
    }

    // el coordinador solo consulta, no crea ni edita nada
    public static boolean puedeEditar() {
        return tieneRol("ADMINISTRADOR") || tieneRol("TRABAJADOR_SOCIAL");
    }

    // como Rol pasó a ser una tabla, ya no puedo comparar con == contra un enum.
    // comparo por nombre y sin importar mayúsculas, así no dependo del id que
    // le haya tocado a la fila en la base.
    private static boolean tieneRol(String nombre) {
        return usuarioActivo != null
            && usuarioActivo.getRol() != null
            && nombre.equalsIgnoreCase(usuarioActivo.getRol().getNombre());
    }

    // lo voy a ocupar más adelante para la bitácora
    public static String nombreUsuario() {
        return usuarioActivo == null ? "desconocido" : usuarioActivo.getUsername();
    }
}
