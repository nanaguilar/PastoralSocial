package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Genero;
import pastoralsocial.modelo.IntegranteFamiliar;

public class IntegranteFamiliarFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtIdentificacion;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtTelefono;
	private JTextField txtCorreo;
	private JTextField txtParentesco;
	private JTextField txtNivelEducativo;
	private JTextField txtSeguro;
	private JTextField txtPais;
	private JTextField txtProvincia;
	private JTextField txtCanton;
	private JTextField txtDistrito;
	private JFormattedTextField txtFechaNacimiento;
	private JTextArea txtDireccion;
	private JComboBox<String> cmbTipoIdentificacion;
	private JComboBox<Genero> cmbGenero;

	private JRadioButton rdbSiJefeHogar;
	private JRadioButton rdbNoJefeHogar;

	private JButton btnGuardar;

	private final IntegranteFamiliarController integranteController;

	private final CasoSocial casoSocial;
	private IntegranteFamiliar integrante;

	private boolean guardado;
	private boolean modoEdicion;

	private boolean hayCambios;

	// CREAR INTEGRANTE FAMILIAR
	public IntegranteFamiliarFormDialog(Frame parent, CasoSocial casoSocial) {

		super(parent, "Nuevo integrante familiar", true);

		this.casoSocial = casoSocial;
		this.integrante = null;
		this.modoEdicion = false;
		this.integranteController = new IntegranteFamiliarController();

		inicializarInterfaz();

		configurarDeteccionCambios();
		hayCambios = false;

		setLocationRelativeTo(parent);
	}

	// EDITAR INTEGRANTE FAMILIAR
	public IntegranteFamiliarFormDialog(Frame parent, CasoSocial casoSocial, IntegranteFamiliar integrante) {

		super(parent, "Editar integrante familiar", true);

		this.casoSocial = casoSocial;
		this.integrante = integrante;
		this.modoEdicion = true;
		this.integranteController = new IntegranteFamiliarController();

		inicializarInterfaz();

		cargarDatos();

		configurarDeteccionCambios();
		hayCambios = false;

		setLocationRelativeTo(parent);
	}

	private void inicializarInterfaz() {

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(245, 247, 250));
		contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		// TITULO
		JLabel lblTitulo = new JLabel(modoEdicion ? "Editar integrante familiar" : "Nuevo integrante familiar");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(25, 15, 430, 40);
		contentPanel.add(lblTitulo);

		// DESCRIPCION
		JLabel lblDescripcion = new JLabel(
				modoEdicion ? "Actualice la información del integrante del caso " + casoSocial.getNumeroFicha() + "."
						: "Registre un nuevo integrante en el caso " + casoSocial.getNumeroFicha() + ".");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(25, 52, 600, 25);
		contentPanel.add(lblDescripcion);

		// INFORMACION PERSONAL
		JLabel lblInformacionPersonal = new JLabel("Información personal");
		lblInformacionPersonal.setForeground(new Color(0, 45, 102));
		lblInformacionPersonal.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblInformacionPersonal.setBounds(25, 90, 250, 25);
		contentPanel.add(lblInformacionPersonal);

		// TIPO IDENTIFICACION
		JLabel lblTipoIdentificacion = crearEtiqueta("Tipo de identificación");
		lblTipoIdentificacion.setBounds(25, 125, 210, 20);
		contentPanel.add(lblTipoIdentificacion);

		cmbTipoIdentificacion = new JComboBox<String>();
		cmbTipoIdentificacion.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Seleccione", "Cédula nacional", "DIMEX", "Pasaporte" }));
		cmbTipoIdentificacion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTipoIdentificacion.setBounds(25, 148, 210, 35);
		contentPanel.add(cmbTipoIdentificacion);

		// IDENTIFICACION
		JLabel lblIdentificacion = crearEtiqueta("Identificación");
		lblIdentificacion.setBounds(265, 125, 200, 20);
		contentPanel.add(lblIdentificacion);
		txtIdentificacion = crearCampoTexto();
		txtIdentificacion.setBounds(265, 148, 200, 35);
		contentPanel.add(txtIdentificacion);

		// GENERO
		JLabel lblGenero = crearEtiqueta("Género");
		lblGenero.setBounds(495, 125, 215, 20);
		contentPanel.add(lblGenero);
		cmbGenero = new JComboBox<Genero>();
		cmbGenero.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbGenero.setBounds(495, 148, 215, 35);
		contentPanel.add(cmbGenero);

		cargarGeneros();

		// NOMBRE
		JLabel lblNombre = crearEtiqueta("Nombre");
		lblNombre.setBounds(25, 195, 210, 20);
		contentPanel.add(lblNombre);
		txtNombre = crearCampoTexto();
		txtNombre.setBounds(25, 218, 210, 35);
		contentPanel.add(txtNombre);

		// APELLIDOS
		JLabel lblApellidos = crearEtiqueta("Apellidos");
		lblApellidos.setBounds(265, 195, 200, 20);
		contentPanel.add(lblApellidos);
		txtApellidos = crearCampoTexto();
		txtApellidos.setBounds(265, 218, 200, 35);
		contentPanel.add(txtApellidos);

		// FECHA NACIMIENTO
		JLabel lblFechaNacimiento = crearEtiqueta("Fecha de nacimiento");
		lblFechaNacimiento.setBounds(495, 195, 215, 20);
		contentPanel.add(lblFechaNacimiento);
		txtFechaNacimiento = crearCampoFecha();
		txtFechaNacimiento.setBounds(495, 218, 215, 35);
		contentPanel.add(txtFechaNacimiento);

		// INFORMACION FAMILIAR Y CONTACTO
		JLabel lblInformacionFamiliar = new JLabel("Información familiar y contacto");
		lblInformacionFamiliar.setForeground(new Color(0, 45, 102));
		lblInformacionFamiliar.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblInformacionFamiliar.setBounds(25, 280, 350, 25);
		contentPanel.add(lblInformacionFamiliar);

		// PARENTESCO
		JLabel lblParentesco = crearEtiqueta("Parentesco");
		lblParentesco.setBounds(25, 315, 210, 20);
		contentPanel.add(lblParentesco);
		txtParentesco = crearCampoTexto();
		txtParentesco.setBounds(25, 338, 210, 35);
		contentPanel.add(txtParentesco);

		// JEFE HOGAR
		JLabel lblJefeHogar = crearEtiqueta("Jefe de hogar");
		lblJefeHogar.setBounds(265, 315, 200, 20);
		contentPanel.add(lblJefeHogar);

		rdbSiJefeHogar = new JRadioButton("Sí");
		rdbNoJefeHogar = new JRadioButton("No");
		rdbSiJefeHogar.setBackground(new Color(245, 247, 250));
		rdbNoJefeHogar.setBackground(new Color(245, 247, 250));
		rdbSiJefeHogar.setFont(new Font("SansSerif", Font.PLAIN, 12));
		rdbNoJefeHogar.setFont(new Font("SansSerif", Font.PLAIN, 12));
		rdbSiJefeHogar.setBounds(265, 340, 60, 30);
		rdbNoJefeHogar.setBounds(330, 340, 60, 30);

		ButtonGroup grupoJefe = new ButtonGroup();
		grupoJefe.add(rdbSiJefeHogar);
		grupoJefe.add(rdbNoJefeHogar);
		rdbNoJefeHogar.setSelected(true);
		contentPanel.add(rdbSiJefeHogar);
		contentPanel.add(rdbNoJefeHogar);

		// TELEFONO
		JLabel lblTelefono = crearEtiqueta("Teléfono");
		lblTelefono.setBounds(495, 315, 215, 20);
		contentPanel.add(lblTelefono);
		txtTelefono = crearCampoTexto();
		txtTelefono.setBounds(495, 338, 215, 35);
		contentPanel.add(txtTelefono);

		// CORREO
		JLabel lblCorreo = crearEtiqueta("Correo electrónico");
		lblCorreo.setBounds(25, 385, 210, 20);
		contentPanel.add(lblCorreo);
		txtCorreo = crearCampoTexto();
		txtCorreo.setBounds(25, 408, 210, 35);
		contentPanel.add(txtCorreo);

		// NIVEL EDUCATIVO
		JLabel lblNivelEducativo = crearEtiqueta("Nivel educativo");
		lblNivelEducativo.setBounds(265, 385, 200, 20);
		contentPanel.add(lblNivelEducativo);
		txtNivelEducativo = crearCampoTexto();
		txtNivelEducativo.setBounds(265, 408, 200, 35);
		contentPanel.add(txtNivelEducativo);

		// SEGURO
		JLabel lblSeguro = crearEtiqueta("Seguro");
		lblSeguro.setBounds(495, 385, 215, 20);
		contentPanel.add(lblSeguro);
		txtSeguro = crearCampoTexto();
		txtSeguro.setBounds(495, 408, 215, 35);
		contentPanel.add(txtSeguro);

		// UBICACION
		JLabel lblUbicacion = new JLabel("Ubicación");
		lblUbicacion.setForeground(new Color(0, 45, 102));
		lblUbicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblUbicacion.setBounds(25, 470, 250, 25);
		contentPanel.add(lblUbicacion);

		// PAIS
		JLabel lblPais = crearEtiqueta("País");
		lblPais.setBounds(25, 505, 155, 20);
		contentPanel.add(lblPais);
		txtPais = crearCampoTexto();
		txtPais.setBounds(25, 528, 155, 35);
		contentPanel.add(txtPais);

		// PROVINCIA
		JLabel lblProvincia = crearEtiqueta("Provincia");
		lblProvincia.setBounds(195, 505, 155, 20);
		contentPanel.add(lblProvincia);
		txtProvincia = crearCampoTexto();
		txtProvincia.setBounds(195, 528, 155, 35);
		contentPanel.add(txtProvincia);

		// CANTON
		JLabel lblCanton = crearEtiqueta("Cantón");
		lblCanton.setBounds(365, 505, 155, 20);
		contentPanel.add(lblCanton);
		txtCanton = crearCampoTexto();
		txtCanton.setBounds(365, 528, 155, 35);
		contentPanel.add(txtCanton);

		// DISTRITO
		JLabel lblDistrito = crearEtiqueta("Distrito");
		lblDistrito.setBounds(535, 505, 175, 20);
		contentPanel.add(lblDistrito);
		txtDistrito = crearCampoTexto();
		txtDistrito.setBounds(535, 528, 175, 35);
		contentPanel.add(txtDistrito);

		// DIRECCION
		JLabel lblDireccion = crearEtiqueta("Dirección");
		lblDireccion.setBounds(25, 575, 200, 20);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextArea();
		txtDireccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtDireccion.setLineWrap(true);
		txtDireccion.setWrapStyleWord(true);

		JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
		scrollDireccion.setBounds(25, 598, 685, 55);
		contentPanel.add(scrollDireccion);

		// CANCELAR
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setBounds(400, 665, 120, 40);
		contentPanel.add(btnCancelar);

		// GUARDAR
		btnGuardar = new JButton(modoEdicion ? "Guardar cambios" : "Guardar integrante");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setBounds(535, 665, 175, 40);
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

				guardar();
			}
		});

		setSize(760, 760);
		setResizable(false);
	}

	private JLabel crearEtiqueta(String texto) {

		JLabel label = new JLabel(texto);

		label.setForeground(new Color(0, 45, 102));
		label.setFont(new Font("SansSerif", Font.BOLD, 12));

		return label;
	}

	private JTextField crearCampoTexto() {

		JTextField campo = new JTextField();

		campo.setFont(new Font("SansSerif", Font.PLAIN, 12));

		return campo;
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

	private void cargarGeneros() {

		cmbGenero.removeAllItems();
		cmbGenero.addItem(null);

		Genero[] generos = Genero.values();

		for (int i = 0; i < generos.length; i++) {

			cmbGenero.addItem(generos[i]);
		}
	}

	private void cargarDatos() {

		if (integrante == null) {
			return;
		}

		if (integrante.getTipoIdentificacion() != null) {
			cmbTipoIdentificacion.setSelectedItem(integrante.getTipoIdentificacion());
		}

		txtIdentificacion.setText(integrante.getIdentificacion() != null ? integrante.getIdentificacion() : "");
		txtNombre.setText(integrante.getNombre() != null ? integrante.getNombre() : "");
		txtApellidos.setText(integrante.getApellidos() != null ? integrante.getApellidos() : "");

		if (integrante.getFechaNacimiento() != null) {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			txtFechaNacimiento.setText(formato.format(integrante.getFechaNacimiento()));
		}

		if (integrante.getGenero() != null) {
			cmbGenero.setSelectedItem(integrante.getGenero());
		}

		if (integrante.isJefeHogar()) {

			rdbSiJefeHogar.setSelected(true);

		} else {

			rdbNoJefeHogar.setSelected(true);
		}

		txtParentesco.setText(integrante.getParentesco() != null ? integrante.getParentesco() : "");
		txtTelefono.setText(integrante.getTelefono() != null ? integrante.getTelefono() : "");
		txtCorreo.setText(integrante.getCorreoElectronico() != null ? integrante.getCorreoElectronico() : "");
		txtNivelEducativo.setText(integrante.getNivelEducativo() != null ? integrante.getNivelEducativo() : "");
		txtSeguro.setText(integrante.getSeguro() != null ? integrante.getSeguro() : "");
		txtPais.setText(integrante.getPais() != null ? integrante.getPais() : "");
		txtProvincia.setText(integrante.getProvincia() != null ? integrante.getProvincia() : "");
		txtCanton.setText(integrante.getCanton() != null ? integrante.getCanton() : "");
		txtDistrito.setText(integrante.getDistrito() != null ? integrante.getDistrito() : "");
		txtDireccion.setText(integrante.getDireccion() != null ? integrante.getDireccion() : "");
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

		ActionListener cambioCombo = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				hayCambios = true;
			}
		};

		cmbTipoIdentificacion.addActionListener(cambioCombo);
		cmbGenero.addActionListener(cambioCombo);
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

	private void guardar() {

		try {

			validarFormulario();

			// CONFIRMAR ANTES DE GUARDAR
			String mensaje;

			if (modoEdicion) {

				mensaje = "¿Desea guardar los cambios realizados?";

			} else {

				mensaje = "¿Desea registrar este integrante en el caso " + casoSocial.getNumeroFicha() + "?";
			}

			int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (opcion != JOptionPane.YES_OPTION) {

				return;
			}

			// CREAR O UTILIZAR EL INTEGRANTE EXISTENTE
			IntegranteFamiliar integranteGuardar;

			if (modoEdicion) {

				integranteGuardar = integrante;

			} else {

				integranteGuardar = new IntegranteFamiliar();
				integranteGuardar.setCasoSocial(casoSocial);
				integranteGuardar.setEstado(true);
			}

			// ASIGNAR DATOS DEL FORMULARIO
			integranteGuardar.setTipoIdentificacion(cmbTipoIdentificacion.getSelectedItem().toString());
			integranteGuardar.setIdentificacion(txtIdentificacion.getText().trim());
			integranteGuardar.setNombre(txtNombre.getText().trim());
			integranteGuardar.setApellidos(txtApellidos.getText().trim());
			integranteGuardar.setFechaNacimiento(obtenerFechaNacimiento());
			integranteGuardar.setGenero((Genero) cmbGenero.getSelectedItem());
			integranteGuardar.setJefeHogar(rdbSiJefeHogar.isSelected());
			integranteGuardar.setParentesco(txtParentesco.getText().trim());
			integranteGuardar.setTelefono(txtTelefono.getText().trim());
			integranteGuardar.setCorreoElectronico(txtCorreo.getText().trim());
			integranteGuardar.setNivelEducativo(txtNivelEducativo.getText().trim());
			integranteGuardar.setSeguro(txtSeguro.getText().trim());
			integranteGuardar.setPais(txtPais.getText().trim());
			integranteGuardar.setProvincia(txtProvincia.getText().trim());
			integranteGuardar.setCanton(txtCanton.getText().trim());
			integranteGuardar.setDistrito(txtDistrito.getText().trim());
			integranteGuardar.setDireccion(txtDireccion.getText().trim());

			// GUARDAR EN BASE DE DATOS
			if (modoEdicion) {

				integranteController.actualizar(integranteGuardar);

			} else {

				integranteController.registrar(integranteGuardar);
			}

			guardado = true;
			hayCambios = false;

			JOptionPane.showMessageDialog(this,
					modoEdicion ? "El integrante se actualizó correctamente."
							: "El integrante se registró correctamente.",
					"Grupo familiar", JOptionPane.INFORMATION_MESSAGE);

			dispose();

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar la información del integrante.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void validarFormulario() {

		if (cmbTipoIdentificacion.getSelectedIndex() <= 0) {

			throw new IllegalArgumentException("Debe seleccionar el tipo de identificación.");
		}

		if (txtIdentificacion.getText() == null || txtIdentificacion.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("La identificación es obligatoria.");
		}

		if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("El nombre es obligatorio.");
		}

		if (txtApellidos.getText() == null || txtApellidos.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Los apellidos son obligatorios.");
		}

		Date fechaNacimiento = obtenerFechaNacimiento();

		if (fechaNacimiento != null && fechaNacimiento.after(new Date())) {

			throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
		}

		String correo = txtCorreo.getText().trim();

		if (!correo.isEmpty() && !correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

			throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
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

	public boolean isGuardado() {

		return guardado;
	}
}