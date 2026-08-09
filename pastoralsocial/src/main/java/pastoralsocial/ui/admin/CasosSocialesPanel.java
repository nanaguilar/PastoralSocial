package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.CasoSocialController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.ReporteUtil;

public class CasosSocialesPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaCasos;
	private DefaultTableModel modeloTabla;

	private final CasoSocialController casosSocialController;
	private final Usuario usuarioActual;

	public CasosSocialesPanel(Usuario usuarioActual) {

		this.usuarioActual = usuarioActual;
		this.casosSocialController = new CasoSocialController();

		inicializarInterfaz();
		cargarCasos();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		// TTTULO
		JLabel lblTitulo = new JLabel("Casos sociales");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 300, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Gestione los casos sociales registrados en el sistema.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 75, 500, 25);
		add(lblDescripcion);

		// BUSCADOR
		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtBuscar.setBounds(40, 125, 430, 38);
		add(txtBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setForeground(new Color(0, 45, 102));
		btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnBuscar.setBounds(485, 125, 110, 38);
		add(btnBuscar);

		// NUEVO CASO
		JButton btnNuevoCaso = new JButton("Nuevo caso social");
		btnNuevoCaso.setForeground(new Color(0, 45, 102));
		btnNuevoCaso.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevoCaso.setBounds(720, 125, 190, 38);
		add(btnNuevoCaso);

		// TABLA
		modeloTabla = new DefaultTableModel(
				new Object[] { "ID", "N.º ficha", "Fecha inicio", "Estado", "Parroquia", "Responsable" }, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaCasos = new JTable(modeloTabla);
		tablaCasos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tablaCasos.setRowHeight(30);
		tablaCasos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tablaCasos);
		scrollPane.setBounds(40, 195, 870, 285);
		add(scrollPane);

		// BOTONES INFERIORES
		JButton btnVerDetalle = new JButton("Ver detalle");
		btnVerDetalle.setForeground(new Color(0, 45, 102));
		btnVerDetalle.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnVerDetalle.setBounds(565, 505, 150, 40);
		add(btnVerDetalle);

		JButton btnEditar = new JButton("Editar caso");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditar.setBounds(730, 505, 180, 40);
		add(btnEditar);

		// BOTÓN GENERAR REPORTE
		JButton btnReporte = new JButton("Generar Reporte");
		btnReporte.setForeground(new Color(0, 45, 102));
		btnReporte.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnReporte.setBounds(40, 505, 180, 40);
		add(btnReporte);

		// EVENTOS
		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buscarCaso();
			}
		});

		btnNuevoCaso.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nuevoCaso();
			}
		});

		btnEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editarCaso();
			}
		});

		btnVerDetalle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				verDetalle();
			}
		});

		btnReporte.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				generarReporte();
			}
		});
	}

	private void cargarCasos() {

		try {

			modeloTabla.setRowCount(0);

			List<CasoSocial> casos = casosSocialController.listar();

			for (CasoSocial caso : casos) {

				String responsable = "";

				if (caso.getUsuario() != null) {

					responsable = caso.getUsuario().getNombre() + " " + caso.getUsuario().getApellidos();
				}

				modeloTabla.addRow(new Object[] {

						caso.getIdCasoSocial(), caso.getNumeroFicha(), caso.getFechaInicio(), caso.getDetalleEstado(),
						caso.getParroquia(), responsable });
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los casos sociales.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void buscarCaso() {

		String texto = txtBuscar.getText().trim();

		try {

			List<CasoSocial> casos;

			if (texto.isEmpty()) {

				casos = casosSocialController.listar();

			} else {

				casos = casosSocialController.buscar(texto);
			}

			modeloTabla.setRowCount(0);

			for (CasoSocial caso : casos) {

				String responsable = "";

				if (caso.getUsuario() != null) {

					responsable = caso.getUsuario().getNombre() + " " + caso.getUsuario().getApellidos();
				}

				modeloTabla.addRow(new Object[] {

						caso.getIdCasoSocial(), caso.getNumeroFicha(), caso.getFechaInicio(), caso.getDetalleEstado(),
						caso.getParroquia(), responsable });
			}

			if (casos.isEmpty()) {

				JOptionPane.showMessageDialog(this, "No se encontraron casos sociales que coincidan con la búsqueda.",
						"Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible realizar la búsqueda.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private CasoSocial obtenerCasoSeleccionado() {

		int fila = tablaCasos.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar un caso social.", "Casos sociales",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		int idCaso = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

		return casosSocialController.buscarPorId(idCaso);
	}

	private void nuevoCaso() {

		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		CasoSocialFormDialog dialog = new CasoSocialFormDialog(parent);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {

			cargarCasos();
		}
	}

	private void editarCaso() {

		CasoSocial caso = obtenerCasoSeleccionado();

		if (caso == null) {
			return;
		}

		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		CasoSocialFormDialog dialog = new CasoSocialFormDialog(parent, caso);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {
			cargarCasos();
		}
	}

	private void verDetalle() {

		CasoSocial caso = obtenerCasoSeleccionado();

		if (caso == null) {
			return;
		}

		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		CasoSocialDetalleDialog dialog = new CasoSocialDetalleDialog(parent, caso);

		dialog.setVisible(true);
	}

	private void generarReporte() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			try {

				String ruta = chooser.getSelectedFile().getAbsolutePath();
				List<CasoSocial> casos = casosSocialController.listar();

				if (casos == null || casos.isEmpty()) {

					JOptionPane.showMessageDialog(this, "No hay casos sociales para generar reportes.", "Sin datos",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				int generados = 0;

				for (CasoSocial caso : casos) {

					try {

						String nombreCaso = caso.getNumeroFicha() != null ? caso.getNumeroFicha()
								: "Caso_" + caso.getIdCasoSocial();

						// Generar PDF detallado
						String rutaPDF = ruta + "/CASO_" + nombreCaso + ".pdf";
						ReporteUtil.generarPDFCasoSocial(caso, rutaPDF);

						// Generar Excel detallado
						String rutaXLS = ruta + "/CASO_" + nombreCaso + ".xls";
						ReporteUtil.generarExcelCasoSocial(caso, rutaXLS);

						generados++;

					} catch (Exception e) {

						e.printStackTrace();
						JOptionPane.showMessageDialog(this,
								"Error generando reporte para caso: " + caso.getNumeroFicha() + "\n"
										+ e.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				JOptionPane.showMessageDialog(this,
						"Reportes generados exitosamente.\n" + generados + " PDF y " + generados
								+ " Excel creados en:\n" + ruta,
						"Éxito", JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception e) {

				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al generar reportes: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}