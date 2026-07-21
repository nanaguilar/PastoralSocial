package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import pastoralsocial.modelo.IntegranteFamiliar;

public class IntegranteFamiliarDetalleDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final IntegranteFamiliar integrante;

	public IntegranteFamiliarDetalleDialog(Frame parent, IntegranteFamiliar integrante) {

		super(parent, "Detalle del integrante familiar", true);

		this.integrante = integrante;

		inicializarInterfaz();

		setSize(790, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
	}

	private void inicializarInterfaz() {

		JPanel contentPanel = new JPanel();
		contentPanel.setBackground(new Color(245, 247, 250));
		contentPanel.setLayout(null);
		setContentPane(contentPanel);

		// TITULO
		JLabel lblTitulo = new JLabel("Detalle del integrante familiar");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(32, 20, 450, 40);
		contentPanel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel("Información registrada del integrante perteneciente al caso "
				+ integrante.getCasoSocial().getNumeroFicha() + ".");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(32, 58, 680, 25);
		contentPanel.add(lblDescripcion);

		// INFORMACION PERSONAL
		JLabel lblInformacionPersonal = new JLabel("Información personal");
		lblInformacionPersonal.setForeground(new Color(0, 45, 102));
		lblInformacionPersonal.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblInformacionPersonal.setBounds(32, 95, 250, 25);
		contentPanel.add(lblInformacionPersonal);

		JPanel panelPersonal = new JPanel();
		panelPersonal.setBackground(Color.WHITE);
		panelPersonal.setLayout(null);
		panelPersonal.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 228)));
		panelPersonal.setBounds(32, 128, 710, 125);
		contentPanel.add(panelPersonal);

		// FILA PRINCIPAL
		agregarDatoDestacado(panelPersonal, "IDENTIFICACIÓN", valor(integrante.getIdentificacion()), 20, 15, 200);
		agregarDatoDestacado(panelPersonal, "NOMBRE COMPLETO", nombreCompleto(), 245, 15, 280);
		agregarDatoDestacado(panelPersonal, "ESTADO", integrante.isEstado() ? "Activo" : "Inactivo", 550, 15, 130);

		// FILA SECUNDARIA
		agregarDato(panelPersonal, "Tipo de identificación", valor(integrante.getTipoIdentificacion()), 20, 78, 200);
		agregarDato(panelPersonal, "Fecha de nacimiento", formatearFecha(), 245, 78, 200);
		agregarDato(panelPersonal, "Género",
				integrante.getGenero() != null ? integrante.getGenero().toString() : "No especificado", 470, 78, 200);

		// INFORMACION FAMILIAR Y CONTACTO
		JLabel lblInformacionFamiliar = new JLabel("Información familiar y contacto");
		lblInformacionFamiliar.setForeground(new Color(0, 45, 102));
		lblInformacionFamiliar.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblInformacionFamiliar.setBounds(32, 270, 320, 25);
		contentPanel.add(lblInformacionFamiliar);

		JPanel panelFamiliar = new JPanel();
		panelFamiliar.setBackground(Color.WHITE);
		panelFamiliar.setLayout(null);
		panelFamiliar.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 228)));
		panelFamiliar.setBounds(32, 303, 710, 140);
		contentPanel.add(panelFamiliar);

		agregarDato(panelFamiliar, "Parentesco", valor(integrante.getParentesco()), 20, 18, 200);
		agregarDato(panelFamiliar, "Jefe de hogar", integrante.isJefeHogar() ? "Sí" : "No", 245, 18, 200);
		agregarDato(panelFamiliar, "Nivel educativo", valor(integrante.getNivelEducativo()), 470, 18, 200);
		agregarDato(panelFamiliar, "Teléfono", valor(integrante.getTelefono()), 20, 82, 200);
		agregarDato(panelFamiliar, "Correo electrónico", valor(integrante.getCorreoElectronico()), 245, 82, 200);
		agregarDato(panelFamiliar, "Seguro", valor(integrante.getSeguro()), 470, 82, 200);

		// UBICACION
		JLabel lblUbicacion = new JLabel("Ubicación");
		lblUbicacion.setForeground(new Color(0, 45, 102));
		lblUbicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblUbicacion.setBounds(32, 460, 250, 25);
		contentPanel.add(lblUbicacion);

		JPanel panelUbicacion = new JPanel();
		panelUbicacion.setBackground(Color.WHITE);
		panelUbicacion.setLayout(null);
		panelUbicacion.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 228)));
		panelUbicacion.setBounds(32, 493, 710, 105);
		contentPanel.add(panelUbicacion);

		agregarDato(panelUbicacion, "País", valor(integrante.getPais()), 20, 15, 145);
		agregarDato(panelUbicacion, "Provincia", valor(integrante.getProvincia()), 185, 15, 145);
		agregarDato(panelUbicacion, "Cantón", valor(integrante.getCanton()), 350, 15, 145);
		agregarDato(panelUbicacion, "Distrito", valor(integrante.getDistrito()), 515, 15, 165);

		JLabel lblDireccion = new JLabel("Dirección");
		lblDireccion.setForeground(new Color(70, 70, 70));
		lblDireccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblDireccion.setBounds(20, 62, 100, 18);
		panelUbicacion.add(lblDireccion);

		JTextArea txtDireccion = new JTextArea();
		txtDireccion.setText(valor(integrante.getDireccion()));
		txtDireccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtDireccion.setForeground(new Color(0, 45, 102));
		txtDireccion.setBackground(Color.WHITE);
		txtDireccion.setEditable(false);
		txtDireccion.setFocusable(false);
		txtDireccion.setLineWrap(true);
		txtDireccion.setWrapStyleWord(true);
		txtDireccion.setBorder(null);

		JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
		scrollDireccion.setBorder(null);
		scrollDireccion.setBounds(20, 82, 660, 20);
		panelUbicacion.add(scrollDireccion);

		// BOTON CERRAR
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setForeground(new Color(0, 45, 102));
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCerrar.setBounds(617, 608, 125, 38);

		btnCerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});

		contentPanel.add(btnCerrar);
	}

	private void agregarDatoDestacado(JPanel panel, String titulo, String contenido, int x, int y, int ancho) {

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(new Color(70, 70, 70));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 10));
		lblTitulo.setBounds(x, y, ancho, 18);
		panel.add(lblTitulo);

		JLabel lblContenido = new JLabel(contenido);
		lblContenido.setForeground(new Color(0, 45, 102));
		lblContenido.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblContenido.setBounds(x, y + 22, ancho, 25);
		panel.add(lblContenido);
	}

	private void agregarDato(JPanel panel, String titulo, String contenido, int x, int y, int ancho) {

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(new Color(70, 70, 70));
		lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblTitulo.setBounds(x, y, ancho, 18);
		panel.add(lblTitulo);

		JLabel lblContenido = new JLabel(contenido);
		lblContenido.setForeground(new Color(0, 45, 102));
		lblContenido.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblContenido.setBounds(x, y + 22, ancho, 20);
		panel.add(lblContenido);
	}

	private String nombreCompleto() {

		String nombre = valorSimple(integrante.getNombre());
		String apellidos = valorSimple(integrante.getApellidos());

		String nombreCompleto = (nombre + " " + apellidos).trim();

		if (nombreCompleto.isEmpty()) {

			return "No especificado";
		}

		return nombreCompleto;
	}

	private String formatearFecha() {

		if (integrante.getFechaNacimiento() == null) {

			return "No especificada";
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		return formato.format(integrante.getFechaNacimiento());
	}

	private String valor(String texto) {

		if (texto == null || texto.trim().isEmpty()) {

			return "No especificado";
		}

		return texto.trim();
	}

	private String valorSimple(String texto) {

		if (texto == null) {

			return "";
		}

		return texto.trim();
	}
}