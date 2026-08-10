package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.CasoSocialController;
import pastoralsocial.controller.AsistenciaController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.ReporteUtil;

public class AsistenciasPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaAsistencias;
	private DefaultTableModel modeloTabla;

	private JButton btnBuscar;
	private JButton btnNuevaAsistencia;
	private JButton btnEditar;
	private JButton btnEliminar;
	private JButton btnReporte;

	@SuppressWarnings("unused")
	private Usuario usuarioActual;

	private final AsistenciaController asistenciaController;

	public AsistenciasPanel(Usuario usuarioActual) {

		this.usuarioActual = usuarioActual;

		asistenciaController = new AsistenciaController();

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		inicializarComponentes();
		configurarEventos();
		cargarAsistencias();
	}

	private void inicializarComponentes() {

		// TITULO
		JLabel lblTitulo = new JLabel("Asistencias");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 300, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Registre y consulte las asistencias otorgadas.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 75, 500, 25);
		add(lblDescripcion);

		// BUSCADOR
		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtBuscar.setBounds(40, 125, 430, 38);
		add(txtBuscar);

		// BOTON BUSCAR
		btnBuscar = new JButton("Buscar");
		btnBuscar.setForeground(new Color(0, 45, 102));
		btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBounds(485, 125, 110, 38);
		add(btnBuscar);

		// BOTON NUEVA ASISTENCIA
		btnNuevaAsistencia = new JButton("Nueva asistencia");
		btnNuevaAsistencia.setForeground(new Color(0, 45, 102));
		btnNuevaAsistencia.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevaAsistencia.setFocusPainted(false);
		btnNuevaAsistencia.setBounds(690, 125, 210, 38);
		add(btnNuevaAsistencia);

		// MODELO TABLA
		modeloTabla = new DefaultTableModel(

				new Object[][] {},

				new String[] { "ID", "Fecha", "Caso social", "Tipo de asistencia", "Modalidad", "Valor" }) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};

		// TABLA
		tablaAsistencias = new JTable(modeloTabla);
		tablaAsistencias.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tablaAsistencias.setRowHeight(32);
		tablaAsistencias.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		tablaAsistencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// SCROLL
		JScrollPane scrollPane = new JScrollPane(tablaAsistencias);
		scrollPane.setBounds(40, 195, 860, 280);
		add(scrollPane);

		// BOTON EDITAR
		btnEditar = new JButton("Editar asistencia");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditar.setFocusPainted(false);
		btnEditar.setBounds(540, 500, 170, 40);
		add(btnEditar);

		// BOTON ELIMINAR
		btnEliminar = new JButton("Eliminar asistencia");
		btnEliminar.setForeground(new Color(0, 45, 102));
		btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEliminar.setFocusPainted(false);
		btnEliminar.setBounds(730, 500, 170, 40);
		add(btnEliminar);

		// BOTON GENERAR REPORTE
		btnReporte = new JButton("Generar Reporte");
		btnReporte.setForeground(new Color(0, 45, 102));
		btnReporte.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnReporte.setFocusPainted(false);
		btnReporte.setBounds(40, 500, 180, 40);
		add(btnReporte);
	}

	private void configurarEventos() {

		// BUSCAR
		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarAsistencias();
			}
		});

		// BUSCADOR (ENTER)
		txtBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarAsistencias();
			}
		});

		// NUEVA ASISTENCIA
		btnNuevaAsistencia.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				nuevaAsistencia();
			}
		});

		// EDITAR
		btnEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				editarAsistencia();
			}
		});

		// ELIMINAR
		btnEliminar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				eliminarAsistencia();
			}
		});

		// GENERAR REPORTE
		btnReporte.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				generarReporte();
			}
		});
	}

	public void cargarAsistencias() {

		try {

			List<Asistencia> asistencias = asistenciaController.listar();

			mostrarAsistencias(asistencias);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar las asistencias.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostrarAsistencias(List<Asistencia> asistencias) {

		modeloTabla.setRowCount(0);

		if (asistencias == null) {

			return;
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		for (Asistencia asistencia : asistencias) {

			String fecha = asistencia.getFecha() != null ? formato.format(asistencia.getFecha()) : "";

			String caso = asistencia.getCasoSocial() != null ? asistencia.getCasoSocial().toString() : "";

			String tipo = asistencia.getDetalleTipoAsistencia() != null
					? asistencia.getDetalleTipoAsistencia().toString()
					: "";

			String valor = asistencia.getValor() != null ? asistencia.getValor().toPlainString() : "";

			modeloTabla.addRow(

					new Object[] {

							asistencia.getIdAsistencia(), fecha, caso, tipo, asistencia.getModalidad(), valor });
		}
	}

	private void buscarAsistencias() {

		try {

			String texto = txtBuscar.getText().trim().toLowerCase();

			List<Asistencia> asistencias = asistenciaController.listar();

			modeloTabla.setRowCount(0);

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			int encontrados = 0;

			for (Asistencia asistencia : asistencias) {

				String caso = asistencia.getCasoSocial() != null ? asistencia.getCasoSocial().toString() : "";

				String tipo = asistencia.getDetalleTipoAsistencia() != null
						? asistencia.getDetalleTipoAsistencia().toString()
						: "";

				String modalidad = asistencia.getModalidad() != null ? asistencia.getModalidad() : "";

				if (texto.isEmpty() || caso.toLowerCase().contains(texto) || tipo.toLowerCase().contains(texto)
						|| modalidad.toLowerCase().contains(texto)) {

					encontrados++;

					String fecha = asistencia.getFecha() != null ? formato.format(asistencia.getFecha()) : "";

					String valor = asistencia.getValor() != null ? asistencia.getValor().toPlainString() : "";

					modeloTabla.addRow(new Object[] {

							asistencia.getIdAsistencia(), fecha, caso, tipo, modalidad, valor });
				}
			}

			if (!texto.isEmpty() && encontrados == 0) {

				JOptionPane.showMessageDialog(this, "No se encontraron asistencias que coincidan con la búsqueda.",
						"Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible realizar la búsqueda.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void nuevaAsistencia() {

		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		CasoSocialController casoSocialController = new CasoSocialController();

		try {

			List<CasoSocial> casos = casoSocialController.listar();

			if (casos == null || casos.isEmpty()) {

				JOptionPane.showMessageDialog(this, "No existen casos sociales registrados para asociar la asistencia.",
						"Nueva asistencia", JOptionPane.WARNING_MESSAGE);

				return;
			}

			CasoSocial[] opciones = casos.toArray(new CasoSocial[casos.size()]);

			CasoSocial casoSeleccionado = (CasoSocial) JOptionPane.showInputDialog(this,
					"Seleccione el caso social al que pertenece la asistencia:", "Nueva asistencia",
					JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

			if (casoSeleccionado == null) {
				return;
			}

			AsistenciaFormDialog dialog = new AsistenciaFormDialog(parent, casoSeleccionado);

			dialog.setVisible(true);

			if (dialog.isGuardado()) {
				cargarAsistencias();
			}

		} catch (Exception ex) {

			ex.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible seleccionar el caso social.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editarAsistencia() {

		int filaSeleccionada = tablaAsistencias.getSelectedRow();

		if (filaSeleccionada == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar una asistencia para editar.", "Editar asistencia",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		try {

			int filaModelo = tablaAsistencias.convertRowIndexToModel(filaSeleccionada);

			int idAsistencia = Integer.parseInt(modeloTabla.getValueAt(filaModelo, 0).toString());

			Asistencia asistencia = asistenciaController.buscarPorId(idAsistencia);

			if (asistencia == null) {

				JOptionPane.showMessageDialog(this, "No se encontró la asistencia seleccionada.", "Editar asistencia",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			Frame framePadre = (Frame) SwingUtilities.getWindowAncestor(this);

			AsistenciaFormDialog dialog = new AsistenciaFormDialog(framePadre, asistencia);

			dialog.setVisible(true);

			if (dialog.isGuardado()) {

				cargarAsistencias();
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar la asistencia para editar.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void eliminarAsistencia() {

		int filaSeleccionada = tablaAsistencias.getSelectedRow();

		if (filaSeleccionada == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar una asistencia para eliminar.", "Eliminar asistencia",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la asistencia seleccionada?",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (opcion != JOptionPane.YES_OPTION) {

			return;
		}

		try {

			int filaModelo = tablaAsistencias.convertRowIndexToModel(filaSeleccionada);

			int idAsistencia = Integer.parseInt(modeloTabla.getValueAt(filaModelo, 0).toString());

			Asistencia asistencia = asistenciaController.buscarPorId(idAsistencia);

			asistenciaController.eliminar(asistencia);

			JOptionPane.showMessageDialog(this, "Asistencia eliminada correctamente.", "Asistencias",
					JOptionPane.INFORMATION_MESSAGE);

			cargarAsistencias();

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Eliminar asistencia", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible eliminar la asistencia.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void generarReporte() {

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			try {

				String ruta = chooser.getSelectedFile().getAbsolutePath();

				CasoSocialController casoController = new CasoSocialController();
				List<CasoSocial> casos = casoController.listar();

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

						// Generar PDF Adendum de Asistencias
						String rutaPDF = ruta + "/ADENDUM_" + nombreCaso + ".pdf";
						ReporteUtil.generarPDFAsistenciaCaso(caso, rutaPDF);

						// Generar Excel Adendum de Asistencias
						String rutaXLS = ruta + "/ADENDUM_" + nombreCaso + ".xls";
						ReporteUtil.generarExcelAsistenciaCaso(caso, rutaXLS);

						generados++;

					} catch (Exception e) {

						e.printStackTrace();
						JOptionPane.showMessageDialog(this,
								"Error generando reporte para caso: " + caso.getNumeroFicha() + "\n" + e.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}

				JOptionPane
						.showMessageDialog(this,
								"Reportes de Asistencias (Adendum) generados exitosamente.\n" + generados + " PDF y "
										+ generados + " Excel creados en:\n" + ruta,
								"Éxito", JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception e) {

				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error al generar reportes: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}