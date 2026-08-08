package pastoralsocial.ui.trabajadorsocial;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import pastoralsocial.controller.CasoSocialController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.ui.admin.CasoSocialDetalleDialog;
import pastoralsocial.ui.admin.CasoSocialFormDialog;

public class CasosSocialesTrabajadorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaCasos;
	private DefaultTableModel modeloTabla;

	private final CasoSocialController casosSocialController;
	private final Usuario usuarioActual;

	public CasosSocialesTrabajadorPanel(Usuario usuarioActual) {

		this.usuarioActual = usuarioActual;
		this.casosSocialController = new CasoSocialController();

		inicializarInterfaz();
		cargarCasos();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		JLabel lblTitulo = new JLabel("Mis casos sociales");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 350, 40);
		add(lblTitulo);

		JLabel lblDescripcion = new JLabel("Consulte y gestione los casos sociales asignados.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 75, 550, 25);
		add(lblDescripcion);

		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtBuscar.setBounds(40, 125, 430, 38);
		add(txtBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setForeground(new Color(0, 45, 102));
		btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnBuscar.setBounds(485, 125, 110, 38);
		add(btnBuscar);

		modeloTabla = new DefaultTableModel(new Object[] { "ID", "N.º ficha", "Fecha inicio", "Estado", "Parroquia" },
				0) {

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

		JButton btnVerDetalle = new JButton("Ver detalle");
		btnVerDetalle.setForeground(new Color(0, 45, 102));
		btnVerDetalle.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnVerDetalle.setBounds(595, 505, 150, 40);
		add(btnVerDetalle);

		JButton btnEditar = new JButton("Editar caso");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditar.setBounds(760, 505, 150, 40);
		add(btnEditar);

		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarCasos();
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

				editarCaso();
			}
		});
	}

	private void cargarCasos() {

		try {

			modeloTabla.setRowCount(0);

			List<CasoSocial> casos = casosSocialController.buscarPorUsuario(usuarioActual);

			for (CasoSocial caso : casos) {

				agregarCasoTabla(caso);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los casos sociales asignados.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarCasoTabla(CasoSocial caso) {

		modeloTabla.addRow(new Object[] {

				caso.getIdCasoSocial(), caso.getNumeroFicha(), caso.getFechaInicio(), caso.getDetalleEstado(),
				caso.getParroquia() });
	}

	private void buscarCasos() {

		String texto = txtBuscar.getText().trim();

		if (texto.isEmpty()) {

			cargarCasos();
			return;
		}

		try {

			List<CasoSocial> casos = casosSocialController.buscarPorUsuario(usuarioActual);

			modeloTabla.setRowCount(0);

			String busqueda = texto.toLowerCase();

			boolean encontrado = false;

			for (CasoSocial caso : casos) {

				boolean coincide = false;

				if (caso.getNumeroFicha() != null && caso.getNumeroFicha().toLowerCase().contains(busqueda)) {

					coincide = true;
				}

				if (caso.getParroquia() != null && caso.getParroquia().toLowerCase().contains(busqueda)) {

					coincide = true;
				}

				if (caso.getDetalleEstado() != null
				        && caso.getDetalleEstado().toString().toLowerCase().contains(busqueda)) {

				    coincide = true;
				}

				if (coincide) {

					agregarCasoTabla(caso);
					encontrado = true;
				}
			}

			if (!encontrado) {

				JOptionPane.showMessageDialog(this, "No se encontraron casos sociales que coincidan con la búsqueda.",
						"Sin resultados", JOptionPane.INFORMATION_MESSAGE);
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

		CasoSocial caso = casosSocialController.buscarPorId(idCaso);

		if (caso == null || caso.getUsuario() == null
				|| caso.getUsuario().getIdUsuario() != usuarioActual.getIdUsuario()) {

			JOptionPane.showMessageDialog(this, "No tiene acceso a este caso social.", "Acceso denegado",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		return caso;
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

	private void editarCaso() {

		CasoSocial caso = obtenerCasoSeleccionado();

		if (caso == null) {
			return;
		}

		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		CasoSocialFormDialog dialog = new CasoSocialFormDialog(parent, caso, false);

		dialog.setVisible(true);

		if (dialog.isGuardado()) {
			cargarCasos();
		}
	}
}