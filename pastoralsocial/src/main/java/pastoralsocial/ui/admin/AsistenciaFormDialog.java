package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import pastoralsocial.controller.AsistenciaController;
import pastoralsocial.controller.DetalleController;
import pastoralsocial.controller.MaestroController;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Detalle;
import pastoralsocial.modelo.Maestro;

public class AsistenciaFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String NOMBRE_CATALOGO_TIPO_ASISTENCIA = "Tipo de Asistencia";

	private JFormattedTextField txtFecha;
	private JComboBox<Detalle> cmbTipoAsistencia;
	private JTextField txtModalidad;
	private JTextField txtFrecuencia;
	private JTextField txtDuracion;
	private JTextField txtValor;
	private JTextArea txtObservaciones;

	private JButton btnGuardar;

	private final AsistenciaController asistenciaController;
	private final DetalleController detalleController;
	private final MaestroController maestroController;

	private final CasoSocial casoSocial;
	private Asistencia asistencia;

	private boolean modoEdicion;
	private boolean guardado;
	private boolean hayCambios;

	private JFormattedTextField txtProlongacion1;
	private JFormattedTextField txtProlongacion2;
	private JFormattedTextField txtProlongacion3;
	private JFormattedTextField txtProlongacion4;
	private JFormattedTextField txtProlongacion5;

	// CREAR ASISTENCIA NUEVA
	public AsistenciaFormDialog(Frame parent, CasoSocial casoSocial) {

		super(parent, "Nueva asistencia", true);

		this.casoSocial = casoSocial;
		this.asistencia = null;
		this.modoEdicion = false;
		this.asistenciaController = new AsistenciaController();
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializarInterfaz();

		configurarDeteccionCambios();

		setLocationRelativeTo(parent);
	}

	// EDITAR ASISTENCIA EXISTENTE
	public AsistenciaFormDialog(Frame parent, Asistencia asistencia) {

		super(parent, "Editar asistencia", true);

		this.casoSocial = asistencia.getCasoSocial();
		this.asistencia = asistencia;
		this.modoEdicion = true;
		this.asistenciaController = new AsistenciaController();
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializarInterfaz();

		cargarDatos();

		configurarDeteccionCambios();

		setLocationRelativeTo(parent);
	}

	private void inicializarInterfaz() {

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(245, 247, 250));
		contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		// TITULO
		JLabel lblTitulo = new JLabel(modoEdicion ? "Editar asistencia" : "Nueva asistencia");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(25, 15, 430, 40);
		contentPanel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				modoEdicion ? "Actualice la información de la asistencia del caso " + casoSocial.getNumeroFicha() + "."
						: "Registre una nueva asistencia para el caso " + casoSocial.getNumeroFicha() + ".");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(25, 52, 600, 25);
		contentPanel.add(lblDescripcion);

		// FECHA
		JLabel lblFecha = crearEtiqueta("Fecha");
		lblFecha.setBounds(25, 100, 210, 20);
		contentPanel.add(lblFecha);

		txtFecha = crearCampoFecha();
		txtFecha.setBounds(25, 123, 210, 35);
		contentPanel.add(txtFecha);

		// TIPO DE ASISTENCIA
		JLabel lblTipoAsistencia = crearEtiqueta("Tipo de asistencia");
		lblTipoAsistencia.setBounds(265, 100, 250, 20);
		contentPanel.add(lblTipoAsistencia);

		cmbTipoAsistencia = new JComboBox<Detalle>();
		cmbTipoAsistencia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTipoAsistencia.setBounds(265, 123, 250, 35);
		contentPanel.add(cmbTipoAsistencia);

		cargarComboTipoAsistencia();

		// MODALIDAD
		JLabel lblModalidad = crearEtiqueta("Modalidad");
		lblModalidad.setBounds(25, 190, 210, 20);
		contentPanel.add(lblModalidad);

		txtModalidad = crearCampoTexto();
		txtModalidad.setBounds(25, 213, 210, 35);
		contentPanel.add(txtModalidad);

		// FRECUENCIA
		JLabel lblFrecuencia = crearEtiqueta("Frecuencia");
		lblFrecuencia.setBounds(265, 190, 250, 20);
		contentPanel.add(lblFrecuencia);

		txtFrecuencia = crearCampoTexto();
		txtFrecuencia.setBounds(265, 213, 250, 35);
		contentPanel.add(txtFrecuencia);

		// DURACION
		JLabel lblDuracion = crearEtiqueta("Duración");
		lblDuracion.setBounds(25, 280, 210, 20);
		contentPanel.add(lblDuracion);

		txtDuracion = crearCampoTexto();
		txtDuracion.setBounds(25, 303, 210, 35);
		contentPanel.add(txtDuracion);

		// VALOR
		JLabel lblValor = crearEtiqueta("Valor");
		lblValor.setBounds(265, 280, 250, 20);
		contentPanel.add(lblValor);

		txtValor = crearCampoTexto();
		txtValor.setBounds(265, 303, 250, 35);
		contentPanel.add(txtValor);

		// PROLONGACIONES
		JLabel lblProlongaciones = new JLabel("Prolongaciones de la ayuda");
		lblProlongaciones.setForeground(new Color(0, 45, 102));
		lblProlongaciones.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblProlongaciones.setBounds(25, 365, 300, 25);
		contentPanel.add(lblProlongaciones);

		// PROLONGACION 1
		JLabel lblProlongacion1 = crearEtiqueta("Prolongación 1");
		lblProlongacion1.setBounds(25, 398, 150, 20);
		contentPanel.add(lblProlongacion1);

		txtProlongacion1 = crearCampoFecha();
		txtProlongacion1.setBounds(25, 421, 150, 35);
		contentPanel.add(txtProlongacion1);

		// PROLONGACION 2
		JLabel lblProlongacion2 = crearEtiqueta("Prolongación 2");
		lblProlongacion2.setBounds(195, 398, 150, 20);
		contentPanel.add(lblProlongacion2);

		txtProlongacion2 = crearCampoFecha();
		txtProlongacion2.setBounds(195, 421, 150, 35);
		contentPanel.add(txtProlongacion2);

		// PROLONGACION 3
		JLabel lblProlongacion3 = crearEtiqueta("Prolongación 3");
		lblProlongacion3.setBounds(365, 398, 150, 20);
		contentPanel.add(lblProlongacion3);

		txtProlongacion3 = crearCampoFecha();
		txtProlongacion3.setBounds(365, 421, 150, 35);
		contentPanel.add(txtProlongacion3);

		// PROLONGACION 4
		JLabel lblProlongacion4 = crearEtiqueta("Prolongación 4");
		lblProlongacion4.setBounds(25, 470, 150, 20);
		contentPanel.add(lblProlongacion4);

		txtProlongacion4 = crearCampoFecha();
		txtProlongacion4.setBounds(25, 493, 150, 35);
		contentPanel.add(txtProlongacion4);

		// PROLONGACION 5
		JLabel lblProlongacion5 = crearEtiqueta("Prolongación 5");
		lblProlongacion5.setBounds(195, 470, 150, 20);
		contentPanel.add(lblProlongacion5);

		txtProlongacion5 = crearCampoFecha();
		txtProlongacion5.setBounds(195, 493, 150, 35);
		contentPanel.add(txtProlongacion5);

		// OBSERVACIONES
		JLabel lblObservaciones = crearEtiqueta("Observaciones");
		lblObservaciones.setBounds(25, 530, 250, 20);
		contentPanel.add(lblObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);

		JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
		scrollObservaciones.setBounds(25, 553, 490, 65);
		contentPanel.add(scrollObservaciones);

		// CANCELAR
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setBounds(220, 630, 120, 40);
		contentPanel.add(btnCancelar);

		// GUARDAR
		btnGuardar = new JButton(modoEdicion ? "Guardar cambios" : "Guardar asistencia");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setBounds(355, 630, 175, 40);
		contentPanel.add(btnGuardar);

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

		addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {

				intentarCerrar();
			}
		});

		setSize(560, 740);
		setResizable(false);
	}

	private void cargarComboTipoAsistencia() {

		cmbTipoAsistencia.removeAllItems();

		Maestro catalogoTipoAsistencia = maestroController.buscarPorNombre(NOMBRE_CATALOGO_TIPO_ASISTENCIA);

		if (catalogoTipoAsistencia == null) {

			JOptionPane.showMessageDialog(this,
					"No se encontró el catálogo 'Tipo de Asistencia'. Verifique que se haya sembrado en Main.java.",
					"Catálogo no encontrado", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<Detalle> detalles = detalleController.listarPorMaestro(catalogoTipoAsistencia.getIdMaestro());

		for (Detalle detalle : detalles) {

			cmbTipoAsistencia.addItem(detalle);
		}
	}

	private void cargarDatos() {

		if (asistencia == null) {
			return;
		}

		if (asistencia.getFecha() != null) {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			txtFecha.setText(formato.format(asistencia.getFecha()));
		}

		if (asistencia.getDetalleTipoAsistencia() != null) {

			cmbTipoAsistencia.setSelectedItem(asistencia.getDetalleTipoAsistencia());
		}

		txtModalidad.setText(asistencia.getModalidad() != null ? asistencia.getModalidad() : "");
		txtFrecuencia.setText(asistencia.getFrecuencia() != null ? asistencia.getFrecuencia() : "");
		txtDuracion.setText(asistencia.getDuracion() != null ? asistencia.getDuracion() : "");
		txtValor.setText(asistencia.getValor() != null ? asistencia.getValor().toString() : "");
		txtObservaciones.setText(asistencia.getObservaciones() != null ? asistencia.getObservaciones() : "");

		txtProlongacion1.setText(formatearFechaCampo(asistencia.getFechaProlongacion1()));
		txtProlongacion2.setText(formatearFechaCampo(asistencia.getFechaProlongacion2()));
		txtProlongacion3.setText(formatearFechaCampo(asistencia.getFechaProlongacion3()));
		txtProlongacion4.setText(formatearFechaCampo(asistencia.getFechaProlongacion4()));
		txtProlongacion5.setText(formatearFechaCampo(asistencia.getFechaProlongacion5()));
	}

	private String formatearFechaCampo(Date fecha) {

		if (fecha == null) {
			return "";
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		return formato.format(fecha);
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

	private void configurarDeteccionCambios() {

		java.awt.event.KeyAdapter cambioTexto = new java.awt.event.KeyAdapter() {

			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {

				hayCambios = true;
			}
		};

		// FECHA
		txtFecha.addKeyListener(cambioTexto);

		// DATOS DE LA ASISTENCIA
		txtModalidad.addKeyListener(cambioTexto);
		txtFrecuencia.addKeyListener(cambioTexto);
		txtDuracion.addKeyListener(cambioTexto);
		txtValor.addKeyListener(cambioTexto);
		txtObservaciones.addKeyListener(cambioTexto);

		// PROLONGACIONES
		txtProlongacion1.addKeyListener(cambioTexto);
		txtProlongacion2.addKeyListener(cambioTexto);
		txtProlongacion3.addKeyListener(cambioTexto);
		txtProlongacion4.addKeyListener(cambioTexto);
		txtProlongacion5.addKeyListener(cambioTexto);

		// TIPO DE ASISTENCIA
		cmbTipoAsistencia.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				hayCambios = true;
			}
		});
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

			String mensaje = modoEdicion ? "¿Desea guardar los cambios de esta asistencia?"
					: "¿Desea registrar esta asistencia para el caso " + casoSocial.getNumeroFicha() + "?";

			int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (opcion != JOptionPane.YES_OPTION) {

				return;
			}

			Asistencia asistenciaGuardar = modoEdicion ? asistencia : new Asistencia();

			asistenciaGuardar.setCasoSocial(casoSocial);
			asistenciaGuardar.setFecha(obtenerFecha());
			asistenciaGuardar.setDetalleTipoAsistencia((Detalle) cmbTipoAsistencia.getSelectedItem());
			asistenciaGuardar.setModalidad(txtModalidad.getText().trim());
			asistenciaGuardar.setFrecuencia(txtFrecuencia.getText().trim());
			asistenciaGuardar.setDuracion(txtDuracion.getText().trim());
			asistenciaGuardar.setValor(obtenerValor());
			asistenciaGuardar.setObservaciones(txtObservaciones.getText().trim());

			asistenciaGuardar.setFechaProlongacion1(obtenerFechaOpcional(txtProlongacion1));
			asistenciaGuardar.setFechaProlongacion2(obtenerFechaOpcional(txtProlongacion2));
			asistenciaGuardar.setFechaProlongacion3(obtenerFechaOpcional(txtProlongacion3));
			asistenciaGuardar.setFechaProlongacion4(obtenerFechaOpcional(txtProlongacion4));
			asistenciaGuardar.setFechaProlongacion5(obtenerFechaOpcional(txtProlongacion5));

			if (modoEdicion) {

				asistenciaController.actualizar(asistenciaGuardar);

			} else {

				asistenciaController.registrar(asistenciaGuardar);
			}

			guardado = true;

			JOptionPane.showMessageDialog(this,
					modoEdicion ? "La asistencia se actualizó correctamente."
							: "La asistencia se registró correctamente.",
					"Asistencias", JOptionPane.INFORMATION_MESSAGE);

			dispose();

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar la asistencia.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Date obtenerFechaOpcional(JFormattedTextField campo) {

		String texto = campo.getText();

		if (texto == null || texto.replace("/", "").replace("_", "").trim().isEmpty()) {

			return null;
		}

		if (texto.contains("_")) {

			throw new IllegalArgumentException("Complete correctamente las fechas de prolongación.");
		}

		try {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			formato.setLenient(false);

			return formato.parse(texto);

		} catch (ParseException e) {

			throw new IllegalArgumentException("Las fechas de prolongación deben tener el formato dd/mm/aaaa.");
		}
	}

	private void validarFormulario() {

		Date fechaAsistencia = obtenerFecha();

		Date fechaProlongacion1 = obtenerFechaOpcional(txtProlongacion1);
		Date fechaProlongacion2 = obtenerFechaOpcional(txtProlongacion2);
		Date fechaProlongacion3 = obtenerFechaOpcional(txtProlongacion3);
		Date fechaProlongacion4 = obtenerFechaOpcional(txtProlongacion4);
		Date fechaProlongacion5 = obtenerFechaOpcional(txtProlongacion5);

		if (fechaProlongacion1 != null && !fechaProlongacion1.after(fechaAsistencia)) {
			throw new IllegalArgumentException("La prolongación 1 debe ser posterior a la fecha de la asistencia.");
		}

		if (fechaProlongacion2 != null) {

			if (fechaProlongacion1 == null) {
				throw new IllegalArgumentException("Debe registrar primero la prolongación 1.");
			}

			if (!fechaProlongacion2.after(fechaProlongacion1)) {
				throw new IllegalArgumentException("La prolongación 2 debe ser posterior a la prolongación 1.");
			}
		}

		if (fechaProlongacion3 != null) {

			if (fechaProlongacion2 == null) {
				throw new IllegalArgumentException("Debe registrar primero la prolongación 2.");
			}

			if (!fechaProlongacion3.after(fechaProlongacion2)) {
				throw new IllegalArgumentException("La prolongación 3 debe ser posterior a la prolongación 2.");
			}
		}

		if (fechaProlongacion4 != null) {

			if (fechaProlongacion3 == null) {
				throw new IllegalArgumentException("Debe registrar primero la prolongación 3.");
			}

			if (!fechaProlongacion4.after(fechaProlongacion3)) {
				throw new IllegalArgumentException("La prolongación 4 debe ser posterior a la prolongación 3.");
			}
		}

		if (fechaProlongacion5 != null) {

			if (fechaProlongacion4 == null) {
				throw new IllegalArgumentException("Debe registrar primero la prolongación 4.");
			}

			if (!fechaProlongacion5.after(fechaProlongacion4)) {
				throw new IllegalArgumentException("La prolongación 5 debe ser posterior a la prolongación 4.");
			}
		}
	}

	private Date obtenerFecha() {

		String texto = txtFecha.getText();

		if (texto == null || texto.replace("/", "").replace("_", "").trim().isEmpty()) {

			return null;
		}

		if (texto.contains("_")) {

			throw new IllegalArgumentException("Complete correctamente la fecha.");
		}

		try {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			formato.setLenient(false);

			return formato.parse(texto);

		} catch (ParseException e) {

			throw new IllegalArgumentException("La fecha debe tener el formato dd/mm/aaaa.");
		}
	}

	private BigDecimal obtenerValor() {

		String texto = txtValor.getText();

		if (texto == null || texto.trim().isEmpty()) {

			return null;
		}

		try {

			BigDecimal valor = new BigDecimal(texto.trim());

			if (valor.compareTo(BigDecimal.ZERO) < 0) {

				throw new IllegalArgumentException("El valor no puede ser negativo.");
			}

			return valor;

		} catch (NumberFormatException e) {

			throw new IllegalArgumentException("El valor debe ser un número válido.");
		}
	}

	public boolean isGuardado() {

		return guardado;
	}
}