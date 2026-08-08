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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import pastoralsocial.controller.CasoSocialController;
import pastoralsocial.controller.DetalleController;
import pastoralsocial.controller.MaestroController;
import pastoralsocial.dao.UsuarioDAO;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Detalle;
import pastoralsocial.modelo.EstadoCaso;
import pastoralsocial.modelo.Maestro;
import pastoralsocial.modelo.Usuario;

public class CasoSocialFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtNumeroFicha;
	private JTextField txtFechaInicio;
	private JFormattedTextField txtFechaPrevistaFin;

	private JTextField txtParroquia;
	private JTextField txtSectorFilial;
	private JTextField txtProvincia;
	private JTextField txtCanton;
	private JTextField txtDistrito;
	private JTextArea txtDireccion;
	private JTextArea txtObservaciones;
	private JComboBox<EstadoCaso> cmbEstado;
	private JComboBox<Usuario> cmbResponsable;
	private JComboBox<Detalle> cmbTipoVivienda;
	private JComboBox<Detalle> cmbTenencia;
	private JComboBox<Detalle> cmbCondicion;

	// Nombres de los catalogos (Maestro) para cada combo de vivienda.
	private static final String CATALOGO_TIPO_VIVIENDA = "Tipo de Vivienda";
	private static final String CATALOGO_TENENCIA_VIVIENDA = "Tenencia de Vivienda";
	private static final String CATALOGO_CONDICION_VIVIENDA = "Condición de Vivienda";

	private JButton btnGuardar;
	private JButton btnCancelar;

	private final CasoSocialController casoSocialController;

	private final UsuarioDAO usuarioDAO;
	private final DetalleController detalleController;
	private final MaestroController maestroController;

	private CasoSocial casoSocial;
	private boolean guardado;
	private boolean hayCambios;

	private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

	// CREAR CASO SOCIAL
	public CasoSocialFormDialog(Frame parent) {

		super(parent, "Nuevo caso social", true);

		this.casoSocial = null;
		this.guardado = false;

		this.casoSocialController = new CasoSocialController();

		this.usuarioDAO = new UsuarioDAO();
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializarInterfaz();
		cargarCombos();
		prepararNuevoCaso();

		configurarDeteccionCambios();
		hayCambios = false;
	}

	// EDITAR CASO SOCIAL
	public CasoSocialFormDialog(Frame parent, CasoSocial casoSocial) {

		super(parent, "Editar caso social", true);

		this.casoSocial = casoSocial;
		this.guardado = false;

		this.casoSocialController = new CasoSocialController();

		this.usuarioDAO = new UsuarioDAO();
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializarInterfaz();
		cargarCombos();
		cargarDatosCaso();

		configurarDeteccionCambios();
		hayCambios = false;
	}

	public CasoSocialFormDialog(Frame parent, CasoSocial casoSocial, Usuario usuarioActual) {

		this(parent, casoSocial);

		if (usuarioActual != null) {

			cmbResponsable.setEnabled(false);
		}
	}

	// EDITAR CASOS SOLO RESPONSABLES
	public CasoSocialFormDialog(Frame parent, CasoSocial casoSocial, boolean permitirCambiarResponsable) {

		this(parent, casoSocial);

		cmbResponsable.setEnabled(permitirCambiarResponsable);
	}

	private void inicializarInterfaz() {

		setTitle("Gestión de Casos Sociales");
		setBounds(100, 100, 790, 700);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		getContentPane().setBackground(new Color(245, 247, 250));
		((JComponent) getContentPane()).setBorder(new EmptyBorder(0, 0, 0, 0));
		getContentPane().setLayout(null);

		// TITULO
		JLabel lblTitulo = new JLabel(casoSocial == null ? "Nuevo caso social" : "Editar caso social");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(35, 20, 400, 35);
		getContentPane().add(lblTitulo);

		JLabel lblDescripcion = new JLabel("Complete la información general del caso social.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(35, 55, 500, 25);
		getContentPane().add(lblDescripcion);

		// INFORMACION DEL CASO

		JLabel lblSeccionCaso = new JLabel("Información del caso");
		lblSeccionCaso.setForeground(new Color(0, 45, 102));
		lblSeccionCaso.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblSeccionCaso.setBounds(35, 90, 250, 25);
		getContentPane().add(lblSeccionCaso);

		// NUMERO FICHA
		JLabel lblNumeroFicha = new JLabel("Número de ficha *");
		lblNumeroFicha.setForeground(new Color(0, 45, 102));
		lblNumeroFicha.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblNumeroFicha.setBounds(35, 125, 200, 20);
		getContentPane().add(lblNumeroFicha);

		txtNumeroFicha = new JTextField();
		txtNumeroFicha.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtNumeroFicha.setBounds(35, 148, 220, 32);
		txtNumeroFicha.setEditable(false);
		txtNumeroFicha.setBackground(new Color(238, 241, 245));
		txtNumeroFicha.setForeground(new Color(90, 90, 90));
		txtNumeroFicha.setText("Generado automáticamente");
		getContentPane().add(txtNumeroFicha);

		// FECHA INICIO
		JLabel lblFechaInicio = new JLabel("Fecha de inicio *");
		lblFechaInicio.setForeground(new Color(0, 45, 102));
		lblFechaInicio.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFechaInicio.setBounds(280, 125, 200, 20);
		getContentPane().add(lblFechaInicio);

		txtFechaInicio = new JTextField();
		txtFechaInicio.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtFechaInicio.setBounds(280, 148, 220, 32);
		txtFechaInicio.setEditable(false);
		txtFechaInicio.setBackground(new Color(238, 238, 238));
		txtFechaInicio.setForeground(new Color(0, 45, 102));
		getContentPane().add(txtFechaInicio);

		// FECHA FIN
		JLabel lblFechaFin = new JLabel("Fecha prevista de fin");
		lblFechaFin.setForeground(new Color(0, 45, 102));
		lblFechaFin.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFechaFin.setBounds(525, 125, 200, 20);
		getContentPane().add(lblFechaFin);

		try {

			MaskFormatter mascaraFecha = new MaskFormatter("##/##/####");
			mascaraFecha.setPlaceholderCharacter('_');
			txtFechaPrevistaFin = new JFormattedTextField(mascaraFecha);
			txtFechaPrevistaFin.setForeground(new Color(0, 45, 102));
			txtFechaPrevistaFin.setFont(new Font("SansSerif", Font.PLAIN, 12));
			txtFechaPrevistaFin.setBounds(525, 148, 220, 32);
			getContentPane().add(txtFechaPrevistaFin);

		} catch (ParseException e) {

			throw new RuntimeException("No fue posible configurar el campo de fecha.", e);
		}

		// ESTADO
		JLabel lblEstado = new JLabel("Estado *");
		lblEstado.setForeground(new Color(0, 45, 102));
		lblEstado.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblEstado.setBounds(35, 195, 200, 20);
		getContentPane().add(lblEstado);

		cmbEstado = new JComboBox<EstadoCaso>();
		cmbEstado.setForeground(new Color(0, 45, 102));
		cmbEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbEstado.setBounds(35, 218, 220, 32);
		getContentPane().add(cmbEstado);

		// RESPONSABLE
		JLabel lblResponsable = new JLabel("Responsable *");
		lblResponsable.setForeground(new Color(0, 45, 102));
		lblResponsable.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblResponsable.setBounds(280, 195, 200, 20);
		getContentPane().add(lblResponsable);

		cmbResponsable = new JComboBox<Usuario>();
		cmbResponsable.setForeground(new Color(0, 45, 102));
		cmbResponsable.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbResponsable.setBounds(280, 218, 465, 32);
		getContentPane().add(cmbResponsable);

		// UBICACION
		JLabel lblUbicacion = new JLabel("Ubicación");
		lblUbicacion.setForeground(new Color(0, 45, 102));
		lblUbicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblUbicacion.setBounds(35, 270, 250, 25);
		getContentPane().add(lblUbicacion);

		// PARROQUIA
		JLabel lblParroquia = new JLabel("Parroquia");
		lblParroquia.setForeground(new Color(0, 45, 102));
		lblParroquia.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblParroquia.setBounds(35, 305, 200, 20);
		getContentPane().add(lblParroquia);

		txtParroquia = new JTextField();
		txtParroquia.setForeground(new Color(0, 45, 102));
		txtParroquia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtParroquia.setBounds(35, 328, 220, 32);
		getContentPane().add(txtParroquia);

		// SECTOR FILIAL
		JLabel lblSector = new JLabel("Sector / Filial");
		lblSector.setForeground(new Color(0, 45, 102));
		lblSector.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblSector.setBounds(280, 305, 200, 20);
		getContentPane().add(lblSector);

		txtSectorFilial = new JTextField();
		txtSectorFilial.setForeground(new Color(0, 45, 102));
		txtSectorFilial.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtSectorFilial.setBounds(280, 328, 220, 32);
		getContentPane().add(txtSectorFilial);

		// PROVINCIA
		JLabel lblProvincia = new JLabel("Provincia");
		lblProvincia.setForeground(new Color(0, 45, 102));
		lblProvincia.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblProvincia.setBounds(525, 305, 200, 20);
		getContentPane().add(lblProvincia);

		txtProvincia = new JTextField();
		txtProvincia.setForeground(new Color(0, 45, 102));
		txtProvincia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtProvincia.setBounds(525, 328, 220, 32);
		getContentPane().add(txtProvincia);

		// CANTON
		JLabel lblCanton = new JLabel("Cantón");
		lblCanton.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblCanton.setForeground(new Color(0, 45, 102));
		lblCanton.setBounds(35, 370, 200, 20);
		getContentPane().add(lblCanton);

		txtCanton = new JTextField();
		txtCanton.setForeground(new Color(0, 45, 102));
		txtCanton.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtCanton.setBounds(35, 393, 220, 32);
		getContentPane().add(txtCanton);

		// DISTRITO
		JLabel lblDistrito = new JLabel("Distrito");
		lblDistrito.setForeground(new Color(0, 45, 102));
		lblDistrito.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDistrito.setBounds(280, 370, 200, 20);
		getContentPane().add(lblDistrito);

		txtDistrito = new JTextField();
		txtDistrito.setForeground(new Color(0, 45, 102));
		txtDistrito.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtDistrito.setBounds(280, 393, 220, 32);
		getContentPane().add(txtDistrito);

		// DIRECCION
		JLabel lblDireccion = new JLabel("Dirección");
		lblDireccion.setForeground(new Color(0, 45, 102));
		lblDireccion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDireccion.setBounds(525, 370, 200, 20);
		getContentPane().add(lblDireccion);

		txtDireccion = new JTextArea();
		txtDireccion.setForeground(new Color(0, 45, 102));
		txtDireccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtDireccion.setLineWrap(true);
		txtDireccion.setWrapStyleWord(true);

		JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
		scrollDireccion.setBounds(525, 393, 220, 60);
		getContentPane().add(scrollDireccion);

		// VIVIENDA
		JLabel lblVivienda = new JLabel("Información de vivienda");
		lblVivienda.setForeground(new Color(0, 45, 102));
		lblVivienda.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblVivienda.setBounds(35, 465, 300, 25);
		getContentPane().add(lblVivienda);

		// TIPO VIVIENDA
		JLabel lblTipoVivienda = new JLabel("Tipo de vivienda *");
		lblTipoVivienda.setForeground(new Color(0, 45, 102));
		lblTipoVivienda.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTipoVivienda.setBounds(35, 500, 200, 20);
		getContentPane().add(lblTipoVivienda);

		cmbTipoVivienda = new JComboBox<Detalle>();
		cmbTipoVivienda.setForeground(new Color(0, 45, 102));
		cmbTipoVivienda.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTipoVivienda.setBounds(35, 523, 220, 32);
		getContentPane().add(cmbTipoVivienda);

		// TENENCIA
		JLabel lblTenencia = new JLabel("Tenencia *");
		lblTenencia.setForeground(new Color(0, 45, 102));
		lblTenencia.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTenencia.setBounds(280, 500, 200, 20);
		getContentPane().add(lblTenencia);

		cmbTenencia = new JComboBox<Detalle>();
		cmbTenencia.setForeground(new Color(0, 45, 102));
		cmbTenencia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTenencia.setBounds(280, 523, 220, 32);
		getContentPane().add(cmbTenencia);

		// CONDICION
		JLabel lblCondicion = new JLabel("Condición *");
		lblCondicion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblCondicion.setForeground(new Color(0, 45, 102));
		lblCondicion.setBounds(525, 500, 200, 20);
		getContentPane().add(lblCondicion);

		cmbCondicion = new JComboBox<Detalle>();
		cmbCondicion.setForeground(new Color(0, 45, 102));
		cmbCondicion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbCondicion.setBounds(525, 523, 220, 32);
		getContentPane().add(cmbCondicion);

		// OBSERVACIONES
		JLabel lblObservaciones = new JLabel("Observaciones");
		lblObservaciones.setForeground(new Color(0, 45, 102));
		lblObservaciones.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblObservaciones.setBounds(35, 570, 200, 20);
		getContentPane().add(lblObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setForeground(new Color(0, 45, 102));
		txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);

		JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
		scrollObservaciones.setBounds(35, 593, 420, 55);
		getContentPane().add(scrollObservaciones);

		// BOTON CANCELAR
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setBackground(Color.WHITE);
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setBounds(475, 608, 115, 40);
		getContentPane().add(btnCancelar);

		// BOTON GUARDAR
		btnGuardar = new JButton(casoSocial == null ? "Guardar caso" : "Guardar cambios");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setBackground(Color.WHITE);
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBounds(600, 608, 145, 40);
		getContentPane().add(btnGuardar);

		btnGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				guardar();
			}
		});

		btnCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				intentarCerrar();
			}
		});

		addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {

				intentarCerrar();
			}
		});
	}

	private void configurarDeteccionCambios() {

		java.awt.event.KeyAdapter cambioTexto = new java.awt.event.KeyAdapter() {

			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				hayCambios = true;
			}
		};

		txtFechaPrevistaFin.addKeyListener(cambioTexto);
		txtParroquia.addKeyListener(cambioTexto);
		txtSectorFilial.addKeyListener(cambioTexto);
		txtProvincia.addKeyListener(cambioTexto);
		txtCanton.addKeyListener(cambioTexto);
		txtDistrito.addKeyListener(cambioTexto);
		txtDireccion.addKeyListener(cambioTexto);
		txtObservaciones.addKeyListener(cambioTexto);

		ActionListener cambioCombo = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hayCambios = true;
			}
		};

		cmbEstado.addActionListener(cambioCombo);
		cmbResponsable.addActionListener(cambioCombo);
		cmbTipoVivienda.addActionListener(cambioCombo);
		cmbTenencia.addActionListener(cambioCombo);
		cmbCondicion.addActionListener(cambioCombo);
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

	private void cargarCombos() {

		try {

			// ESTADOS
			cmbEstado.removeAllItems();

			EstadoCaso[] estados = EstadoCaso.values();

			for (int i = 0; i < estados.length; i++) {

				cmbEstado.addItem(estados[i]);
			}

			// USUARIOS ACTIVOS
			cmbResponsable.removeAllItems();

			List<Usuario> usuarios = usuarioDAO.getAll();

			for (Usuario usuario : usuarios) {

				if (usuario.getEstado()) {

					cmbResponsable.addItem(usuario);
				}
			}

			// TIPOS DE VIVIENDA (cargados desde Detalle, filtrando por su Maestro)
			cmbTipoVivienda.removeAllItems();

			for (Detalle detalle : obtenerDetallesDelCatalogo(CATALOGO_TIPO_VIVIENDA)) {

				cmbTipoVivienda.addItem(detalle);
			}

			// TENENCIAS
			cmbTenencia.removeAllItems();

			for (Detalle detalle : obtenerDetallesDelCatalogo(CATALOGO_TENENCIA_VIVIENDA)) {

				cmbTenencia.addItem(detalle);
			}

			// CONDICIONES
			cmbCondicion.removeAllItems();

			for (Detalle detalle : obtenerDetallesDelCatalogo(CATALOGO_CONDICION_VIVIENDA)) {

				cmbCondicion.addItem(detalle);
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los datos del formulario.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Busca el catalogo (Maestro) por su nombre y devuelve unicamente los Detalle
	 * que le pertenecen. Es el mismo patron usado para el combo de Tipo de
	 * asistencia.
	 */
	private List<Detalle> obtenerDetallesDelCatalogo(String nombreCatalogo) {

		Maestro catalogo = maestroController.buscarPorNombre(nombreCatalogo);

		if (catalogo == null) {
			return new java.util.ArrayList<Detalle>();
		}

		return detalleController.listarPorMaestro(catalogo.getIdMaestro());
	}

	private void prepararNuevoCaso() {

		txtFechaInicio.setText(formatoFecha.format(new Date()));

		cmbEstado.setSelectedItem(EstadoCaso.ACTIVO);

		cmbEstado.setEnabled(false);

		cmbResponsable.setSelectedIndex(-1);
		cmbTipoVivienda.setSelectedIndex(-1);
		cmbTenencia.setSelectedIndex(-1);
		cmbCondicion.setSelectedIndex(-1);
	}

	private void cargarDatosCaso() {

		if (casoSocial == null) {
			return;
		}

		txtNumeroFicha.setText(casoSocial.getNumeroFicha());

		if (casoSocial.getFechaInicio() != null) {

			txtFechaInicio.setText(formatoFecha.format(casoSocial.getFechaInicio()));
		}

		if (casoSocial.getFechaPrevistaFin() != null) {

			txtFechaPrevistaFin.setText(formatoFecha.format(casoSocial.getFechaPrevistaFin()));
		}

		cmbEstado.setSelectedItem(casoSocial.getEstado());
		txtParroquia.setText(valorSeguro(casoSocial.getParroquia()));
		txtSectorFilial.setText(valorSeguro(casoSocial.getSectorFilial()));
		txtProvincia.setText(valorSeguro(casoSocial.getProvincia()));
		txtCanton.setText(valorSeguro(casoSocial.getCanton()));
		txtDistrito.setText(valorSeguro(casoSocial.getDistrito()));
		txtDireccion.setText(valorSeguro(casoSocial.getDireccion()));
		txtObservaciones.setText(valorSeguro(casoSocial.getObservaciones()));
		seleccionarUsuario(casoSocial.getUsuario());
		seleccionarTipoVivienda(casoSocial.getDetalleTipoVivienda());
		seleccionarTenencia(casoSocial.getDetalleTenenciaVivienda());
		seleccionarCondicion(casoSocial.getDetalleCondicionVivienda());
	}

	private void guardar() {

		try {

			validarFormulario();

			// CONVERTIR Y VALIDAR FECHAS
			Date fechaInicio = convertirFechaObligatoria(txtFechaInicio.getText());

			Date fechaPrevistaFin = convertirFechaOpcionalEnmascarada(txtFechaPrevistaFin.getText());

			if (fechaPrevistaFin != null && fechaPrevistaFin.before(fechaInicio)) {

				throw new IllegalArgumentException(
						"La fecha prevista de fin no puede ser anterior a la fecha de inicio.");
			}

			EstadoCaso estado = (EstadoCaso) cmbEstado.getSelectedItem();
			Usuario responsable = (Usuario) cmbResponsable.getSelectedItem();

			validarResponsable(responsable);

			Detalle tipoVivienda = (Detalle) cmbTipoVivienda.getSelectedItem();
			Detalle tenencia = (Detalle) cmbTenencia.getSelectedItem();
			Detalle condicion = (Detalle) cmbCondicion.getSelectedItem();

			// NUEVO CASO
			if (casoSocial == null) {

				casoSocial = new CasoSocial();

				casoSocial.setFechaInicio(new Date());
				casoSocial.setFechaPrevistaFin(fechaPrevistaFin);
				casoSocial.setEstado(estado);
				casoSocial.setParroquia(txtParroquia.getText().trim());
				casoSocial.setSectorFilial(txtSectorFilial.getText().trim());
				casoSocial.setProvincia(txtProvincia.getText().trim());
				casoSocial.setCanton(txtCanton.getText().trim());
				casoSocial.setDistrito(txtDistrito.getText().trim());
				casoSocial.setDireccion(txtDireccion.getText().trim());
				casoSocial.setObservaciones(txtObservaciones.getText().trim());
				casoSocial.setUsuario(responsable);
				casoSocial.setDetalleTipoVivienda(tipoVivienda);
				casoSocial.setDetalleTenenciaVivienda(tenencia);
				casoSocial.setDetalleCondicionVivienda(condicion);

				casoSocialController.registrar(casoSocial);

				guardado = true;

				JOptionPane.showMessageDialog(this, "El caso social fue registrado correctamente.", "Caso social",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();

			} else {

				// EDITAR CASO EXISTENTE
				casoSocial.setFechaPrevistaFin(fechaPrevistaFin);
				casoSocial.setEstado(estado);
				casoSocial.setParroquia(txtParroquia.getText().trim());
				casoSocial.setSectorFilial(txtSectorFilial.getText().trim());
				casoSocial.setProvincia(txtProvincia.getText().trim());
				casoSocial.setCanton(txtCanton.getText().trim());
				casoSocial.setDistrito(txtDistrito.getText().trim());
				casoSocial.setDireccion(txtDireccion.getText().trim());
				casoSocial.setObservaciones(txtObservaciones.getText().trim());
				casoSocial.setUsuario(responsable);
				casoSocial.setDetalleTipoVivienda(tipoVivienda);
				casoSocial.setDetalleTenenciaVivienda(tenencia);
				casoSocial.setDetalleCondicionVivienda(condicion);

				casoSocialController.actualizar(casoSocial);

				guardado = true;

				JOptionPane.showMessageDialog(this, "El caso social fue actualizado correctamente.", "Caso social",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();
			}

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar el caso social.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void validarFormulario() {

		if (txtNumeroFicha.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar el número de ficha.");
		}

		if (txtFechaInicio.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("Debe ingresar la fecha de inicio.");
		}

		if (cmbEstado.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar el estado del caso.");
		}

		if (cmbResponsable.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar un responsable.");
		}

		if (cmbTipoVivienda.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar el tipo de vivienda.");
		}

		if (cmbTenencia.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar la tenencia de la vivienda.");
		}

		if (cmbCondicion.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar la condición de la vivienda.");
		}
	}

	private void validarResponsable(Usuario responsable) {

		if (responsable == null) {

			throw new IllegalArgumentException("Debe seleccionar un responsable.");
		}

		if (responsable.getEstado()) {

			return;
		}

		if (casoSocial != null && casoSocial.getUsuario() != null
				&& casoSocial.getUsuario().getIdUsuario() == responsable.getIdUsuario()) {

			return;
		}

		throw new IllegalArgumentException("No se puede asignar un usuario inactivo como responsable.");
	}

	private Date convertirFechaObligatoria(String texto) {

		try {

			formatoFecha.setLenient(false);

			return formatoFecha.parse(texto.trim());

		} catch (ParseException e) {

			throw new IllegalArgumentException("La fecha de inicio debe tener el formato dd/MM/yyyy.");
		}
	}

	private Date convertirFechaOpcionalEnmascarada(String texto) {

		if (texto == null) {

			return null;
		}

		String fecha = texto.trim();

		// Permite dejar la fecha completamente vacía
		String soloContenido = fecha.replace("/", "").replace("_", "").trim();

		if (soloContenido.isEmpty()) {

			return null;
		}

		// Detecta una fecha incompleta
		if (fecha.contains("_")) {

			throw new IllegalArgumentException("Debe completar la fecha prevista de fin en formato dd/MM/aaaa.");
		}

		try {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			formato.setLenient(false);

			return formato.parse(fecha);

		} catch (ParseException e) {

			throw new IllegalArgumentException("La fecha prevista de fin no es válida.");
		}
	}

	private String valorSeguro(String valor) {

		return valor == null ? "" : valor;
	}

	private void seleccionarUsuario(Usuario usuario) {

		if (usuario == null) {
			return;
		}

		for (int i = 0; i < cmbResponsable.getItemCount(); i++) {

			Usuario item = cmbResponsable.getItemAt(i);

			if (item.getIdUsuario() == usuario.getIdUsuario()) {

				cmbResponsable.setSelectedIndex(i);

				return;
			}
		}

		cmbResponsable.addItem(usuario);
		cmbResponsable.setSelectedItem(usuario);
	}

	private void seleccionarTipoVivienda(Detalle tipoActual) {

		if (tipoActual == null) {
			return;
		}

		for (int i = 0; i < cmbTipoVivienda.getItemCount(); i++) {

			Detalle item = cmbTipoVivienda.getItemAt(i);

			if (item.getIdDetalle() == tipoActual.getIdDetalle()) {

				cmbTipoVivienda.setSelectedIndex(i);

				return;
			}
		}
	}

	private void seleccionarTenencia(Detalle tenenciaActual) {

		if (tenenciaActual == null) {
			return;
		}

		for (int i = 0; i < cmbTenencia.getItemCount(); i++) {

			Detalle item = cmbTenencia.getItemAt(i);

			if (item.getIdDetalle() == tenenciaActual.getIdDetalle()) {

				cmbTenencia.setSelectedIndex(i);

				return;
			}
		}
	}

	private void seleccionarCondicion(Detalle condicionActual) {

		if (condicionActual == null) {
			return;
		}

		for (int i = 0; i < cmbCondicion.getItemCount(); i++) {

			Detalle item = cmbCondicion.getItemAt(i);

			if (item.getIdDetalle() == condicionActual.getIdDetalle()) {

				cmbCondicion.setSelectedIndex(i);

				return;
			}
		}
	}

	public boolean isGuardado() {
		return guardado;
	}
}