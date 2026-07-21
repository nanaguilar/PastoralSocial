package pastoralsocial.ui.admin;

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.UsuarioController;
import pastoralsocial.modelo.Usuario;

public class UsuariosPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtBuscar;
	private JTable tablaUsuarios;
	private DefaultTableModel modeloTabla;

	private JButton btnBuscar;
	private JButton btnNuevoUsuario;
	private JButton btnEditar;
	private JButton btnCambiarEstado;

	private Usuario usuarioActual;

	private final UsuarioController usuarioController;

	public UsuariosPanel(Usuario usuarioActual) {

		this.usuarioActual = usuarioActual;

		usuarioController = new UsuarioController();

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		inicializarComponentes();
		configurarEventos();
		cargarUsuarios();
	}

	private void inicializarComponentes() {

		// TITULO
		JLabel lblTitulo = new JLabel("Usuarios");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 300, 40);
		add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel("Administre los usuarios con acceso al sistema.");
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

		// BOTON NUEVO USUARIO
		btnNuevoUsuario = new JButton("Nuevo usuario");
		btnNuevoUsuario.setForeground(new Color(0, 45, 102));
		btnNuevoUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnNuevoUsuario.setFocusPainted(false);
		btnNuevoUsuario.setBounds(720, 125, 180, 38);
		add(btnNuevoUsuario);

		// MODELO TABLA
		modeloTabla = new DefaultTableModel(

				new Object[][] {},

				new String[] { "ID", "Nombre Completo", "Identificación", "Usuario", "Rol", "Estado"

				}) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {

				return false;
			}
		};

		// TABLA
		tablaUsuarios = new JTable(modeloTabla);
		tablaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tablaUsuarios.setRowHeight(32);
		tablaUsuarios.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// SCROLL
		JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
		scrollPane.setBounds(40, 195, 860, 280);
		add(scrollPane);

		// BOTON EDITAR USUARIO
		btnEditar = new JButton("Editar usuario");
		btnEditar.setForeground(new Color(0, 45, 102));
		btnEditar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditar.setFocusPainted(false);
		btnEditar.setBounds(560, 500, 160, 40);
		add(btnEditar);

		// BOTON CAMBIAR ESTADO USUARIO
		btnCambiarEstado = new JButton("Cambiar estado");
		btnCambiarEstado.setForeground(new Color(0, 45, 102));
		btnCambiarEstado.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCambiarEstado.setFocusPainted(false);
		btnCambiarEstado.setBounds(740, 500, 160, 40);
		add(btnCambiarEstado);
	}

	private void configurarEventos() {

		// BUSCAR
		btnBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarUsuarios();
			}
		});

		// BUSCADOR
		txtBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				buscarUsuarios();
			}
		});

		// NUEVO USUARIO
		btnNuevoUsuario.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Frame parent = (Frame) SwingUtilities.getWindowAncestor(UsuariosPanel.this);

				UsuarioFormDialog dialog = new UsuarioFormDialog(parent);

				dialog.setVisible(true);

				if (dialog.isOperacionRealizada()) {

					cargarUsuarios();
				}
			}
		});

		// EDITAR USUARIO
		btnEditar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				editarUsuario();
			}
		});

		// CAMBIAR ESTADO
		btnCambiarEstado.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cambiarEstadoUsuario();
			}
		});
	}

	public void cargarUsuarios() {

		try {

			List<Usuario> usuarios = usuarioController.listar();

			mostrarUsuarios(usuarios);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los usuarios.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostrarUsuarios(List<Usuario> usuarios) {

		modeloTabla.setRowCount(0);

		if (usuarios == null) {

			return;
		}

		for (Usuario usuario : usuarios) {

			String nombreCompleto = "";

			if (usuario.getNombre() != null) {

				nombreCompleto = usuario.getNombre();
			}

			if (usuario.getApellidos() != null) {

				nombreCompleto = nombreCompleto + " " + usuario.getApellidos();
			}

			nombreCompleto = nombreCompleto.trim();

			String rol = "Sin rol";

			if (usuario.getRol() != null) {

				rol = usuario.getRol().getNombre();
			}

			String estado;

			if (usuario.getEstado()) {

				estado = "Activo";

			} else {

				estado = "Inactivo";
			}

			modeloTabla.addRow(

					new Object[] {

							usuario.getIdUsuario(), nombreCompleto, usuario.getIdentificacion(), usuario.getUsername(),
							rol, estado });
		}
	}

	private void buscarUsuarios() {

		try {

			String texto = txtBuscar.getText().trim().toLowerCase();

			List<Usuario> usuarios = usuarioController.listar();

			modeloTabla.setRowCount(0);

			int encontrados = 0;

			for (Usuario usuario : usuarios) {

				String nombre = "";
				String apellidos = "";
				String identificacion = "";
				String username = "";
				String rol = "";

				if (usuario.getNombre() != null) {

					nombre = usuario.getNombre().toLowerCase();
				}

				if (usuario.getApellidos() != null) {

					apellidos = usuario.getApellidos().toLowerCase();
				}

				if (usuario.getIdentificacion() != null) {

					identificacion = String.valueOf(usuario.getIdentificacion()).toLowerCase();
				}

				if (usuario.getUsername() != null) {

					username = usuario.getUsername().toLowerCase();
				}

				if (usuario.getRol() != null && usuario.getRol().getNombre() != null) {

					rol = usuario.getRol().getNombre().toLowerCase();
				}

				if (texto.isEmpty() || nombre.contains(texto) || apellidos.contains(texto)
						|| identificacion.contains(texto) || username.contains(texto) || rol.contains(texto)) {

					encontrados++;

					String nombreCompleto = ((usuario.getNombre() != null ? usuario.getNombre() : "") + " "
							+ (usuario.getApellidos() != null ? usuario.getApellidos() : "")).trim();

					String nombreRol = usuario.getRol() != null ? usuario.getRol().getNombre() : "Sin rol";

					String estado;

					if (usuario.getEstado()) {

						estado = "Activo";

					} else {

						estado = "Inactivo";
					}

					modeloTabla.addRow(new Object[] {

							usuario.getIdUsuario(), nombreCompleto, usuario.getIdentificacion(), usuario.getUsername(),
							nombreRol, estado });
				}
			}

			if (!texto.isEmpty() && encontrados == 0) {

				JOptionPane.showMessageDialog(this, "No se encontraron usuarios que coincidan con la búsqueda.",
						"Búsqueda", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible realizar la búsqueda.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Usuario obtenerUsuarioSeleccionado() {

		int fila = tablaUsuarios.getSelectedRow();

		if (fila == -1) {

			return null;
		}

		int filaModelo = tablaUsuarios.convertRowIndexToModel(fila);

		Object valorId = modeloTabla.getValueAt(filaModelo, 0);

		if (valorId == null) {

			return null;
		}

		int idUsuario = Integer.parseInt(valorId.toString());

		return usuarioController.buscarPorId(idUsuario);
	}

	private void editarUsuario() {

		int filaSeleccionada = tablaUsuarios.getSelectedRow();

		if (filaSeleccionada == -1) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para editar.", "Editar usuario",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		try {
			int idUsuario = Integer.parseInt(tablaUsuarios.getValueAt(filaSeleccionada, 0).toString());

			Usuario usuario = usuarioController.buscarPorId(idUsuario);

			if (usuario == null) {

				JOptionPane.showMessageDialog(this, "No se encontró el usuario seleccionado.", "Editar usuario",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			Frame framePadre = (Frame) SwingUtilities.getWindowAncestor(this);

			UsuarioFormDialog dialog = new UsuarioFormDialog(framePadre, usuario);

			dialog.setVisible(true);

			if (dialog.isOperacionRealizada()) {

				cargarUsuarios();
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar el usuario para editar.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cambiarEstadoUsuario() {

		Usuario usuarioSeleccionado = obtenerUsuarioSeleccionado();

		if (usuarioSeleccionado == null) {

			JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para cambiar su estado.", "Cambiar estado",
					JOptionPane.WARNING_MESSAGE);

			return;
		}

		if (usuarioActual != null && usuarioSeleccionado.getIdUsuario() == usuarioActual.getIdUsuario()
				&& usuarioSeleccionado.getEstado()) {

			JOptionPane.showMessageDialog(this,
					"No puede desactivar su propio usuario mientras mantiene una sesión activa.",
					"Operación no permitida", JOptionPane.WARNING_MESSAGE);

			return;
		}

		boolean estadoActual = usuarioSeleccionado.getEstado();

		String estadoActualTexto;

		String nuevoEstadoTexto;

		if (estadoActual) {

			estadoActualTexto = "Activo";
			nuevoEstadoTexto = "Inactivo";

		} else {

			estadoActualTexto = "Inactivo";
			nuevoEstadoTexto = "Activo";
		}

		String mensaje = "Usuario: " + usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellidos() + "\n"
				+ "Estado actual: " + estadoActualTexto + "\n\n" + "¿Desea cambiar el estado a " + nuevoEstadoTexto
				+ "?";

		int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar cambio de estado",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (opcion != JOptionPane.YES_OPTION) {

			return;
		}

		try {

			usuarioController.cambiarEstado(usuarioSeleccionado, usuarioActual);

			JOptionPane.showMessageDialog(this,
					"El estado del usuario fue cambiado a " + nuevoEstadoTexto + " correctamente.", "Cambiar estado",
					JOptionPane.INFORMATION_MESSAGE);

			cargarUsuarios();

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Cambiar estado", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cambiar el estado del usuario.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}