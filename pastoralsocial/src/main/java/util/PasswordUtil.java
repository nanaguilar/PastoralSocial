package util;

import java.security.MessageDigest;

// acá junto todo lo de contraseñas para no andar repitiendo el SHA-256 en cada pantalla.
// guardo el hash en hexadecimal (64 caracteres) para que quepa en la columna
// password VARCHAR(100) que ya existe y no haya que cambiarla.
public class PasswordUtil {

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                // el & 0xff es para que el byte no me salga negativo, y el "0" de
                // relleno para que cada byte ocupe siempre 2 caracteres
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (Exception e) {
            // si esto truena es porque el JDK no trae SHA-256, cosa que no debería pasar
            throw new RuntimeException("No pude generar el hash de la contraseña", e);
        }
    }

    // me dice si lo que está guardado ya es un hash mío o todavía es texto plano.
    // lo necesito para migrar los usuarios viejos sin tener que tocar la base a mano.
    public static boolean pareceHash(String valor) {
        if (valor == null || valor.length() != 64) {
            return false;
        }
        for (int i = 0; i < valor.length(); i++) {
            char c = valor.charAt(i);
            boolean esHex = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
            if (!esHex) {
                return false;
            }
        }
        return true;
    }
}
