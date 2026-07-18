package pastoralsocial.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

import pastoralsocial.controller.UsuarioController;
import pastoralsocial.modelo.Usuario;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JTextField txtPassword;

	private final UsuarioController usuarioController;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void iniciarSesion() {

		try {
			String username = txtUsuario.getText();
			String password = txtPassword.getText();

			Usuario usuario = usuarioController.login(username, password);

			JOptionPane.showMessageDialog(this, "Bienvenido(a), " + usuario.getNombre() + " " + usuario.getApellidos(),
					"Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);

		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Inicio de sesión", JOptionPane.WARNING_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "No fue posible iniciar sesión.", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {

		usuarioController = new UsuarioController();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 570, 600);
		panel.setBackground(new Color(0, 45, 102));
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPastoralSocial = new JLabel("PASTORAL SOCIAL");
		lblPastoralSocial.setHorizontalAlignment(SwingConstants.LEFT);
		lblPastoralSocial.setForeground(new Color(255, 255, 255));
		lblPastoralSocial.setBounds(75, 65, 300, 30);
		lblPastoralSocial.setFont(new Font("SansSerif", Font.BOLD, 18));
		panel.add(lblPastoralSocial);

		JLabel lblBienvenido = new JLabel("BIENVENIDO A SIGCAS");
		lblBienvenido.setHorizontalAlignment(SwingConstants.LEFT);
		lblBienvenido.setForeground(new Color(255, 255, 255));
		lblBienvenido.setBounds(75, 130, 420, 55);
		lblBienvenido.setFont(new Font("SansSerif", Font.BOLD, 36));
		panel.add(lblBienvenido);

		JLabel lblDescripcion = new JLabel(
				"<html>\r\nSistema de Gestión de Asistencia Social<br><br>\r\nGestión integral de casos sociales,<br>\r\nfamilias y asistencias.\r\n</html>");
		lblDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		lblDescripcion.setForeground(new Color(255, 255, 255));
		lblDescripcion.setBounds(75, 220, 400, 91);
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 15));
		panel.add(lblDescripcion);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(622, 69, 310, 440);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTituloLogin = new JLabel("Iniciar sesión");
		lblTituloLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblTituloLogin.setForeground(new Color(0, 45, 102));
		lblTituloLogin.setFont(new Font("SansSerif", Font.PLAIN, 26));
		lblTituloLogin.setBounds(30, 40, 250, 40);
		panel_1.add(lblTituloLogin);

		JLabel lblIndicacion = new JLabel("Ingrese sus credenciales para continuar");
		lblIndicacion.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblIndicacion.setForeground(new Color(0, 45, 102));
		lblIndicacion.setBounds(30, 80, 250, 25);
		panel_1.add(lblIndicacion);

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblUsuario.setForeground(new Color(0, 45, 102));
		lblUsuario.setBounds(30, 130, 250, 25);
		panel_1.add(lblUsuario);

		txtUsuario = new JTextField();
		txtUsuario.setForeground(new Color(0, 45, 102));
		txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 10));
		txtUsuario.setBounds(30, 160, 250, 40);
		panel_1.add(txtUsuario);
		txtUsuario.setColumns(10);

		JLabel lblPassword = new JLabel("Contraseña");
		lblPassword.setForeground(new Color(0, 45, 102));
		lblPassword.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblPassword.setBounds(30, 220, 250, 25);
		panel_1.add(lblPassword);

		txtPassword = new JTextField();
		txtPassword.setForeground(new Color(0, 45, 102));
		txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 10));
		txtPassword.setBounds(30, 250, 250, 40);
		panel_1.add(txtPassword);
		txtPassword.setColumns(10);

		JButton btnIniciarSesion = new JButton("INICIAR SESIÓN");
		btnIniciarSesion.setBackground(new Color(255, 255, 255));
		btnIniciarSesion.setForeground(new Color(0, 45, 102));
		btnIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnIniciarSesion.setBounds(30, 325, 250, 42);
		panel_1.add(btnIniciarSesion);

		btnIniciarSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarSesion();
			}
		});

	}
}
