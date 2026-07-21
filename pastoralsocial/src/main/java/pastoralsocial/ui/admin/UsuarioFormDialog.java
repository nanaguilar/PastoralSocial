package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import pastoralsocial.controller.UsuarioController;
import pastoralsocial.dao.RolDAO;
import pastoralsocial.modelo.Rol;
import pastoralsocial.modelo.Genero;
import pastoralsocial.modelo.Usuario;

public class UsuarioFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel;

	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JComboBox<String> cmbTipoIdentificacion;
	private JTextField txtIdentificacion;
	private JTextField txtCorreo;
	private JTextField txtTelefono;
	private JFormattedTextField txtFechaNacimiento;
	private JComboBox<Genero> cmbGenero;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JComboBox<Rol> cmbRol;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private JLabel lblTitulo;

	private final UsuarioController usuarioController;
	private final RolDAO rolDAO;

	private Usuario usuarioEditar;

	private boolean operacionRealizada;
	private boolean hayCambios;

	// CREAR USUARIO
	public UsuarioFormDialog(Frame parent) {
		this(parent, null);

	}

	// EDITAR USUARIO
	public UsuarioFormDialog(Frame parent, Usuario usuarioEditar) {
		super(parent, true);

		this.usuarioController = new UsuarioController();
		this.rolDAO = new RolDAO();
		this.usuarioEditar = usuarioEditar;
		this.operacionRealizada = false;

		inicializarInterfaz();
		cargarRoles();

		if (usuarioEditar == null) {

			prepararNuevoUsuario();

		} else {

			prepararEdicion();
		}

		configurarDeteccionCambios();
		hayCambios = false;
	}

	private void inicializarInterfaz() {

		setTitle("Gestión de Usuarios");
		setBounds(100, 100, 720, 700);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		contentPanel = new JPanel();
		contentPanel.setBackground(new Color(245, 247, 250));
		contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		// TITULO
		lblTitulo = new JLabel("Nuevo usuario");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(40, 25, 400, 35);
		contentPanel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				"Complete la información necesaria para gestionar el acceso del usuario al sistema.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(40, 65, 600, 25);
		contentPanel.add(lblDescripcion);

		// INFORMACION PERSONAL
		JLabel lblInformacionPersonal = new JLabel("INFORMACIÓN PERSONAL");
		lblInformacionPersonal.setForeground(new Color(0, 45, 102));
		lblInformacionPersonal.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblInformacionPersonal.setBounds(40, 105, 250, 25);
		contentPanel.add(lblInformacionPersonal);

		// NOMBRE
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setForeground(new Color(0, 45, 102));
		lblNombre.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblNombre.setBounds(40, 140, 280, 25);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtNombre.setForeground(new Color(0, 45, 102));
		txtNombre.setBounds(40, 165, 280, 35);
		txtNombre.setColumns(10);
		contentPanel.add(txtNombre);

		// APELLIDOS
		JLabel lblApellidos = new JLabel("Apellidos");
		lblApellidos.setForeground(new Color(0, 45, 102));
		lblApellidos.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblApellidos.setBounds(365, 140, 280, 25);
		contentPanel.add(lblApellidos);

		txtApellidos = new JTextField();
		txtApellidos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtApellidos.setForeground(new Color(0, 45, 102));
		txtApellidos.setBounds(365, 165, 280, 35);
		txtApellidos.setColumns(10);
		contentPanel.add(txtApellidos);

		// TIPO DE IDENTIFICACION
		JLabel lblTipoIdentificacion = new JLabel("Tipo de identificación");
		lblTipoIdentificacion.setForeground(new Color(0, 45, 102));
		lblTipoIdentificacion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTipoIdentificacion.setBounds(40, 215, 280, 25);
		contentPanel.add(lblTipoIdentificacion);

		cmbTipoIdentificacion = new JComboBox<String>();
		cmbTipoIdentificacion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTipoIdentificacion.setBounds(40, 240, 280, 35);

		cmbTipoIdentificacion.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Seleccione", "Cédula nacional", "DIMEX", "Pasaporte" }));

		contentPanel.add(cmbTipoIdentificacion);

		// IDENTIFICACION
		JLabel lblIdentificacion = new JLabel("Identificación");
		lblIdentificacion.setForeground(new Color(0, 45, 102));
		lblIdentificacion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblIdentificacion.setBounds(365, 215, 280, 25);
		contentPanel.add(lblIdentificacion);

		txtIdentificacion = new JTextField();
		txtIdentificacion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtIdentificacion.setForeground(new Color(0, 45, 102));
		txtIdentificacion.setBounds(365, 240, 280, 35);
		txtIdentificacion.setColumns(10);
		contentPanel.add(txtIdentificacion);

		// CORREO ELECTRONICO
		JLabel lblCorreo = new JLabel("Correo electrónico");
		lblCorreo.setForeground(new Color(0, 45, 102));
		lblCorreo.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblCorreo.setBounds(40, 290, 280, 25);
		contentPanel.add(lblCorreo);

		txtCorreo = new JTextField();
		txtCorreo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtCorreo.setForeground(new Color(0, 45, 102));
		txtCorreo.setBounds(40, 315, 280, 35);
		txtCorreo.setColumns(10);
		contentPanel.add(txtCorreo);

		// TELEFONO
		JLabel lblTelefono = new JLabel("Teléfono");
		lblTelefono.setForeground(new Color(0, 45, 102));
		lblTelefono.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTelefono.setBounds(365, 290, 280, 25);
		contentPanel.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtTelefono.setForeground(new Color(0, 45, 102));
		txtTelefono.setBounds(365, 315, 280, 35);
		txtTelefono.setColumns(10);
		contentPanel.add(txtTelefono);

		// FECHA DE NACIMIENTO
		JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento");
		lblFechaNacimiento.setForeground(new Color(0, 45, 102));
		lblFechaNacimiento.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFechaNacimiento.setBounds(40, 365, 280, 25);
		contentPanel.add(lblFechaNacimiento);

		txtFechaNacimiento = crearCampoFecha();
		txtFechaNacimiento.setForeground(new Color(0, 45, 102));
		txtFechaNacimiento.setBounds(40, 390, 280, 35);
		txtFechaNacimiento.setToolTipText("Formato: dd/mm/aaaa");
		contentPanel.add(txtFechaNacimiento);

		// GENERO
		JLabel lblGenero = new JLabel("Género");
		lblGenero.setForeground(new Color(0, 45, 102));
		lblGenero.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblGenero.setBounds(365, 365, 280, 25);
		contentPanel.add(lblGenero);

		cmbGenero = new JComboBox<Genero>();
		cmbGenero.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbGenero.setBounds(365, 390, 280, 35);

		for (Genero genero : Genero.values()) {
			cmbGenero.addItem(genero);
		}

		cmbGenero.setSelectedIndex(-1);
		contentPanel.add(cmbGenero);

		// DATOS DE ACCESO
		JLabel lblDatosAcceso = new JLabel("DATOS DE ACCESO");
		lblDatosAcceso.setForeground(new Color(0, 45, 102));
		lblDatosAcceso.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDatosAcceso.setBounds(40, 445, 250, 25);
		contentPanel.add(lblDatosAcceso);

		// USERNAME
		JLabel lblUsername = new JLabel("Usuario");
		lblUsername.setForeground(new Color(0, 45, 102));
		lblUsername.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblUsername.setBounds(40, 475, 280, 25);
		contentPanel.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtUsername.setForeground(new Color(0, 45, 102));
		txtUsername.setBounds(40, 500, 280, 35);
		txtUsername.setColumns(10);
		contentPanel.add(txtUsername);

		// PASSWORD
		JLabel lblPassword = new JLabel("Contraseña");
		lblPassword.setForeground(new Color(0, 45, 102));
		lblPassword.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblPassword.setBounds(365, 475, 280, 25);
		contentPanel.add(lblPassword);

		txtPassword = new JTextField();
		txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtPassword.setForeground(new Color(0, 45, 102));
		txtPassword.setBounds(365, 500, 280, 35);
		txtPassword.setColumns(10);
		contentPanel.add(txtPassword);

		// ROL
		JLabel lblRol = new JLabel("Rol");
		lblRol.setForeground(new Color(0, 45, 102));
		lblRol.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblRol.setBounds(40, 550, 280, 25);
		contentPanel.add(lblRol);

		cmbRol = new JComboBox<Rol>();
		cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbRol.setBounds(40, 575, 280, 35);
		contentPanel.add(cmbRol);

		// CANCELAR
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setBackground(new Color(255, 255, 255));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setBounds(365, 575, 110, 38);
		contentPanel.add(btnCancelar);

		// GUARDAR
		btnGuardar = new JButton("Guardar usuario");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setBackground(new Color(255, 255, 255));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBounds(485, 575, 160, 38);
		contentPanel.add(btnGuardar);

		// EVENTOS
		btnCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				intentarCerrar();
			}
		});

		btnGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				guardarUsuario();
			}
		});

		addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {

				intentarCerrar();
			}
		});
	}

	private void prepararNuevoUsuario() {

		setTitle("Nuevo usuario");

		lblTitulo.setText("Nuevo usuario");

		btnGuardar.setText("Guardar usuario");
	}

	private void prepararEdicion() {

		setTitle("Editar usuario");

		lblTitulo.setText("Editar usuario");

		btnGuardar.setText("Guardar cambios");

		cargarDatosUsuario();
	}

	private JFormattedTextField crearCampoFecha() {

		try {

			MaskFormatter mascara = new MaskFormatter("##/##/####");
			mascara.setPlaceholderCharacter('_');

			JFormattedTextField campo = new JFormattedTextField(mascara);
			campo.setFont(new Font("SansSerif", Font.PLAIN, 12));

			return campo;

		} catch (ParseException e) {

			return new JFormattedTextField();
		}
	}

	private void cargarRoles() {

		try {

			cmbRol.removeAllItems();

			List<Rol> roles = rolDAO.getAll();

			if (roles != null) {

				for (Rol rol : roles) {

					cmbRol.addItem(rol);
				}
			}

			cmbRol.setSelectedIndex(-1);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los roles.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarDatosUsuario() {

		if (usuarioEditar == null) {

			return;
		}

		txtNombre.setText(usuarioEditar.getNombre());
		txtApellidos.setText(usuarioEditar.getApellidos());
		txtIdentificacion.setText(usuarioEditar.getIdentificacion());
		txtCorreo.setText(usuarioEditar.getCorreoElectronico());
		txtTelefono.setText(usuarioEditar.getTelefono());
		txtUsername.setText(usuarioEditar.getUsername());
		txtPassword.setText(usuarioEditar.getPassword());

		if (usuarioEditar.getTipoIdentificacion() != null) {

			cmbTipoIdentificacion.setSelectedItem(usuarioEditar.getTipoIdentificacion());
		}

		if (usuarioEditar.getGenero() != null) {

			cmbGenero.setSelectedItem(usuarioEditar.getGenero());
		}

		if (usuarioEditar.getFechaNacimiento() != null) {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			txtFechaNacimiento.setText(formato.format(usuarioEditar.getFechaNacimiento()));
		}

		if (usuarioEditar.getRol() != null) {

			for (int i = 0; i < cmbRol.getItemCount(); i++) {

				Rol rol = cmbRol.getItemAt(i);

				if (rol != null && rol.getIdRol() == usuarioEditar.getRol().getIdRol()) {

					cmbRol.setSelectedIndex(i);

					break;
				}
			}
		}
	}

	private void configurarDeteccionCambios() {

		java.awt.event.KeyAdapter cambioTexto = new java.awt.event.KeyAdapter() {

			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {

				hayCambios = true;
			}
		};

		txtNombre.addKeyListener(cambioTexto);
		txtApellidos.addKeyListener(cambioTexto);
		txtIdentificacion.addKeyListener(cambioTexto);
		txtCorreo.addKeyListener(cambioTexto);
		txtTelefono.addKeyListener(cambioTexto);
		txtFechaNacimiento.addKeyListener(cambioTexto);
		txtUsername.addKeyListener(cambioTexto);
		txtPassword.addKeyListener(cambioTexto);

		ActionListener cambioCombo = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				hayCambios = true;
			}
		};

		cmbTipoIdentificacion.addActionListener(cambioCombo);
		cmbGenero.addActionListener(cambioCombo);
		cmbRol.addActionListener(cambioCombo);
	}

	private void intentarCerrar() {

		if (!hayCambios) {

			dispose();
			return;
		}

		int opcion = JOptionPane.showConfirmDialog(this, "Hay cambios sin guardar. ¿Desea salir de todas formas?",
				"Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (opcion == JOptionPane.YES_OPTION) {

			dispose();
		}
	}

	private void guardarUsuario() {

		try {

			validarFormulario();

			String nombre = txtNombre.getText().trim();
			String apellidos = txtApellidos.getText().trim();
			String tipoIdentificacion = cmbTipoIdentificacion.getSelectedItem().toString();
			String identificacion = txtIdentificacion.getText().trim();
			String correo = txtCorreo.getText().trim();
			String telefono = txtTelefono.getText().trim();
			Genero genero = (Genero) cmbGenero.getSelectedItem();
			String username = txtUsername.getText().trim();
			String password = txtPassword.getText();
			Rol rol = (Rol) cmbRol.getSelectedItem();
			Date fechaNacimiento = obtenerFechaNacimiento();

			// NUEVO USUARIO
			if (usuarioEditar == null) {

				Usuario nuevoUsuario = new Usuario();

				nuevoUsuario.setNombre(nombre);
				nuevoUsuario.setApellidos(apellidos);
				nuevoUsuario.setTipoIdentificacion(tipoIdentificacion);
				nuevoUsuario.setIdentificacion(identificacion);
				nuevoUsuario.setCorreoElectronico(correo);
				nuevoUsuario.setTelefono(telefono);
				nuevoUsuario.setFechaNacimiento(fechaNacimiento);
				nuevoUsuario.setGenero(genero);
				nuevoUsuario.setUsername(username);
				nuevoUsuario.setPassword(password);
				nuevoUsuario.setRol(rol);
				nuevoUsuario.setEstado(true);

				usuarioController.registrar(nuevoUsuario);

				operacionRealizada = true;

				JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.", "Usuarios",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();

			} else {

				usuarioEditar.setNombre(nombre);
				usuarioEditar.setApellidos(apellidos);
				usuarioEditar.setTipoIdentificacion(tipoIdentificacion);
				usuarioEditar.setIdentificacion(identificacion);
				usuarioEditar.setCorreoElectronico(correo);
				usuarioEditar.setTelefono(telefono);
				usuarioEditar.setFechaNacimiento(fechaNacimiento);
				usuarioEditar.setGenero(genero);
				usuarioEditar.setUsername(username);
				usuarioEditar.setPassword(password);
				usuarioEditar.setRol(rol);

				usuarioController.actualizar(usuarioEditar);

				operacionRealizada = true;

				JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Usuarios",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();
			}

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar el usuario.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void validarFormulario() {

		if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar el nombre.");
		}

		if (txtApellidos.getText() == null || txtApellidos.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar los apellidos.");
		}

		if (cmbTipoIdentificacion.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar el tipo de identificación.");
		}

		String tipoIdentificacionSeleccionado = cmbTipoIdentificacion.getSelectedItem().toString();

		if ("Seleccione".equals(tipoIdentificacionSeleccionado)) {

			throw new IllegalArgumentException("Debe seleccionar el tipo de identificación.");
		}

		if (txtIdentificacion.getText() == null || txtIdentificacion.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar la identificación.");
		}

		String correo = txtCorreo.getText().trim();

		if (!correo.isEmpty() && !correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

			throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
		}

		Date fechaNacimiento = obtenerFechaNacimiento();

		if (fechaNacimiento != null && fechaNacimiento.after(new Date())) {

			throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
		}

		if (cmbGenero.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar el género.");
		}

		if (txtUsername.getText() == null || txtUsername.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar el nombre de usuario.");
		}

		if (txtPassword.getText() == null || txtPassword.getText().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar la contraseña.");
		}

		if (cmbRol.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar un rol.");
		}
	}

	private Date obtenerFechaNacimiento() {

		String texto = txtFechaNacimiento.getText();

		if (texto == null || texto.replace("/", "").replace("_", "").trim().isEmpty()) {

			return null;
		}

		if (texto.contains("_")) {

			throw new IllegalArgumentException("Complete correctamente la fecha de nacimiento.");
		}

		try {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			formato.setLenient(false);

			return formato.parse(texto);

		} catch (ParseException e) {

			throw new IllegalArgumentException("La fecha de nacimiento debe tener el formato dd/mm/aaaa.");
		}
	}

	public boolean isOperacionRealizada() {
		return operacionRealizada;
	}
}
