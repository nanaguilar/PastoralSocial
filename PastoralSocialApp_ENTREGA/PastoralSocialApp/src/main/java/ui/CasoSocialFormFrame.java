package ui;

import dao.ExpedienteDAO;
import dao.ParroquiaDAO;
import model.Expediente;
import model.Parroquia;
import model.Persona;
import model.SectorFilial;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Formulario maestro-detalle del "Caso Social": en una sola pantalla se
 * gestionan el expediente (maestro) y sus familiares (detalle).
 *
 * <p>La ubicación del núcleo familiar (parroquia, sector, dirección) se registra
 * una sola vez aquí y la heredan los familiares, evitando ubicaciones repetidas.
 * La parroquia y el sector, que antes eran texto libre, ahora se seleccionan
 * desde el catálogo de la base de datos, con combos en cascada.
 */
public class CasoSocialFormFrame extends JFrame {

    private ExpedienteDAO expedienteDAO = new ExpedienteDAO();
    private ParroquiaDAO parroquiaDAO = new ParroquiaDAO();
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    private JTextField txtNumeroFicha = new JTextField(15);
    private JTextField txtFechaInicio = new JTextField(10);
    private JTextField txtFechaFin = new JTextField(10);
    private JTextField txtEntrevistador = new JTextField(15);

    private JComboBox<Parroquia> cmbParroquia = new JComboBox<>();
    private JComboBox<SectorFilial> cmbSector = new JComboBox<>();
    private JTextField txtDireccion = new JTextField(15);
    private JTextField txtTelefono = new JTextField(15);

    private JComboBox<Expediente.TipoVivienda> cmbTipoVivienda =
        new JComboBox<>(Expediente.TipoVivienda.values());
    private JComboBox<Expediente.Tenencia> cmbTenencia =
        new JComboBox<>(Expediente.Tenencia.values());
    private JComboBox<Expediente.CondicionVivienda> cmbCondicion =
        new JComboBox<>(Expediente.CondicionVivienda.values());

    private JTextArea txtObservaciones = new JTextArea(3, 20);

    private List<Persona> familiares = new ArrayList<>();
    private FamiliaresTableModel tableModel = new FamiliaresTableModel();
    private JTable tblFamiliares = new JTable(tableModel);

    private Runnable alCerrar;
    private Expediente expediente;

    public CasoSocialFormFrame(Integer idExpediente, Runnable alCerrar) {
        this.alCerrar = alCerrar;

        setTitle(idExpediente == null ? "Caso Social - Nuevo" : "Caso Social - Editar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);

        cargarParroquias();
        cmbParroquia.addActionListener(e -> refrescarSectores());

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        contenido.add(panelDatosGenerales());
        contenido.add(panelUbicacion());
        contenido.add(panelVivienda());
        contenido.add(panelFamiliares());
        contenido.add(panelObservaciones());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Ficha del Caso Social");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(contenido), BorderLayout.CENTER);
        panel.add(panelBotones(), BorderLayout.SOUTH);

        add(panel);

        if (idExpediente != null) {
            cargarExpediente(idExpediente);
        } else {
            refrescarSectores();
        }
    }

    // ---------------------------------------------------------------- Secciones

    private JPanel seccion(String titulo, int filas) {
        JPanel p = new JPanel(new GridLayout(filas, 2, 10, 10));
        p.setBorder(BorderFactory.createTitledBorder(titulo));
        return p;
    }

    private JPanel panelDatosGenerales() {
        JPanel p = seccion("Datos generales", 4);
        p.add(new JLabel("N° de ficha:"));         p.add(txtNumeroFicha);
        p.add(new JLabel("Fecha inicio (aaaa-mm-dd):")); p.add(txtFechaInicio);
        p.add(new JLabel("Fecha prevista fin:"));  p.add(txtFechaFin);
        p.add(new JLabel("Entrevistador:"));       p.add(txtEntrevistador);
        return p;
    }

    private JPanel panelUbicacion() {
        JPanel p = seccion("Ubicación del núcleo familiar (la heredan los familiares)", 4);
        p.add(new JLabel("Parroquia:"));           p.add(cmbParroquia);
        p.add(new JLabel("Sector / filial:"));     p.add(cmbSector);
        p.add(new JLabel("Dirección:"));           p.add(txtDireccion);
        p.add(new JLabel("Teléfono de contacto:")); p.add(txtTelefono);
        return p;
    }

    private JPanel panelVivienda() {
        JPanel p = seccion("Vivienda", 3);
        p.add(new JLabel("Tipo de vivienda:"));    p.add(cmbTipoVivienda);
        p.add(new JLabel("Tenencia:"));            p.add(cmbTenencia);
        p.add(new JLabel("Condición:"));           p.add(cmbCondicion);
        return p;
    }

    private JPanel panelFamiliares() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createTitledBorder("Familiares (detalle del caso social)"));

        JScrollPane sp = new JScrollPane(tblFamiliares);
        sp.setPreferredSize(new Dimension(600, 130));
        tblFamiliares.getTableHeader().setReorderingAllowed(false);
        tblFamiliares.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnAgregar = new JButton("Agregar familiar");
        JButton btnEditar = new JButton("Editar");
        JButton btnQuitar = new JButton("Quitar");
        btnAgregar.addActionListener(e -> agregarFamiliar());
        btnEditar.addActionListener(e -> editarFamiliar());
        btnQuitar.addActionListener(e -> quitarFamiliar());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        acciones.add(btnAgregar);
        acciones.add(btnEditar);
        acciones.add(btnQuitar);

        p.add(sp, BorderLayout.CENTER);
        p.add(acciones, BorderLayout.SOUTH);
        return p;
    }

    private JPanel panelObservaciones() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        p.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);
        return p;
    }

    private JPanel panelBotones() {
        JButton btnGuardar = new JButton("Guardar caso social");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> cerrar());

        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.add(btnCancelar);
        p.add(btnGuardar);
        return p;
    }

    // ---------------------------------------------------------------- Catálogos

    private void cargarParroquias() {
        parroquiaDAO.sembrarSiVacio();
        cmbParroquia.removeAllItems();
        cmbParroquia.addItem(null);
        for (Parroquia p : parroquiaDAO.listar()) {
            cmbParroquia.addItem(p);
        }
    }

    private void refrescarSectores() {
        Parroquia seleccionada = (Parroquia) cmbParroquia.getSelectedItem();
        SectorFilial previo = (SectorFilial) cmbSector.getSelectedItem();
        cmbSector.removeAllItems();
        cmbSector.addItem(null);
        if (seleccionada != null) {
            for (SectorFilial s : parroquiaDAO.listarSectores(seleccionada)) {
                cmbSector.addItem(s);
                if (previo != null && previo.getId() != null
                        && previo.getId().equals(s.getId())) {
                    cmbSector.setSelectedItem(s);
                }
            }
        }
    }

    // ---------------------------------------------------------------- Familiares

    private void agregarFamiliar() {
        PersonaDialog dlg = new PersonaDialog(this, null);
        dlg.setVisible(true);
        if (dlg.fueGuardado()) {
            familiares.add(dlg.getPersona());
            tableModel.fireTableDataChanged();
        }
    }

    private void editarFamiliar() {
        int fila = tblFamiliares.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un familiar para editar.");
            return;
        }
        PersonaDialog dlg = new PersonaDialog(this, familiares.get(fila));
        dlg.setVisible(true);
        if (dlg.fueGuardado()) {
            tableModel.fireTableRowsUpdated(fila, fila);
        }
    }

    private void quitarFamiliar() {
        int fila = tblFamiliares.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un familiar para quitar.");
            return;
        }
        familiares.remove(fila);
        tableModel.fireTableDataChanged();
    }

    // ---------------------------------------------------------- Carga / guardado

    private void cargarExpediente(Integer id) {
        expediente = expedienteDAO.buscarConDetalle(id);
        if (expediente == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el caso social.");
            return;
        }
        txtNumeroFicha.setText(nvl(expediente.getNumeroFicha()));
        txtFechaInicio.setText(expediente.getFechaInicio() != null
            ? fmt.format(expediente.getFechaInicio()) : "");
        txtFechaFin.setText(expediente.getFechaPrevistaFin() != null
            ? fmt.format(expediente.getFechaPrevistaFin()) : "");
        txtEntrevistador.setText(nvl(expediente.getEntrevistador()));

        seleccionarParroquia(expediente.getParroquia());
        refrescarSectores();
        seleccionarSector(expediente.getSectorFilial());

        txtDireccion.setText(nvl(expediente.getDireccion()));
        txtTelefono.setText(nvl(expediente.getTelefonoContacto()));
        if (expediente.getTipoVivienda() != null) cmbTipoVivienda.setSelectedItem(expediente.getTipoVivienda());
        if (expediente.getTenencia() != null) cmbTenencia.setSelectedItem(expediente.getTenencia());
        if (expediente.getCondicionVivienda() != null) cmbCondicion.setSelectedItem(expediente.getCondicionVivienda());
        txtObservaciones.setText(nvl(expediente.getObservaciones()));

        familiares.clear();
        familiares.addAll(expediente.getPersonas());
        tableModel.fireTableDataChanged();
    }

    private void seleccionarParroquia(Parroquia p) {
        if (p == null) return;
        for (int i = 0; i < cmbParroquia.getItemCount(); i++) {
            Parroquia item = cmbParroquia.getItemAt(i);
            if (item != null && item.getId().equals(p.getId())) {
                cmbParroquia.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarSector(SectorFilial s) {
        if (s == null) return;
        for (int i = 0; i < cmbSector.getItemCount(); i++) {
            SectorFilial item = cmbSector.getItemAt(i);
            if (item != null && item.getId().equals(s.getId())) {
                cmbSector.setSelectedIndex(i);
                return;
            }
        }
    }

    private void guardar() {
        String numeroFicha = txtNumeroFicha.getText().trim();
        if (numeroFicha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El número de ficha es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer idActual = (expediente != null) ? expediente.getId() : null;
        if (expedienteDAO.existeNumeroFicha(numeroFicha, idActual)) {
            JOptionPane.showMessageDialog(this,
                "Ya existe un caso social con ese número de ficha.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date fechaInicio = parsearFecha(txtFechaInicio.getText().trim(), "Fecha de inicio");
        Date fechaFin = parsearFecha(txtFechaFin.getText().trim(), "Fecha prevista de fin");
        if (fechaInvalida) {
            return;
        }

        if (expediente == null) {
            expediente = new Expediente();
        }
        expediente.setNumeroFicha(numeroFicha);
        expediente.setFechaInicio(fechaInicio);
        expediente.setFechaPrevistaFin(fechaFin);
        expediente.setEntrevistador(vacioANull(txtEntrevistador.getText().trim()));
        expediente.setParroquia((Parroquia) cmbParroquia.getSelectedItem());
        expediente.setSectorFilial((SectorFilial) cmbSector.getSelectedItem());
        expediente.setDireccion(vacioANull(txtDireccion.getText().trim()));
        expediente.setTelefonoContacto(vacioANull(txtTelefono.getText().trim()));
        expediente.setTipoVivienda((Expediente.TipoVivienda) cmbTipoVivienda.getSelectedItem());
        expediente.setTenencia((Expediente.Tenencia) cmbTenencia.getSelectedItem());
        expediente.setCondicionVivienda((Expediente.CondicionVivienda) cmbCondicion.getSelectedItem());
        expediente.setObservaciones(vacioANull(txtObservaciones.getText().trim()));

        // Reconstruye la relación maestro-detalle (los familiares heredan el maestro).
        expediente.getPersonas().clear();
        for (Persona persona : familiares) {
            expediente.agregarPersona(persona);
        }

        try {
            if (idActual == null) {
                expedienteDAO.guardar(expediente);
            } else {
                expediente = expedienteDAO.actualizar(expediente);
            }
            JOptionPane.showMessageDialog(this, "Caso social guardado correctamente.");
            cerrar();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudo guardar el caso social:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean fechaInvalida;

    private Date parsearFecha(String texto, String campo) {
        fechaInvalida = false;
        if (texto.isEmpty()) {
            return null;
        }
        try {
            fmt.setLenient(false);
            return fmt.parse(texto);
        } catch (Exception e) {
            fechaInvalida = true;
            JOptionPane.showMessageDialog(this,
                campo + " no tiene el formato aaaa-mm-dd.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void cerrar() {
        if (alCerrar != null) {
            alCerrar.run();
        }
        dispose();
    }

    private static String nvl(String v) {
        return v == null ? "" : v;
    }

    private static String vacioANull(String v) {
        return (v == null || v.isEmpty()) ? null : v;
    }

    // ------------------------------------------------------------ Modelo tabla

    private class FamiliaresTableModel extends AbstractTableModel {
        private String[] cols = {"Nombre", "Parentesco", "Edad", "Sexo", "Jefatura"};

        public int getRowCount() { return familiares.size(); }
        public int getColumnCount() { return cols.length; }
        public String getColumnName(int c) { return cols[c]; }

        public Object getValueAt(int r, int c) {
            Persona p = familiares.get(r);
            switch (c) {
                case 0: return p.getApellidosNombres();
                case 1: return p.getRelacion() != null ? p.getRelacion() : "";
                case 2: return p.getEdad() != null ? p.getEdad() : "";
                case 3: return p.getSexo() != null ? p.getSexo().name() : "";
                case 4: return p.isJefatura() ? "Sí" : "No";
                default: return "";
            }
        }
    }
}
