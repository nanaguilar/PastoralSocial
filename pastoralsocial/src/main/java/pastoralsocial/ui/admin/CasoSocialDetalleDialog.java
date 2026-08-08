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

import pastoralsocial.modelo.CasoSocial;

public class CasoSocialDetalleDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final CasoSocial casoSocial;

	public CasoSocialDetalleDialog(Frame parent, CasoSocial casoSocial) {

		super(parent, "Detalle del caso social", true);

		this.casoSocial = casoSocial;

		inicializarInterfaz();

		setSize(790, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
	}

	private void inicializarInterfaz() {

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		getContentPane().setBackground(new Color(245, 247, 250));
		getContentPane().setLayout(null);

		// TITULO
		JLabel lblTitulo = new JLabel("Detalle del caso social");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(35, 20, 400, 35);
		getContentPane().add(lblTitulo);

		JLabel lblDescripcion = new JLabel("Información general registrada para el caso social.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDescripcion.setBounds(35, 55, 500, 25);
		getContentPane().add(lblDescripcion);

		// INFORMACION PRINCIPAL
		JLabel lblSeccionCaso = new JLabel("Información del caso");
		lblSeccionCaso.setForeground(new Color(0, 45, 102));
		lblSeccionCaso.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblSeccionCaso.setBounds(35, 90, 250, 25);
		getContentPane().add(lblSeccionCaso);

		JPanel panelCaso = new JPanel();
		panelCaso.setForeground(new Color(0, 45, 102));
		panelCaso.setBackground(Color.WHITE);
		panelCaso.setLayout(null);
		panelCaso.setBounds(35, 125, 710, 110);
		panelCaso.setBorder(BorderFactory.createLineBorder(new Color(215, 222, 230)));
		getContentPane().add(panelCaso);

		// NUMERO DE FICHA
		JLabel lblFichaTitulo = new JLabel("N.º DE FICHA");
		lblFichaTitulo.setForeground(new Color(90, 90, 90));
		lblFichaTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblFichaTitulo.setBounds(20, 15, 130, 18);
		panelCaso.add(lblFichaTitulo);

		JLabel lblFichaValor = new JLabel(valor(casoSocial.getNumeroFicha()));
		lblFichaValor.setForeground(new Color(0, 45, 102));
		lblFichaValor.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblFichaValor.setBounds(20, 35, 170, 28);
		panelCaso.add(lblFichaValor);

		// ESTADO
		JLabel lblEstadoTitulo = new JLabel("ESTADO");
		lblEstadoTitulo.setForeground(new Color(90, 90, 90));
		lblEstadoTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblEstadoTitulo.setBounds(215, 15, 100, 18);
		panelCaso.add(lblEstadoTitulo);

		JLabel lblEstadoValor = new JLabel(formatearEstado());
		lblEstadoValor.setForeground(new Color(0, 45, 102));
		lblEstadoValor.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblEstadoValor.setBounds(215, 38, 150, 25);
		panelCaso.add(lblEstadoValor);

		// RESPONSABLE
		JLabel lblResponsableTitulo = new JLabel("RESPONSABLE");
		lblResponsableTitulo.setForeground(new Color(90, 90, 90));
		lblResponsableTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblResponsableTitulo.setBounds(390, 15, 150, 18);
		panelCaso.add(lblResponsableTitulo);

		JLabel lblResponsableValor = new JLabel(obtenerResponsable());
		lblResponsableValor.setForeground(new Color(0, 45, 102));
		lblResponsableValor.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblResponsableValor.setBounds(390, 38, 290, 25);
		panelCaso.add(lblResponsableValor);

		// FECHAS
		JLabel lblFechaInicioTitulo = new JLabel("Fecha de inicio");
		lblFechaInicioTitulo.setForeground(new Color(90, 90, 90));
		lblFechaInicioTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblFechaInicioTitulo.setBounds(20, 72, 130, 18);
		panelCaso.add(lblFechaInicioTitulo);

		JLabel lblFechaInicioValor = new JLabel(formatearFecha(casoSocial.getFechaInicio()));
		lblFechaInicioValor.setForeground(new Color(45, 45, 45));
		lblFechaInicioValor.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFechaInicioValor.setBounds(115, 72, 130, 18);
		panelCaso.add(lblFechaInicioValor);

		JLabel lblFechaFinTitulo = new JLabel("Fecha prevista de fin");
		lblFechaFinTitulo.setForeground(new Color(90, 90, 90));
		lblFechaFinTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblFechaFinTitulo.setBounds(390, 72, 150, 18);
		panelCaso.add(lblFechaFinTitulo);

		JLabel lblFechaFinValor = new JLabel(formatearFecha(casoSocial.getFechaPrevistaFin()));
		lblFechaFinValor.setForeground(new Color(45, 45, 45));
		lblFechaFinValor.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFechaFinValor.setBounds(530, 72, 150, 18);
		panelCaso.add(lblFechaFinValor);

		// UBICACION
		JLabel lblUbicacion = new JLabel("Ubicación");
		lblUbicacion.setForeground(new Color(0, 45, 102));
		lblUbicacion.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblUbicacion.setBounds(35, 255, 250, 25);
		getContentPane().add(lblUbicacion);

		JPanel panelUbicacion = new JPanel();
		panelUbicacion.setForeground(new Color(0, 45, 102));
		panelUbicacion.setBackground(Color.WHITE);
		panelUbicacion.setLayout(null);
		panelUbicacion.setBounds(35, 290, 710, 145);
		panelUbicacion.setBorder(BorderFactory.createLineBorder(new Color(215, 222, 230)));
		getContentPane().add(panelUbicacion);

		// PARROQUIA
		JLabel lblParroquiaTitulo = new JLabel("Parroquia");
		lblParroquiaTitulo.setForeground(new Color(90, 90, 90));
		lblParroquiaTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblParroquiaTitulo.setBounds(20, 15, 190, 18);
		panelUbicacion.add(lblParroquiaTitulo);

		JLabel lblParroquiaValor = new JLabel(valor(casoSocial.getParroquia()));

		lblParroquiaValor.setForeground(new Color(0, 45, 102));
		lblParroquiaValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblParroquiaValor.setBounds(20, 35, 200, 22);
		panelUbicacion.add(lblParroquiaValor);

		// SECTOR
		JLabel lblSectorTitulo = new JLabel("Sector / Filial");
		lblSectorTitulo.setForeground(new Color(90, 90, 90));
		lblSectorTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblSectorTitulo.setBounds(255, 15, 190, 18);
		panelUbicacion.add(lblSectorTitulo);

		JLabel lblSectorValor = new JLabel(valor(casoSocial.getSectorFilial()));
		lblSectorValor.setForeground(new Color(0, 45, 102));
		lblSectorValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblSectorValor.setBounds(255, 35, 200, 22);
		panelUbicacion.add(lblSectorValor);

		// PROVINCIA
		JLabel lblProvinciaTitulo = new JLabel("Provincia");
		lblProvinciaTitulo.setForeground(new Color(90, 90, 90));
		lblProvinciaTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblProvinciaTitulo.setBounds(490, 15, 190, 18);
		panelUbicacion.add(lblProvinciaTitulo);

		JLabel lblProvinciaValor = new JLabel(valor(casoSocial.getProvincia()));
		lblProvinciaValor.setForeground(new Color(0, 45, 102));
		lblProvinciaValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblProvinciaValor.setBounds(490, 35, 190, 22);
		panelUbicacion.add(lblProvinciaValor);

		// CANTON
		JLabel lblCantonTitulo = new JLabel("Cantón");
		lblCantonTitulo.setForeground(new Color(90, 90, 90));
		lblCantonTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblCantonTitulo.setBounds(20, 78, 190, 18);
		panelUbicacion.add(lblCantonTitulo);

		JLabel lblCantonValor = new JLabel(valor(casoSocial.getCanton()));
		lblCantonValor.setForeground(new Color(0, 45, 102));
		lblCantonValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblCantonValor.setBounds(20, 98, 200, 22);
		panelUbicacion.add(lblCantonValor);

		// DISTRITO
		JLabel lblDistritoTitulo = new JLabel("Distrito");
		lblDistritoTitulo.setForeground(new Color(90, 90, 90));
		lblDistritoTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblDistritoTitulo.setBounds(255, 78, 190, 18);
		panelUbicacion.add(lblDistritoTitulo);

		JLabel lblDistritoValor = new JLabel(valor(casoSocial.getDistrito()));

		lblDistritoValor.setForeground(new Color(0, 45, 102));
		lblDistritoValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDistritoValor.setBounds(255, 98, 200, 22);
		panelUbicacion.add(lblDistritoValor);

		// DIRECCION
		JLabel lblDireccionTitulo = new JLabel("Dirección");
		lblDireccionTitulo.setForeground(new Color(90, 90, 90));
		lblDireccionTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblDireccionTitulo.setBounds(490, 78, 190, 18);
		panelUbicacion.add(lblDireccionTitulo);

		JLabel lblDireccionValor = new JLabel(valor(casoSocial.getDireccion()));

		lblDireccionValor.setForeground(new Color(0, 45, 102));
		lblDireccionValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblDireccionValor.setBounds(490, 98, 190, 22);
		panelUbicacion.add(lblDireccionValor);

		// INFORMACION DE VIVIENDA
		JLabel lblVivienda = new JLabel("Información de vivienda");
		lblVivienda.setForeground(new Color(0, 45, 102));
		lblVivienda.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblVivienda.setBounds(35, 455, 300, 25);
		getContentPane().add(lblVivienda);

		JPanel panelVivienda = new JPanel();
		panelVivienda.setForeground(new Color(0, 45, 102));
		panelVivienda.setBackground(Color.WHITE);
		panelVivienda.setLayout(null);
		panelVivienda.setBounds(35, 490, 710, 70);
		panelVivienda.setBorder(BorderFactory.createLineBorder(new Color(215, 222, 230)));
		getContentPane().add(panelVivienda);

		// TIPO VIVIENDA
		JLabel lblTipoTitulo = new JLabel("Tipo de vivienda");
		lblTipoTitulo.setForeground(new Color(90, 90, 90));
		lblTipoTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblTipoTitulo.setBounds(20, 12, 190, 18);
		panelVivienda.add(lblTipoTitulo);

		JLabel lblTipoValor = new JLabel(obtenerTipoVivienda());
		lblTipoValor.setForeground(new Color(0, 45, 102));
		lblTipoValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblTipoValor.setBounds(20, 32, 200, 22);
		panelVivienda.add(lblTipoValor);

		// TENENCIA
		JLabel lblTenenciaTitulo = new JLabel("Tenencia");
		lblTenenciaTitulo.setForeground(new Color(90, 90, 90));
		lblTenenciaTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblTenenciaTitulo.setBounds(255, 12, 190, 18);
		panelVivienda.add(lblTenenciaTitulo);

		JLabel lblTenenciaValor = new JLabel(obtenerTenencia());
		lblTenenciaValor.setForeground(new Color(0, 45, 102));
		lblTenenciaValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblTenenciaValor.setBounds(255, 32, 200, 22);
		panelVivienda.add(lblTenenciaValor);

		// CONDICION
		JLabel lblCondicionTitulo = new JLabel("Condición");
		lblCondicionTitulo.setForeground(new Color(90, 90, 90));
		lblCondicionTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblCondicionTitulo.setBounds(490, 12, 190, 18);
		panelVivienda.add(lblCondicionTitulo);

		JLabel lblCondicionValor = new JLabel(obtenerCondicion());
		lblCondicionValor.setForeground(new Color(0, 45, 102));
		lblCondicionValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblCondicionValor.setBounds(490, 32, 190, 22);
		panelVivienda.add(lblCondicionValor);

		// OBSERVACIONES
		JLabel lblObservaciones = new JLabel("Observaciones");
		lblObservaciones.setForeground(new Color(0, 45, 102));
		lblObservaciones.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblObservaciones.setBounds(35, 575, 200, 20);
		getContentPane().add(lblObservaciones);

		JTextArea txtObservaciones = new JTextArea();
		txtObservaciones.setText(valor(casoSocial.getObservaciones()));
		txtObservaciones.setForeground(new Color(45, 45, 45));
		txtObservaciones.setBackground(Color.WHITE);
		txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtObservaciones.setEditable(false);
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);
		txtObservaciones.setBorder(new EmptyBorder(7, 8, 7, 8));

		JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
		scrollObservaciones.setBounds(35, 600, 535, 48);
		getContentPane().add(scrollObservaciones);

		// BOTON CERRAR
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setForeground(new Color(0, 45, 102));
		btnCerrar.setBackground(Color.WHITE);
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBounds(620, 608, 125, 40);
		getContentPane().add(btnCerrar);

		btnCerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});
	}

	private String obtenerResponsable() {

		if (casoSocial.getUsuario() == null) {

			return "No asignado";
		}

		String nombre = casoSocial.getUsuario().getNombre();

		String apellidos = casoSocial.getUsuario().getApellidos();

		if (nombre == null) {

			nombre = "";
		}

		if (apellidos == null) {

			apellidos = "";
		}

		String responsable = (nombre + " " + apellidos).trim();

		if (responsable.isEmpty()) {

			return "No asignado";
		}

		return responsable;
	}

	private String obtenerTipoVivienda() {

		if (casoSocial.getDetalleTipoVivienda() == null) {

			return "No especificado";
		}

		return casoSocial.getDetalleTipoVivienda().toString();
	}

	private String obtenerTenencia() {

		if (casoSocial.getDetalleTenenciaVivienda() == null) {

			return "No especificado";
		}

		return casoSocial.getDetalleTenenciaVivienda().toString();
	}

	private String obtenerCondicion() {

		if (casoSocial.getDetalleCondicionVivienda() == null) {

			return "No especificado";
		}

		return casoSocial.getDetalleCondicionVivienda().toString();
	}

	private String formatearFecha(java.util.Date fecha) {

		if (fecha == null) {

			return "No especificada";
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		return formato.format(fecha);
	}

	private String formatearEstado() {

		String estado = casoSocial.getEstado().toString();

		if ("ENPROCESO".equals(estado)) {

			return "En proceso";
		}

		if ("ACTIVO".equals(estado)) {

			return "Activo";
		}

		if ("CERRADO".equals(estado)) {

			return "Cerrado";
		}

		return estado;
	}

	private String valor(String texto) {

		if (texto == null || texto.trim().isEmpty()) {

			return "No especificado";
		}

		return texto.trim();
	}
}