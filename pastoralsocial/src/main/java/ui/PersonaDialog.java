package ui;

import dao.PersonaDAO;
import model.Expediente;
import model.Persona;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// dialog para agregar o editar un integrante del grupo familiar.
// separé los campos en dos recuadros: los datos de la persona arriba y la
// sección de Ingreso Familiar abajo, que es la que pidió el profe.
public class PersonaDialog extends JDialog {

    private PersonaDAO personaDAO = new PersonaDAO();

    private Expediente expediente;
    private Persona persona;      // null cuando estoy agregando
    private boolean esNuevo;
    private boolean soloLectura;
    private boolean guardado = false;

    // cuando el caso todavía no se ha guardado no puedo mandar nada a la base
    // (no existe expediente_id), así que en ese modo solo lleno el objeto y se lo
    // devuelvo al formulario, que lo mantiene en memoria hasta que le den Guardar
    private boolean guardarEnBase;
    private Persona resultado;

    private JTextField txtNombres = new JTextField(18);
    private JTextField txtDocumento = new JTextField(18);
    private JComboBox<Persona.Sexo> cmbSexo = new JComboBox<Persona.Sexo>();
    private JTextField txtRelacion = new JTextField(18);
    private JTextField txtFechaNacimiento = new JTextField(18);
    private JLabel lblEdad = new JLabel("-");
    private JCheckBox chkJefatura = new JCheckBox("Es jefe/a de hogar");
    private JTextField txtPais = new JTextField(18);
    private JTextField txtMigracion = new JTextField(18);
    private JTextField txtEducacion = new JTextField(18);
    private JTextField txtSalud = new JTextField(18);
    private JCheckBox chkAlarmaMedica = new JCheckBox("Tiene alarma médica");
    private JTextField txtSeguro = new JTextField(18);

    private JTextField txtOcupacion = new JTextField(18);
    private JCheckBox chkTrabaja = new JCheckBox("Trabaja actualmente");
    private JCheckBox chkPensionado = new JCheckBox("Recibe pensión");
    private JTextField txtIngreso = new JTextField(18);

    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    // el de siempre: guarda directo en la base
    public PersonaDialog(Dialog padre, Expediente expediente, Persona persona,
                         boolean soloLectura) {
        this(padre, expediente, persona, soloLectura, true);
    }

    public PersonaDialog(Dialog padre, Expediente expediente, Persona persona,
                         boolean soloLectura, boolean guardarEnBase) {
        super(padre, true);
        this.expediente = expediente;
        this.persona = persona;
        this.esNuevo = (persona == null);
        this.soloLectura = soloLectura;
        this.guardarEnBase = guardarEnBase;

        setTitle(esNuevo ? "Agregar integrante" : "Integrante del grupo familiar");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 640);
        setLocationRelativeTo(padre);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.add(panelDatos(), BorderLayout.NORTH);
        centro.add(panelIngreso(), BorderLayout.CENTER);

        panel.add(new JScrollPane(centro), BorderLayout.CENTER);
        panel.add(panelBotones(), BorderLayout.SOUTH);
        add(panel);

        cargarSexos();

        // cuando salgo del campo de la fecha recalculo la edad, así se ve de una
        // vez que la edad no se escribe sino que sale sola
        txtFechaNacimiento.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                refrescarEdad();
            }
        });

        if (!esNuevo) {
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
    public Persona getResultado() {
        return resultado;
    }

    // ------------------------------------------------------------------ armado

    private JPanel panelDatos() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del integrante"));

        int fila = 0;
        agregar(p, fila++, "Apellidos y nombres:", txtNombres);
        agregar(p, fila++, "Documento de identidad:", txtDocumento);
        agregar(p, fila++, "Sexo:", cmbSexo);
        agregar(p, fila++, "Relación / parentesco:", txtRelacion);
        agregar(p, fila++, "Fecha de nacimiento:", txtFechaNacimiento);
        agregar(p, fila++, "Edad:", lblEdad);
        agregar(p, fila++, "", chkJefatura);
        agregar(p, fila++, "País:", txtPais);
        agregar(p, fila++, "Migración:", txtMigracion);
        agregar(p, fila++, "Educación:", txtEducacion);
        agregar(p, fila++, "Salud:", txtSalud);
        agregar(p, fila++, "", chkAlarmaMedica);
        agregar(p, fila++, "Seguro:", txtSeguro);
        return p;
    }

    private JPanel panelIngreso() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Ingreso familiar"));

        int fila = 0;
        agregar(p, fila++, "Ocupación / pensión:", txtOcupacion);
        agregar(p, fila++, "", chkTrabaja);
        agregar(p, fila++, "", chkPensionado);
        agregar(p, fila++, "Ingreso mensual aprox.:", txtIngreso);
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

    private void cargarSexos() {
        cmbSexo.addItem(null);   // por si no lo quieren indicar
        for (Persona.Sexo s : Persona.Sexo.values()) {
            cmbSexo.addItem(s);
        }
    }

    private void cargarDatos() {
        txtNombres.setText(textoSeguro(persona.getApellidosNombres()));
        txtDocumento.setText(textoSeguro(persona.getDocumentoIdentidad()));
        cmbSexo.setSelectedItem(persona.getSexo());
        txtRelacion.setText(textoSeguro(persona.getRelacion()));
        txtFechaNacimiento.setText(persona.getFechaNacimiento() == null
            ? "" : formato.format(persona.getFechaNacimiento()));
        chkJefatura.setSelected(persona.isJefatura());
        txtPais.setText(textoSeguro(persona.getPais()));
        txtMigracion.setText(textoSeguro(persona.getMigracion()));
        txtEducacion.setText(textoSeguro(persona.getEducacion()));
        txtSalud.setText(textoSeguro(persona.getSalud()));
        chkAlarmaMedica.setSelected(persona.isAlarmaMedica());
        txtSeguro.setText(textoSeguro(persona.getSeguro()));

        txtOcupacion.setText(textoSeguro(persona.getOcupacion()));
        chkTrabaja.setSelected(persona.isTrabaja());
        chkPensionado.setSelected(persona.isPensionado());
        txtIngreso.setText(persona.getIngresoMensual() == null
            ? "" : String.valueOf(persona.getIngresoMensual()));

        refrescarEdad();
    }

    private void refrescarEdad() {
        Date nacimiento;
        try {
            nacimiento = leerFecha();
        } catch (IllegalArgumentException e) {
            lblEdad.setText("(fecha mal escrita)");
            return;
        }

        if (nacimiento == null) {
            lblEdad.setText("-");
            return;
        }

        // uso una Persona de paso solo para no repetir el cálculo de la edad
        Persona temporal = new Persona();
        temporal.setFechaNacimiento(nacimiento);
        Integer edad = temporal.calcularEdad();
        lblEdad.setText(edad == null ? "-" : edad + " años");
    }

    private void bloquearCampos() {
        txtNombres.setEditable(false);
        txtDocumento.setEditable(false);
        txtRelacion.setEditable(false);
        txtFechaNacimiento.setEditable(false);
        txtPais.setEditable(false);
        txtMigracion.setEditable(false);
        txtEducacion.setEditable(false);
        txtSalud.setEditable(false);
        txtSeguro.setEditable(false);
        txtOcupacion.setEditable(false);
        txtIngreso.setEditable(false);
        cmbSexo.setEnabled(false);
        chkJefatura.setEnabled(false);
        chkAlarmaMedica.setEnabled(false);
        chkTrabaja.setEnabled(false);
        chkPensionado.setEnabled(false);
    }

    // ------------------------------------------------------------------ guardar

    private void guardar() {
        String nombres = txtNombres.getText().trim();
        if (nombres.isEmpty()) {
            error("Los apellidos y nombres son obligatorios.");
            return;
        }

        Date nacimiento;
        try {
            nacimiento = leerFecha();
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return;
        }

        if (nacimiento != null && nacimiento.after(new Date())) {
            error("La fecha de nacimiento no puede ser futura.");
            return;
        }

        Double ingreso;
        try {
            ingreso = leerIngreso();
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return;
        }

        // acá está el flujo de HU-13: no dejo quitarle la jefatura al único jefe
        // activo, primero hay que asignársela a otro integrante.
        // si el caso todavía está en memoria no consulto la base, de eso se
        // encarga el formulario recorriendo su propia lista.
        if (guardarEnBase && !esNuevo && persona.isJefatura() && !chkJefatura.isSelected()
                && personaDAO.contarJefesActivos(expediente.getId()) <= 1) {
            error("No podés quitarle la jefatura de hogar sin asignársela a otro.\n"
                + "Marcá primero a otro integrante como jefe/a de hogar.");
            return;
        }

        Persona destino = esNuevo ? new Persona() : persona;

        if (esNuevo) {
            destino.setExpediente(expediente);
            destino.setActivo(true);
        }

        destino.setApellidosNombres(nombres);
        destino.setDocumentoIdentidad(txtDocumento.getText().trim());
        destino.setSexo((Persona.Sexo) cmbSexo.getSelectedItem());
        destino.setRelacion(txtRelacion.getText().trim());
        destino.setFechaNacimiento(nacimiento);
        destino.setJefatura(chkJefatura.isSelected());
        destino.setPais(txtPais.getText().trim());
        destino.setMigracion(txtMigracion.getText().trim());
        destino.setEducacion(txtEducacion.getText().trim());
        destino.setSalud(txtSalud.getText().trim());
        destino.setAlarmaMedica(chkAlarmaMedica.isSelected());
        destino.setSeguro(txtSeguro.getText().trim());

        destino.setOcupacion(txtOcupacion.getText().trim());
        destino.setTrabaja(chkTrabaja.isSelected());
        destino.setPensionado(chkPensionado.isSelected());
        destino.setIngresoMensual(ingreso);

        // sigo llenando la columna edad con el valor calculado para no romper
        // nada que la esté leyendo (el toString de Persona, por ejemplo)
        destino.setEdad(destino.calcularEdad());

        // si el caso todavía está en memoria no toco la base: solo dejo el objeto
        // listo y el formulario se encarga de sumarlo a su lista
        if (!guardarEnBase) {
            resultado = destino;
            guardado = true;
            dispose();
            return;
        }

        boolean leQuitoJefaturaAOtro = chkJefatura.isSelected()
            && personaDAO.contarJefesActivos(expediente.getId()) > 0
            && (esNuevo || !persona.isJefatura());

        try {
            personaDAO.guardar(destino);
        } catch (RuntimeException e) {
            error("No pude guardar al integrante:\n" + e.getMessage());
            return;
        }

        if (leQuitoJefaturaAOtro) {
            JOptionPane.showMessageDialog(this,
                "Listo. Como marcaste a esta persona como jefe/a de hogar,\n"
                + "se le quitó la jefatura a quien la tenía antes.");
        }

        resultado = destino;
        guardado = true;
        dispose();
    }

    // ---------------------------------------------------------------- utilidades

    private Date leerFecha() {
        String texto = txtFechaNacimiento.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            formato.setLenient(false);
            return formato.parse(texto);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                "La fecha de nacimiento tiene que ir en formato dd/mm/aaaa.");
        }
    }

    private Double leerIngreso() {
        String texto = txtIngreso.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            // acepto que lo escriban con coma decimal, es lo más común acá
            return Double.valueOf(texto.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "El ingreso mensual tiene que ser un número.");
        }
    }

    private String textoSeguro(String valor) {
        return (valor == null) ? "" : valor;
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
