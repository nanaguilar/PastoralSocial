package pastoralsocial.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import pastoralsocial.controller.CasoSocialController;
import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.controller.AsistenciaController;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.ui.admin.AsistenciasPanel;
import pastoralsocial.ui.admin.CasosSocialesPanel;

public class MenuPrincipalCoordinadorFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel panelContenido;
	private CardLayout cardLayout;

	private Usuario usuarioActual;

	private CasoSocialController casoSocialController;
	private IntegranteFamiliarController integranteFamiliarController;
	private AsistenciaController asistenciaController;

	private JLabel lblCasosValor;
	private JLabel lblFamiliasValor;
	private JLabel lblAsistenciasValor;

	public MenuPrincipalCoordinadorFrame(Usuario usuario) {

		this.usuarioActual = usuario;

		casoSocialController = new CasoSocialController();
		integranteFamiliarController = new IntegranteFamiliarController();
		asistenciaController = new AsistenciaController();

		inicializarInterfaz();

	}

	private void inicializarInterfaz() {

		setTitle("SIGCAS - Sistema Integral de Gestión de Casos Sociales");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// PANEL SUPERIOR
		JPanel panelSuperior = new JPanel();
		panelSuperior.setBackground(Color.WHITE);
		panelSuperior.setBounds(0, 0, 1184, 70);
		panelSuperior.setLayout(null);
		contentPane.add(panelSuperior);

		JLabel lblSigcas = new JLabel("SIGCAS");
		lblSigcas.setForeground(new Color(0, 45, 102));
		lblSigcas.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblSigcas.setBounds(30, 15, 200, 40);
		panelSuperior.add(lblSigcas);

		JLabel lblUsuarioActual = new JLabel();
		lblUsuarioActual.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsuarioActual.setForeground(new Color(0, 45, 102));
		lblUsuarioActual.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblUsuarioActual.setBounds(800, 15, 350, 40);

		if (usuarioActual != null) {

			lblUsuarioActual.setText(usuarioActual.getNombre() + " " + usuarioActual.getApellidos());
		}

		panelSuperior.add(lblUsuarioActual);

		// MENÚ LATERAL
		JPanel panelMenu = new JPanel();
		panelMenu.setBackground(new Color(0, 45, 102));
		panelMenu.setBounds(0, 70, 230, 591);
		panelMenu.setLayout(null);
		contentPane.add(panelMenu);

		// IDENTIDAD DEL SISTEMA
		JLabel lblPastoralSocial = new JLabel("PASTORAL SOCIAL");
		lblPastoralSocial.setForeground(Color.WHITE);
		lblPastoralSocial.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblPastoralSocial.setBounds(30, 25, 180, 30);
		panelMenu.add(lblPastoralSocial);

		JLabel lblSistema = new JLabel("SIGCAS");
		lblSistema.setForeground(new Color(180, 200, 225));
		lblSistema.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblSistema.setBounds(30, 53, 180, 20);
		panelMenu.add(lblSistema);

		// INICIO
		JButton btnInicio = crearBotonMenu("Inicio", 95);
		panelMenu.add(btnInicio);

		// SECCIÓN GESTIÓN
		JLabel lblGestion = new JLabel("GESTIÓN");
		lblGestion.setForeground(new Color(150, 175, 205));
		lblGestion.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblGestion.setBounds(30, 145, 170, 20);
		panelMenu.add(lblGestion);

		JButton btnCasosSociales = crearBotonMenu("Casos sociales", 170);
		panelMenu.add(btnCasosSociales);

		JButton btnGrupoFamiliar = crearBotonMenu("Grupo familiar", 215);
		panelMenu.add(btnGrupoFamiliar);

		JButton btnAsistencias = crearBotonMenu("Asistencias", 260);
		panelMenu.add(btnAsistencias);

		// SECCIÓN ADMINISTRACIÓN
		JLabel lblAdministracion = new JLabel("ADMINISTRACIÓN");
		lblAdministracion.setForeground(new Color(150, 175, 205));
		lblAdministracion.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblAdministracion.setBounds(30, 315, 170, 20);
		panelMenu.add(lblAdministracion);

		JButton btnReportes = crearBotonMenu("Reportes", 340);
		panelMenu.add(btnReportes);

		// ZONA INFERIOR
		JPanel separadorInferior = new JPanel();
		separadorInferior.setBackground(new Color(40, 80, 130));
		separadorInferior.setBounds(20, 475, 190, 1);
		panelMenu.add(separadorInferior);

		JLabel lblRol = new JLabel("Coordinador");
		lblRol.setForeground(new Color(180, 200, 225));
		lblRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblRol.setBounds(30, 485, 170, 20);
		panelMenu.add(lblRol);

		JButton btnCerrarSesion = crearBotonMenu("Cerrar sesión", 515);
		panelMenu.add(btnCerrarSesion);

		// CONTENEDOR PRINCIPAL
		cardLayout = new CardLayout();

		panelContenido = new JPanel();
		panelContenido.setBounds(230, 70, 954, 591);
		panelContenido.setLayout(cardLayout);
		contentPane.add(panelContenido);

		// PANEL INICIO
		JPanel panelInicio = crearPanelInicio();
		CasosSocialesPanel casosSocialesPanel = new CasosSocialesPanel(usuarioActual);

		// MODULOS
		JPanel panelGrupoFamiliar = crearPanelModulo("Grupo familiar",
				"Consulte y administre los integrantes de los grupos familiares.");
		AsistenciasPanel panelAsistencias = new AsistenciasPanel(usuarioActual);
		JPanel panelReportes = crearPanelModulo("Reportes", "Consulte y genere información consolidada del sistema.");

		panelContenido.add(panelInicio, "INICIO");
		panelContenido.add(casosSocialesPanel, "CASOS SOCIALES");
		panelContenido.add(panelGrupoFamiliar, "FAMILIAS");
		panelContenido.add(panelAsistencias, "ASISTENCIAS");
		panelContenido.add(panelReportes, "REPORTES");

		btnInicio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cardLayout.show(panelContenido, "INICIO");
				actualizarIndicadores();
			}
		});

		btnCasosSociales.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cardLayout.show(panelContenido, "CASOS SOCIALES");
			}
		});

		btnGrupoFamiliar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cardLayout.show(panelContenido, "FAMILIAS");
			}
		});

		btnAsistencias.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cardLayout.show(panelContenido, "ASISTENCIAS");
			}
		});

		btnReportes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				cardLayout.show(panelContenido, "REPORTES");
			}
		});

		btnCerrarSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				LoginFrame loginFrame = new LoginFrame();

				loginFrame.setVisible(true);

				dispose();
			}
		});

		// MOSTRAR INICIO CUANDO SE ABRE EL MENU
		cardLayout.show(panelContenido, "INICIO");
		actualizarIndicadores();

	}

	private void actualizarIndicadores() {

		try {

			lblCasosValor.setText(String.valueOf(casoSocialController.contarCasosActivos()));

			lblFamiliasValor.setText(String.valueOf(integranteFamiliarController.contarFamiliasRegistradas()));

			lblAsistenciasValor.setText(String.valueOf(asistenciaController.contarAsistencias()));

		} catch (Exception e) {

			e.printStackTrace();

			lblCasosValor.setText("0");
			lblFamiliasValor.setText("0");
			lblAsistenciasValor.setText("0");
		}
	}

	// BOTONES DEL MENU LATERAL
	private JButton crearBotonMenu(String texto, int posicionY) {

		JButton boton = new JButton(texto);

		boton.setHorizontalAlignment(SwingConstants.LEFT);
		boton.setForeground(Color.WHITE);
		boton.setBackground(new Color(0, 45, 102));
		boton.setFont(new Font("SansSerif", Font.PLAIN, 14));

		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(true);

		boton.setBounds(20, posicionY, 190, 38);

		return boton;
	}

	// PANEL DE CADA MODULO
	private JPanel crearPanelModulo(String titulo, String descripcion) {

		JPanel panel = new JPanel();

		panel.setBackground(new Color(245, 247, 250));
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel(titulo);

		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));

		lblTitulo.setBounds(40, 30, 500, 40);

		panel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(descripcion);

		lblDescripcion.setForeground(new Color(90, 90, 90));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));

		lblDescripcion.setBounds(40, 75, 750, 30);

		panel.add(lblDescripcion);

		return panel;
	}

	// PANEL INICIO
	private JPanel crearPanelInicio() {

		JPanel panelInicio = new JPanel();
		panelInicio.setBackground(new Color(245, 247, 250));
		panelInicio.setLayout(null);

		// TITULO
		JLabel lblTitulo = new JLabel("Inicio");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(40, 30, 150, 40);
		panelInicio.add(lblTitulo);

		// BIENVENIDA
		JLabel lblBienvenida = new JLabel("Bienvenido(a)");
		lblBienvenida.setForeground(new Color(0, 45, 102));
		lblBienvenida.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblBienvenida.setBounds(40, 75, 600, 30);

		if (usuarioActual != null) {
			lblBienvenida.setText(
					"Bienvenido(a), " + usuarioActual.getNombre() + ". Aquí tienes un resumen general del sistema.");
		}

		panelInicio.add(lblBienvenida);

		// TARJETA CASOS ACTIVOS
		JPanel tarjetaCasos = new JPanel();
		tarjetaCasos.setBackground(Color.WHITE);
		tarjetaCasos.setBounds(40, 140, 250, 120);
		tarjetaCasos.setLayout(null);
		panelInicio.add(tarjetaCasos);

		JLabel lblCasosTitulo = new JLabel("Casos activos");
		lblCasosTitulo.setForeground(new Color(90, 90, 90));
		lblCasosTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCasosTitulo.setBounds(20, 18, 210, 25);
		tarjetaCasos.add(lblCasosTitulo);

		lblCasosValor = new JLabel("0");
		lblCasosValor.setForeground(new Color(0, 45, 102));
		lblCasosValor.setFont(new Font("SansSerif", Font.BOLD, 32));
		lblCasosValor.setBounds(20, 55, 210, 40);
		tarjetaCasos.add(lblCasosValor);

		// TARJETA FAMILIAS
		JPanel tarjetaFamilias = new JPanel();
		tarjetaFamilias.setBackground(Color.WHITE);
		tarjetaFamilias.setBounds(330, 140, 250, 120);
		tarjetaFamilias.setLayout(null);
		panelInicio.add(tarjetaFamilias);

		JLabel lblFamiliasTitulo = new JLabel("Familias registradas");
		lblFamiliasTitulo.setForeground(new Color(90, 90, 90));
		lblFamiliasTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblFamiliasTitulo.setBounds(20, 18, 210, 25);
		tarjetaFamilias.add(lblFamiliasTitulo);

		lblFamiliasValor = new JLabel("0");
		lblFamiliasValor.setForeground(new Color(0, 45, 102));
		lblFamiliasValor.setFont(new Font("SansSerif", Font.BOLD, 32));
		lblFamiliasValor.setBounds(20, 55, 210, 40);
		tarjetaFamilias.add(lblFamiliasValor);

		// TARJETA ASISTENCIAS
		JPanel tarjetaAsistencias = new JPanel();
		tarjetaAsistencias.setBackground(Color.WHITE);
		tarjetaAsistencias.setBounds(620, 140, 250, 120);
		tarjetaAsistencias.setLayout(null);
		panelInicio.add(tarjetaAsistencias);

		JLabel lblAsistenciasTitulo = new JLabel("Asistencias registradas");
		lblAsistenciasTitulo.setForeground(new Color(90, 90, 90));
		lblAsistenciasTitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblAsistenciasTitulo.setBounds(20, 18, 210, 25);
		tarjetaAsistencias.add(lblAsistenciasTitulo);

		lblAsistenciasValor = new JLabel("0");
		lblAsistenciasValor.setForeground(new Color(0, 45, 102));
		lblAsistenciasValor.setFont(new Font("SansSerif", Font.BOLD, 32));
		lblAsistenciasValor.setBounds(20, 55, 210, 40);
		tarjetaAsistencias.add(lblAsistenciasValor);

		// ACCESOS RAPIDOS
		JLabel lblAccesos = new JLabel("Accesos rápidos");
		lblAccesos.setForeground(new Color(0, 45, 102));
		lblAccesos.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblAccesos.setBounds(40, 310, 300, 30);
		panelInicio.add(lblAccesos);

		JLabel lblAccesosDescripcion = new JLabel("Accede rápidamente a las principales funciones de gestión.");
		lblAccesosDescripcion.setForeground(new Color(90, 90, 90));
		lblAccesosDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblAccesosDescripcion.setBounds(40, 350, 500, 25);
		panelInicio.add(lblAccesosDescripcion);

		return panelInicio;
	}

}