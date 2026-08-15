package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.swing.text.MaskFormatter;

import pastoralsocial.controller.AdendumController;
import pastoralsocial.modelo.Adendum;
import pastoralsocial.modelo.CasoSocial;

public class AdendumFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final AdendumController adendumController;
	private final CasoSocial casoSocial;
	private final Adendum adendum;

	private boolean guardado;

	private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

	// SERVICIOS PUBLICOS
	private JFormattedTextField txtElectricidadFecha1;
	private JFormattedTextField txtElectricidadFecha2;
	private JFormattedTextField txtElectricidadFecha3;

	private JFormattedTextField txtAguaFecha1;
	private JFormattedTextField txtAguaFecha2;
	private JFormattedTextField txtAguaFecha3;

	private JFormattedTextField txtTelefonoFecha1;
	private JFormattedTextField txtTelefonoFecha2;
	private JFormattedTextField txtTelefonoFecha3;

	private JFormattedTextField txtInternetFecha1;
	private JFormattedTextField txtInternetFecha2;
	private JFormattedTextField txtInternetFecha3;

	private JFormattedTextField txtCableFecha1;
	private JFormattedTextField txtCableFecha2;
	private JFormattedTextField txtCableFecha3;

	// GASTOS DE VIVIENDA
	private JFormattedTextField txtAlquilerFecha1;
	private JFormattedTextField txtAlquilerFecha2;
	private JFormattedTextField txtAlquilerFecha3;

	private JFormattedTextField txtAlimentacionFecha1;
	private JFormattedTextField txtAlimentacionFecha2;
	private JFormattedTextField txtAlimentacionFecha3;

	// GASTO DE SALUD
	private JFormattedTextField txtMedicamentoFecha1;
	private JFormattedTextField txtMedicamentoFecha2;
	private JFormattedTextField txtMedicamentoFecha3;

	// OTROS GASTOS
	private JTextField txtOtroGasto1;
	private JFormattedTextField txtOtroGasto1Fecha1;
	private JFormattedTextField txtOtroGasto1Fecha2;
	private JFormattedTextField txtOtroGasto1Fecha3;

	private JTextField txtOtroGasto2;
	private JFormattedTextField txtOtroGasto2Fecha1;
	private JFormattedTextField txtOtroGasto2Fecha2;
	private JFormattedTextField txtOtroGasto2Fecha3;

	// OBSERVACIONES
	private JTextArea txtObservaciones;

	// RECOMENDACIONES
	private JComboBox<String> cmbRecomendacion1;
	private JFormattedTextField txtRecomendacion1Fecha;

	private JComboBox<String> cmbRecomendacion2;
	private JFormattedTextField txtRecomendacion2Fecha;

	private JComboBox<String> cmbRecomendacion3;
	private JFormattedTextField txtRecomendacion3Fecha;

	/**
	 * @wbp.parser.constructor
	 */
	public AdendumFormDialog(Window parent, CasoSocial casoSocial) {
		this(parent, casoSocial, null);
	}

	public AdendumFormDialog(Window parent, CasoSocial casoSocial, Adendum adendum) {

		super(parent, adendum == null ? "Nuevo adendum" : "Editar adendum", ModalityType.APPLICATION_MODAL);

		this.casoSocial = casoSocial;
		this.adendum = adendum;
		this.adendumController = new AdendumController();

		inicializarInterfaz();

		if (adendum != null) {
			cargarDatos();
		}
	}

	private void inicializarInterfaz() {

		setTitle("Gestión de Adendums");
		setBounds(100, 100, 720, 700);
		setLocationRelativeTo(getParent());
		setResizable(false);

		getContentPane().setLayout(null);

		JPanel panelContenido = new JPanel();
		panelContenido.setLayout(null);
		panelContenido.setBackground(new Color(245, 247, 250));
		panelContenido.setPreferredSize(new Dimension(680, 1100));

		JScrollPane scrollPane = new JScrollPane(panelContenido);
		scrollPane.setBounds(0, 0, 704, 660);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		getContentPane().add(scrollPane);

		// TITULO
		JLabel lblTitulo = new JLabel(adendum == null ? "Nuevo adendum" : "Editar adendum");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(30, 20, 500, 35);
		panelContenido.add(lblTitulo);

		// EXPEDIENTE
		JLabel lblExpediente = new JLabel("Adendum al Expediente N.º:");
		lblExpediente.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblExpediente.setBounds(30, 65, 210, 25);
		panelContenido.add(lblExpediente);

		String numeroFicha = "";

		if (casoSocial != null && casoSocial.getNumeroFicha() != null) {

			numeroFicha = casoSocial.getNumeroFicha();
		}

		JLabel valorExpediente = new JLabel(numeroFicha);
		valorExpediente.setForeground(new Color(0, 45, 102));
		valorExpediente.setFont(new Font("SansSerif", Font.BOLD, 14));
		valorExpediente.setBounds(245, 65, 250, 25);
		panelContenido.add(valorExpediente);

		// INFORMACION
		JLabel lblInformacion = new JLabel("Información adicional relevante para el expediente:");
		lblInformacion.setForeground(new Color(90, 90, 90));
		lblInformacion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblInformacion.setBounds(30, 95, 500, 25);
		panelContenido.add(lblInformacion);

		// PAGOS MENSUALES ACTUALES
		JLabel lblPagos = new JLabel("Pagos mensuales actuales:");
		lblPagos.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblPagos.setForeground(Color.BLACK);
		lblPagos.setBounds(30, 135, 500, 25);
		panelContenido.add(lblPagos);

		JLabel lblFecha1 = new JLabel("Fecha:");
		lblFecha1.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFecha1.setBounds(250, 165, 70, 20);
		panelContenido.add(lblFecha1);

		JLabel lblFecha2 = new JLabel("Fecha:");
		lblFecha2.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFecha2.setBounds(350, 165, 70, 20);
		panelContenido.add(lblFecha2);

		JLabel lblFecha3 = new JLabel("Fecha:");
		lblFecha3.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblFecha3.setBounds(450, 165, 70, 20);
		panelContenido.add(lblFecha3);

		// SERVICIOS PUBLICOS
		JLabel lblServicios = new JLabel("Servicios Públicos");
		lblServicios.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblServicios.setForeground(Color.BLACK);
		lblServicios.setBounds(30, 195, 300, 25);
		panelContenido.add(lblServicios);

		JLabel lblElectricidad = new JLabel("Electricidad:");
		lblElectricidad.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblElectricidad.setBounds(30, 225, 190, 25);
		panelContenido.add(lblElectricidad);

		txtElectricidadFecha1 = crearCampoFecha();
		txtElectricidadFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtElectricidadFecha1.setToolTipText("dd/MM/yyyy");
		txtElectricidadFecha1.setBounds(250, 225, 90, 25);
		panelContenido.add(txtElectricidadFecha1);

		txtElectricidadFecha2 = crearCampoFecha();
		txtElectricidadFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtElectricidadFecha2.setToolTipText("dd/MM/yyyy");
		txtElectricidadFecha2.setBounds(350, 225, 90, 25);
		panelContenido.add(txtElectricidadFecha2);

		txtElectricidadFecha3 = crearCampoFecha();
		txtElectricidadFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtElectricidadFecha3.setToolTipText("dd/MM/yyyy");
		txtElectricidadFecha3.setBounds(450, 225, 90, 25);
		panelContenido.add(txtElectricidadFecha3);

		JLabel lblAgua = new JLabel("Agua:");
		lblAgua.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblAgua.setBounds(30, 255, 190, 25);
		panelContenido.add(lblAgua);

		txtAguaFecha1 = crearCampoFecha();
		txtAguaFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAguaFecha1.setToolTipText("dd/MM/yyyy");
		txtAguaFecha1.setBounds(250, 255, 90, 25);
		panelContenido.add(txtAguaFecha1);

		txtAguaFecha2 = crearCampoFecha();
		txtAguaFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAguaFecha2.setToolTipText("dd/MM/yyyy");
		txtAguaFecha2.setBounds(350, 255, 90, 25);
		panelContenido.add(txtAguaFecha2);

		txtAguaFecha3 = crearCampoFecha();
		txtAguaFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAguaFecha3.setToolTipText("dd/MM/yyyy");
		txtAguaFecha3.setBounds(450, 255, 90, 25);
		panelContenido.add(txtAguaFecha3);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblTelefono.setBounds(30, 285, 190, 25);
		panelContenido.add(lblTelefono);

		txtTelefonoFecha1 = crearCampoFecha();
		txtTelefonoFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtTelefonoFecha1.setToolTipText("dd/MM/yyyy");
		txtTelefonoFecha1.setBounds(250, 285, 90, 25);
		panelContenido.add(txtTelefonoFecha1);

		txtTelefonoFecha2 = crearCampoFecha();
		txtTelefonoFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtTelefonoFecha2.setToolTipText("dd/MM/yyyy");
		txtTelefonoFecha2.setBounds(350, 285, 90, 25);
		panelContenido.add(txtTelefonoFecha2);

		txtTelefonoFecha3 = crearCampoFecha();
		txtTelefonoFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtTelefonoFecha3.setToolTipText("dd/MM/yyyy");
		txtTelefonoFecha3.setBounds(450, 285, 90, 25);
		panelContenido.add(txtTelefonoFecha3);

		JLabel lblInternet = new JLabel("Internet:");
		lblInternet.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblInternet.setBounds(30, 315, 190, 25);
		panelContenido.add(lblInternet);

		txtInternetFecha1 = crearCampoFecha();
		txtInternetFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtInternetFecha1.setToolTipText("dd/MM/yyyy");
		txtInternetFecha1.setBounds(250, 315, 90, 25);
		panelContenido.add(txtInternetFecha1);

		txtInternetFecha2 = crearCampoFecha();
		txtInternetFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtInternetFecha2.setToolTipText("dd/MM/yyyy");
		txtInternetFecha2.setBounds(350, 315, 90, 25);
		panelContenido.add(txtInternetFecha2);

		txtInternetFecha3 = crearCampoFecha();
		txtInternetFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtInternetFecha3.setToolTipText("dd/MM/yyyy");
		txtInternetFecha3.setBounds(450, 315, 90, 25);
		panelContenido.add(txtInternetFecha3);

		JLabel lblCable = new JLabel("Cable:");
		lblCable.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblCable.setBounds(30, 345, 190, 25);
		panelContenido.add(lblCable);

		txtCableFecha1 = crearCampoFecha();
		txtCableFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCableFecha1.setToolTipText("dd/MM/yyyy");
		txtCableFecha1.setBounds(250, 345, 90, 25);
		panelContenido.add(txtCableFecha1);

		txtCableFecha2 = crearCampoFecha();
		txtCableFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCableFecha2.setToolTipText("dd/MM/yyyy");
		txtCableFecha2.setBounds(350, 345, 90, 25);
		panelContenido.add(txtCableFecha2);

		txtCableFecha3 = crearCampoFecha();
		txtCableFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtCableFecha3.setToolTipText("dd/MM/yyyy");
		txtCableFecha3.setBounds(450, 345, 90, 25);
		panelContenido.add(txtCableFecha3);

		// GASTO DE VIVIENDA
		JLabel lblVivienda = new JLabel("Gasto de vivienda:");
		lblVivienda.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblVivienda.setForeground(Color.BLACK);
		lblVivienda.setBounds(30, 385, 300, 25);
		panelContenido.add(lblVivienda);

		JLabel lblAlquiler = new JLabel("Alquiler o préstamo:");
		lblAlquiler.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblAlquiler.setBounds(30, 415, 190, 25);
		panelContenido.add(lblAlquiler);

		txtAlquilerFecha1 = crearCampoFecha();
		txtAlquilerFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAlquilerFecha1.setToolTipText("dd/MM/yyyy");
		txtAlquilerFecha1.setBounds(250, 415, 90, 25);
		panelContenido.add(txtAlquilerFecha1);

		txtAlquilerFecha2 = crearCampoFecha();
		txtAlquilerFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAlquilerFecha2.setToolTipText("dd/MM/yyyy");
		txtAlquilerFecha2.setBounds(350, 415, 90, 25);
		panelContenido.add(txtAlquilerFecha2);

		txtAlquilerFecha3 = crearCampoFecha();
		txtAlquilerFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAlquilerFecha3.setToolTipText("dd/MM/yyyy");
		txtAlquilerFecha3.setBounds(450, 415, 90, 25);
		panelContenido.add(txtAlquilerFecha3);

		JLabel lblAlimentacion = new JLabel("Alimentación:");
		lblAlimentacion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblAlimentacion.setBounds(30, 445, 190, 25);
		panelContenido.add(lblAlimentacion);

		txtAlimentacionFecha1 = crearCampoFecha();
		txtAlimentacionFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAlimentacionFecha1.setToolTipText("dd/MM/yyyy");
		txtAlimentacionFecha1.setBounds(250, 445, 90, 25);
		panelContenido.add(txtAlimentacionFecha1);

		txtAlimentacionFecha2 = crearCampoFecha();
		txtAlimentacionFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAlimentacionFecha2.setToolTipText("dd/MM/yyyy");
		txtAlimentacionFecha2.setBounds(350, 445, 90, 25);
		panelContenido.add(txtAlimentacionFecha2);

		txtAlimentacionFecha3 = crearCampoFecha();
		txtAlimentacionFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtAlimentacionFecha3.setToolTipText("dd/MM/yyyy");
		txtAlimentacionFecha3.setBounds(450, 445, 90, 25);
		panelContenido.add(txtAlimentacionFecha3);

		// GASTO DE SALUD
		JLabel lblSalud = new JLabel("Gasto de salud:");
		lblSalud.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblSalud.setForeground(Color.BLACK);
		lblSalud.setBounds(30, 485, 300, 25);
		panelContenido.add(lblSalud);

		JLabel lblMedicamento = new JLabel("Medicamento mensual:");
		lblMedicamento.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblMedicamento.setBounds(30, 515, 190, 25);
		panelContenido.add(lblMedicamento);

		txtMedicamentoFecha1 = crearCampoFecha();
		txtMedicamentoFecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtMedicamentoFecha1.setToolTipText("dd/MM/yyyy");
		txtMedicamentoFecha1.setBounds(250, 515, 90, 25);
		panelContenido.add(txtMedicamentoFecha1);

		txtMedicamentoFecha2 = crearCampoFecha();
		txtMedicamentoFecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtMedicamentoFecha2.setToolTipText("dd/MM/yyyy");
		txtMedicamentoFecha2.setBounds(350, 515, 90, 25);
		panelContenido.add(txtMedicamentoFecha2);

		txtMedicamentoFecha3 = crearCampoFecha();
		txtMedicamentoFecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtMedicamentoFecha3.setToolTipText("dd/MM/yyyy");
		txtMedicamentoFecha3.setBounds(450, 515, 90, 25);
		panelContenido.add(txtMedicamentoFecha3);

		// OTROS GASTOS
		JLabel lblOtrosGastos = new JLabel("Otros gastos:");
		lblOtrosGastos.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblOtrosGastos.setForeground(Color.BLACK);
		lblOtrosGastos.setBounds(30, 555, 300, 25);
		panelContenido.add(lblOtrosGastos);

		txtOtroGasto1 = new JTextField();
		txtOtroGasto1.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtOtroGasto1.setBounds(30, 585, 190, 25);
		panelContenido.add(txtOtroGasto1);

		txtOtroGasto1Fecha1 = crearCampoFecha();
		txtOtroGasto1Fecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtOtroGasto1Fecha1.setToolTipText("dd/MM/yyyy");
		txtOtroGasto1Fecha1.setBounds(250, 585, 90, 25);
		panelContenido.add(txtOtroGasto1Fecha1);

		txtOtroGasto1Fecha2 = crearCampoFecha();
		txtOtroGasto1Fecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtOtroGasto1Fecha2.setToolTipText("dd/MM/yyyy");
		txtOtroGasto1Fecha2.setBounds(350, 585, 90, 25);
		panelContenido.add(txtOtroGasto1Fecha2);

		txtOtroGasto1Fecha3 = crearCampoFecha();
		txtOtroGasto1Fecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtOtroGasto1Fecha3.setToolTipText("dd/MM/yyyy");
		txtOtroGasto1Fecha3.setBounds(450, 585, 90, 25);
		panelContenido.add(txtOtroGasto1Fecha3);

		txtOtroGasto2 = new JTextField();
		txtOtroGasto2.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtOtroGasto2.setBounds(30, 615, 190, 25);
		panelContenido.add(txtOtroGasto2);

		txtOtroGasto2Fecha1 = crearCampoFecha();
		txtOtroGasto2Fecha1.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtOtroGasto2Fecha1.setToolTipText("dd/MM/yyyy");
		txtOtroGasto2Fecha1.setBounds(250, 615, 90, 25);
		panelContenido.add(txtOtroGasto2Fecha1);

		txtOtroGasto2Fecha2 = crearCampoFecha();
		txtOtroGasto2Fecha2.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtOtroGasto2Fecha2.setToolTipText("dd/MM/yyyy");
		txtOtroGasto2Fecha2.setBounds(350, 615, 90, 25);
		panelContenido.add(txtOtroGasto2Fecha2);

		txtOtroGasto2Fecha3 = crearCampoFecha();
		txtOtroGasto2Fecha3.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtOtroGasto2Fecha3.setToolTipText("dd/MM/yyyy");
		txtOtroGasto2Fecha3.setBounds(450, 615, 90, 25);
		panelContenido.add(txtOtroGasto2Fecha3);

		// OBSERVACIONES
		JLabel lblObservaciones = new JLabel("Otras observaciones determinadas al momento de la entrevista:");
		lblObservaciones.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblObservaciones.setForeground(Color.BLACK);
		lblObservaciones.setBounds(30, 690, 650, 25);
		panelContenido.add(lblObservaciones);

		txtObservaciones = new JTextArea();
		txtObservaciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);

		JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
		scrollObservaciones.setBounds(30, 725, 600, 130);
		panelContenido.add(scrollObservaciones);

		// RECOMENDACION DE AYUDA
		JLabel lblRecomendacion = new JLabel(
				"¿De acuerdo a lo indagado y observado durante la entrevista, se recomienda la ayuda?");
		lblRecomendacion.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblRecomendacion.setForeground(Color.BLACK);
		lblRecomendacion.setBounds(30, 875, 650, 25);
		panelContenido.add(lblRecomendacion);

		// RECOMENDACIÓN 1
		JLabel lblRecomendacion1 = new JLabel("1.");
		lblRecomendacion1.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblRecomendacion1.setBounds(30, 915, 25, 25);
		panelContenido.add(lblRecomendacion1);

		cmbRecomendacion1 = new JComboBox<String>();
		cmbRecomendacion1.addItem("");
		cmbRecomendacion1.addItem("Sí");
		cmbRecomendacion1.addItem("No");
		cmbRecomendacion1.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbRecomendacion1.setBounds(50, 915, 80, 25);
		panelContenido.add(cmbRecomendacion1);

		JLabel lblFechaRecomendacion1 = new JLabel("Fecha:");
		lblFechaRecomendacion1.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFechaRecomendacion1.setBounds(155, 915, 50, 25);
		panelContenido.add(lblFechaRecomendacion1);

		txtRecomendacion1Fecha = crearCampoFecha();
		txtRecomendacion1Fecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtRecomendacion1Fecha.setToolTipText("dd/MM/yyyy");
		txtRecomendacion1Fecha.setBounds(210, 915, 100, 25);
		panelContenido.add(txtRecomendacion1Fecha);

		// RECOMENDACIÓN 2
		JLabel lblRecomendacion2 = new JLabel("2.");
		lblRecomendacion2.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblRecomendacion2.setBounds(30, 950, 25, 25);
		panelContenido.add(lblRecomendacion2);

		cmbRecomendacion2 = new JComboBox<String>();
		cmbRecomendacion2.addItem("");
		cmbRecomendacion2.addItem("Sí");
		cmbRecomendacion2.addItem("No");
		cmbRecomendacion2.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbRecomendacion2.setBounds(50, 950, 80, 25);
		panelContenido.add(cmbRecomendacion2);

		JLabel lblFechaRecomendacion2 = new JLabel("Fecha:");
		lblFechaRecomendacion2.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFechaRecomendacion2.setBounds(155, 950, 50, 25);
		panelContenido.add(lblFechaRecomendacion2);

		txtRecomendacion2Fecha = crearCampoFecha();
		txtRecomendacion2Fecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtRecomendacion2Fecha.setToolTipText("dd/MM/yyyy");
		txtRecomendacion2Fecha.setBounds(210, 950, 100, 25);
		panelContenido.add(txtRecomendacion2Fecha);

		// RECOMENDACIÓN 3
		JLabel lblRecomendacion3 = new JLabel("3.");
		lblRecomendacion3.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblRecomendacion3.setBounds(30, 985, 25, 25);
		panelContenido.add(lblRecomendacion3);

		cmbRecomendacion3 = new JComboBox<String>();
		cmbRecomendacion3.addItem("");
		cmbRecomendacion3.addItem("Sí");
		cmbRecomendacion3.addItem("No");
		cmbRecomendacion3.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbRecomendacion3.setBounds(50, 985, 80, 25);
		panelContenido.add(cmbRecomendacion3);

		JLabel lblFechaRecomendacion3 = new JLabel("Fecha:");
		lblFechaRecomendacion3.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFechaRecomendacion3.setBounds(155, 985, 50, 25);
		panelContenido.add(lblFechaRecomendacion3);

		txtRecomendacion3Fecha = crearCampoFecha();
		txtRecomendacion3Fecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
		txtRecomendacion3Fecha.setToolTipText("dd/MM/yyyy");
		txtRecomendacion3Fecha.setBounds(210, 985, 100, 25);
		panelContenido.add(txtRecomendacion3Fecha);

		// BOTONES
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setBackground(new Color(255, 255, 255));
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setFocusPainted(false);
		btnCancelar.setBounds(430, 1030, 120, 35);
		panelContenido.add(btnCancelar);

		JButton btnGuardar = new JButton(adendum == null ? "Guardar adendum" : "Guardar cambios");
		btnGuardar.setForeground(new Color(0, 45, 102));
		btnGuardar.setBackground(new Color(255, 255, 255));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBounds(560, 1030, 120, 35);
		panelContenido.add(btnGuardar);

		// EVENTOS
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

		// SCROLL PRINCIPAL
		JPanel panelFormulario = new JPanel();
		panelFormulario.setLayout(null);
		panelFormulario.setPreferredSize(new Dimension(700, 1100));

		getContentPane().setLayout(null);
		getContentPane().add(scrollPane);

	}

	private void cargarDatos() {

		if (adendum == null) {

			return;
		}

		// SERVICIOS PUBLICOS
		cargarFecha(txtElectricidadFecha1, adendum.getElectricidadFecha1());
		cargarFecha(txtElectricidadFecha2, adendum.getElectricidadFecha2());
		cargarFecha(txtElectricidadFecha3, adendum.getElectricidadFecha3());

		cargarFecha(txtAguaFecha1, adendum.getAguaFecha1());
		cargarFecha(txtAguaFecha2, adendum.getAguaFecha2());
		cargarFecha(txtAguaFecha3, adendum.getAguaFecha3());

		cargarFecha(txtTelefonoFecha1, adendum.getTelefonoFecha1());
		cargarFecha(txtTelefonoFecha2, adendum.getTelefonoFecha2());
		cargarFecha(txtTelefonoFecha3, adendum.getTelefonoFecha3());

		cargarFecha(txtInternetFecha1, adendum.getInternetFecha1());
		cargarFecha(txtInternetFecha2, adendum.getInternetFecha2());
		cargarFecha(txtInternetFecha3, adendum.getInternetFecha3());

		cargarFecha(txtCableFecha1, adendum.getCableFecha1());
		cargarFecha(txtCableFecha2, adendum.getCableFecha2());
		cargarFecha(txtCableFecha3, adendum.getCableFecha3());

		// VIVIENDA
		cargarFecha(txtAlquilerFecha1, adendum.getAlquilerPrestamoFecha1());
		cargarFecha(txtAlquilerFecha2, adendum.getAlquilerPrestamoFecha2());
		cargarFecha(txtAlquilerFecha3, adendum.getAlquilerPrestamoFecha3());

		cargarFecha(txtAlimentacionFecha1, adendum.getAlimentacionFecha1());
		cargarFecha(txtAlimentacionFecha2, adendum.getAlimentacionFecha2());
		cargarFecha(txtAlimentacionFecha3, adendum.getAlimentacionFecha3());

		// SALUD
		cargarFecha(txtMedicamentoFecha1, adendum.getMedicamentoFecha1());
		cargarFecha(txtMedicamentoFecha2, adendum.getMedicamentoFecha2());
		cargarFecha(txtMedicamentoFecha3, adendum.getMedicamentoFecha3());

		// OTROS GASTOS
		txtOtroGasto1.setText(adendum.getOtroGasto1() != null ? adendum.getOtroGasto1() : "");

		cargarFecha(txtOtroGasto1Fecha1, adendum.getOtroGasto1Fecha1());
		cargarFecha(txtOtroGasto1Fecha2, adendum.getOtroGasto1Fecha2());
		cargarFecha(txtOtroGasto1Fecha3, adendum.getOtroGasto1Fecha3());

		txtOtroGasto2.setText(adendum.getOtroGasto2() != null ? adendum.getOtroGasto2() : "");

		cargarFecha(txtOtroGasto2Fecha1, adendum.getOtroGasto2Fecha1());
		cargarFecha(txtOtroGasto2Fecha2, adendum.getOtroGasto2Fecha2());
		cargarFecha(txtOtroGasto2Fecha3, adendum.getOtroGasto2Fecha3());

		// OBSERVACIONES
		if (adendum.getObservaciones() != null) {

			txtObservaciones.setText(adendum.getObservaciones());
		}

		// RECOMENDACIONES
		cargarRecomendacion(cmbRecomendacion1, txtRecomendacion1Fecha, adendum.getRecomendacion1(),
				adendum.getRecomendacion1Fecha());

		cargarRecomendacion(cmbRecomendacion2, txtRecomendacion2Fecha, adendum.getRecomendacion2(),
				adendum.getRecomendacion2Fecha());

		cargarRecomendacion(cmbRecomendacion3, txtRecomendacion3Fecha, adendum.getRecomendacion3(),
				adendum.getRecomendacion3Fecha());
	}

	// CARGAR RECOMENDACION
	private void cargarRecomendacion(JComboBox<String> combo, JTextField campoFecha, Boolean recomendacion,
			Date fecha) {

		if (recomendacion == null) {

			combo.setSelectedIndex(0);

		} else if (recomendacion.booleanValue()) {

			combo.setSelectedItem("Sí");

		} else {

			combo.setSelectedItem("No");
		}

		cargarFecha(campoFecha, fecha);
	}

	// GUARDAR
	private void guardar() {

		try {

			if (casoSocial == null) {

				JOptionPane.showMessageDialog(this, "Debe existir un caso social para registrar el adendum.", "Adendum",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			Adendum registro;

			if (adendum == null) {

				registro = new Adendum();

				registro.setFechaRegistro(new Date());

			} else {

				registro = adendum;
			}

			registro.setCasoSocial(casoSocial);

			// SERVICIOS PUBLICOS
			registro.setElectricidadFecha1(convertirFechaOpcional(txtElectricidadFecha1));
			registro.setElectricidadFecha2(convertirFechaOpcional(txtElectricidadFecha2));
			registro.setElectricidadFecha3(convertirFechaOpcional(txtElectricidadFecha3));

			registro.setAguaFecha1(convertirFechaOpcional(txtAguaFecha1));
			registro.setAguaFecha2(convertirFechaOpcional(txtAguaFecha2));
			registro.setAguaFecha3(convertirFechaOpcional(txtAguaFecha3));

			registro.setTelefonoFecha1(convertirFechaOpcional(txtTelefonoFecha1));
			registro.setTelefonoFecha2(convertirFechaOpcional(txtTelefonoFecha2));
			registro.setTelefonoFecha3(convertirFechaOpcional(txtTelefonoFecha3));

			registro.setInternetFecha1(convertirFechaOpcional(txtInternetFecha1));
			registro.setInternetFecha2(convertirFechaOpcional(txtInternetFecha2));
			registro.setInternetFecha3(convertirFechaOpcional(txtInternetFecha3));

			registro.setCableFecha1(convertirFechaOpcional(txtCableFecha1));
			registro.setCableFecha2(convertirFechaOpcional(txtCableFecha2));
			registro.setCableFecha3(convertirFechaOpcional(txtCableFecha3));

			// VIVIENDA
			registro.setAlquilerPrestamoFecha1(convertirFechaOpcional(txtAlquilerFecha1));
			registro.setAlquilerPrestamoFecha2(convertirFechaOpcional(txtAlquilerFecha2));
			registro.setAlquilerPrestamoFecha3(convertirFechaOpcional(txtAlquilerFecha3));
			registro.setAlimentacionFecha1(convertirFechaOpcional(txtAlimentacionFecha1));
			registro.setAlimentacionFecha2(convertirFechaOpcional(txtAlimentacionFecha2));
			registro.setAlimentacionFecha3(convertirFechaOpcional(txtAlimentacionFecha3));

			// SALUD
			registro.setMedicamentoFecha1(convertirFechaOpcional(txtMedicamentoFecha1));
			registro.setMedicamentoFecha2(convertirFechaOpcional(txtMedicamentoFecha2));
			registro.setMedicamentoFecha3(convertirFechaOpcional(txtMedicamentoFecha3));

			// OTROS GASTOS
			registro.setOtroGasto1(txtOtroGasto1.getText().trim());
			registro.setOtroGasto1Fecha1(convertirFechaOpcional(txtOtroGasto1Fecha1));
			registro.setOtroGasto1Fecha2(convertirFechaOpcional(txtOtroGasto1Fecha2));
			registro.setOtroGasto1Fecha3(convertirFechaOpcional(txtOtroGasto1Fecha3));

			registro.setOtroGasto2(txtOtroGasto2.getText().trim());
			registro.setOtroGasto2Fecha1(convertirFechaOpcional(txtOtroGasto2Fecha1));
			registro.setOtroGasto2Fecha2(convertirFechaOpcional(txtOtroGasto2Fecha2));
			registro.setOtroGasto2Fecha3(convertirFechaOpcional(txtOtroGasto2Fecha3));

			// OBSERVACIONES
			registro.setObservaciones(txtObservaciones.getText().trim());

			// RECOMENDACIONES
			registro.setRecomendacion1(obtenerBooleano(cmbRecomendacion1));
			registro.setRecomendacion1Fecha(convertirFechaOpcional(txtRecomendacion1Fecha));

			registro.setRecomendacion2(obtenerBooleano(cmbRecomendacion2));
			registro.setRecomendacion2Fecha(convertirFechaOpcional(txtRecomendacion2Fecha));

			registro.setRecomendacion3(obtenerBooleano(cmbRecomendacion3));
			registro.setRecomendacion3Fecha(convertirFechaOpcional(txtRecomendacion3Fecha));

			// PERSISTENCIA
			if (adendum == null) {

				adendumController.registrar(registro);

				JOptionPane.showMessageDialog(this, "El adendum fue registrado correctamente.", "Adendum",
						JOptionPane.INFORMATION_MESSAGE);

			} else {

				adendumController.actualizar(registro);

				JOptionPane.showMessageDialog(this, "El adendum fue actualizado correctamente.", "Adendum",
						JOptionPane.INFORMATION_MESSAGE);
			}

			guardado = true;

			dispose();

		} catch (IllegalArgumentException e) {

			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible guardar el adendum.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// FORMATO FECHAS
	private JFormattedTextField crearCampoFecha() {

		try {

			MaskFormatter mascara = new MaskFormatter("##/##/####");
			mascara.setPlaceholderCharacter('_');

			JFormattedTextField campo = new JFormattedTextField(mascara);
			campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
			campo.setHorizontalAlignment(JFormattedTextField.CENTER);

			return campo;

		} catch (ParseException e) {

			throw new RuntimeException("No fue posible configurar el campo de fecha.", e);
		}
	}

	private void cargarFecha(JTextField campo, Date fecha) {

		if (campo != null && fecha != null) {

			campo.setText(formatoFecha.format(fecha));
		}
	}

	// CONVERTIR FECHA OPCIONAL
	private Date convertirFechaOpcional(JFormattedTextField campo) {

		try {

			campo.commitEdit();

			String texto = campo.getText().trim();

			if (texto.isEmpty() || texto.contains("_")) {
				return null;
			}

			String[] partes = texto.split("/");

			if (partes.length != 3) {
				return null;
			}

			int dia = Integer.parseInt(partes[0]);
			int mes = Integer.parseInt(partes[1]);
			int anio = Integer.parseInt(partes[2]);

			java.util.Calendar calendario = java.util.Calendar.getInstance();

			calendario.setLenient(false);
			calendario.set(anio, mes - 1, dia, 0, 0, 0);
			calendario.set(java.util.Calendar.MILLISECOND, 0);

			return calendario.getTime();

		} catch (Exception e) {

			throw new IllegalArgumentException(
					"Una de las fechas ingresadas no es válida. Utilice el formato dd/MM/yyyy.");
		}
	}

	// OBTENER BOOLEANO
	private Boolean obtenerBooleano(JComboBox<String> combo) {

		if (combo == null) {

			return null;
		}

		String valor = (String) combo.getSelectedItem();

		if (valor == null || valor.trim().isEmpty()) {

			return null;
		}

		if ("Sí".equals(valor)) {

			return Boolean.TRUE;
		}

		if ("No".equals(valor)) {

			return Boolean.FALSE;
		}

		return null;
	}

	// GUARDADO
	public boolean isGuardado() {
		return guardado;
	}
}