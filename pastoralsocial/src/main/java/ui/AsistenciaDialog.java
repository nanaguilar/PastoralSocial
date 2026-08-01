package ui;

import dao.AsistenciaDAO;
import model.Asistencia;
import model.Expediente;
import model.TipoAsistencia;
import util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// dialog para registrar o consultar una asistencia de un caso.
// los combos de modalidad y frecuencia los dejé editables: traen sugerencias
// para no tener que escribir siempre lo mismo, pero se puede poner otra cosa.
// así no quedan valores fijos amarrados en el código.
public class AsistenciaDialog extends JDialog {

    private AsistenciaDAO asistenciaDAO = new AsistenciaDAO();

    private Expediente expediente;
    private Asistencia asistencia;    // null cuando estoy registrando
    private boolean esNueva;
    private boolean soloLectura;
    private boolean guardado = false;

    // mismo truco que en PersonaDialog: si el caso todavía no existe en la base,
    // solo lleno el objeto y se lo devuelvo al formulario
    private boolean guardarEnBase;
    private Asistencia resultado;

    private JComboBox<TipoAsistencia> cmbTipo = new JComboBox<TipoAsistencia>();
    private JComboBox<String> cmbModalidad = new JComboBox<String>(
        new String[] { "", "Especie", "Efectivo", "Servicio", "Mixta" });
    private JComboBox<String> cmbFrecuencia = new JComboBox<String>(
        new String[] { "", "Única vez", "Semanal", "Quincenal", "Mensual" });
    private JTextField txtDuracion = new JTextField(18);
    private JTextField txtValor = new JTextField(18);
    private JTextField txtFechaSolicitud = new JTextField(18);
    private JTextField txtFechaEntrega = new JTextField(18);
    private JComboBox<String> cmbEstado = new JComboBox<String>(
        new String[] { Asistencia.ESTADO_SOLICITADA, Asistencia.ESTADO_BRINDADA });

    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public AsistenciaDialog(Dialog padre, Expediente expediente, Asistencia asistencia,
                            boolean soloLectura) {
        this(padre, expediente, asistencia, soloLectura, true);
    }

    public AsistenciaDialog(Dialog padre, Expediente expediente, Asistencia asistencia,
                            boolean soloLectura, boolean guardarEnBase) {
        super(padre, true);
        this.expediente = expediente;
        this.asistencia = asistencia;
        this.esNueva = (asistencia == null);
        this.soloLectura = soloLectura;
        this.guardarEnBase = guardarEnBase;

        setTitle(esNueva ? "Registrar asistencia" : "Asistencia del caso");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(460, 420);
        setLocationRelativeTo(padre);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(panelCampos(), BorderLayout.CENTER);
        panel.add(panelBotones(), BorderLayout.SOUTH);
        add(panel);

        cmbModalidad.setEditable(true);
        cmbFrecuencia.setEditable(true);

        cargarTipos();

        if (esNueva) {
            txtFechaSolicitud.setText(formato.format(new Date()));
        } else {
            cargarDatos();
        }

        if (soloLectura) {
            bloquearCampos();
        }
    }

    public boolean seGuardo() {
        return guardado;
    }

    // lo usa el formulario cuando el caso todavía está en memoria
    public Asistencia getResultado() {
        return resultado;
    }

    // ------------------------------------------------------------------ armado

    private JPanel panelCampos() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Asistencia solicitada y brindada"));

        int fila = 0;
        agregar(p, fila++, "Tipo de asistencia:", cmbTipo);
        agregar(p, fila++, "Modalidad:", cmbModalidad);
        agregar(p, fila++, "Frecuencia:", cmbFrecuencia);
        agregar(p, fila++, "Duración:", txtDuracion);
        agregar(p, fila++, "Valor:", txtValor);
        agregar(p, fila++, "Fecha de solicitud:", txtFechaSolicitud);
        agregar(p, fila++, "Fecha de entrega:", txtFechaEntrega);
        agregar(p, fila++, "Estado:", cmbEstado);
        agregar(p, fila++, "", new JLabel("Las fechas van en formato dd/mm/aaaa"));
        return p;
    }

    private void agregar(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, gbc);
    }

    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

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

    // ------------------------------------------------------------------ cargar

    private void cargarTipos() {
        cmbTipo.addItem(null);
        for (TipoAsistencia t : asistenciaDAO.listarTipos()) {
            cmbTipo.addItem(t);
        }
    }

    private void cargarDatos() {
        // el tipo del combo viene de otra consulta que el de la asistencia,
        // así que los comparo por id y no por objeto
        if (asistencia.getTipoAsistencia() != null) {
            for (int i = 0; i < cmbTipo.getItemCount(); i++) {
                TipoAsistencia t = cmbTipo.getItemAt(i);
                if (t != null && t.getId().equals(asistencia.getTipoAsistencia().getId())) {
                    cmbTipo.setSelectedIndex(i);
                    break;
                }
            }
        }

        cmbModalidad.setSelectedItem(textoSeguro(asistencia.getModalidad()));
        cmbFrecuencia.setSelectedItem(textoSeguro(asistencia.getFrecuencia()));
        txtDuracion.setText(textoSeguro(asistencia.getDuracion()));
        txtValor.setText(asistencia.getValor() == null
            ? "" : String.valueOf(asistencia.getValor()));
        txtFechaSolicitud.setText(formatear(asistencia.getFechaSolicitud()));
        txtFechaEntrega.setText(formatear(asistencia.getFechaEntrega()));

        // una asistencia anulada no vuelve a SOLICITADA ni BRINDADA, así que le
        // agrego el estado al combo solo para poder mostrarlo
        if (asistencia.estaAnulada()) {
            cmbEstado.addItem(Asistencia.ESTADO_ANULADA);
        }
        cmbEstado.setSelectedItem(asistencia.getEstado());
    }

    private void bloquearCampos() {
        cmbTipo.setEnabled(false);
        cmbModalidad.setEnabled(false);
        cmbFrecuencia.setEnabled(false);
        cmbEstado.setEnabled(false);
        txtDuracion.setEditable(false);
        txtValor.setEditable(false);
        txtFechaSolicitud.setEditable(false);
        txtFechaEntrega.setEditable(false);
    }

    // ------------------------------------------------------------------ guardar

    private void guardar() {
        TipoAsistencia tipo = (TipoAsistencia) cmbTipo.getSelectedItem();
        if (tipo == null) {
            error("Tenés que elegir el tipo de asistencia.");
            return;
        }

        Date fechaSolicitud;
        Date fechaEntrega;
        Double valor;
        try {
            fechaSolicitud = leerFecha(txtFechaSolicitud, "fecha de solicitud");
            fechaEntrega = leerFecha(txtFechaEntrega, "fecha de entrega");
            valor = leerValor();
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return;
        }

        String estado = (String) cmbEstado.getSelectedItem();

        // si ya la di por brindada tiene que tener fecha de entrega, si no el
        // historial queda con ayudas entregadas sin saber cuándo
        if (Asistencia.ESTADO_BRINDADA.equals(estado) && fechaEntrega == null) {
            error("Si la asistencia está brindada, tenés que poner la fecha de entrega.");
            return;
        }

        if (fechaEntrega != null && fechaSolicitud != null
                && fechaEntrega.before(fechaSolicitud)) {
            error("La fecha de entrega no puede ser anterior a la de solicitud.");
            return;
        }

        Asistencia destino = esNueva ? new Asistencia() : asistencia;

        if (esNueva) {
            destino.setExpediente(expediente);
            destino.setRegistradoPor(Sesion.nombreUsuario());
        }

        destino.setTipoAsistencia(tipo);
        destino.setModalidad(textoDelCombo(cmbModalidad));
        destino.setFrecuencia(textoDelCombo(cmbFrecuencia));
        destino.setDuracion(txtDuracion.getText().trim());
        destino.setValor(valor);
        destino.setFechaSolicitud(fechaSolicitud);
        destino.setFechaEntrega(fechaEntrega);
        destino.setEstado(estado);

        if (!guardarEnBase) {
            resultado = destino;
            guardado = true;
            dispose();
            return;
        }

        try {
            asistenciaDAO.guardar(destino);
        } catch (RuntimeException e) {
            error("No pude guardar la asistencia:\n" + e.getMessage());
            return;
        }

        resultado = destino;
        guardado = true;
        dispose();
    }

    // ---------------------------------------------------------------- utilidades

    // como el combo es editable, lo que hay adentro puede ser lo que el usuario
    // escribió y no una de mis sugerencias. por eso lo saco así.
    private String textoDelCombo(JComboBox<String> combo) {
        Object valor = combo.getSelectedItem();
        return (valor == null) ? "" : valor.toString().trim();
    }

    private Date leerFecha(JTextField campo, String nombre) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            formato.setLenient(false);
            return formato.parse(texto);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                "La " + nombre + " tiene que ir en formato dd/mm/aaaa.");
        }
    }

    private Double leerValor() {
        String texto = txtValor.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(texto.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor tiene que ser un número.");
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
