package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.IngresoFamiliarController;
import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IngresoFamiliar;
import pastoralsocial.modelo.IntegranteFamiliar;

public class IntegrantesFamiliaresPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable tablaIntegrantes;
	private DefaultTableModel modeloTabla;

	private JTable tablaIngresos;
	private DefaultTableModel modeloTablaIngresos;

	private final IntegranteFamiliarController integranteController;
	private final IngresoFamiliarController ingresoController;

	private final CasoSocial casoSocial;

	private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

	private final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));

	public IntegrantesFamiliaresPanel(CasoSocial casoSocial) {

		this.casoSocial = casoSocial;

		this.integranteController = new IntegranteFamiliarController();
		this.ingresoController = new IngresoFamiliarController();

		inicializarInterfaz();

		cargarIntegrantes();
		cargarIngresos();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		// GRUPO FAMILIAR
		JLabel lblTitulo = new JLabel("Grupo familiar");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(40, 20, 350, 40);
		add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				"Integrantes e información económica registrada en el caso " + casoSocial.getNumeroFicha() + ".");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 60, 650, 25);
		add(lblDescripcion);

		// INTEGRANTES
		JLabel lblIntegrantes = new JLabel("Integrantes familiares");
		lblIntegrantes.setForeground(new Color(0, 45, 102));
		lblIntegrantes.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblIntegrantes.setBounds(40, 100, 300, 25);
		add(lblIntegrantes);

		JButton btnNuevoIntegrante = new JButton("Nuevo integrante");
		btnNuevoIntegrante.setForeground(new Color(0, 45, 102));
		btnNuevoIntegrante.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevoIntegrante.setBounds(720, 95, 190, 38);
		add(btnNuevoIntegrante);

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
		tablaIntegrantes.setRowHeight(28);
		tablaIntegrantes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollIntegrantes = new JScrollPane(tablaIntegrantes);
		scrollIntegrantes.setBounds(40, 135, 870, 190);
		add(scrollIntegrantes);

		JButton btnVerDetalle = new JButton("Ver detalle");
		btnVerDetalle.setForeground(new Color(0, 45, 102));
		btnVerDetalle.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnVerDetalle.setBounds(400, 335, 140, 38);
		add(btnVerDetalle);

		JButton btnEditar = new JButton("Editar integrante");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEditar.setBounds(555, 335, 165, 38);
		add(btnEditar);

		JButton btnCambiarEstado = new JButton("Cambiar estado");
		btnCambiarEstado.setForeground(new Color(0, 45, 102));
		btnCambiarEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCambiarEstado.setBounds(735, 335, 175, 38);
		add(btnCambiarEstado);

		// INGRESOS FAMILIARES
		JLabel lblIngresos = new JLabel("Ingresos familiares");
		lblIngresos.setForeground(new Color(0, 45, 102));
		lblIngresos.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblIngresos.setBounds(40, 400, 300, 25);
		add(lblIngresos);

		JButton btnNuevoIngreso = new JButton("Nuevo ingreso");
		btnNuevoIngreso.setForeground(new Color(0, 45, 102));
		btnNuevoIngreso.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevoIngreso.setBounds(720, 395, 190, 38);
		add(btnNuevoIngreso);

		modeloTablaIngresos = new DefaultTableModel(
				new Object[] { "ID", "Integrante", "Ocupación", "Trabaja", "Ingreso mensual" }, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaIngresos = new JTable(modeloTablaIngresos);
		tablaIngresos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tablaIngresos.setRowHeight(28);
		tablaIngresos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollIngresos = new JScrollPane(tablaIngresos);
		scrollIngresos.setBounds(40, 440, 870, 145);
		add(scrollIngresos);

		JButton btnEditarIngreso = new JButton("Editar ingreso");
		btnEditarIngreso.setForeground(new Color(0, 45, 102));
		btnEditarIngreso.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEditarIngreso.setBounds(565, 600, 160, 38);
		add(btnEditarIngreso);

		JButton btnEliminarIngreso = new JButton("Eliminar ingreso");
		btnEliminarIngreso.setForeground(new Color(0, 45, 102));
		btnEliminarIngreso.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEliminarIngreso.setBounds(735, 600, 175, 38);
		add(btnEliminarIngreso);

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

		btnNuevoIngreso.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nuevoIngreso();
			}
		});

		btnEditarIngreso.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editarIngreso();
			}
		});

		btnEliminarIngreso.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				eliminarIngreso();
			}
		});
	}

	// INTEGRANTES
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
				integrante.getDetalleParentesco() != null ? integrante.getDetalleParentesco().getDescripcion()
						: "No especificado",
				fechaNacimiento, jefeHogar, estado });
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

	// INGRESOS
	private void cargarIngresos() {

		try {

			modeloTablaIngresos.setRowCount(0);

			List<IngresoFamiliar> ingresos = ingresoController.buscarPorCasoSocial(casoSocial);

			for (IngresoFamiliar ingreso : ingresos) {

				agregarIngresoTabla(ingreso);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los ingresos familiares.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarIngresoTabla(IngresoFamiliar ingreso) {

		String trabaja = ingreso.isTrabaja() ? "Sí" : "No";

		String monto = ingreso.getIngresoMensual() != null ? formatoMoneda.format(ingreso.getIngresoMensual())
				: "₡0.00";

		modeloTablaIngresos.addRow(new Object[] {

				ingreso.getIdIngreso(), ingreso.getIntegrante(), ingreso.getOcupacion(), trabaja, monto });
	}

	private IngresoFamiliar obtenerIngresoSeleccionado() {

		int fila = tablaIngresos.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar un ingreso familiar.", "Ingresos familiares",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		int idIngreso = Integer.parseInt(modeloTablaIngresos.getValueAt(fila, 0).toString());

		return ingresoController.buscarPorId(idIngreso);
	}

	private void nuevoIngreso() {

		Frame parent = obtenerFramePadre();

		IngresoFamiliarFormDialog dialog = new IngresoFamiliarFormDialog(parent, casoSocial);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {
			cargarIngresos();
		}
	}

	private void editarIngreso() {

		IngresoFamiliar ingreso = obtenerIngresoSeleccionado();

		if (ingreso == null) {
			return;
		}

		Frame parent = obtenerFramePadre();

		IngresoFamiliarFormDialog dialog = new IngresoFamiliarFormDialog(parent, casoSocial, ingreso);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {
			cargarIngresos();
		}
	}

	private void eliminarIngreso() {

		IngresoFamiliar ingreso = obtenerIngresoSeleccionado();

		if (ingreso == null) {
			return;
		}

		int opcion = JOptionPane.showConfirmDialog(this,
				"¿Está seguro de eliminar el ingreso de " + ingreso.getIntegrante() + "?", "Confirmar eliminación",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		try {

			ingresoController.eliminar(ingreso);

			JOptionPane.showMessageDialog(this, "El ingreso familiar se eliminó correctamente.", "Ingresos familiares",
					JOptionPane.INFORMATION_MESSAGE);

			cargarIngresos();

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible eliminar el ingreso familiar.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// =============================================================
	// UTILIDADES
	// =============================================================

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
}