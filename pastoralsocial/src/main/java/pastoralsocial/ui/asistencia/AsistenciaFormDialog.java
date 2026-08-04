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

import javax.swing.DefaultComboBoxModel;
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
import pastoralsocial.controller.CasoSocialController;
import pastoralsocial.controller.TipoAsistenciaController;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.TipoAsistencia;

public class AsistenciaFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel;

	private JComboBox<CasoSocial> cmbCasoSocial;
	private JComboBox<TipoAsistencia> cmbTipoAsistencia;
	private JFormattedTextField txtFecha;
	private JComboBox<String> cmbModalidad;
	private JTextField txtFrecuencia;
	private JTextField txtDuracion;
	private JTextField txtValor;
	private JTextArea txtObservaciones;

	private JButton btnCancelar;
	private JButton btnGuardar;
	private JLabel lblTitulo;

	private final AsistenciaController asistenciaController;
	private final CasoSocialController casoSocialController;
	private final TipoAsistenciaController tipoAsistenciaController;

	private Asistencia asistenciaEditar;

	private boolean operacionRealizada;
	private boolean hayCambios;

	// NUEVA ASISTENCIA
	public AsistenciaFormDialog(Frame parent) {
		this(parent, null);
	}

	// EDITAR ASISTENCIA (si asistenciaEditar es null, se comporta como "nueva")
	public AsistenciaFormDialog(Frame parent, Asistencia asistenciaEditar) {
		super(parent, true);

		this.asistenciaController = new AsistenciaController();
		this.casoSocialController = new CasoSocialController();
		this.tipoAsistenciaController = new TipoAsistenciaController();
		this.asistenciaEditar = asistenciaEditar;
		this.operacionRealizada = false;

		inicializarInterfaz();
		cargarCombos();

		if (asistenciaEditar == null) {
			prepararNuevaAsistencia();
		} else {
			prepararEdicion();
		}

		configurarDeteccionCambios();
		hayCambios = false;
	}

	private void inicializarInterfaz() {

		setTitle("Gestión de Asistencias");
		setBounds(100, 100, 640, 650);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		contentPanel = new JPanel();
		contentPanel.setBounds(0, 0, 634, 611);
		contentPanel.setBackground(new Color(245, 247, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel);

		// TITULO
		lblTitulo = new JLabel("Nueva asistencia");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblTitulo.setBounds(30, 20, 400, 35);
		contentPanel.add(lblTitulo);

		// CASO SOCIAL
		JLabel lblCasoSocial = new JLabel("Caso social");
		lblCasoSocial.setForeground(new Color(0, 45, 102));
		lblCasoSocial.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblCasoSocial.setBounds(30, 75, 280, 25);
		contentPanel.add(lblCasoSocial);

		cmbCasoSocial = new JComboBox<CasoSocial>();
		cmbCasoSocial.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbCasoSocial.setBounds(30, 100, 280, 35);
		contentPanel.add(cmbCasoSocial);

		// TIPO DE ASISTENCIA
		JLabel lblTipoAsistencia = new JLabel("Tipo de asistencia");
		lblTipoAsistencia.setForeground(new Color(0, 45, 102));
		lblTipoAsistencia.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTipoAsistencia.setBounds(325, 75, 280, 25);
		contentPanel.add(lblTipoAsistencia);

		cmbTipoAsistencia = new JComboBox<TipoAsistencia>();
		cmbTipoAsistencia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbTipoAsistencia.setBounds(325, 100, 280, 35);
		contentPanel.add(cmbTipoAsistencia);

		// FECHA
		JLabel lblFecha = new JLabel("Fecha");
		lblFecha.setForeground(new Color(0, 45, 102));
		lblFecha.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFecha.setBounds(30, 150, 280, 25);
		contentPanel.add(lblFecha);

		txtFecha = crearCampoFecha();
		txtFecha.setForeground(new Color(0, 45, 102));
		txtFecha.setBounds(30, 175, 280, 35);
		txtFecha.setToolTipText("Formato: dd/mm/aaaa");
		contentPanel.add(txtFecha);

		// MODALIDAD
		JLabel lblModalidad = new JLabel("Modalidad");
		lblModalidad.setForeground(new Color(0, 45, 102));
		lblModalidad.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblModalidad.setBounds(325, 150, 280, 25);
		contentPanel.add(lblModalidad);

		cmbModalidad = new JComboBox<String>();
		cmbModalidad.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbModalidad.setBounds(325, 175, 280, 35);
		cmbModalidad.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Presencial", "Virtual", "Domicilio" }));
		contentPanel.add(cmbModalidad);

		// FRECUENCIA
		JLabel lblFrecuencia = new JLabel("Frecuencia");
		lblFrecuencia.setForeground(new Color(0, 45, 102));
		lblFrecuencia.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFrecuencia.setBounds(30, 225, 280, 25);
		contentPanel.add(lblFrecuencia);

		txtFrecuencia = new JTextField();
		txtFrecuencia.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtFrecuencia.setForeground(new Color(0, 45, 102));
		txtFrecuencia.setBounds(30, 250, 280, 35);
		txtFrecuencia.setColumns(10);
		contentPanel.add(txtFrecuencia);

		// DURACION
		JLabel lblDuracion = new JLabel("Duración");
		lblDuracion.setForeground(new Color(0, 45, 102));
		lblDuracion.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDuracion.setBounds(325, 225, 280, 25);
		contentPanel.add(lblDuracion);

		txtDuracion = new JTextField();
		txtDuracion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtDuracion.setForeground(new Color(0, 45, 102));
		txtDuracion.setBounds(325, 250, 280, 35);
		txtDuracion.setColumns(10);
		contentPanel.add(txtDuracion);

		// VALOR
		JLabel lblValor = new JLabel("Valor (₡)");
		lblValor.setForeground(new Color(0, 45, 102));
		lblValor.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblValor.setBounds(30, 300, 280, 25);
		contentPanel.add(lblValor);

		txtValor = new JTextField();
		txtValor.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtValor.setForeground(new Color(0, 45, 102));
		txtValor.setBounds(30, 325, 280, 35);
		txtValor.setColumns(10);
		contentPanel.add(txtValor);

		// OBSERVACIONES
		JLabel lblObservaciones = new JLabel("Observaciones");
		lblObservaciones.setForeground(new Color(0, 45, 102));
		lblObservaciones.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblObservaciones.setBounds(30, 375, 280, 25);
		contentPanel.add(lblObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);

		JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
		scrollObservaciones.setBounds(30, 400, 575, 110);
		contentPanel.add(scrollObservaciones);

		// BOTON CANCELAR
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setBounds(325, 535, 130, 40);
		contentPanel.add(btnCancelar);

		// BOTON GUARDAR
		btnGuardar = new JButton("Guardar");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBounds(475, 535, 130, 40);
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
				guardarAsistencia();
			}
		});
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

	private void cargarCombos() {

		try {

			List<CasoSocial> casos = casoSocialController.listar();
			cmbCasoSocial.setModel(new DefaultComboBoxModel<CasoSocial>(casos.toArray(new CasoSocial[0])));

			List<TipoAsistencia> tipos = tipoAsistenciaController.listar();
			cmbTipoAsistencia.setModel(new DefaultComboBoxModel<TipoAsistencia>(tipos.toArray(new TipoAsistencia[0])));

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los casos sociales o los tipos de asistencia.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void prepararNuevaAsistencia() {

		lblTitulo.setText("Nueva asistencia");
		cmbCasoSocial.setSelectedIndex(cmbCasoSocial.getItemCount() > 0 ? 0 : -1);
		cmbTipoAsistencia.setSelectedIndex(cmbTipoAsistencia.getItemCount() > 0 ? 0 : -1);

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		txtFecha.setText(formato.format(new Date()));
	}

	private void prepararEdicion() {

		lblTitulo.setText("Editar asistencia");

		cmbCasoSocial.setSelectedItem(asistenciaEditar.getCasoSocial());
		cmbTipoAsistencia.setSelectedItem(asistenciaEditar.getTipoAsistencia());

		if (asistenciaEditar.getFecha() != null) {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			txtFecha.setText(formato.format(asistenciaEditar.getFecha()));
		}

		if (asistenciaEditar.getModalidad() != null) {
			cmbModalidad.setSelectedItem(asistenciaEditar.getModalidad());
		}

		if (asistenciaEditar.getFrecuencia() != null) {
			txtFrecuencia.setText(asistenciaEditar.getFrecuencia());
		}

		if (asistenciaEditar.getDuracion() != null) {
			txtDuracion.setText(asistenciaEditar.getDuracion());
		}

		if (asistenciaEditar.getValor() != null) {
			txtValor.setText(asistenciaEditar.getValor().toPlainString());
		}

		if (asistenciaEditar.getObservaciones() != null) {
			txtObservaciones.setText(asistenciaEditar.getObservaciones());
		}
	}

	private void configurarDeteccionCambios() {

		hayCambios = true;
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

	private void guardarAsistencia() {

		try {

			CasoSocial casoSocial = (CasoSocial) cmbCasoSocial.getSelectedItem();
			TipoAsistencia tipoAsistencia = (TipoAsistencia) cmbTipoAsistencia.getSelectedItem();
			Date fecha = obtenerFecha();
			String modalidad = cmbModalidad.getSelectedItem() != null ? cmbModalidad.getSelectedItem().toString() : null;
			String frecuencia = txtFrecuencia.getText().trim();
			String duracion = txtDuracion.getText().trim();
			BigDecimal valor = obtenerValor();
			String observaciones = txtObservaciones.getText().trim();

			// NUEVA ASISTENCIA
			if (asistenciaEditar == null) {

				Asistencia nuevaAsistencia = new Asistencia(fecha, modalidad, frecuencia, duracion, valor,
						observaciones, casoSocial, tipoAsistencia);

				asistenciaController.registrar(nuevaAsistencia);

				operacionRealizada = true;

				JOptionPane.showMessageDialog(this, "Asistencia registrada correctamente.", "Asistencias",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();

			} else {

				asistenciaEditar.setCasoSocial(casoSocial);
				asistenciaEditar.setTipoAsistencia(tipoAsistencia);
				asistenciaEditar.setFecha(fecha);
				asistenciaEditar.setModalidad(modalidad);
				asistenciaEditar.setFrecuencia(frecuencia);
				asistenciaEditar.setDuracion(duracion);
				asistenciaEditar.setValor(valor);
				asistenciaEditar.setObservaciones(observaciones);

				asistenciaController.actualizar(asistenciaEditar);

				operacionRealizada = true;

				JOptionPane.showMessageDialog(this, "Asistencia actualizada correctamente.", "Asistencias",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();
			}

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Asistencias", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar la asistencia.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Date obtenerFecha() {

		String texto = txtFecha.getText();

		if (texto == null || texto.replace("/", "").replace("_", "").trim().isEmpty()) {

			throw new IllegalArgumentException("La fecha es obligatoria.");
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

		String texto = txtValor.getText().trim();

		if (texto.isEmpty()) {

			return null;
		}

		try {

			return new BigDecimal(texto.replace(",", "."));

		} catch (NumberFormatException e) {

			throw new IllegalArgumentException("El valor debe ser un número válido.");
		}
	}

	public boolean isOperacionRealizada() {
		return operacionRealizada;
	}
}
