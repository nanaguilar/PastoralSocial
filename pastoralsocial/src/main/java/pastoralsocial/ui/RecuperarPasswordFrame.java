package pastoralsocial.ui;

import pastoralsocial.dao.BitacoraDAO;
import pastoralsocial.dao.UsuarioDAO;
import pastoralsocial.modelo.Bitacora;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.EmailUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RecuperarPasswordFrame extends JFrame {

	private JTextField txtCorreo = new JTextField(15);
	private JTextField txtCodigo = new JTextField(15);
	private JPasswordField txtNuevaClave = new JPasswordField(15);
	private JPasswordField txtConfirmarClave = new JPasswordField(15);

	private JButton btnEnviarCodigo = new JButton("Enviar código");
	private JButton btnValidarCodigo = new JButton("Verificar código");
	private JButton btnCambiarClave = new JButton("Guardar contraseña");

	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private BitacoraDAO bitacoraDAO = new BitacoraDAO();

	private String correoSolicitado;
	private Usuario usuarioEnRecuperacion;

	public RecuperarPasswordFrame() {

		setTitle("SIGCAS - Recuperar contraseña");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(420, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(new CardLayout());

		getContentPane().add(panelCorreo(), "PasoCorreo");
		getContentPane().add(panelCodigo(), "PasoCodigo");
		getContentPane().add(panelClave(), "PasoClave");

		btnEnviarCodigo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				enviarCodigo();
			}
		});

		btnValidarCodigo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				validarCodigo();
			}
		});

		btnCambiarClave.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				cambiarContrasena();
			}
		});
	}

	private JPanel panelCorreo() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel("Recuperar contraseña");
		lblTitulo.setBounds(30, 30, 340, 40);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		panel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				"<html>Para restablecer tu contraseña, ingresá el correo electrónico con el que estás registrado en el sistema.</html>");
		lblDescripcion.setBounds(30, 90, 340, 65);
		lblDescripcion.setForeground(new Color(70, 70, 70));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel.add(lblDescripcion);

		JLabel lblCorreo = new JLabel("Correo electrónico");
		lblCorreo.setBounds(30, 180, 250, 20);
		lblCorreo.setForeground(new Color(0, 45, 102));
		lblCorreo.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(lblCorreo);

		txtCorreo.setBounds(30, 205, 340, 42);
		txtCorreo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel.add(txtCorreo);

		btnEnviarCodigo.setBounds(30, 275, 340, 45);
		btnEnviarCodigo.setBackground(Color.WHITE);
		btnEnviarCodigo.setForeground(new Color(0, 45, 102));
		btnEnviarCodigo.setFocusPainted(false);
		btnEnviarCodigo.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(btnEnviarCodigo);

		JPanel info = new JPanel();
		info.setLayout(null);
		info.setBackground(new Color(240, 247, 255));
		info.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 245)));
		info.setBounds(30, 346, 340, 75);

		JLabel lblInfo = new JLabel(
				"<html>Te enviaremos un código de verificación de 6 dígitos al correo electrónico registrado.</html>");
		lblInfo.setBounds(15, 12, 310, 45);
		lblInfo.setForeground(new Color(0, 45, 102));
		lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		info.add(lblInfo);

		panel.add(info);
		return panel;
	}

	private JPanel panelCodigo() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel("Verificar código");
		lblTitulo.setBounds(30, 30, 340, 40);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		panel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				"<html>Ingrese el código de verificación de 6 dígitos enviado al correo electrónico registrado.</html>");
		lblDescripcion.setBounds(30, 90, 340, 60);
		lblDescripcion.setForeground(new Color(70, 70, 70));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel.add(lblDescripcion);

		JLabel lblCodigo = new JLabel("Código de verificación");
		lblCodigo.setBounds(30, 170, 250, 20);
		lblCodigo.setForeground(new Color(0, 45, 102));
		lblCodigo.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(lblCodigo);

		txtCodigo.setBounds(30, 195, 340, 42);
		txtCodigo.setFont(new Font("SansSerif", Font.PLAIN, 16));
		txtCodigo.setHorizontalAlignment(JTextField.CENTER);
		panel.add(txtCodigo);

		btnValidarCodigo.setBounds(30, 270, 340, 45);
		btnValidarCodigo.setBackground(Color.WHITE);
		btnValidarCodigo.setForeground(new Color(0, 45, 102));
		btnValidarCodigo.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnValidarCodigo.setFocusPainted(false);
		panel.add(btnValidarCodigo);

		JPanel info = new JPanel();
		info.setBackground(new Color(240, 247, 255));
		info.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 245)));
		info.setLayout(null);
		info.setBounds(30, 345, 340, 90);

		JLabel lblInfo = new JLabel(
				"<html>El código enviado tiene una vigencia de <b>15 minutos</b>. Si expiró, deberá solicitar uno nuevo.</html>");

		lblInfo.setBounds(15, 15, 310, 60);
		lblInfo.setForeground(new Color(0, 45, 102));
		lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));

		info.add(lblInfo);
		panel.add(info);

		return panel;
	}

	private JPanel panelClave() {

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel("Nueva contraseña");
		lblTitulo.setBounds(30, 30, 340, 40);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		panel.add(lblTitulo);

		JLabel lblDescripcion = new JLabel(
				"<html>La contraseña debe ser segura y fácil de recordar para usted. Confirme ambos campos para finalizar la recuperación.</html>");
		lblDescripcion.setBounds(30, 90, 340, 65);
		lblDescripcion.setForeground(new Color(70, 70, 70));
		lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel.add(lblDescripcion);

		JLabel lblNueva = new JLabel("Nueva contraseña");
		lblNueva.setBounds(30, 170, 250, 20);
		lblNueva.setForeground(new Color(0, 45, 102));
		lblNueva.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(lblNueva);

		txtNuevaClave.setBounds(30, 195, 340, 42);
		txtNuevaClave.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel.add(txtNuevaClave);

		JLabel lblConfirmar = new JLabel("Confirmar contraseña");
		lblConfirmar.setBounds(30, 255, 250, 20);
		lblConfirmar.setForeground(new Color(0, 45, 102));
		lblConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
		panel.add(lblConfirmar);

		txtConfirmarClave.setBounds(30, 280, 340, 42);
		txtConfirmarClave.setFont(new Font("SansSerif", Font.PLAIN, 14));
		panel.add(txtConfirmarClave);

		btnCambiarClave.setBounds(30, 350, 340, 45);
		btnCambiarClave.setBackground(Color.WHITE);
		btnCambiarClave.setForeground(new Color(0, 45, 102));
		btnCambiarClave.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnCambiarClave.setFocusPainted(false);
		panel.add(btnCambiarClave);

		JPanel info = new JPanel();
		info.setBackground(new Color(240, 247, 255));
		info.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 245)));
		info.setLayout(null);
		info.setBounds(30, 420, 340, 75);

		JLabel lblInfo = new JLabel(
				"<html>Una vez guardada la nueva contraseña podrá iniciar sesión nuevamente en SIGCAS.</html>");

		lblInfo.setBounds(15, 15, 310, 45);
		lblInfo.setForeground(new Color(0, 45, 102));
		lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));

		info.add(lblInfo);
		panel.add(info);

		return panel;
	}

	private void mostrarPaso(String nombre) {
		CardLayout cl = (CardLayout) getContentPane().getLayout();
		cl.show(getContentPane(), nombre);
	}

	private void enviarCodigo() {
		String correo = txtCorreo.getText().trim();

		if (correo.isEmpty()) {
			aviso("Escribí tu correo.");
			return;
		}

		Usuario u;
		try {
			u = usuarioDAO.buscarPorCorreo(correo);
		} catch (RuntimeException e) {
			errorDeBase(e);
			return;
		}

		if (u == null) {
			error("No hay ninguna cuenta activa con ese correo.");
			return;
		}

		String codigo = String.format("%06d", new Random().nextInt(900000) + 100000);

		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.MINUTE, 15);
		Date expiracion = calendario.getTime();

		try {
			if (!usuarioDAO.guardarTokenRecuperacion(correo, codigo, expiracion)) {
				error("No pude generar el código. Intentá de nuevo.");
				return;
			}
		} catch (RuntimeException e) {
			errorDeBase(e);
			return;
		}

		correoSolicitado = correo;

		btnEnviarCodigo.setEnabled(false);
		btnEnviarCodigo.setText("Enviando...");

		new Thread(new Runnable() {

			@Override
			public void run() {

				boolean enviado = EmailUtil.enviarCodigoRecuperacion(correo, codigo);

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						btnEnviarCodigo.setEnabled(true);
						btnEnviarCodigo.setText("Enviar código");

						if (enviado) {
							JOptionPane.showMessageDialog(RecuperarPasswordFrame.this,
									"El código de verificación fue enviado correctamente a:\n\n" + correo
											+ "\n\nEl código tendrá una vigencia de 15 minutos.",
									"Código enviado", JOptionPane.INFORMATION_MESSAGE);
							mostrarPaso("PasoCodigo");
						} else {
							error("No pude enviar el correo.\nRevisá la conexión a internet y volvé a intentar.");
						}
					}
				});
			}
		}).start();
	}

	private void validarCodigo() {
		String codigo = txtCodigo.getText().trim();

		if (codigo.isEmpty()) {
			aviso("Escribí el código que te llegó al correo.");
			return;
		}

		try {
			usuarioEnRecuperacion = usuarioDAO.validarToken(correoSolicitado, codigo);
		} catch (RuntimeException e) {
			errorDeBase(e);
			return;
		}

		if (usuarioEnRecuperacion == null) {
			error("El código es incorrecto o ya se venció.");
			return;
		}

		mostrarPaso("PasoClave");
	}

	private void cambiarContrasena() {
		String nueva = new String(txtNuevaClave.getPassword()).trim();
		String confirmacion = new String(txtConfirmarClave.getPassword()).trim();

		if (nueva.isEmpty() || confirmacion.isEmpty()) {
			aviso("Completá los dos campos.");
			return;
		}

		if (!nueva.equals(confirmacion)) {
			error("Las contraseñas no coinciden.");
			return;
		}

		boolean actualizada;
		try {
			actualizada = usuarioDAO.restablecerConToken(usuarioEnRecuperacion.getIdUsuario(), nueva);
		} catch (RuntimeException e) {
			errorDeBase(e);
			return;
		}

		if (!actualizada) {
			error("No pude actualizar la contraseña.");
			return;
		}

		bitacoraDAO.registrar(Bitacora.RESTABLECER_CLAVE, "Usuario", usuarioEnRecuperacion.getUsername(),
				"Recuperación por código enviado al correo");

		JOptionPane.showMessageDialog(this,
				"La contraseña fue restablecida correctamente.\n\n"
						+ "Ya puede iniciar sesión con su nueva contraseña.",
				"Contraseña actualizada", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	private void aviso(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
	}

	private void error(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void errorDeBase(RuntimeException e) {
		Throwable causa = e;
		while (causa.getCause() != null) {
			causa = causa.getCause();
		}
		JOptionPane.showMessageDialog(this, "No pude conectarme a la base de datos.\n\nDetalle: " + causa.getMessage(),
				"Error de conexión", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
}