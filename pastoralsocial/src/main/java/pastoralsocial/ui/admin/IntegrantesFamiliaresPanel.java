package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.Window;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IntegranteFamiliar;

public class IntegrantesFamiliaresPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable tablaIntegrantes;
	private DefaultTableModel modeloTabla;

	private final IntegranteFamiliarController integranteController;
	private final CasoSocial casoSocial;

	private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

	public IntegrantesFamiliaresPanel(CasoSocial casoSocial) {

		this.casoSocial = casoSocial;
		this.integranteController = new IntegranteFamiliarController();

		inicializarInterfaz();
		cargarIntegrantes();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		// TITULO
		JLabel lblTitulo = new JLabel("Grupo familiar");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(40, 25, 350, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Integrantes registrados en el caso " + casoSocial.getNumeroFicha() + ".");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 65, 550, 25);
		add(lblDescripcion);

		// NUEVO INTEGRANTE
		JButton btnNuevoIntegrante = new JButton("Nuevo integrante");
		btnNuevoIntegrante.setForeground(new Color(0, 45, 102));
		btnNuevoIntegrante.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevoIntegrante.setBounds(720, 45, 190, 38);
		add(btnNuevoIntegrante);

		// TABLA
		modeloTabla = new DefaultTableModel(new Object[] { "ID", "Identificación", "Nombre completo", "Parentesco",
				"Fecha nacimiento", "Jefe hogar", "Estado" }, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};

		tablaIntegrantes = new JTable(modeloTabla);
		tablaIntegrantes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tablaIntegrantes.setRowHeight(30);
		tablaIntegrantes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tablaIntegrantes);
		scrollPane.setBounds(40, 120, 870, 330);
		add(scrollPane);

		// BOTONES
		JButton btnVerDetalle = new JButton("Ver detalle");
		btnVerDetalle.setForeground(new Color(0, 45, 102));
		btnVerDetalle.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnVerDetalle.setBounds(400, 480, 150, 40);
		add(btnVerDetalle);

		JButton btnEditar = new JButton("Editar integrante");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditar.setBounds(565, 480, 165, 40);
		add(btnEditar);

		JButton btnCambiarEstado = new JButton("Cambiar estado");
		btnCambiarEstado.setForeground(new Color(0, 45, 102));
		btnCambiarEstado.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCambiarEstado.setBounds(745, 480, 165, 40);
		add(btnCambiarEstado);

		// EVENTOS
		btnNuevoIntegrante.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				nuevoIntegrante();
			}
		});

		btnVerDetalle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				verDetalle();
			}
		});

		btnEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				editarIntegrante();
			}
		});

		btnCambiarEstado.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cambiarEstado();
			}
		});
	}

	private Frame obtenerFramePadre() {

		Window ventana = SwingUtilities.getWindowAncestor(this);

		if (ventana instanceof Frame) {

			return (Frame) ventana;
		}

		if (ventana != null && ventana.getOwner() instanceof Frame) {

			return (Frame) ventana.getOwner();
		}

		return null;
	}

	private void cargarIntegrantes() {

		try {

			modeloTabla.setRowCount(0);

			List<IntegranteFamiliar> integrantes = integranteController.buscarPorCasoSocial(casoSocial);

			for (IntegranteFamiliar integrante : integrantes) {

				agregarIntegranteTabla(integrante);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los integrantes familiares.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarIntegranteTabla(IntegranteFamiliar integrante) {

		String nombreCompleto = integrante.getNombre() + " " + integrante.getApellidos();

		String fechaNacimiento = "";

		if (integrante.getFechaNacimiento() != null) {

			fechaNacimiento = formatoFecha.format(integrante.getFechaNacimiento());
		}

		String jefeHogar = integrante.isJefeHogar() ? "Sí" : "No";

		String estado = integrante.isEstado() ? "Activo" : "Inactivo";

		modeloTabla.addRow(new Object[] {

				integrante.getIdIntegrante(), integrante.getIdentificacion(), nombreCompleto,
				integrante.getParentesco(), fechaNacimiento, jefeHogar, estado });
	}

	private IntegranteFamiliar obtenerIntegranteSeleccionado() {

		int fila = tablaIntegrantes.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar un integrante familiar.", "Grupo familiar",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		int idIntegrante = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

		return integranteController.buscarPorId(idIntegrante);
	}

	private void nuevoIntegrante() {

		Frame parent = obtenerFramePadre();

		IntegranteFamiliarFormDialog dialog = new IntegranteFamiliarFormDialog(parent, casoSocial);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {

			cargarIntegrantes();
		}
	}

	private void editarIntegrante() {

		IntegranteFamiliar integrante = obtenerIntegranteSeleccionado();

		if (integrante == null) {

			return;
		}

		Frame parent = obtenerFramePadre();

		IntegranteFamiliarFormDialog dialog = new IntegranteFamiliarFormDialog(parent, casoSocial, integrante);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {

			cargarIntegrantes();
		}
	}

	private void verDetalle() {

		IntegranteFamiliar integrante = obtenerIntegranteSeleccionado();

		if (integrante == null) {

			return;
		}

		Frame parent = obtenerFramePadre();

		IntegranteFamiliarDetalleDialog dialog = new IntegranteFamiliarDetalleDialog(parent, integrante);

		dialog.setVisible(true);
	}

	private void cambiarEstado() {

		IntegranteFamiliar integrante = obtenerIntegranteSeleccionado();

		if (integrante == null) {

			return;
		}

		String estadoActual = integrante.isEstado() ? "activo" : "inactivo";

		String nuevoEstado = integrante.isEstado() ? "inactivo" : "activo";

		int opcion = JOptionPane.showConfirmDialog(this,
				"El integrante actualmente se encuentra " + estadoActual + ".\n¿Desea cambiar su estado a "
						+ nuevoEstado + "?",
				"Confirmar cambio de estado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (opcion != JOptionPane.YES_OPTION) {

			return;
		}

		try {

			integrante.setEstado(!integrante.isEstado());

			integranteController.actualizar(integrante);

			JOptionPane.showMessageDialog(this, "El estado del integrante se actualizó correctamente.",
					"Grupo familiar", JOptionPane.INFORMATION_MESSAGE);

			cargarIntegrantes();

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cambiar el estado del integrante.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}