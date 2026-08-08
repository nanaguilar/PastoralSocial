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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.util.List;

import pastoralsocial.controller.DetalleController;
import pastoralsocial.controller.MaestroController;
import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Detalle;
import pastoralsocial.modelo.Maestro;
import pastoralsocial.modelo.IntegranteFamiliar;

public class IntegranteFamiliarFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String CATALOGO_GENERO = "Genero";
	private static final String CATALOGO_PARENTESCO = "Parentesco";
	private static final String CATALOGO_NIVEL_EDUCATIVO = "Nivel Educativo";
	private static final String CATALOGO_SEGURO = "Seguro";

	private JTextField txtIdentificacion;
	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtTelefono;
	private JTextField txtCorreo;
	private JComboBox<Detalle> cmbParentesco;
	private JComboBox<Detalle> cmbNivelEducativo;
	private JComboBox<Detalle> cmbSeguro;
	private JTextField txtSalud;
	private JTextField txtMigracion;
	private JTextField txtPais;
	private JFormattedTextField txtFechaNacimiento;
	private JComboBox<String> cmbTipoIdentificacion;
	private JComboBox<Detalle> cmbGenero;

	private JRadioButton rdbSiJefeHogar;
	private JRadioButton rdbNoJefeHogar;

	private JButton btnGuardar;

	private final IntegranteFamiliarController integranteController;
	private final DetalleController detalleController;
	private final MaestroController maestroController;

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
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializarInterfaz();

		cargarCatalogos();

		cmbGenero.removeAllItems();

		for (Detalle detalle : obtenerDetallesDelCatalogo(CATALOGO_GENERO)) {

			cmbGenero.addItem(detalle);
		}

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
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializarInterfaz();

		cargarCatalogos();
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

		// GÉNERO
		JLabel lblGenero = new JLabel("Género");
		lblGenero.setForeground(new Color(0, 45, 102));
		lblGenero.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblGenero.setBounds(495, 125, 215, 20);
		getContentPane().add(lblGenero);

		cmbGenero = new JComboBox<Detalle>();
		cmbGenero.setForeground(new Color(0, 45, 102));
		cmbGenero.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbGenero.setBounds(495, 148, 215, 35);
		getContentPane().add(cmbGenero);

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
		cmbParentesco = new JComboBox<Detalle>();
		cmbParentesco.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbParentesco.setBounds(25, 338, 210, 35);
		contentPanel.add(cmbParentesco);

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
		cmbNivelEducativo = new JComboBox<Detalle>();
		cmbNivelEducativo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbNivelEducativo.setBounds(265, 408, 200, 35);
		contentPanel.add(cmbNivelEducativo);

		// SEGURO
		JLabel lblSeguro = crearEtiqueta("Seguro");
		lblSeguro.setBounds(495, 385, 215, 20);
		contentPanel.add(lblSeguro);
		cmbSeguro = new JComboBox<Detalle>();
		cmbSeguro.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbSeguro.setBounds(495, 408, 215, 35);
		contentPanel.add(cmbSeguro);

		// SALUD
		JLabel lblSalud = crearEtiqueta("Salud");
		lblSalud.setBounds(25, 470, 210, 20);
		contentPanel.add(lblSalud);
		txtSalud = crearCampoTexto();
		txtSalud.setBounds(25, 493, 210, 35);
		contentPanel.add(txtSalud);

		// MIGRACIÓN
		JLabel lblMigracion = crearEtiqueta("Migración");
		lblMigracion.setBounds(265, 470, 200, 20);
		contentPanel.add(lblMigracion);
		txtMigracion = crearCampoTexto();
		txtMigracion.setBounds(265, 493, 200, 35);
		contentPanel.add(txtMigracion);

		// PAIS
		JLabel lblPais = crearEtiqueta("País");
		lblPais.setBounds(495, 470, 215, 20);
		contentPanel.add(lblPais);
		txtPais = crearCampoTexto();
		txtPais.setBounds(495, 493, 215, 35);
		contentPanel.add(txtPais);

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

		if (modoEdicion) {

			cmbTipoIdentificacion.setEnabled(false);
			txtIdentificacion.setEditable(false);
			txtNombre.setEditable(false);
			txtApellidos.setEditable(false);
			txtFechaNacimiento.setEditable(false);
		}

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

		try {

			cmbGenero.removeAllItems();

			Maestro catalogoGenero = maestroController.buscarPorNombre("Genero");

			if (catalogoGenero == null) {

				JOptionPane.showMessageDialog(this,
						"No se encontró el catálogo 'Genero'. Verifique que esté registrado.", "Catálogo no encontrado",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			List<Detalle> generos = detalleController.listarPorMaestro(catalogoGenero.getIdMaestro());

			for (Detalle detalle : generos) {

				cmbGenero.addItem(detalle);
			}

			cmbGenero.setSelectedIndex(-1);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los géneros.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarCatalogos() {

		cargarCatalogo(CATALOGO_PARENTESCO, cmbParentesco);
		cargarCatalogo(CATALOGO_NIVEL_EDUCATIVO, cmbNivelEducativo);
		cargarCatalogo(CATALOGO_SEGURO, cmbSeguro);
		cargarCatalogo(CATALOGO_GENERO, cmbGenero);
	}

	private void cargarCatalogo(String nombreCatalogo, JComboBox<Detalle> combo) {

		combo.removeAllItems();

		Maestro maestro = maestroController.buscarPorNombre(nombreCatalogo);

		if (maestro == null) {

			JOptionPane.showMessageDialog(this,
					"No se encontró el catálogo '" + nombreCatalogo + "'. Verifique que esté registrado en Main.java.",
					"Catálogo no encontrado", JOptionPane.WARNING_MESSAGE);

			return;
		}

		List<Detalle> detalles = detalleController.listarPorMaestro(maestro.getIdMaestro());

		for (Detalle detalle : detalles) {

			combo.addItem(detalle);
		}
	}

	private List<Detalle> obtenerDetallesDelCatalogo(String nombreCatalogo) {

		Maestro catalogo = maestroController.buscarPorNombre(nombreCatalogo);

		if (catalogo == null) {

			return new java.util.ArrayList<Detalle>();
		}

		return detalleController.listarPorMaestro(catalogo.getIdMaestro());
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

		if (integrante.getDetalleGenero() != null) {

			for (int i = 0; i < cmbGenero.getItemCount(); i++) {

				Detalle detalle = cmbGenero.getItemAt(i);

				if (detalle != null && detalle.getIdDetalle() == integrante.getDetalleGenero().getIdDetalle()) {

					cmbGenero.setSelectedIndex(i);
					break;
				}
			}
		}

		if (integrante.isJefeHogar()) {
			rdbSiJefeHogar.setSelected(true);
		} else {
			rdbNoJefeHogar.setSelected(true);
		}

		if (integrante.getDetalleParentesco() != null) {
			cmbParentesco.setSelectedItem(integrante.getDetalleParentesco());
		}

		txtTelefono.setText(integrante.getTelefono() != null ? integrante.getTelefono() : "");

		txtCorreo.setText(integrante.getCorreoElectronico() != null ? integrante.getCorreoElectronico() : "");

		if (integrante.getDetalleNivelEducativo() != null) {
			cmbNivelEducativo.setSelectedItem(integrante.getDetalleNivelEducativo());
		}

		if (integrante.getDetalleSeguro() != null) {
			cmbSeguro.setSelectedItem(integrante.getDetalleSeguro());
		}

		txtSalud.setText(integrante.getSalud() != null ? integrante.getSalud() : "");

		txtMigracion.setText(integrante.getMigracion() != null ? integrante.getMigracion() : "");

		txtPais.setText(integrante.getPais() != null ? integrante.getPais() : "");
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
		txtSalud.addKeyListener(cambioTexto);
		txtMigracion.addKeyListener(cambioTexto);
		txtPais.addKeyListener(cambioTexto);

		ActionListener cambioCombo = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				hayCambios = true;
			}
		};

		cmbTipoIdentificacion.addActionListener(cambioCombo);
		cmbGenero.addActionListener(cambioCombo);
		cmbParentesco.addActionListener(cambioCombo);
		cmbNivelEducativo.addActionListener(cambioCombo);
		cmbSeguro.addActionListener(cambioCombo);
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
			integranteGuardar.setDetalleGenero((Detalle) cmbGenero.getSelectedItem());
			integranteGuardar.setJefeHogar(rdbSiJefeHogar.isSelected());
			integranteGuardar.setDetalleParentesco((Detalle) cmbParentesco.getSelectedItem());
			integranteGuardar.setTelefono(txtTelefono.getText().trim());
			integranteGuardar.setCorreoElectronico(txtCorreo.getText().trim());
			integranteGuardar.setDetalleNivelEducativo((Detalle) cmbNivelEducativo.getSelectedItem());
			integranteGuardar.setDetalleSeguro((Detalle) cmbSeguro.getSelectedItem());
			integranteGuardar.setSalud(txtSalud.getText().trim());
			integranteGuardar.setMigracion(txtMigracion.getText().trim());
			integranteGuardar.setPais(txtPais.getText().trim());

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

		if (cmbGenero.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar el género.");
		}

		if (cmbParentesco.getSelectedItem() == null) {
			throw new IllegalArgumentException("Debe seleccionar el parentesco.");
		}

		if (cmbNivelEducativo.getSelectedItem() == null) {
			throw new IllegalArgumentException("Debe seleccionar el nivel educativo.");
		}

		if (cmbSeguro.getSelectedItem() == null) {
			// Es opcional
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