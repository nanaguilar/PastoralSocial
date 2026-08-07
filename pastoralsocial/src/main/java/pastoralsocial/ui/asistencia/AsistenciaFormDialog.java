package pastoralsocial.ui.asistencia;

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

	// Nombre del catalogo (Maestro) que agrupa los tipos de asistencia.
	// Debe coincidir exactamente con el que se sembro en Main.java.
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

		// TIPO DE ASISTENCIA (ComboBox cargado desde Detalle)
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

		// OBSERVACIONES
		JLabel lblObservaciones = crearEtiqueta("Observaciones");
		lblObservaciones.setBounds(25, 370, 250, 20);
		contentPanel.add(lblObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);

		JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
		scrollObservaciones.setBounds(25, 393, 490, 90);
		contentPanel.add(scrollObservaciones);

		// CANCELAR
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setBounds(220, 500, 120, 40);
		contentPanel.add(btnCancelar);

		// GUARDAR
		btnGuardar = new JButton(modoEdicion ? "Guardar cambios" : "Guardar asistencia");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setBounds(355, 500, 175, 40);
		contentPanel.add(btnGuardar);

		btnCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		btnGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});

		setSize(560, 600);
		setResizable(false);
	}

	/**
	 * Este es el metodo clave del patron Maestro-Detalle: en vez de leer
	 * de una tabla dedicada (como se hacia antes con TipoAsistenciaController),
	 * busca el catalogo por nombre y carga unicamente los Detalle que le
	 * pertenecen.
	 */
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

			if (modoEdicion) {

				asistenciaController.actualizar(asistenciaGuardar);

			} else {

				asistenciaController.registrar(asistenciaGuardar);
			}

			guardado = true;

			JOptionPane.showMessageDialog(this,
					modoEdicion ? "La asistencia se actualizó correctamente." : "La asistencia se registró correctamente.",
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

	private void validarFormulario() {

		if (obtenerFecha() == null) {

			throw new IllegalArgumentException("Debe indicar la fecha de la asistencia.");
		}

		if (cmbTipoAsistencia.getSelectedItem() == null) {

			throw new IllegalArgumentException("Debe seleccionar un tipo de asistencia.");
		}

		if (txtModalidad.getText() == null || txtModalidad.getText().trim().isEmpty()) {

			throw new IllegalArgumentException("La modalidad es obligatoria.");
		}

		obtenerValor();
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