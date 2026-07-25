package util;

/**
 * Catálogos de valores controlados usados por los combos de la interfaz.
 *
 * <p>Centraliza en un solo lugar las listas de opciones seleccionables de la
 * entidad {@code Persona} (parentesco, país, migración, educación, seguro), de
 * modo que:
 * <ul>
 *   <li>no queden "quemadas" y dispersas dentro de cada formulario;</li>
 *   <li>el usuario no pueda escribir valores arbitrarios que generen
 *       duplicados o inconsistencias;</li>
 *   <li>sea trivial reutilizarlas y mantenerlas.</li>
 * </ul>
 *
 * <p>Las parroquias y sectores, por ser datos propios de la organización y
 * cambiantes, se gestionan como catálogos persistentes en la base de datos
 * ({@code model.Parroquia} / {@code model.SectorFilial}); estas listas, en
 * cambio, son vocabularios fijos y pequeños, coherentes con el uso de enums
 * que ya hacía el proyecto para la vivienda.
 */
public final class Catalogos {

    private Catalogos() {
    }

    /** Opción en blanco para permitir "sin seleccionar" en los combos. */
    public static final String VACIO = "";

    public static final String[] PARENTESCO = {
        VACIO, "Jefe(a) de hogar", "Cónyuge / Pareja", "Hijo(a)", "Padre / Madre",
        "Hermano(a)", "Abuelo(a)", "Nieto(a)", "Otro familiar", "No familiar"
    };

    public static final String[] SEXO = { VACIO, "F", "M", "OTRO" };

    public static final String[] PAIS = {
        VACIO, "Costa Rica", "Nicaragua", "Panamá", "Colombia", "Venezuela",
        "El Salvador", "Honduras", "Guatemala", "Cuba", "Otro"
    };

    public static final String[] MIGRACION = {
        VACIO, "Nacional", "Residente", "Refugiado(a)", "Solicitante de refugio",
        "Permiso laboral", "Irregular", "Otro"
    };

    public static final String[] EDUCACION = {
        VACIO, "Ninguna", "Primaria incompleta", "Primaria completa",
        "Secundaria incompleta", "Secundaria completa", "Técnica",
        "Universitaria incompleta", "Universitaria completa"
    };

    public static final String[] SEGURO = {
        VACIO, "Asegurado directo", "Asegurado familiar", "Por el Estado",
        "Pensionado", "No asegurado"
    };
}
