package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pastoralsocial.controller.IngresoFamiliarController;
import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IngresoFamiliar;
import pastoralsocial.modelo.IntegranteFamiliar;

public class IngresoFamiliarFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JComboBox<IntegranteFamiliar> cmbIntegrante;
	private JTextField txtOcupacion;
	private JComboBox<String> cmbTrabaja;
	private JTextField txtIngresoMensual;

	private final IngresoFamiliarController ingresoController;
	private final IntegranteFamiliarController integranteController;

	private final CasoSocial casoSocial;
	private final IngresoFamiliar ingresoEditar;

	private boolean guardado;

	/**
	 * @wbp.parser.constructor
	 */
	public IngresoFamiliarFormDialog(Frame parent, CasoSocial casoSocial) {

		super(parent, "Nuevo ingreso familiar", true);

		this.casoSocial = casoSocial;
		this.ingresoEditar = null;

		this.ingresoController = new IngresoFamiliarController();
		this.integranteController = new IntegranteFamiliarController();

		inicializarInterfaz();
		cargarIntegrantes();

		setSize(600, 430);
		setLocationRelativeTo(parent);
		setResizable(false);
	}

	public IngresoFamiliarFormDialog(Frame parent, CasoSocial casoSocial, IngresoFamiliar ingreso) {

		super(parent, "Editar ingreso familiar", true);

		this.casoSocial = casoSocial;
		this.ingresoEditar = ingreso;

		this.ingresoController = new IngresoFamiliarController();
		this.integranteController = new IntegranteFamiliarController();

		inicializarInterfaz();
		cargarIntegrantes();
		cargarDatos();

		setSize(600, 430);
		setLocationRelativeTo(parent);
		setResizable(false);
	}

	private void inicializarInterfaz() {

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(245, 247, 250));
		contentPanel.setLayout(null);

		setContentPane(contentPanel);

		// TITULO
		JLabel lblTitulo = new JLabel(ingresoEditar == null ? "Nuevo ingreso familiar" : "Editar ingreso familiar");

		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setBounds(35, 25, 450, 35);
		contentPanel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				"Registre la información económica del integrante del caso " + casoSocial.getNumeroFicha() + ".");

		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(35, 62, 500, 25);
		contentPanel.add(lblDescripcion);

		// PANEL FORMULARIO
		JPanel panelFormulario = new JPanel();
		panelFormulario.setBackground(Color.WHITE);
		panelFormulario.setLayout(null);
		panelFormulario.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 228)));

		panelFormulario.setBounds(35, 100, 510, 220);

		contentPanel.add(panelFormulario);

		// INTEGRANTE
		JLabel lblIntegrante = new JLabel("Integrante *");
		lblIntegrante.setForeground(new Color(0, 45, 102));
		lblIntegrante.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblIntegrante.setBounds(20, 20, 220, 20);

		panelFormulario.add(lblIntegrante);

		cmbIntegrante = new JComboBox<IntegranteFamiliar>();

		cmbIntegrante.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbIntegrante.setBounds(20, 45, 220, 35);

		panelFormulario.add(cmbIntegrante);

		// OCUPACION
		JLabel lblOcupacion = new JLabel("Ocupación *");
		lblOcupacion.setForeground(new Color(0, 45, 102));
		lblOcupacion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblOcupacion.setBounds(265, 20, 220, 20);

		panelFormulario.add(lblOcupacion);

		txtOcupacion = new JTextField();
		txtOcupacion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtOcupacion.setBounds(265, 45, 220, 35);

		panelFormulario.add(txtOcupacion);

		// TRABAJA
		JLabel lblTrabaja = new JLabel("¿Trabaja? *");
		lblTrabaja.setForeground(new Color(0, 45, 102));
		lblTrabaja.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTrabaja.setBounds(20, 100, 220, 20);

		panelFormulario.add(lblTrabaja);

		cmbTrabaja = new JComboBox<String>(new String[] { "Sí", "No" });

		cmbTrabaja.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTrabaja.setBounds(20, 125, 220, 35);

		panelFormulario.add(cmbTrabaja);

		// INGRESO MENSUAL
		JLabel lblIngreso = new JLabel("Ingreso mensual");
		lblIngreso.setForeground(new Color(0, 45, 102));
		lblIngreso.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblIngreso.setBounds(265, 100, 220, 20);

		panelFormulario.add(lblIngreso);

		txtIngresoMensual = new JTextField();
		txtIngresoMensual.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtIngresoMensual.setBounds(265, 125, 220, 35);

		panelFormulario.add(txtIngresoMensual);

		JLabel lblNota = new JLabel(
				"<html>Si el integrante no trabaja, el ingreso mensual<br>" + "no es obligatorio.</html>");

		lblNota.setForeground(new Color(100, 100, 100));
		lblNota.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblNota.setBounds(20, 175, 460, 35);

		panelFormulario.add(lblNota);

		// BOTONES
		JButton btnCancelar = new JButton("Cancelar");

		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setBounds(280, 340, 125, 38);

		contentPanel.add(btnCancelar);

		JButton btnGuardar = new JButton(ingresoEditar == null ? "Guardar" : "Guardar cambios");

		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setBounds(420, 340, 125, 38);

		contentPanel.add(btnGuardar);

		// EVENTOS
		cmbTrabaja.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				actualizarEstadoIngreso();
			}
		});

		btnCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});

		btnGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				guardar();
			}
		});
	}

	private void cargarIntegrantes() {

		try {

			List<IntegranteFamiliar> integrantes = integranteController.buscarPorCasoSocial(casoSocial);

			cmbIntegrante.removeAllItems();

			for (IntegranteFamiliar integrante : integrantes) {

				cmbIntegrante.addItem(integrante);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los integrantes familiares.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarDatos() {

		if (ingresoEditar == null) {
			return;
		}

		txtOcupacion.setText(ingresoEditar.getOcupacion() != null ? ingresoEditar.getOcupacion() : "");

		cmbTrabaja.setSelectedItem(ingresoEditar.isTrabaja() ? "Sí" : "No");

		if (ingresoEditar.getIngresoMensual() != null) {

			txtIngresoMensual.setText(ingresoEditar.getIngresoMensual().toString());

		} else {

			txtIngresoMensual.setText("");
		}

		for (int i = 0; i < cmbIntegrante.getItemCount(); i++) {

			IntegranteFamiliar integrante = cmbIntegrante.getItemAt(i);

			if (integrante.getIdIntegrante() == ingresoEditar.getIntegrante().getIdIntegrante()) {

				cmbIntegrante.setSelectedIndex(i);
				break;
			}
		}

		actualizarEstadoIngreso();
	}

	private void actualizarEstadoIngreso() {

		boolean trabaja = "Sí".equals(cmbTrabaja.getSelectedItem());

		txtIngresoMensual.setEnabled(trabaja);

		if (!trabaja) {

			txtIngresoMensual.setText("");
		}
	}

	private void guardar() {

		try {

			IntegranteFamiliar integranteSeleccionado = (IntegranteFamiliar) cmbIntegrante.getSelectedItem();

			if (integranteSeleccionado == null) {

				JOptionPane.showMessageDialog(this, "Debe seleccionar un integrante.", "Validación",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			String ocupacion = txtOcupacion.getText().trim();

			if (ocupacion.isEmpty()) {

				JOptionPane.showMessageDialog(this, "La ocupación es obligatoria.", "Validación",
						JOptionPane.WARNING_MESSAGE);

				txtOcupacion.requestFocus();
				return;
			}

			boolean trabaja = "Sí".equals(cmbTrabaja.getSelectedItem());

			BigDecimal ingresoMensual = null;

			if (trabaja) {

				String textoIngreso = txtIngresoMensual.getText().trim();

				if (textoIngreso.isEmpty()) {

					JOptionPane.showMessageDialog(this, "Debe indicar el ingreso mensual.", "Validación",
							JOptionPane.WARNING_MESSAGE);

					txtIngresoMensual.requestFocus();
					return;
				}

				try {

					ingresoMensual = new BigDecimal(textoIngreso);

				} catch (NumberFormatException e) {

					JOptionPane.showMessageDialog(this, "El ingreso mensual debe ser un número válido.", "Validación",
							JOptionPane.WARNING_MESSAGE);

					txtIngresoMensual.requestFocus();
					return;
				}

				if (ingresoMensual.compareTo(BigDecimal.ZERO) < 0) {

					JOptionPane.showMessageDialog(this, "El ingreso mensual no puede ser negativo.", "Validación",
							JOptionPane.WARNING_MESSAGE);

					txtIngresoMensual.requestFocus();
					return;
				}
			}

			if (ingresoEditar == null) {

				IngresoFamiliar ingreso = new IngresoFamiliar(integranteSeleccionado, ocupacion, trabaja,
						ingresoMensual, casoSocial);

				ingresoController.registrar(ingreso);

			} else {

				ingresoEditar.setIntegrante(integranteSeleccionado);
				ingresoEditar.setOcupacion(ocupacion);
				ingresoEditar.setTrabaja(trabaja);
				ingresoEditar.setIngresoMensual(ingresoMensual);
				ingresoController.actualizar(ingresoEditar);
			}

			guardado = true;

			JOptionPane.showMessageDialog(this,
					ingresoEditar == null ? "El ingreso familiar se registró correctamente."
							: "El ingreso familiar se actualizó correctamente.",
					"Ingresos familiares", JOptionPane.INFORMATION_MESSAGE);

			dispose();

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar el ingreso familiar.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isGuardado() {

		return guardado;
	}
}