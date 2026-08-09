package pastoralsocial.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import pastoralsocial.controller.IngresoFamiliarController;
import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.controller.AsistenciaController;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IngresoFamiliar;
import pastoralsocial.modelo.IntegranteFamiliar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReporteUtil {

    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    private static final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
    private static final Font FONT_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font FONT_SECCION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
    private static final Font FONT_ETIQUETA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
    private static final Font FONT_TEXTO = FontFactory.getFont(FontFactory.HELVETICA, 9);
    private static final Font FONT_ENCABEZADO_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
    private static final Font FONT_CELDA = FontFactory.getFont(FontFactory.HELVETICA, 8);

    // ===================== PDF - CASO SOCIAL (Ficha de Registro) =====================
    public static void generarPDFCasoSocial(CasoSocial caso, String rutaArchivo) throws IOException {
        try {
            Document document = new Document(PageSize.A4, 30, 30, 30, 30);
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            agregarTitulo(document, "PASTORAL SOCIAL DIÓCESIS DE CARTAGO", "FICHA DE REGISTRO DE PARTICIPANTES");

            // Datos del registro
            agregarSeccion(document, "DATOS DEL REGISTRO");
            PdfPTable tablaRegistro = new PdfPTable(3);
            tablaRegistro.setWidthPercentage(100);
            agregarFilaEtiquetaValor(tablaRegistro, "Ficha N°", safe(caso.getNumeroFicha()));
            agregarFilaEtiquetaValor(tablaRegistro, "Fecha de inicio", formatearFecha(caso.getFechaInicio()));
            agregarFilaEtiquetaValor(tablaRegistro, "Fecha prevista de conclusión", formatearFecha(caso.getFechaPrevistaFin()));
            document.add(tablaRegistro);

            // Datos parroquiales
            agregarSeccion(document, "DATOS PARROQUIALES");
            PdfPTable tablaParroquia = new PdfPTable(2);
            tablaParroquia.setWidthPercentage(100);
            agregarFilaEtiquetaValor(tablaParroquia, "Parroquia", safe(caso.getParroquia()));
            agregarFilaEtiquetaValor(tablaParroquia, "Sector o filial", safe(caso.getSectorFilial()));
            document.add(tablaParroquia);

            // Datos generales / dirección
            agregarSeccion(document, "DATOS GENERALES");
            PdfPTable tablaGenerales = new PdfPTable(2);
            tablaGenerales.setWidthPercentage(100);
            agregarFilaEtiquetaValor(tablaGenerales, "Dirección", safe(caso.getDireccion()));
            agregarFilaEtiquetaValor(tablaGenerales, "Provincia", safe(caso.getProvincia()));
            agregarFilaEtiquetaValor(tablaGenerales, "Cantón", safe(caso.getCanton()));
            agregarFilaEtiquetaValor(tablaGenerales, "Distrito", safe(caso.getDistrito()));
            document.add(tablaGenerales);

            // Participantes
            agregarSeccion(document, "PERSONAS PARTICIPANTES");
            List<IntegranteFamiliar> integrantes = obtenerIntegrantesDelCaso(caso);
            document.add(construirTablaIntegrantes(integrantes));

            // Vivienda
            agregarSeccion(document, "DATOS DE LA VIVIENDA");
            PdfPTable tablaVivienda = new PdfPTable(3);
            tablaVivienda.setWidthPercentage(100);
            agregarFilaEtiquetaValor(tablaVivienda, "Tipo de vivienda",
                    caso.getDetalleTipoVivienda() != null ? caso.getDetalleTipoVivienda().getDescripcion() : "");
            agregarFilaEtiquetaValor(tablaVivienda, "Tenencia",
                    caso.getDetalleTenenciaVivienda() != null ? caso.getDetalleTenenciaVivienda().getDescripcion() : "");
            agregarFilaEtiquetaValor(tablaVivienda, "Condición",
                    caso.getDetalleCondicionVivienda() != null ? caso.getDetalleCondicionVivienda().getDescripcion() : "");
            document.add(tablaVivienda);

            // Ingreso familiar
            agregarSeccion(document, "DATOS DEL INGRESO FAMILIAR");
            List<IngresoFamiliar> ingresos = obtenerIngresosDelCaso(caso);
            document.add(construirTablaIngresos(ingresos));

            // Asistencias
            agregarSeccion(document, "ASISTENCIAS BRINDADAS");
            List<Asistencia> asistencias = obtenerAsistenciasDelCaso(caso);
            document.add(construirTablaAsistencias(asistencias));

            // Estado y responsable
            agregarSeccion(document, "ESTADO Y RESPONSABLE");
            PdfPTable tablaEstado = new PdfPTable(2);
            tablaEstado.setWidthPercentage(100);
            agregarFilaEtiquetaValor(tablaEstado, "Estado",
                    caso.getDetalleEstado() != null ? caso.getDetalleEstado().toString() : "");
            String responsable = "";
            if (caso.getUsuario() != null) {
                responsable = safe(caso.getUsuario().getNombre()) + " " + safe(caso.getUsuario().getApellidos());
            }
            agregarFilaEtiquetaValor(tablaEstado, "Responsable", responsable);
            document.add(tablaEstado);

            // Observaciones
            if (caso.getObservaciones() != null && !caso.getObservaciones().trim().isEmpty()) {
                agregarSeccion(document, "OBSERVACIONES");
                document.add(new Paragraph(caso.getObservaciones(), FONT_TEXTO));
            }

            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error generando el PDF: " + e.getMessage(), e);
        }
    }

    // ===================== EXCEL - CASO SOCIAL =====================
    public static void generarExcelCasoSocial(CasoSocial caso, String rutaArchivo) throws IOException {
        List<IntegranteFamiliar> integrantes = obtenerIntegrantesDelCaso(caso);
        List<IngresoFamiliar> ingresos = obtenerIngresosDelCaso(caso);
        List<Asistencia> asistencias = obtenerAsistenciasDelCaso(caso);

        String responsable = "";
        if (caso.getUsuario() != null) {
            responsable = safe(caso.getUsuario().getNombre()) + " " + safe(caso.getUsuario().getApellidos());
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"></head><body>\n");
        html.append("<h2>Ficha de Registro de Participantes</h2>\n");

        html.append("<h3>Datos del registro</h3><table border=\"1\">\n");
        fila(html, "Ficha N°", safe(caso.getNumeroFicha()));
        fila(html, "Fecha de inicio", formatearFecha(caso.getFechaInicio()));
        fila(html, "Fecha prevista de conclusión", formatearFecha(caso.getFechaPrevistaFin()));
        html.append("</table>\n");

        html.append("<h3>Datos parroquiales y generales</h3><table border=\"1\">\n");
        fila(html, "Parroquia", safe(caso.getParroquia()));
        fila(html, "Sector o filial", safe(caso.getSectorFilial()));
        fila(html, "Dirección", safe(caso.getDireccion()));
        fila(html, "Provincia", safe(caso.getProvincia()));
        fila(html, "Cantón", safe(caso.getCanton()));
        fila(html, "Distrito", safe(caso.getDistrito()));
        html.append("</table>\n");

        html.append("<h3>Personas participantes</h3><table border=\"1\">\n");
        html.append("<tr><th>Nombre</th><th>Identificación</th><th>Sexo</th><th>Jefatura</th><th>Relación</th>"
                + "<th>Edad</th><th>País</th><th>Migración</th><th>Educación</th><th>Salud</th><th>Seguro</th></tr>\n");
        for (IntegranteFamiliar i : integrantes) {
            html.append("<tr>")
                .append("<td>").append(safe(i.getNombre())).append(" ").append(safe(i.getApellidos())).append("</td>")
                .append("<td>").append(safe(i.getTipoIdentificacion())).append(" ").append(safe(i.getIdentificacion())).append("</td>")
                .append("<td>").append(i.getDetalleGenero() != null ? i.getDetalleGenero().getDescripcion() : "").append("</td>")
                .append("<td>").append(i.isJefeHogar() ? "Sí" : "No").append("</td>")
                .append("<td>").append(i.getDetalleParentesco() != null ? i.getDetalleParentesco().getDescripcion() : "").append("</td>")
                .append("<td>").append(calcularEdad(i.getFechaNacimiento())).append("</td>")
                .append("<td>").append(safe(i.getPais())).append("</td>")
                .append("<td>").append(safe(i.getMigracion())).append("</td>")
                .append("<td>").append(i.getDetalleNivelEducativo() != null ? i.getDetalleNivelEducativo().getDescripcion() : "").append("</td>")
                .append("<td>").append(safe(i.getSalud())).append("</td>")
                .append("<td>").append(i.getDetalleSeguro() != null ? i.getDetalleSeguro().getDescripcion() : "").append("</td>")
                .append("</tr>\n");
        }
        html.append("</table>\n");

        html.append("<h3>Datos de la vivienda</h3><table border=\"1\">\n");
        fila(html, "Tipo de vivienda", caso.getDetalleTipoVivienda() != null ? caso.getDetalleTipoVivienda().getDescripcion() : "");
        fila(html, "Tenencia", caso.getDetalleTenenciaVivienda() != null ? caso.getDetalleTenenciaVivienda().getDescripcion() : "");
        fila(html, "Condición", caso.getDetalleCondicionVivienda() != null ? caso.getDetalleCondicionVivienda().getDescripcion() : "");
        html.append("</table>\n");

        html.append("<h3>Datos del ingreso familiar</h3><table border=\"1\">\n");
        html.append("<tr><th>Persona</th><th>Ocupación/Pensión</th><th>¿Trabaja?</th><th>Ingreso mensual</th></tr>\n");
        for (IngresoFamiliar ing : ingresos) {
            html.append("<tr>")
                .append("<td>").append(ing.getIntegrante() != null ? ing.getIntegrante().toString() : "").append("</td>")
                .append("<td>").append(safe(ing.getOcupacion())).append("</td>")
                .append("<td>").append(ing.isTrabaja() ? "Sí" : "No").append("</td>")
                .append("<td>").append(ing.getIngresoMensual() != null ? ing.getIngresoMensual().toString() : "").append("</td>")
                .append("</tr>\n");
        }
        html.append("</table>\n");

        html.append("<h3>Asistencias brindadas</h3><table border=\"1\">\n");
        html.append("<tr><th>Fecha</th><th>Tipo</th><th>Modalidad</th><th>Frecuencia</th><th>Duración</th><th>Valor</th></tr>\n");
        for (Asistencia a : asistencias) {
            html.append("<tr>")
                .append("<td>").append(formatearFecha(a.getFecha())).append("</td>")
                .append("<td>").append(a.getDetalleTipoAsistencia() != null ? a.getDetalleTipoAsistencia().getDescripcion() : "").append("</td>")
                .append("<td>").append(safe(a.getModalidad())).append("</td>")
                .append("<td>").append(safe(a.getFrecuencia())).append("</td>")
                .append("<td>").append(safe(a.getDuracion())).append("</td>")
                .append("<td>").append(a.getValor() != null ? a.getValor().toString() : "").append("</td>")
                .append("</tr>\n");
        }
        html.append("</table>\n");

        html.append("<h3>Estado y responsable</h3><table border=\"1\">\n");
        fila(html, "Estado", caso.getDetalleEstado() != null ? caso.getDetalleEstado().toString() : "");
        fila(html, "Responsable", responsable);
        html.append("</table>\n");

        if (caso.getObservaciones() != null && !caso.getObservaciones().trim().isEmpty()) {
            html.append("<h3>Observaciones</h3><p>").append(caso.getObservaciones()).append("</p>\n");
        }

        html.append("</body></html>");
        Files.write(Paths.get(rutaArchivo), html.toString().getBytes("UTF-8"));
    }

    // ===================== PDF - ASISTENCIAS DE UN CASO =====================
    public static void generarPDFAsistenciaCaso(CasoSocial caso, String rutaArchivo) throws IOException {
        try {
            Document document = new Document(PageSize.A4, 30, 30, 30, 30);
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            agregarTitulo(document, "PASTORAL SOCIAL DIÓCESIS DE CARTAGO",
                    "REPORTE DE ASISTENCIAS - CASO: " + safe(caso.getNumeroFicha()));

            List<Asistencia> asistencias = obtenerAsistenciasDelCaso(caso);
            document.add(construirTablaAsistencias(asistencias));

            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error generando el PDF: " + e.getMessage(), e);
        }
    }

    // ===================== EXCEL - ASISTENCIAS DE UN CASO =====================
    public static void generarExcelAsistenciaCaso(CasoSocial caso, String rutaArchivo) throws IOException {
        List<Asistencia> asistencias = obtenerAsistenciasDelCaso(caso);

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"></head><body>\n");
        html.append("<h2>Reporte de Asistencias - Caso: ").append(safe(caso.getNumeroFicha())).append("</h2>\n");
        html.append("<table border=\"1\">\n<tr><th>Fecha</th><th>Tipo</th><th>Modalidad</th><th>Frecuencia</th><th>Duración</th><th>Valor</th></tr>\n");

        for (Asistencia a : asistencias) {
            html.append("<tr>")
                .append("<td>").append(formatearFecha(a.getFecha())).append("</td>")
                .append("<td>").append(a.getDetalleTipoAsistencia() != null ? a.getDetalleTipoAsistencia().getDescripcion() : "").append("</td>")
                .append("<td>").append(safe(a.getModalidad())).append("</td>")
                .append("<td>").append(safe(a.getFrecuencia())).append("</td>")
                .append("<td>").append(safe(a.getDuracion())).append("</td>")
                .append("<td>").append(a.getValor() != null ? a.getValor().toString() : "").append("</td>")
                .append("</tr>\n");
        }

        html.append("</table></body></html>");
        Files.write(Paths.get(rutaArchivo), html.toString().getBytes("UTF-8"));
    }

    // ===================== Helpers de datos =====================
    private static List<Asistencia> obtenerAsistenciasDelCaso(CasoSocial caso) {
        try {
            return new AsistenciaController().buscarPorCasoSocial(caso);
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    private static List<IntegranteFamiliar> obtenerIntegrantesDelCaso(CasoSocial caso) {
        try {
            return new IntegranteFamiliarController().buscarPorCasoSocial(caso);
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    private static List<IngresoFamiliar> obtenerIngresosDelCaso(CasoSocial caso) {
        try {
            return new IngresoFamiliarController().buscarPorCasoSocial(caso);
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    // ===================== Helpers de construcción PDF =====================
    private static void agregarTitulo(Document document, String titulo, String subtitulo) throws DocumentException {
        Paragraph pTitulo = new Paragraph(titulo, FONT_TITULO);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(pTitulo);

        Paragraph pSubtitulo = new Paragraph(subtitulo, FONT_SUBTITULO);
        pSubtitulo.setAlignment(Element.ALIGN_CENTER);
        pSubtitulo.setSpacingAfter(10);
        document.add(pSubtitulo);
    }

    private static void agregarSeccion(Document document, String textoSeccion) throws DocumentException {
        Paragraph p = new Paragraph(textoSeccion, FONT_SECCION);
        p.setSpacingBefore(10);
        p.setSpacingAfter(4);
        document.add(p);
    }

    private static void agregarFilaEtiquetaValor(PdfPTable tabla, String etiqueta, String valor) {
        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta + ":", FONT_ETIQUETA));
        celdaEtiqueta.setPadding(4);
        tabla.addCell(celdaEtiqueta);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor != null ? valor : "", FONT_TEXTO));
        celdaValor.setPadding(4);
        if (tabla.getNumberOfColumns() > 2) {
            celdaValor.setColspan(tabla.getNumberOfColumns() - 1);
        }
        tabla.addCell(celdaValor);
    }

    private static PdfPTable construirTablaIntegrantes(List<IntegranteFamiliar> integrantes) {
        PdfPTable tabla = new PdfPTable(new float[]{3, 1, 1.2f, 1.2f, 0.7f, 1, 1.2f, 1.2f, 1.2f});
        tabla.setWidthPercentage(100);
        String[] encabezados = {"Nombre / Identificación", "Sexo", "Jefatura", "Relación", "Edad", "País", "Migración", "Educación", "Seguro"};
        for (String enc : encabezados) {
            PdfPCell c = new PdfPCell(new Phrase(enc, FONT_ENCABEZADO_TABLA));
            c.setPadding(3);
            tabla.addCell(c);
        }

        if (integrantes.isEmpty()) {
            PdfPCell vacio = new PdfPCell(new Phrase("No hay integrantes registrados.", FONT_CELDA));
            vacio.setColspan(9);
            vacio.setPadding(4);
            tabla.addCell(vacio);
        }

        for (IntegranteFamiliar i : integrantes) {
            agregarCelda(tabla, safe(i.getNombre()) + " " + safe(i.getApellidos()) + "\n" + safe(i.getTipoIdentificacion()) + " " + safe(i.getIdentificacion()));
            agregarCelda(tabla, i.getDetalleGenero() != null ? i.getDetalleGenero().getDescripcion() : "");
            agregarCelda(tabla, i.isJefeHogar() ? "Sí" : "No");
            agregarCelda(tabla, i.getDetalleParentesco() != null ? i.getDetalleParentesco().getDescripcion() : "");
            agregarCelda(tabla, calcularEdad(i.getFechaNacimiento()));
            agregarCelda(tabla, safe(i.getPais()));
            agregarCelda(tabla, safe(i.getMigracion()));
            agregarCelda(tabla, i.getDetalleNivelEducativo() != null ? i.getDetalleNivelEducativo().getDescripcion() : "");
            agregarCelda(tabla, i.getDetalleSeguro() != null ? i.getDetalleSeguro().getDescripcion() : "");
        }
        return tabla;
    }

    private static PdfPTable construirTablaIngresos(List<IngresoFamiliar> ingresos) {
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        String[] encabezados = {"Persona", "Ocupación/Pensión", "¿Trabaja?", "Ingreso mensual"};
        for (String enc : encabezados) {
            PdfPCell c = new PdfPCell(new Phrase(enc, FONT_ENCABEZADO_TABLA));
            c.setPadding(3);
            tabla.addCell(c);
        }

        if (ingresos.isEmpty()) {
            PdfPCell vacio = new PdfPCell(new Phrase("No hay ingresos registrados.", FONT_CELDA));
            vacio.setColspan(4);
            vacio.setPadding(4);
            tabla.addCell(vacio);
        }

        for (IngresoFamiliar ing : ingresos) {
            agregarCelda(tabla, ing.getIntegrante() != null ? ing.getIntegrante().toString() : "");
            agregarCelda(tabla, safe(ing.getOcupacion()));
            agregarCelda(tabla, ing.isTrabaja() ? "Sí" : "No");
            agregarCelda(tabla, ing.getIngresoMensual() != null ? ing.getIngresoMensual().toString() : "");
        }
        return tabla;
    }

    private static PdfPTable construirTablaAsistencias(List<Asistencia> asistencias) {
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        String[] encabezados = {"Fecha", "Tipo", "Modalidad", "Frecuencia/Duración", "Valor"};
        for (String enc : encabezados) {
            PdfPCell c = new PdfPCell(new Phrase(enc, FONT_ENCABEZADO_TABLA));
            c.setPadding(3);
            tabla.addCell(c);
        }

        if (asistencias.isEmpty()) {
            PdfPCell vacio = new PdfPCell(new Phrase("No hay asistencias registradas para este caso.", FONT_CELDA));
            vacio.setColspan(5);
            vacio.setPadding(4);
            tabla.addCell(vacio);
        }

        for (Asistencia a : asistencias) {
            agregarCelda(tabla, formatearFecha(a.getFecha()));
            agregarCelda(tabla, a.getDetalleTipoAsistencia() != null ? a.getDetalleTipoAsistencia().getDescripcion() : "");
            agregarCelda(tabla, safe(a.getModalidad()));
            agregarCelda(tabla, safe(a.getFrecuencia()) + (a.getDuracion() != null ? " / " + a.getDuracion() : ""));
            agregarCelda(tabla, a.getValor() != null ? a.getValor().toString() : "");
        }
        return tabla;
    }

    private static void agregarCelda(PdfPTable tabla, String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto != null ? texto : "", FONT_CELDA));
        c.setPadding(3);
        tabla.addCell(c);
    }

    // ===================== Helpers generales =====================
    private static String safe(String s) {
        return s != null ? s : "";
    }

    private static String formatearFecha(Date fecha) {
        return fecha != null ? FORMATO_FECHA.format(fecha) : "";
    }

    private static String calcularEdad(Date fechaNacimiento) {
        if (fechaNacimiento == null) {
            return "";
        }
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.setTime(fechaNacimiento);
        Calendar hoy = Calendar.getInstance();

        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        return String.valueOf(edad);
    }

    private static void fila(StringBuilder html, String campo, String valor) {
        html.append("<tr><th>").append(campo).append("</th><td>").append(valor).append("</td></tr>\n");
    }
}