package pastoralsocial.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DocumentoUtil {

	private static final String CARPETA_DOCUMENTOS = "documentos";

	private DocumentoUtil() {
	}

	public static String guardarArchivo(Path archivoOrigen, String numeroFicha) throws IOException {

		if (archivoOrigen == null) {
			throw new IOException("Debe seleccionar un archivo.");
		}

		if (!Files.exists(archivoOrigen)) {
			throw new IOException("El archivo seleccionado no existe.");
		}

		if (numeroFicha == null || numeroFicha.trim().isEmpty()) {
			throw new IOException("El número de ficha es obligatorio.");
		}

		Path carpeta = Paths.get(CARPETA_DOCUMENTOS, numeroFicha);
		Files.createDirectories(carpeta);

		Path destino = carpeta.resolve(archivoOrigen.getFileName());
		Files.copy(archivoOrigen, destino, StandardCopyOption.REPLACE_EXISTING);

		return destino.toAbsolutePath().toString();
	}

	public static void eliminarArchivo(String rutaArchivo) throws IOException {

		if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {

			return;
		}

		Path archivo = Paths.get(rutaArchivo);

		if (Files.exists(archivo)) {
			Files.delete(archivo);
		}
	}
}