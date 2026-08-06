package pastoralsocial.util;

import java.security.MessageDigest;

public class PasswordUtil {

	public static String hash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(password.getBytes("UTF-8"));

			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				String hex = Integer.toHexString(b & 0xff);
				if (hex.length() == 1) {
					sb.append("0");
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("No pude generar el hash de la contraseña", e);
		}
	}

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
