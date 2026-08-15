package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.AdendumController;
import pastoralsocial.modelo.Adendum;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Usuario;

public class AdendumPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaAdendums;
	private DefaultTableModel modeloTabla;

	private JButton btnBuscar;
	private JButton btnNuevo;
	private JButton btnDetalle;
	private JButton btnEditar;
	private JButton btnEliminar;

	private final AdendumController adendumController;
	private final CasoSocial casoSocial;
	private final Usuario usuarioActual;

	public AdendumPanel(CasoSocial casoSocial, Usuario usuarioActual) {

		this.casoSocial = casoSocial;
		this.usuarioActual = usuarioActual;

		this.adendumController = new AdendumController();

		inicializarInterfaz();
		configurarEventos();
		cargarAdendums();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		// TITULO
		String numeroFicha = "";

		if (casoSocial != null && casoSocial.getNumeroFicha() != null) {
			numeroFicha = casoSocial.getNumeroFicha();
		}

		JLabel lblTitulo = new JLabel("Adendum del caso " + numeroFicha);
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 500, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Información adicional registrada para este caso social.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 75, 650, 25);
		add(lblDescripcion);

		// BUSCADOR
		JLabel lblBuscar = new JLabel("Buscar:");
		lblBuscar.setForeground(new Color(0, 45, 102));
		lblBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblBuscar.setBounds(40, 125, 60, 38);
		add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtBuscar.setBounds(100, 125, 350, 38);
		add(txtBuscar);

		// BOTON BUSCAR
		btnBuscar = new JButton("Buscar");
		btnBuscar.setForeground(new Color(0, 45, 102));
		btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBounds(465, 125, 110, 38);
		add(btnBuscar);

		// BOTON NUEVO
		btnNuevo = new JButton("Nuevo adendum");
		btnNuevo.setForeground(new Color(0, 45, 102));
		btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevo.setFocusPainted(false);
		btnNuevo.setBounds(720, 125, 180, 38);
		add(btnNuevo);

		// TABLA
		modeloTabla = new DefaultTableModel(new Object[] { "ID", "Fecha", "Caso social", "Observaciones" }, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaAdendums = new JTable(modeloTabla);
		tablaAdendums.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tablaAdendums.setRowHeight(28);
		tablaAdendums.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

		tablaAdendums.getColumnModel().getColumn(0).setPreferredWidth(60);
		tablaAdendums.getColumnModel().getColumn(1).setPreferredWidth(120);
		tablaAdendums.getColumnModel().getColumn(2).setPreferredWidth(180);
		tablaAdendums.getColumnModel().getColumn(3).setPreferredWidth(350);

		JScrollPane scrollTabla = new JScrollPane(tablaAdendums);
		scrollTabla.setBounds(40, 185, 860, 300);
		add(scrollTabla);

		// BOTON VER DETALLE
		btnDetalle = new JButton("Ver detalle");
		btnDetalle.setForeground(new Color(0, 45, 102));
		btnDetalle.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnDetalle.setFocusPainted(false);
		btnDetalle.setBounds(280, 505, 140, 40);
		add(btnDetalle);

		// BOTON EDITAR
		btnEditar = new JButton("Editar");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditar.setFocusPainted(false);
		btnEditar.setBounds(440, 505, 140, 40);
		add(btnEditar);

		// BOTON ELIMINAR
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setForeground(new Color(0, 45, 102));
		btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEliminar.setFocusPainted(false);
		btnEliminar.setBounds(600, 505, 140, 40);
		add(btnEliminar);

		// PERMISOS
		String rol = "";

		if (usuarioActual != null && usuarioActual.getDetalleRol() != null) {

			rol = usuarioActual.getDetalleRol().getDescripcion();
		}

		if ("Coordinador".equalsIgnoreCase(rol)) {

			btnNuevo.setVisible(false);
			btnEditar.setVisible(false);
			btnEliminar.setVisible(false);
		}
	}

	private void configurarEventos() {

		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarAdendums();
			}
		});

		txtBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarAdendums();
			}
		});

		btnNuevo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				nuevoAdendum();
			}
		});

		btnDetalle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				verDetalle();
			}
		});

		btnEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				editarAdendum();
			}
		});

		btnEliminar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				eliminarAdendum();
			}
		});
	}

	private void cargarAdendums() {

		try {

			if (casoSocial == null) {

				modeloTabla.setRowCount(0);
				return;
			}

			List<Adendum> adendums = adendumController.listarPorCaso(casoSocial);

			mostrarAdendums(adendums);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los adendums del caso social.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostrarAdendums(List<Adendum> adendums) {

		modeloTabla.setRowCount(0);

		if (adendums == null) {
			return;
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		for (Adendum adendum : adendums) {

			String fecha = "";

			if (adendum.getFechaRegistro() != null) {
				fecha = formato.format(adendum.getFechaRegistro());
			}

			String caso = "";

			if (adendum.getCasoSocial() != null && adendum.getCasoSocial().getNumeroFicha() != null) {

				caso = adendum.getCasoSocial().getNumeroFicha();
			}

			String observaciones = "";

			if (adendum.getObservaciones() != null) {
				observaciones = adendum.getObservaciones();
			}

			modeloTabla.addRow(new Object[] { adendum.getIdAdendum(), fecha, caso, observaciones });
		}
	}

	private void buscarAdendums() {

		String texto = txtBuscar.getText().trim().toLowerCase();

		try {

			if (casoSocial == null) {

				modeloTabla.setRowCount(0);
				return;
			}

			List<Adendum> adendums = adendumController.listarPorCaso(casoSocial);

			modeloTabla.setRowCount(0);

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			int encontrados = 0;

			for (Adendum adendum : adendums) {

				String id = String.valueOf(adendum.getIdAdendum());

				String fecha = "";

				if (adendum.getFechaRegistro() != null) {
					fecha = formato.format(adendum.getFechaRegistro());
				}

				String caso = "";

				if (adendum.getCasoSocial() != null && adendum.getCasoSocial().getNumeroFicha() != null) {

					caso = adendum.getCasoSocial().getNumeroFicha();
				}

				String observaciones = "";

				if (adendum.getObservaciones() != null) {
					observaciones = adendum.getObservaciones();
				}

				if (texto.isEmpty() || id.toLowerCase().contains(texto) || fecha.toLowerCase().contains(texto)
						|| caso.toLowerCase().contains(texto) || observaciones.toLowerCase().contains(texto)) {

					modeloTabla.addRow(new Object[] { adendum.getIdAdendum(), fecha, caso, observaciones });

					encontrados++;
				}
			}

			if (!texto.isEmpty() && encontrados == 0) {

				JOptionPane.showMessageDialog(this, "No se encontraron adendums que coincidan con la búsqueda.",
						"Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible realizar la búsqueda.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Adendum obtenerAdendumSeleccionado() {

		int fila = tablaAdendums.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar un adendum.", "Adendums",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		int filaModelo = tablaAdendums.convertRowIndexToModel(fila);

		int id = Integer.parseInt(modeloTabla.getValueAt(filaModelo, 0).toString());

		Adendum adendum = adendumController.buscarPorId(id);

		if (adendum == null) {

			JOptionPane.showMessageDialog(this, "No fue posible encontrar el adendum seleccionado.", "Adendums",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		if (adendum.getCasoSocial() == null || casoSocial == null
				|| adendum.getCasoSocial().getIdCasoSocial() != casoSocial.getIdCasoSocial()) {

			JOptionPane.showMessageDialog(this, "El adendum seleccionado no pertenece a este caso social.", "Adendums",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		return adendum;
	}

	private boolean tienePermisoEdicion() {

		if (usuarioActual == null || usuarioActual.getDetalleRol() == null) {

			return false;
		}

		String rol = usuarioActual.getDetalleRol().getDescripcion();

		return "Administrador".equalsIgnoreCase(rol) || "Trabajador Social".equalsIgnoreCase(rol);
	}

	private void nuevoAdendum() {

		if (casoSocial == null) {

			JOptionPane.showMessageDialog(this, "No se ha seleccionado un caso social.", "Adendum",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		try {

			Window parent = SwingUtilities.getWindowAncestor(this);

			AdendumFormDialog dialog = new AdendumFormDialog(parent, casoSocial);

			dialog.setVisible(true);

			if (dialog.isGuardado()) {
				buscarAdendums();
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible abrir el formulario de adendum.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void verDetalle() {

		Adendum adendum = obtenerAdendumSeleccionado();

		if (adendum == null) {
			return;
		}

		Window parent = SwingUtilities.getWindowAncestor(this);

		AdendumDetalleDialog dialog = new AdendumDetalleDialog(parent, adendum);

		dialog.setVisible(true);
	}

	private void editarAdendum() {

		if (!tienePermisoEdicion()) {

			JOptionPane.showMessageDialog(this, "No tiene permisos para editar adendums.", "Permisos",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		Adendum adendum = obtenerAdendumSeleccionado();

		if (adendum == null) {
			return;
		}

		try {

			Window parent = SwingUtilities.getWindowAncestor(this);

			AdendumFormDialog dialog = new AdendumFormDialog(parent, casoSocial, adendum);

			dialog.setVisible(true);

			if (dialog.isGuardado()) {
				cargarAdendums();
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible abrir el formulario de edición del adendum.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void eliminarAdendum() {

		if (!tienePermisoEdicion()) {

			JOptionPane.showMessageDialog(this, "No tiene permisos para eliminar adendums.", "Permisos",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		Adendum adendum = obtenerAdendumSeleccionado();

		if (adendum == null) {
			return;
		}

		int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el adendum seleccionado?",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		try {

			adendumController.eliminar(adendum);

			JOptionPane.showMessageDialog(this, "El adendum fue eliminado correctamente.", "Adendums",
					JOptionPane.INFORMATION_MESSAGE);

			cargarAdendums();

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible eliminar el adendum.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void actualizar() {

		cargarAdendums();
	}
}