package pastoralsocial.ui.trabajadorsocial;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
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
import pastoralsocial.ui.admin.IntegrantesFamiliaresPanel;

public class GrupoFamiliarTrabajadorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaCasos;
	private DefaultTableModel modeloTabla;

	private final CasoSocialController casoSocialController;
	private final Usuario usuarioActual;

	public GrupoFamiliarTrabajadorPanel(Usuario usuarioActual) {

		this.usuarioActual = usuarioActual;
		this.casoSocialController = new CasoSocialController();

		inicializarInterfaz();
		cargarCasos();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		// TITULO
		JLabel lblTitulo = new JLabel("Grupos familiares");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 350, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Gestione los grupos familiares de sus casos sociales asignados.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 75, 600, 25);
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

		// TABLA
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

		// BOTON
		JButton btnGestionar = new JButton("Gestionar grupo familiar");
		btnGestionar.setForeground(new Color(0, 45, 102));
		btnGestionar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGestionar.setBounds(700, 505, 210, 40);
		add(btnGestionar);

		// EVENTOS
		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarCasos();
			}
		});

		btnGestionar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				gestionarGrupoFamiliar();
			}
		});
	}

	private void cargarCasos() {

		try {

			modeloTabla.setRowCount(0);

			List<CasoSocial> casos = casoSocialController.buscarPorUsuario(usuarioActual);

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

				caso.getIdCasoSocial(), caso.getNumeroFicha(), caso.getFechaInicio(), caso.getEstado(),
				caso.getParroquia() });
	}

	private void buscarCasos() {

		String texto = txtBuscar.getText().trim();

		if (texto.isEmpty()) {

			cargarCasos();
			return;
		}

		try {

			List<CasoSocial> casos = casoSocialController.buscarPorUsuario(usuarioActual);

			modeloTabla.setRowCount(0);

			String busqueda = texto.toLowerCase();

			for (CasoSocial caso : casos) {

				boolean coincide = false;

				if (caso.getNumeroFicha() != null && caso.getNumeroFicha().toLowerCase().contains(busqueda)) {

					coincide = true;
				}

				if (caso.getParroquia() != null && caso.getParroquia().toLowerCase().contains(busqueda)) {

					coincide = true;
				}

				if (caso.getEstado() != null && caso.getEstado().toString().toLowerCase().contains(busqueda)) {

					coincide = true;
				}

				if (coincide) {

					agregarCasoTabla(caso);
				}
			}

			if (modeloTabla.getRowCount() == 0) {

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

			JOptionPane.showMessageDialog(this, "Debe seleccionar un caso social.", "Grupo familiar",
					JOptionPane.WARNING_MESSAGE);

			return null;
		}

		int idCaso = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

		CasoSocial caso = casoSocialController.buscarPorId(idCaso);

		if (caso == null || caso.getUsuario() == null
				|| caso.getUsuario().getIdUsuario() != usuarioActual.getIdUsuario()) {

			JOptionPane.showMessageDialog(this, "No tiene acceso al grupo familiar de este caso social.",
					"Acceso denegado", JOptionPane.WARNING_MESSAGE);

			return null;
		}

		return caso;
	}

	private void gestionarGrupoFamiliar() {

		CasoSocial caso = obtenerCasoSeleccionado();

		if (caso == null) {

			return;
		}

		Frame parent = obtenerFramePadre();

		JDialog dialog = new JDialog(parent, "Grupo familiar - " + caso.getNumeroFicha(), true);

		IntegrantesFamiliaresPanel panel = new IntegrantesFamiliaresPanel(caso);

		dialog.setContentPane(panel);
		dialog.setSize(980, 620);
		dialog.setLocationRelativeTo(parent);
		dialog.setResizable(false);
		dialog.setVisible(true);
	}

	private Frame obtenerFramePadre() {

		Window ventana = SwingUtilities.getWindowAncestor(this);

		if (ventana instanceof Frame) {

			return (Frame) ventana;
		}

		if (ventana != null && ventana.getOwner() instanceof Frame) {

			return (Frame) ventana.getOwner();
		}

		return JOptionPane.getFrameForComponent(this);
	}
}