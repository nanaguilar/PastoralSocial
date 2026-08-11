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

import pastoralsocial.modelo.Asistencia;

public class AsistenciaDetalleDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final Asistencia asistencia;

	public AsistenciaDetalleDialog(Frame parent, Asistencia asistencia) {
		super(parent, "Detalle de asistencia", true);
		this.asistencia = asistencia;
		inicializarInterfaz();
	}

	private void inicializarInterfaz() {

		setSize(720, 560);
		setLocationRelativeTo(getParent());
		setResizable(false);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 247, 250));
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel("Detalle de asistencia");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(35, 25, 500, 40);
		panel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel("Información registrada de la asistencia otorgada.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(35, 65, 500, 25);
		panel.add(lblDescripcion);

		JLabel lblInformacion = new JLabel("Información de la asistencia");
		lblInformacion.setForeground(new Color(0, 45, 102));
		lblInformacion.setFont(new Font("SansSerif", Font.BOLD, 17));
		lblInformacion.setBounds(35, 105, 400, 30);
		panel.add(lblInformacion);

		JPanel panelInformacion = new JPanel();
		panelInformacion.setBackground(Color.WHITE);
		panelInformacion.setBorder(BorderFactory.createLineBorder(new Color(205, 213, 221)));
		panelInformacion.setLayout(null);
		panelInformacion.setBounds(35, 140, 635, 170);
		panel.add(panelInformacion);

		agregarCampo(panelInformacion, "Fecha", obtenerFecha(), 20, 20);
		agregarCampo(panelInformacion, "Tipo de asistencia", obtenerTipo(), 235, 20);
		agregarCampo(panelInformacion, "Modalidad", obtenerModalidad(), 450, 20);

		agregarCampo(panelInformacion, "Valor", obtenerValor(), 20, 90);
		agregarCampo(panelInformacion, "Caso social", obtenerCasoSocial(), 235, 90);

		JLabel lblObservacion = new JLabel("Información adicional");
		lblObservacion.setForeground(new Color(0, 45, 102));
		lblObservacion.setFont(new Font("SansSerif", Font.BOLD, 17));
		lblObservacion.setBounds(35, 330, 400, 30);
		panel.add(lblObservacion);

		JPanel panelAdicional = new JPanel();
		panelAdicional.setBackground(Color.WHITE);
		panelAdicional.setBorder(BorderFactory.createLineBorder(new Color(205, 213, 221)));
		panelAdicional.setLayout(null);
		panelAdicional.setBounds(35, 365, 635, 80);
		panel.add(panelAdicional);

		agregarCampo(panelAdicional, "Duración", obtenerDuracion(), 20, 20);
		agregarCampo(panelAdicional, "Frecuencia", obtenerFrecuencia(), 235, 20);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setForeground(new Color(0, 45, 102));
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBounds(540, 460, 130, 40);
		panel.add(btnCerrar);

		btnCerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setContentPane(panel);
	}

	private void agregarCampo(JPanel panel, String titulo, String valor, int x, int y) {

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(new Color(80, 80, 80));
		lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblTitulo.setBounds(x, y, 190, 20);
		panel.add(lblTitulo);

		JLabel lblValor = new JLabel(valor);
		lblValor.setForeground(new Color(0, 45, 102));
		lblValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblValor.setBounds(x, y + 23, 190, 25);
		panel.add(lblValor);
	}

	private String obtenerFecha() {

		if (asistencia.getFecha() == null) {
			return "No especificada";
		}

		return new SimpleDateFormat("dd/MM/yyyy").format(asistencia.getFecha());
	}

	private String obtenerTipo() {

		if (asistencia.getDetalleTipoAsistencia() == null) {
			return "No especificado";
		}

		return asistencia.getDetalleTipoAsistencia().toString();
	}

	private String obtenerModalidad() {

		if (asistencia.getModalidad() == null || asistencia.getModalidad().trim().isEmpty()) {
			return "No especificada";
		}

		return asistencia.getModalidad();
	}

	private String obtenerValor() {

		if (asistencia.getValor() == null) {
			return "No especificado";
		}

		return asistencia.getValor().toPlainString();
	}

	private String obtenerCasoSocial() {

		if (asistencia.getCasoSocial() == null) {
			return "No especificado";
		}

		if (asistencia.getCasoSocial().getNumeroFicha() == null) {
			return "No especificado";
		}

		return asistencia.getCasoSocial().getNumeroFicha();
	}

	private String obtenerDuracion() {

		return asistencia.getDuracion() != null
				? asistencia.getDuracion().toString()
				: "No especificada";
	}

	private String obtenerFrecuencia() {

		return asistencia.getFrecuencia() != null
				? asistencia.getFrecuencia().toString()
				: "No especificada";
	}
}