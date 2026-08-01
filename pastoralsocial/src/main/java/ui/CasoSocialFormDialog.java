package ui;

import dao.AsistenciaDAO;
import dao.BitacoraDAO;
import dao.CatalogoViviendaDAO;
import dao.ExpedienteDAO;
import dao.ParroquiaDAO;
import dao.PersonaDAO;
import dao.ProlongacionAyudaDAO;
import model.Asistencia;
import model.Bitacora;
import model.CondicionVivienda;
import model.Expediente;
import model.Parroquia;
import model.Persona;
import model.ProlongacionAyuda;
import model.SectorFilial;
import model.Tenencia;
import model.TipoVivienda;
import util.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// armé esta pantalla como una sola hoja larga con scroll, siguiendo el mismo orden
// y los mismos títulos de la ficha de papel de la Pastoral.
//
// lo importante: la ficha se llena COMPLETA y recién al darle Guardar se manda
// todo junto a la base, igual que en el papel. mientras tanto los participantes,
// las asistencias y las prolongaciones viven en memoria colgando del expediente,
// y la cascada de JPA los inserta a todos en la misma transacción. así no se
// gastan números de ficha en casos que después se cancelan.
//
// el mismo dialog sirve para crear, editar y solo consultar:
// - si le paso null, es un caso nuevo
// - si el caso está CERRADO o el que entró es Coordinador, se abre solo lectura
public class CasoSocialFormDialog extends JDialog {

    private ExpedienteDAO expedienteDAO = new ExpedienteDAO();
    private ParroquiaDAO parroquiaDAO = new ParroquiaDAO();
    private CatalogoViviendaDAO catalogoDAO = new CatalogoViviendaDAO();
    private PersonaDAO personaDAO = new PersonaDAO();
    private AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private ProlongacionAyudaDAO prolongacionDAO = new ProlongacionAyudaDAO();
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

    private Expediente expediente;
    private boolean esNuevo;
    private boolean soloLectura;
    private boolean guardado = false;

    // ------------------------------------------------------ datos del registro
    private JTextField txtFicha = new JTextField(18);
    private JTextField txtFechaInicio = new JTextField(18);
    private JTextField txtFechaFin = new JTextField(18);

    private List<ProlongacionAyuda> prolongaciones;
    private DefaultTableModel modeloProlongaciones = new DefaultTableModel(
            new Object[] { "Fecha de prolongación" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tablaProlongaciones = new JTable(modeloProlongaciones);

    // ---------------------------------------------------- datos parroquiales
    private JComboBox<Parroquia> cmbParroquia = new JComboBox<Parroquia>();
    private JComboBox<SectorFilial> cmbSector = new JComboBox<SectorFilial>();

    // ------------------------------------------------------- datos generales
    private JTextField txtDireccion = new JTextField(30);
    private JTextField txtTelefono = new JTextField(20);

    private List<Persona> familiares;
    private DefaultTableModel modeloFamilia = new DefaultTableModel(
            new Object[] { "Apellidos y nombres", "Doc. identidad", "Sexo", "Jefatura",
                           "Relación", "Edad", "País", "Migración", "Educación",
                           "Salud", "Seguro", "Estado" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tablaFamilia = new JTable(modeloFamilia);

    // ------------------------------------------------------ datos de vivienda
    private JComboBox<TipoVivienda> cmbTipoVivienda = new JComboBox<TipoVivienda>();
    private JComboBox<Tenencia> cmbTenencia = new JComboBox<Tenencia>();
    private JComboBox<CondicionVivienda> cmbCondicion = new JComboBox<CondicionVivienda>();

    // ------------------------------------------------------- ingreso familiar
    private DefaultTableModel modeloIngresos = new DefaultTableModel(
            new Object[] { "Persona", "Ocupación / Pensión", "¿Trabaja?",
                           "Ingreso mensual aproximado" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tablaIngresos = new JTable(modeloIngresos);
    private JLabel lblTotalIngreso = new JLabel();

    // ------------------------------------------------------------ asistencias
    private List<Asistencia> asistencias;
    private DefaultTableModel modeloAsistencias = new DefaultTableModel(
            new Object[] { "Tipo de asistencia", "Modalidad", "Frecuencia", "Duración",
                           "Valor", "Solicitud", "Entrega", "Estado" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tablaAsistencias = new JTable(modeloAsistencias);

    private DefaultTableModel modeloTotales = new DefaultTableModel(
            new Object[] { "Tipo de asistencia", "Cantidad", "Total" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private JTable tablaTotales = new JTable(modeloTotales);

    // ------------------------------------------------------------------ cierre
    private JTextField txtEntrevistador = new JTextField(30);
    private JTextArea txtObservaciones = new JTextArea(4, 30);

    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public CasoSocialFormDialog(JFrame padre, Expediente expediente) {
        super(padre, true);

        this.esNuevo = (expediente == null);

        // si es nuevo creo el expediente de una, aunque todavía no lo guarde.
        // así los participantes y demás ya tienen a quién colgarse en memoria.
        this.expediente = esNuevo ? new Expediente() : expediente;

        // el coordinador es solo lectura, y un caso cerrado tampoco se edita
        this.soloLectura = !Sesion.puedeEditar()
            || (!esNuevo && !this.expediente.estaActivo());

        // mientras el caso está en memoria, las listas que muestro son las mismas
        // del expediente: así lo que agrego se va guardando solo para la cascada
        if (esNuevo) {
            familiares = this.expediente.getPersonas();
            asistencias = this.expediente.getAsistencias();
            prolongaciones = this.expediente.getProlongaciones();
        }

        setTitle(tituloVentana());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(940, 760);
        setLocationRelativeTo(padre);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel(tituloVentana());
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        // acá voy apilando las secciones una debajo de otra, en el mismo orden
        // en que aparecen en la ficha de papel
        JPanel contenido = new JPanel(new GridBagLayout());
        int fila = 0;
        apilar(contenido, fila++, seccionDatosRegistro());
        apilar(contenido, fila++, seccionDatosParroquiales());
        apilar(contenido, fila++, seccionDatosGenerales());
        apilar(contenido, fila++, seccionVivienda());
        apilar(contenido, fila++, seccionIngresoFamiliar());
        apilar(contenido, fila++, seccionAsistencias());
        apilar(contenido, fila++, seccionCierre());
        rellenar(contenido, fila);

        JScrollPane scroll = new JScrollPane(contenido);
        // sin esto la rueda del mouse mueve la hoja de a poquitos y desespera
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelBotones(), BorderLayout.SOUTH);
        add(panel);

        // el número de ficha se muestra pero NUNCA se escribe a mano (observación B)
        txtFicha.setEditable(false);
        txtFicha.setBackground(new Color(235, 235, 235));

        cargarCombos();

        // al cambiar de parroquia recargo los sectores, porque el sector depende
        // de la parroquia (es el detalle del catálogo)
        cmbParroquia.addActionListener(e -> recargarSectores());

        if (esNuevo) {
            txtFicha.setText("(se genera al guardar)");
            txtFechaInicio.setText(formato.format(new Date()));
        } else {
            cargarDatos();
        }

        recargarProlongaciones();
        recargarFamilia();
        recargarAsistencias();

        if (soloLectura) {
            bloquearCampos();
        }
    }

    public boolean seGuardo() {
        return guardado;
    }

    private String tituloVentana() {
        if (esNuevo) {
            return "Ficha de registro de participantes - Caso nuevo";
        }
        return soloLectura
            ? "Ficha de registro de participantes - Consulta"
            : "Ficha de registro de participantes";
    }

    // ======================================================== armado de la hoja

    private void apilar(JPanel contenedor, int fila, JComponent seccion) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        contenedor.add(seccion, gbc);
    }

    // un relleno al final que se come el espacio sobrante, si no las secciones
    // se estiran feo cuando la ventana es más alta que el contenido
    private void rellenar(JPanel contenedor, int fila) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contenedor.add(new JPanel(), gbc);
    }

    // me evita repetir el bloque de GridBagConstraints en cada campo
    private void campo(JPanel panel, int fila, String etiqueta, JComponent componente) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(componente, gbc);
    }

    private JPanel seccion(String titulo) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        return p;
    }

    private JScrollPane conAlto(JTable tabla, int alto) {
        // con AUTO_RESIZE_OFF las columnas mantienen su ancho y aparece scroll
        // horizontal, que es lo que necesito en la tabla de participantes
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(100, alto));
        return sp;
    }

    private JPanel acciones() {
        return new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    }

    // ================================================== 1. DATOS DEL REGISTRO

    private JPanel seccionDatosRegistro() {
        JPanel p = seccion("DATOS DEL REGISTRO");

        JPanel campos = new JPanel(new GridBagLayout());
        int fila = 0;
        campo(campos, fila++, "Ficha N°:", txtFicha);
        campo(campos, fila++, "Fecha de inicio:", txtFechaInicio);
        campo(campos, fila++, "Fecha prevista de conclusión:", txtFechaFin);
        campo(campos, fila++, "", new JLabel("Las fechas van en formato dd/mm/aaaa"));

        JPanel prolong = new JPanel(new BorderLayout(10, 10));
        prolong.setBorder(BorderFactory.createTitledBorder("Prolongaciones de la ayuda"));
        prolong.add(conAlto(tablaProlongaciones, 90), BorderLayout.CENTER);

        if (!soloLectura) {
            JButton btnAgregar = new JButton("Agregar fecha");
            JButton btnQuitar = new JButton("Quitar");
            btnAgregar.addActionListener(e -> agregarProlongacion());
            btnQuitar.addActionListener(e -> quitarProlongacion());
            JPanel acc = acciones();
            acc.add(btnAgregar);
            acc.add(btnQuitar);
            prolong.add(acc, BorderLayout.SOUTH);
        }

        p.add(campos, BorderLayout.NORTH);
        p.add(prolong, BorderLayout.CENTER);
        return p;
    }

    private void recargarProlongaciones() {
        if (!esNuevo) {
            prolongaciones = prolongacionDAO.listarPorExpediente(expediente.getId());
        }

        modeloProlongaciones.setRowCount(0);
        for (ProlongacionAyuda pr : prolongaciones) {
            modeloProlongaciones.addRow(new Object[] { formatear(pr.getFecha()) });
        }
    }

    private void agregarProlongacion() {
        String texto = JOptionPane.showInputDialog(this,
            "Fecha de la prolongación (dd/mm/aaaa):",
            "Prolongación de la ayuda", JOptionPane.PLAIN_MESSAGE);

        if (texto == null) {
            return;
        }

        Date fecha;
        try {
            formato.setLenient(false);
            fecha = formato.parse(texto.trim());
        } catch (ParseException e) {
            error("La fecha tiene que ir en formato dd/mm/aaaa.");
            return;
        }

        if (esNuevo) {
            // todavía no hay base: la cuelgo del expediente y viaja con la cascada
            expediente.agregarProlongacion(new ProlongacionAyuda(expediente, fecha));
        } else {
            prolongacionDAO.guardar(new ProlongacionAyuda(expediente, fecha));
        }
        recargarProlongaciones();
    }

    private void quitarProlongacion() {
        int fila = tablaProlongaciones.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una fecha de la lista.");
            return;
        }

        if (esNuevo) {
            prolongaciones.remove(fila);
        } else {
            prolongacionDAO.eliminar(prolongaciones.get(fila).getId());
        }
        recargarProlongaciones();
    }

    // ================================================ 2. DATOS PARROQUIALES

    private JPanel seccionDatosParroquiales() {
        JPanel p = seccion("DATOS PARROQUIALES");
        JPanel campos = new JPanel(new GridBagLayout());
        campo(campos, 0, "Parroquia:", cmbParroquia);
        campo(campos, 1, "Sector o filial:", cmbSector);
        p.add(campos, BorderLayout.CENTER);
        return p;
    }

    // ============ 3. DATOS GENERALES DE LAS PERSONAS PARTICIPANTES

    private JPanel seccionDatosGenerales() {
        JPanel p = seccion("DATOS GENERALES DE LAS PERSONAS PARTICIPANTES");

        JPanel campos = new JPanel(new GridBagLayout());
        campo(campos, 0, "Dirección:", txtDireccion);
        campo(campos, 1, "Teléfonos de contacto:", txtTelefono);

        JPanel tabla = new JPanel(new BorderLayout(10, 10));
        tabla.add(conAlto(tablaFamilia, 150), BorderLayout.CENTER);

        JPanel acc = acciones();
        if (!soloLectura) {
            JButton btnAgregar = new JButton("Agregar participante");
            btnAgregar.addActionListener(e -> agregarIntegrante());
            acc.add(btnAgregar);
        }
        JButton btnAbrir = new JButton(soloLectura ? "Consultar" : "Editar");
        btnAbrir.addActionListener(e -> editarIntegrante());
        acc.add(btnAbrir);
        if (!soloLectura) {
            JButton btnEstado = new JButton(esNuevo ? "Quitar" : "Retirar / Reactivar");
            btnEstado.addActionListener(e -> cambiarEstadoIntegrante());
            acc.add(btnEstado);
        }
        tabla.add(acc, BorderLayout.SOUTH);

        p.add(campos, BorderLayout.NORTH);
        p.add(tabla, BorderLayout.CENTER);
        return p;
    }

    private void recargarFamilia() {
        if (!esNuevo) {
            familiares = personaDAO.listarPorExpediente(expediente.getId());
        }

        modeloFamilia.setRowCount(0);
        for (Persona p : familiares) {
            Integer edad = p.calcularEdad();
            modeloFamilia.addRow(new Object[] {
                p.getApellidosNombres(),
                textoSeguro(p.getDocumentoIdentidad()),
                (p.getSexo() == null) ? "" : p.getSexo().name(),
                p.isJefatura() ? "Sí" : "",
                textoSeguro(p.getRelacion()),
                (edad == null) ? "" : edad,
                textoSeguro(p.getPais()),
                textoSeguro(p.getMigracion()),
                textoSeguro(p.getEducacion()),
                textoSeguro(p.getSalud()),
                textoSeguro(p.getSeguro()),
                p.isActivo() ? "Activo" : "Retirado"
            });
        }

        // la tabla de ingreso familiar sale de la misma gente, así que la refresco
        // de una vez para que no queden desfasadas
        recargarIngresos();
    }

    private Persona integranteSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un participante de la lista.");
            return null;
        }
        return familiares.get(fila);
    }

    private void agregarIntegrante() {
        // el último parámetro es el que decide si el dialog guarda en la base o
        // solo me devuelve el objeto para tenerlo en memoria
        PersonaDialog dialog = new PersonaDialog(this, expediente, null, false, !esNuevo);
        dialog.setVisible(true);

        if (dialog.seGuardo()) {
            if (esNuevo) {
                Persona nueva = dialog.getResultado();
                expediente.agregarPersona(nueva);
                aplicarJefaturaUnica(nueva);
            }
            recargarFamilia();
        }
    }

    private void editarIntegrante() {
        Persona p = integranteSeleccionado(tablaFamilia);
        if (p == null) {
            return;
        }
        // un integrante retirado lo dejo ver pero no editar
        boolean bloqueado = soloLectura || !p.isActivo();
        PersonaDialog dialog = new PersonaDialog(this, expediente, p, bloqueado, !esNuevo);
        dialog.setVisible(true);

        if (dialog.seGuardo()) {
            if (esNuevo) {
                aplicarJefaturaUnica(p);
            }
            recargarFamilia();
        }
    }

    // la regla del jefe/a de hogar único la resuelve el DAO con un UPDATE cuando
    // el caso ya está en la base. mientras está en memoria la aplico yo a mano.
    private void aplicarJefaturaUnica(Persona jefe) {
        if (jefe == null || !jefe.isJefatura()) {
            return;
        }
        for (Persona otro : familiares) {
            if (otro != jefe) {
                otro.setJefatura(false);
            }
        }
    }

    private void cambiarEstadoIntegrante() {
        Persona p = integranteSeleccionado(tablaFamilia);
        if (p == null) {
            return;
        }

        // si el caso todavía no se guardó, no hay historial que conservar:
        // simplemente lo saco de la ficha
        if (esNuevo) {
            int r = JOptionPane.showConfirmDialog(this,
                "¿Quitar a " + p.getApellidosNombres() + " de la ficha?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                familiares.remove(p);
                recargarFamilia();
            }
            return;
        }

        // reactivar no tiene traba, lo hago de una
        if (!p.isActivo()) {
            personaDAO.cambiarEstado(p.getId(), true);
            recargarFamilia();
            return;
        }

        // para retirar sí: todo caso activo necesita al menos un integrante activo
        if (personaDAO.contarActivos(expediente.getId()) <= 1) {
            error("No podés retirar al único participante activo del caso.");
            return;
        }

        String mensaje = "¿Retirar a " + p.getApellidosNombres() + " del grupo familiar?\n"
            + "No se borra nada, solo queda marcado como retirado.";

        if (p.isJefatura()) {
            mensaje += "\n\nOjo: es el/la jefe/a de hogar, así que el caso va a quedar\n"
                     + "sin jefatura hasta que le asignés esa condición a otro.";
        }

        int confirmar = JOptionPane.showConfirmDialog(this, mensaje,
            "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        personaDAO.cambiarEstado(p.getId(), false);
        recargarFamilia();
    }

    // ================================================ 4. DATOS DE LA VIVIENDA

    private JPanel seccionVivienda() {
        JPanel p = seccion("DATOS DE LA VIVIENDA");
        JPanel campos = new JPanel(new GridBagLayout());
        campo(campos, 0, "Tipo de vivienda:", cmbTipoVivienda);
        campo(campos, 1, "Tenencia:", cmbTenencia);
        campo(campos, 2, "Condición de la vivienda:", cmbCondicion);
        p.add(campos, BorderLayout.CENTER);
        return p;
    }

    // =========================================== 5. DATOS DEL INGRESO FAMILIAR

    private JPanel seccionIngresoFamiliar() {
        JPanel p = seccion("DATOS DEL INGRESO FAMILIAR");
        p.add(conAlto(tablaIngresos, 120), BorderLayout.CENTER);

        JPanel abajo = new JPanel(new BorderLayout(10, 5));
        abajo.add(lblTotalIngreso, BorderLayout.NORTH);

        if (!soloLectura) {
            JButton btnEditar = new JButton("Editar ingreso del participante");
            btnEditar.addActionListener(e -> editarIngreso());
            JPanel acc = acciones();
            acc.add(btnEditar);
            abajo.add(acc, BorderLayout.SOUTH);
        }

        p.add(abajo, BorderLayout.SOUTH);
        return p;
    }

    private void recargarIngresos() {
        modeloIngresos.setRowCount(0);

        double total = 0;
        for (Persona p : familiares) {
            modeloIngresos.addRow(new Object[] {
                p.getApellidosNombres(),
                textoSeguro(p.getOcupacion()),
                p.isTrabaja() ? "Sí" : "No",
                (p.getIngresoMensual() == null) ? "" : p.getIngresoMensual()
            });

            if (p.isActivo() && p.getIngresoMensual() != null) {
                total += p.getIngresoMensual();
            }
        }

        // cuando el caso ya está en la base prefiero el SUM del DAO, que no depende
        // de que la lista en pantalla esté al día
        if (!esNuevo) {
            total = personaDAO.ingresoTotal(expediente.getId());
        }

        lblTotalIngreso.setText("  Ingreso mensual total del grupo familiar: " + total);
    }

    // la edición del ingreso la hago con el mismo PersonaDialog, que ya trae el
    // recuadro "Ingreso familiar" adentro. así no duplico validaciones.
    private void editarIngreso() {
        int fila = tablaIngresos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccioná un participante de esta tabla.");
            return;
        }

        Persona p = familiares.get(fila);
        PersonaDialog dialog = new PersonaDialog(this, expediente, p,
            !p.isActivo(), !esNuevo);
        dialog.setVisible(true);

        if (dialog.seGuardo()) {
            if (esNuevo) {
                aplicarJefaturaUnica(p);
            }
            recargarFamilia();
        }
    }

    // ============== 6. DATOS DE LA ASISTENCIA SOLICITADA Y BRINDADA

    private JPanel seccionAsistencias() {
        JPanel p = seccion("DATOS DE LA ASISTENCIA SOLICITADA Y BRINDADA");
        p.add(conAlto(tablaAsistencias, 130), BorderLayout.CENTER);

        JPanel totales = new JPanel(new BorderLayout(5, 5));
        totales.setBorder(BorderFactory.createTitledBorder("Totales por tipo"));
        totales.add(conAlto(tablaTotales, 90), BorderLayout.CENTER);

        JPanel acc = acciones();
        // solo se registran asistencias en casos activos (HU-15). el soloLectura
        // ya me cubre eso, porque se pone en true si el caso está cerrado.
        if (!soloLectura) {
            JButton btnRegistrar = new JButton("Registrar asistencia");
            btnRegistrar.addActionListener(e -> registrarAsistencia());
            acc.add(btnRegistrar);
        }
        JButton btnAbrir = new JButton(soloLectura ? "Consultar" : "Ver / Editar");
        btnAbrir.addActionListener(e -> abrirAsistencia());
        acc.add(btnAbrir);
        // anular es solo del administrador, y solo tiene sentido sobre algo ya guardado
        if (Sesion.esAdmin() && !esNuevo) {
            JButton btnAnular = new JButton("Anular");
            btnAnular.addActionListener(e -> anularAsistencia());
            acc.add(btnAnular);
        }

        JPanel abajo = new JPanel(new BorderLayout(10, 5));
        abajo.add(totales, BorderLayout.CENTER);
        abajo.add(acc, BorderLayout.SOUTH);

        p.add(abajo, BorderLayout.SOUTH);
        return p;
    }

    private void recargarAsistencias() {
        if (!esNuevo) {
            asistencias = asistenciaDAO.listarPorExpediente(expediente.getId());
        }

        modeloAsistencias.setRowCount(0);
        for (Asistencia a : asistencias) {
            modeloAsistencias.addRow(new Object[] {
                (a.getTipoAsistencia() == null) ? "" : a.getTipoAsistencia().getNombre(),
                textoSeguro(a.getModalidad()),
                textoSeguro(a.getFrecuencia()),
                textoSeguro(a.getDuracion()),
                (a.getValor() == null) ? "" : a.getValor(),
                formatear(a.getFechaSolicitud()),
                formatear(a.getFechaEntrega()),
                textoSeguro(a.getEstado())
            });
        }

        modeloTotales.setRowCount(0);
        if (esNuevo) {
            totalesEnMemoria();
        } else {
            for (Object[] fila : asistenciaDAO.totalesPorTipo(expediente.getId())) {
                // fila viene como {nombre del tipo, cantidad, suma}
                modeloTotales.addRow(new Object[] {
                    fila[0], fila[1], (fila[2] == null) ? 0 : fila[2]
                });
            }
        }
    }

    // el mismo GROUP BY del DAO pero a mano, para cuando todavía no hay base.
    // dejo por fuera las anuladas, igual que la consulta.
    private void totalesEnMemoria() {
        Map<String, double[]> porTipo = new LinkedHashMap<String, double[]>();

        for (Asistencia a : asistencias) {
            if (a.estaAnulada()) {
                continue;
            }
            String tipo = (a.getTipoAsistencia() == null)
                ? "" : a.getTipoAsistencia().getNombre();

            double[] acumulado = porTipo.get(tipo);
            if (acumulado == null) {
                acumulado = new double[2];   // [0] cantidad, [1] suma
                porTipo.put(tipo, acumulado);
            }
            acumulado[0]++;
            if (a.getValor() != null) {
                acumulado[1] += a.getValor();
            }
        }

        for (Map.Entry<String, double[]> e : porTipo.entrySet()) {
            modeloTotales.addRow(new Object[] {
                e.getKey(), (int) e.getValue()[0], e.getValue()[1]
            });
        }
    }

    private Asistencia asistenciaSeleccionada() {
        int fila = tablaAsistencias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una asistencia de la lista.");
            return null;
        }
        return asistencias.get(fila);
    }

    private void registrarAsistencia() {
        AsistenciaDialog dialog = new AsistenciaDialog(this, expediente, null,
            false, !esNuevo);
        dialog.setVisible(true);

        if (dialog.seGuardo()) {
            if (esNuevo) {
                expediente.agregarAsistencia(dialog.getResultado());
            }
            recargarAsistencias();
        }
    }

    private void abrirAsistencia() {
        Asistencia a = asistenciaSeleccionada();
        if (a == null) {
            return;
        }
        // una asistencia anulada se puede ver pero ya no se edita
        boolean bloqueada = soloLectura || a.estaAnulada();
        AsistenciaDialog dialog = new AsistenciaDialog(this, expediente, a,
            bloqueada, !esNuevo);
        dialog.setVisible(true);

        if (dialog.seGuardo()) {
            recargarAsistencias();
        }
    }

    private void anularAsistencia() {
        Asistencia a = asistenciaSeleccionada();
        if (a == null) {
            return;
        }
        if (a.estaAnulada()) {
            JOptionPane.showMessageDialog(this, "Esa asistencia ya está anulada.");
            return;
        }

        String motivo = JOptionPane.showInputDialog(this,
            "¿Por qué se anula esta asistencia?", "Anular asistencia",
            JOptionPane.PLAIN_MESSAGE);

        if (motivo == null) {
            return;
        }
        motivo = motivo.trim();
        if (motivo.isEmpty()) {
            error("Tenés que escribir el motivo de la anulación.");
            return;
        }

        // la anulación no borra la fila, solo cambia el estado, así queda la
        // evidencia de que esa ayuda se había registrado por error
        asistenciaDAO.anular(a.getId(), motivo);
        bitacoraDAO.registrar(Bitacora.ANULAR, "Asistencia",
            expediente.getNumeroFicha(), "Motivo: " + motivo);

        recargarAsistencias();
    }

    // ====================================== 7. entrevista y observaciones

    private JPanel seccionCierre() {
        JPanel p = seccion("ENTREVISTA Y OBSERVACIONES");

        JPanel campos = new JPanel(new GridBagLayout());
        campo(campos, 0, "Nombre de la persona que realizó la entrevista:",
            txtEntrevistador);

        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        campo(campos, 1, "Observaciones:", new JScrollPane(txtObservaciones));

        p.add(campos, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        // si es solo lectura no tiene sentido mostrar el botón de guardar
        if (!soloLectura) {
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardar());
            p.add(btnGuardar);
        }

        JButton btnCerrar = new JButton(soloLectura ? "Cerrar" : "Cancelar");
        btnCerrar.addActionListener(e -> dispose());
        p.add(btnCerrar);
        return p;
    }

    // ================================================================= cargar

    private void cargarCombos() {
        // el primer item va en null para poder dejar el campo sin elegir,
        // porque no todos son obligatorios
        cmbParroquia.addItem(null);
        for (Parroquia p : parroquiaDAO.listar()) {
            cmbParroquia.addItem(p);
        }

        cmbTipoVivienda.addItem(null);
        for (TipoVivienda t : catalogoDAO.listar(TipoVivienda.class)) {
            cmbTipoVivienda.addItem(t);
        }

        cmbTenencia.addItem(null);
        for (Tenencia t : catalogoDAO.listar(Tenencia.class)) {
            cmbTenencia.addItem(t);
        }

        cmbCondicion.addItem(null);
        for (CondicionVivienda c : catalogoDAO.listar(CondicionVivienda.class)) {
            cmbCondicion.addItem(c);
        }
    }

    private void recargarSectores() {
        cmbSector.removeAllItems();
        cmbSector.addItem(null);

        Parroquia parroquia = (Parroquia) cmbParroquia.getSelectedItem();
        if (parroquia == null) {
            return;
        }

        List<SectorFilial> sectores = parroquiaDAO.listarSectores(parroquia);
        for (SectorFilial s : sectores) {
            cmbSector.addItem(s);
        }
    }

    private void cargarDatos() {
        txtFicha.setText(expediente.getNumeroFicha());
        txtFechaInicio.setText(formatear(expediente.getFechaInicio()));
        txtFechaFin.setText(formatear(expediente.getFechaPrevistaFin()));
        txtDireccion.setText(textoSeguro(expediente.getDireccion()));
        txtTelefono.setText(textoSeguro(expediente.getTelefonoContacto()));
        txtEntrevistador.setText(textoSeguro(expediente.getEntrevistador()));
        txtObservaciones.setText(textoSeguro(expediente.getObservaciones()));

        // primero la parroquia, porque al seleccionarla se recargan los sectores
        // y recién ahí puedo marcar el sector que tenía guardado
        seleccionar(cmbParroquia, expediente.getParroquia() == null
            ? null : expediente.getParroquia().getId());

        seleccionar(cmbSector, expediente.getSectorFilial() == null
            ? null : expediente.getSectorFilial().getId());

        seleccionar(cmbTipoVivienda, expediente.getTipoVivienda() == null
            ? null : expediente.getTipoVivienda().getId());

        seleccionar(cmbTenencia, expediente.getTenencia() == null
            ? null : expediente.getTenencia().getId());

        seleccionar(cmbCondicion, expediente.getCondicionVivienda() == null
            ? null : expediente.getCondicionVivienda().getId());
    }

    // los objetos del combo vienen de otra consulta que los del expediente, así
    // que aunque sean la misma fila no son el mismo objeto. por eso comparo el id.
    private void seleccionar(JComboBox<?> combo, Integer id) {
        if (id == null) {
            combo.setSelectedItem(null);
            return;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item != null && id.equals(idDe(item))) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private Integer idDe(Object item) {
        if (item instanceof Parroquia) return ((Parroquia) item).getId();
        if (item instanceof SectorFilial) return ((SectorFilial) item).getId();
        if (item instanceof TipoVivienda) return ((TipoVivienda) item).getId();
        if (item instanceof Tenencia) return ((Tenencia) item).getId();
        if (item instanceof CondicionVivienda) return ((CondicionVivienda) item).getId();
        return null;
    }

    private void bloquearCampos() {
        txtFechaInicio.setEditable(false);
        txtFechaFin.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtEntrevistador.setEditable(false);
        txtObservaciones.setEditable(false);
        cmbParroquia.setEnabled(false);
        cmbSector.setEnabled(false);
        cmbTipoVivienda.setEnabled(false);
        cmbTenencia.setEnabled(false);
        cmbCondicion.setEnabled(false);
    }

    // ================================================================ guardar

    private void guardar() {
        Date fechaInicio;
        Date fechaFin;
        try {
            fechaInicio = leerFecha(txtFechaInicio, "fecha de inicio");
            fechaFin = leerFecha(txtFechaFin, "fecha prevista de conclusión");
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return;
        }

        Parroquia parroquia = (Parroquia) cmbParroquia.getSelectedItem();
        String entrevistador = txtEntrevistador.getText().trim();

        if (fechaInicio == null) {
            error("La fecha de inicio es obligatoria.");
            return;
        }
        if (parroquia == null) {
            error("Tenés que elegir la parroquia.\n"
                + "Si la lista está vacía, agregala primero en Parroquias y sectores.");
            return;
        }
        if (entrevistador.isEmpty()) {
            error("El nombre de quien realizó la entrevista es obligatorio.");
            return;
        }
        if (fechaFin != null && fechaFin.before(fechaInicio)) {
            error("La fecha prevista de conclusión no puede ser anterior a la de inicio.");
            return;
        }

        // un caso sin nadie no tiene sentido, y además la HU-14 pide que todo caso
        // activo tenga al menos un participante
        if (esNuevo && familiares.isEmpty()) {
            error("Agregá al menos un participante antes de guardar la ficha.");
            return;
        }

        expediente.setFechaInicio(fechaInicio);
        expediente.setFechaPrevistaFin(fechaFin);
        expediente.setParroquia(parroquia);
        expediente.setSectorFilial((SectorFilial) cmbSector.getSelectedItem());
        expediente.setDireccion(txtDireccion.getText().trim());
        expediente.setTelefonoContacto(txtTelefono.getText().trim());
        expediente.setTipoVivienda((TipoVivienda) cmbTipoVivienda.getSelectedItem());
        expediente.setTenencia((Tenencia) cmbTenencia.getSelectedItem());
        expediente.setCondicionVivienda((CondicionVivienda) cmbCondicion.getSelectedItem());
        expediente.setEntrevistador(entrevistador);
        expediente.setObservaciones(txtObservaciones.getText().trim());

        try {
            if (esNuevo) {
                // el DAO le pone el número de ficha y la cascada se lleva de una
                // los participantes, las asistencias y las prolongaciones
                expedienteDAO.guardarNuevo(expediente, Sesion.nombreUsuario());
                bitacoraDAO.registrar(Bitacora.CREAR, "Caso social",
                    expediente.getNumeroFicha(),
                    "Parroquia: " + parroquia.getNombre()
                    + " / participantes: " + familiares.size());

                JOptionPane.showMessageDialog(this,
                    "Ficha guardada con el número " + expediente.getNumeroFicha() + ".");
            } else {
                expedienteDAO.actualizar(expediente);
                bitacoraDAO.registrar(Bitacora.EDITAR, "Caso social",
                    expediente.getNumeroFicha(), null);

                JOptionPane.showMessageDialog(this, "Ficha actualizada.");
            }
        } catch (RuntimeException e) {
            error("No pude guardar la ficha:\n" + e.getMessage());
            return;
        }

        guardado = true;
        dispose();
    }

    // ============================================================ utilidades

    // devuelve null si el campo está vacío. si está mal escrito tiro la excepción
    // para que guardar() la agarre y muestre el mensaje de una sola vez.
    private Date leerFecha(JTextField campo, String nombre) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            // sin el setLenient(false) me aceptaría cosas como 45/13/2026
            formato.setLenient(false);
            return formato.parse(texto);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                "La " + nombre + " tiene que ir en formato dd/mm/aaaa.");
        }
    }

    private String formatear(Date fecha) {
        return (fecha == null) ? "" : formato.format(fecha);
    }

    private String textoSeguro(String valor) {
        return (valor == null) ? "" : valor;
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
