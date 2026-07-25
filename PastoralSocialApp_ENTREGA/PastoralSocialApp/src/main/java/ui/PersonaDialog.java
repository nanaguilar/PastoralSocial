package ui;

import model.Persona;
import util.Catalogos;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para registrar o editar un familiar (detalle del caso social).
 *
 * <p>Los campos de vocabulario controlado (sexo, parentesco, país, migración,
 * educación, seguro) se capturan con combos no editables, de modo que el usuario
 * no pueda ingresar valores libres. No incluye campos de ubicación: la dirección
 * es única del núcleo familiar y se hereda del expediente.
 */
public class PersonaDialog extends JDialog {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtDocumento = new JTextField(15);
    private JComboBox<String> cmbSexo = new JComboBox<>(Catalogos.SEXO);
    private JComboBox<String> cmbParentesco = new JComboBox<>(Catalogos.PARENTESCO);
    private JSpinner spnEdad = new JSpinner(new SpinnerNumberModel(0, 0, 120, 1));
    private JComboBox<String> cmbPais = new JComboBox<>(Catalogos.PAIS);
    private JComboBox<String> cmbMigracion = new JComboBox<>(Catalogos.MIGRACION);
    private JComboBox<String> cmbEducacion = new JComboBox<>(Catalogos.EDUCACION);
    private JTextField txtSalud = new JTextField(15);
    private JComboBox<String> cmbSeguro = new JComboBox<>(Catalogos.SEGURO);
    private JCheckBox chkJefatura = new JCheckBox("Jefatura del hogar");
    private JCheckBox chkAlarma = new JCheckBox("Alarma médica");

    private Persona persona;
    private boolean guardado = false;

    public PersonaDialog(Window owner, Persona personaExistente) {
        super(owner, "Familiar", ModalityType.APPLICATION_MODAL);
        this.persona = (personaExistente != null) ? personaExistente : new Persona();

        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Apellidos y nombres:")); panel.add(txtNombre);
        panel.add(new JLabel("Documento identidad:")); panel.add(txtDocumento);
        panel.add(new JLabel("Sexo:"));                panel.add(cmbSexo);
        panel.add(new JLabel("Parentesco:"));          panel.add(cmbParentesco);
        panel.add(new JLabel("Edad:"));                panel.add(spnEdad);
        panel.add(new JLabel("País:"));                panel.add(cmbPais);
        panel.add(new JLabel("Condición migratoria:")); panel.add(cmbMigracion);
        panel.add(new JLabel("Educación:"));           panel.add(cmbEducacion);
        panel.add(new JLabel("Salud (observaciones):")); panel.add(txtSalud);
        panel.add(new JLabel("Seguro:"));              panel.add(cmbSeguro);
        panel.add(chkJefatura);                        panel.add(chkAlarma);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        btnAceptar.addActionListener(e -> onAceptar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCancelar);
        botones.add(btnAceptar);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(panel, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);

        setContentPane(contenido);
        pack();
        setLocationRelativeTo(owner);

        cargarDesdePersona();
    }

    private void cargarDesdePersona() {
        if (persona.getApellidosNombres() != null) txtNombre.setText(persona.getApellidosNombres());
        if (persona.getDocumentoIdentidad() != null) txtDocumento.setText(persona.getDocumentoIdentidad());
        if (persona.getSexo() != null) cmbSexo.setSelectedItem(persona.getSexo().name());
        if (persona.getRelacion() != null) cmbParentesco.setSelectedItem(persona.getRelacion());
        if (persona.getEdad() != null) spnEdad.setValue(persona.getEdad());
        if (persona.getPais() != null) cmbPais.setSelectedItem(persona.getPais());
        if (persona.getMigracion() != null) cmbMigracion.setSelectedItem(persona.getMigracion());
        if (persona.getEducacion() != null) cmbEducacion.setSelectedItem(persona.getEducacion());
        if (persona.getSalud() != null) txtSalud.setText(persona.getSalud());
        if (persona.getSeguro() != null) cmbSeguro.setSelectedItem(persona.getSeguro());
        chkJefatura.setSelected(persona.isJefatura());
        chkAlarma.setSelected(persona.isAlarmaMedica());
    }

    private void onAceptar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre del familiar es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        persona.setApellidosNombres(nombre);
        persona.setDocumentoIdentidad(vacioANull(txtDocumento.getText().trim()));

        String sexo = (String) cmbSexo.getSelectedItem();
        persona.setSexo((sexo == null || sexo.isEmpty()) ? null : Persona.Sexo.valueOf(sexo));

        persona.setRelacion(itemCombo(cmbParentesco));
        persona.setEdad((Integer) spnEdad.getValue());
        persona.setPais(itemCombo(cmbPais));
        persona.setMigracion(itemCombo(cmbMigracion));
        persona.setEducacion(itemCombo(cmbEducacion));
        persona.setSalud(vacioANull(txtSalud.getText().trim()));
        persona.setSeguro(itemCombo(cmbSeguro));
        persona.setJefatura(chkJefatura.isSelected());
        persona.setAlarmaMedica(chkAlarma.isSelected());

        guardado = true;
        dispose();
    }

    private static String itemCombo(JComboBox<String> combo) {
        String v = (String) combo.getSelectedItem();
        return (v == null || v.isEmpty()) ? null : v;
    }

    private static String vacioANull(String v) {
        return (v == null || v.isEmpty()) ? null : v;
    }

    public boolean fueGuardado() {
        return guardado;
    }

    public Persona getPersona() {
        return persona;
    }
}
