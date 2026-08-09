package pastoralsocial.util;

import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.controller.AsistenciaController;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReporteUtil {

    public static void generarPDFCasoSocial(CasoSocial caso, String rutaArchivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        contenido.append("BT /F1 16 Tf 50 750 Td (Reporte de Caso Social) Tj ET\n");
        contenido.append("BT /F1 10 Tf 50 725 Td (Generado: ").append(Calendar.getInstance().getTime()).append(") Tj ET\n");

        String responsable = "";
        if (caso.getUsuario() != null) {
            responsable = safe(caso.getUsuario().getNombre()) + " " + safe(caso.getUsuario().getApellidos());
        }

        int y = 690;
        y = escribirLinea(contenido, y, "ID: " + caso.getIdCasoSocial());
        y = escribirLinea(contenido, y, "N. Ficha: " + safe(caso.getNumeroFicha()));
        y = escribirLinea(contenido, y, "Fecha Inicio: " + (caso.getFechaInicio() != null ? caso.getFechaInicio().toString() : ""));
        y = escribirLinea(contenido, y, "Estado: " + (caso.getDetalleEstado() != null ? caso.getDetalleEstado().toString() : ""));
        y = escribirLinea(contenido, y, "Parroquia: " + safe(caso.getParroquia()));
        y = escribirLinea(contenido, y, "Responsable: " + responsable);

        escribirPDF(contenido.toString(), rutaArchivo);
    }

    public static void generarExcelCasoSocial(CasoSocial caso, String rutaArchivo) throws IOException {
        String responsable = "";
        if (caso.getUsuario() != null) {
            responsable = safe(caso.getUsuario().getNombre()) + " " + safe(caso.getUsuario().getApellidos());
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"></head><body>\n");
        html.append("<h2>Reporte de Caso Social</h2>\n");
        html.append("<table border=\"1\">\n");
        fila(html, "ID", String.valueOf(caso.getIdCasoSocial()));
        fila(html, "N. Ficha", safe(caso.getNumeroFicha()));
        fila(html, "Fecha Inicio", caso.getFechaInicio() != null ? caso.getFechaInicio().toString() : "");
        fila(html, "Estado", caso.getDetalleEstado() != null ? caso.getDetalleEstado().toString() : "");
        fila(html, "Parroquia", safe(caso.getParroquia()));
        fila(html, "Responsable", responsable);
        html.append("</table></body></html>");

        Files.write(Paths.get(rutaArchivo), html.toString().getBytes("UTF-8"));
    }

    public static void generarPDFAsistenciaCaso(CasoSocial caso, String rutaArchivo) throws IOException {
        List<Asistencia> asistencias = obtenerAsistenciasDelCaso(caso);

        StringBuilder contenido = new StringBuilder();
        String tituloCaso = safe(caso.getNumeroFicha()).replaceAll("[()\\\\]", " ");
        contenido.append("BT /F1 16 Tf 50 750 Td (Reporte de Asistencias - Caso: ").append(tituloCaso).append(") Tj ET\n");
        contenido.append("BT /F1 10 Tf 50 725 Td (Generado: ").append(Calendar.getInstance().getTime()).append(") Tj ET\n");

        int y = 700;
        contenido.append("BT /F1 10 Tf 50 ").append(y).append(" Td (ID | Fecha | Tipo | Modalidad | Valor) Tj ET\n");
        y -= 20;

        for (Asistencia a : asistencias) {
            if (y < 50) { y = 750; }
            String tipo = a.getDetalleTipoAsistencia() != null ? a.getDetalleTipoAsistencia().toString() : "";
            String linea = a.getIdAsistencia() + " | "
                    + (a.getFecha() != null ? a.getFecha().toString() : "") + " | "
                    + tipo + " | "
                    + safe(a.getModalidad()) + " | "
                    + (a.getValor() != null ? a.getValor().toString() : "");
            y = escribirLinea(contenido, y, linea);
        }

        escribirPDF(contenido.toString(), rutaArchivo);
    }

    public static void generarExcelAsistenciaCaso(CasoSocial caso, String rutaArchivo) throws IOException {
        List<Asistencia> asistencias = obtenerAsistenciasDelCaso(caso);

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"></head><body>\n");
        html.append("<h2>Reporte de Asistencias - Caso: ").append(safe(caso.getNumeroFicha())).append("</h2>\n");
        html.append("<table border=\"1\">\n<tr><th>ID</th><th>Fecha</th><th>Tipo</th><th>Modalidad</th><th>Valor</th></tr>\n");

        for (Asistencia a : asistencias) {
            String tipo = a.getDetalleTipoAsistencia() != null ? a.getDetalleTipoAsistencia().toString() : "";
            html.append("<tr>")
                .append("<td>").append(a.getIdAsistencia()).append("</td>")
                .append("<td>").append(a.getFecha() != null ? a.getFecha().toString() : "").append("</td>")
                .append("<td>").append(tipo).append("</td>")
                .append("<td>").append(safe(a.getModalidad())).append("</td>")
                .append("<td>").append(a.getValor() != null ? a.getValor().toString() : "").append("</td>")
                .append("</tr>\n");
        }

        html.append("</table></body></html>");
        Files.write(Paths.get(rutaArchivo), html.toString().getBytes("UTF-8"));
    }

    private static List<Asistencia> obtenerAsistenciasDelCaso(CasoSocial caso) {
        try {
            AsistenciaController controller = new AsistenciaController();
            return controller.buscarPorCasoSocial(caso);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static String safe(String s) {
        return s != null ? s : "";
    }

    private static void fila(StringBuilder html, String campo, String valor) {
        html.append("<tr><th>").append(campo).append("</th><td>").append(valor).append("</td></tr>\n");
    }

    private static int escribirLinea(StringBuilder contenido, int y, String texto) {
        String limpio = texto.replaceAll("[()\\\\]", " ");
        contenido.append("BT /F1 11 Tf 50 ").append(y).append(" Td (").append(limpio).append(") Tj ET\n");
        return y - 20;
    }

    private static void escribirPDF(String contenidoTexto, String rutaArchivo) throws IOException {
        StringBuilder pdf = new StringBuilder();
        pdf.append("%PDF-1.4\n");
        pdf.append("1 0 obj<</Type/Catalog/Pages 2 0 R>>endobj\n");
        pdf.append("2 0 obj<</Type/Pages/Kids[3 0 R]/Count 1>>endobj\n");
        pdf.append("3 0 obj<</Type/Page/Parent 2 0 R/MediaBox[0 0 612 792]/Contents 4 0 R/Resources<</Font<</F1 5 0 R>>>>>>endobj\n");
        pdf.append("5 0 obj<</Type/Font/Subtype/Type1/BaseFont/Helvetica>>endobj\n");
        pdf.append("4 0 obj<</Length ").append(contenidoTexto.length()).append(">>stream\n");
        pdf.append(contenidoTexto);
        pdf.append("endstream\nendobj\n");
        pdf.append("xref\n0 6\n");
        pdf.append("0000000000 65535 f\n");
        pdf.append("0000000009 00000 n\n");
        pdf.append("0000000058 00000 n\n");
        pdf.append("0000000115 00000 n\n");
        pdf.append("0000000242 00000 n\n");
        pdf.append("0000000214 00000 n\n");
        pdf.append("trailer<</Size 6/Root 1 0 R>>\n");
        pdf.append("startxref\n");
        pdf.append((pdf.length() + contenidoTexto.length())).append("\n");
        pdf.append("%%EOF");

        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            fos.write(pdf.toString().getBytes("ISO-8859-1"));
        }
    }
}