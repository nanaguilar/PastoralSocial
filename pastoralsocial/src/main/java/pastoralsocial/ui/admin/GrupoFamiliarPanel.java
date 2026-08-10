package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
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

public class GrupoFamiliarPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaCasos;
	private DefaultTableModel modeloTabla;

	private final CasoSocialController casoSocialController;
	private final Usuario usuarioActual;

	public GrupoFamiliarPanel(Usuario usuarioActual) {

		this.usuarioActual = usuarioActual;
		this.casoSocialController = new CasoSocialController();

		inicializarInterfaz();
		cargarCasos();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		// TITULO
		JLabel lblTitulo = new JLabel("Grupo familiar");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 350, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Seleccione un caso social para consultar y gestionar su grupo familiar.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(40, 75, 650, 25);
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

		// BOTON GESTIONAR
		JButton btnGestionar = new JButton("Gestionar grupo familiar");
		btnGestionar.setForeground(new Color(0, 45, 102));
		btnGestionar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGestionar.setBounds(690, 505, 220, 40);
		add(btnGestionar);

		// EVENTO BUSCAR
		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarCasos();
			}
		});

		// EVENTO GESTIONAR
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

			List<CasoSocial> casos;

			if (usuarioActual.getDetalleRol().getDescripcion().equals("Administrador")
					|| usuarioActual.getDetalleRol().getDescripcion().equals("Coordinador")) {

				casos = casoSocialController.listar();

			} else {

				casos = casoSocialController.buscarPorUsuario(usuarioActual);
			}

			for (CasoSocial caso : casos) {

				agregarCasoTabla(caso);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los casos sociales.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarCasoTabla(CasoSocial caso) {

		String responsable = "";

		if (caso.getUsuario() != null) {

			responsable = caso.getUsuario().getNombre() + " " + caso.getUsuario().getApellidos();
		}

		modeloTabla.addRow(new Object[] {

				caso.getIdCasoSocial(), caso.getNumeroFicha(), caso.getFechaInicio(), caso.getDetalleEstado(),
				caso.getParroquia(), responsable });
	}

	private void buscarCasos() {

		String texto = txtBuscar.getText().trim().toLowerCase();

		try {

			List<CasoSocial> casos;

			if (usuarioActual.getDetalleRol().getDescripcion().equals("Administrador")
					|| usuarioActual.getDetalleRol().getDescripcion().equals("Coordinador")) {

				casos = casoSocialController.listar();

			} else {

				casos = casoSocialController.buscarPorUsuario(usuarioActual);
			}

			modeloTabla.setRowCount(0);

			for (CasoSocial caso : casos) {

				if (texto.isEmpty()
						|| (caso.getNumeroFicha() != null && caso.getNumeroFicha().toLowerCase().contains(texto))
						|| (caso.getParroquia() != null && caso.getParroquia().toLowerCase().contains(texto))
						|| (caso.getProvincia() != null && caso.getProvincia().toLowerCase().contains(texto))
						|| (caso.getCanton() != null && caso.getCanton().toLowerCase().contains(texto))
						|| (caso.getDistrito() != null && caso.getDistrito().toLowerCase().contains(texto))) {

					agregarCasoTabla(caso);
				}
			}

			if (tablaCasos.getRowCount() == 0) {

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

		return casoSocialController.buscarPorId(idCaso);
	}

	private void gestionarGrupoFamiliar() {

		CasoSocial caso = obtenerCasoSeleccionado();

		if (caso == null) {
			return;
		}

		Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

		JDialog dialog = new JDialog(parent, "Grupo familiar - " + caso.getNumeroFicha(), true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		IntegrantesFamiliaresPanel panelGrupoFamiliar = new IntegrantesFamiliaresPanel(caso);

		dialog.setContentPane(panelGrupoFamiliar);

		dialog.setSize(970, 850);
		dialog.setLocationRelativeTo(parent);
		dialog.setResizable(false);
		dialog.setVisible(true);
	}

	public void actualizar() {
		cargarCasos();
	}
}