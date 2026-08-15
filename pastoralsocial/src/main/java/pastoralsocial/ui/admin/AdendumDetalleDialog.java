package pastoralsocial.ui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pastoralsocial.modelo.Adendum;

public class AdendumDetalleDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final Adendum adendum;

	private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

	public AdendumDetalleDialog(Window parent, Adendum adendum) {

		super(parent, "Detalle del adendum", ModalityType.APPLICATION_MODAL);

		this.adendum = adendum;

		inicializarInterfaz();
	}

	private void inicializarInterfaz() {

		setSize(760, 700);
		setLocationRelativeTo(getParent());
		setResizable(false);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 247, 250));
		panel.setLayout(null);

		// TITULO

		JLabel lblTitulo = new JLabel("Detalle del adendum");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblTitulo.setBounds(35, 25, 500, 35);
		panel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel("Información complementaria registrada para el caso social.");
		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblDescripcion.setBounds(35, 65, 600, 25);
		panel.add(lblDescripcion);

		// CASO SOCIAL

		JLabel lblCaso = new JLabel("Caso social:");
		lblCaso.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblCaso.setBounds(35, 110, 120, 25);
		panel.add(lblCaso);

		String numeroFicha = "";

		if (adendum != null && adendum.getCasoSocial() != null) {
			numeroFicha = adendum.getCasoSocial().getNumeroFicha();
		}

		JLabel valorCaso = new JLabel(numeroFicha);
		valorCaso.setFont(new Font("SansSerif", Font.PLAIN, 13));
		valorCaso.setForeground(new Color(0, 45, 102));
		valorCaso.setBounds(180, 110, 400, 25);
		panel.add(valorCaso);

		// FECHA

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblFecha.setBounds(35, 145, 120, 25);
		panel.add(lblFecha);

		JLabel valorFecha = new JLabel(adendum != null ? formatearFecha(adendum.getFechaRegistro()) : "");
		valorFecha.setFont(new Font("SansSerif", Font.PLAIN, 13));
		valorFecha.setBounds(180, 145, 400, 25);
		panel.add(valorFecha);

		// AREA DE INFORMACION

		JTextArea area = new JTextArea();
		area.setFont(new Font("SansSerif", Font.PLAIN, 13));
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setBackground(Color.WHITE);
		area.setMargin(new java.awt.Insets(10, 10, 10, 10));

		area.setText(construirDetalle());

		JScrollPane scroll = new JScrollPane(area);
		scroll.setBounds(35, 190, 680, 390);
		panel.add(scroll);

		// BOTON CERRAR

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setForeground(new Color(0, 45, 102));
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCerrar.setFocusPainted(false);
		btnCerrar.setBounds(555, 600, 160, 40);
		panel.add(btnCerrar);

		btnCerrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});

		setContentPane(panel);
	}

	private String construirDetalle() {

		if (adendum == null) {
			return "No existe información del adendum.";
		}

		StringBuilder texto = new StringBuilder();

		texto.append("SERVICIOS PÚBLICOS\n");
		texto.append("----------------------------------------\n");

		texto.append("Electricidad: ").append(fechas(adendum.getElectricidadFecha1(), adendum.getElectricidadFecha2(),
				adendum.getElectricidadFecha3())).append("\n");

		texto.append("Agua: ").append(fechas(adendum.getAguaFecha1(), adendum.getAguaFecha2(), adendum.getAguaFecha3()))
				.append("\n");

		texto.append("Teléfono: ")
				.append(fechas(adendum.getTelefonoFecha1(), adendum.getTelefonoFecha2(), adendum.getTelefonoFecha3()))
				.append("\n");

		texto.append("Internet: ")
				.append(fechas(adendum.getInternetFecha1(), adendum.getInternetFecha2(), adendum.getInternetFecha3()))
				.append("\n");

		texto.append("Cable: ")
				.append(fechas(adendum.getCableFecha1(), adendum.getCableFecha2(), adendum.getCableFecha3()))
				.append("\n\n");

		texto.append("GASTOS DE VIVIENDA\n");
		texto.append("----------------------------------------\n");

		texto.append("Alquiler o préstamo: ").append(fechas(adendum.getAlquilerPrestamoFecha1(),
				adendum.getAlquilerPrestamoFecha2(), adendum.getAlquilerPrestamoFecha3())).append("\n");

		texto.append("Alimentación: ").append(fechas(adendum.getAlimentacionFecha1(), adendum.getAlimentacionFecha2(),
				adendum.getAlimentacionFecha3())).append("\n\n");

		texto.append("GASTO DE SALUD\n");
		texto.append("----------------------------------------\n");

		texto.append("Medicamento mensual: ").append(
				fechas(adendum.getMedicamentoFecha1(), adendum.getMedicamentoFecha2(), adendum.getMedicamentoFecha3()))
				.append("\n\n");

		texto.append("OTROS GASTOS\n");
		texto.append("----------------------------------------\n");

		texto.append("1. ").append(valor(adendum.getOtroGasto1())).append("\n");

		texto.append("   Fechas: ").append(
				fechas(adendum.getOtroGasto1Fecha1(), adendum.getOtroGasto1Fecha2(), adendum.getOtroGasto1Fecha3()))
				.append("\n");

		texto.append("2. ").append(valor(adendum.getOtroGasto2())).append("\n");

		texto.append("   Fechas: ").append(
				fechas(adendum.getOtroGasto2Fecha1(), adendum.getOtroGasto2Fecha2(), adendum.getOtroGasto2Fecha3()))
				.append("\n\n");

		texto.append("OBSERVACIONES\n");
		texto.append("----------------------------------------\n");
		texto.append(valor(adendum.getObservaciones())).append("\n\n");

		texto.append("RECOMENDACIÓN DE AYUDA\n");
		texto.append("----------------------------------------\n");

		texto.append("1. ").append(recomendacion(adendum.getRecomendacion1(), adendum.getRecomendacion1Fecha()))
				.append("\n");

		texto.append("2. ").append(recomendacion(adendum.getRecomendacion2(), adendum.getRecomendacion2Fecha()))
				.append("\n");

		texto.append("3. ").append(recomendacion(adendum.getRecomendacion3(), adendum.getRecomendacion3Fecha()))
				.append("\n");

		return texto.toString();
	}

	private String fechas(Date fecha1, Date fecha2, Date fecha3) {

		StringBuilder resultado = new StringBuilder();

		boolean existe = false;

		if (fecha1 != null) {
			resultado.append(formatearFecha(fecha1));
			existe = true;
		}

		if (fecha2 != null) {

			if (existe) {
				resultado.append(" | ");
			}

			resultado.append(formatearFecha(fecha2));
			existe = true;
		}

		if (fecha3 != null) {

			if (existe) {
				resultado.append(" | ");
			}

			resultado.append(formatearFecha(fecha3));
			existe = true;
		}

		if (!existe) {
			return "Sin registros";
		}

		return resultado.toString();
	}

	private String recomendacion(Boolean recomendacion, Date fecha) {

		if (recomendacion == null) {
			return "No especificada";
		}

		String resultado = recomendacion.booleanValue() ? "Sí" : "No";

		if (fecha != null) {
			resultado += " - Fecha: " + formatearFecha(fecha);
		}

		return resultado;
	}

	private String valor(String texto) {

		if (texto == null || texto.trim().isEmpty()) {
			return "Sin información";
		}

		return texto;
	}

	private String formatearFecha(Date fecha) {

		if (fecha == null) {
			return "Sin fecha";
		}

		return formato.format(fecha);
	}
}