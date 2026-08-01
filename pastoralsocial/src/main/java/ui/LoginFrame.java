package ui;

import dao.CatalogoViviendaDAO;
import dao.ExpedienteDAO;
import dao.PersonaDAO;
import dao.RolDAO;
import dao.UsuarioDAO;
import model.Usuario;
import util.Sesion;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername = new JTextField(15);
    private JPasswordField txtPassword = new JPasswordField(15);
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginFrame() {
        setTitle("Pastoral Social - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);

        // le quité el botón "Registrarse": el profe pidió que solo el administrador
        // pueda crear usuarios, así que el alta ahora vive en Gestión de usuarios.
        JButton btnLogin = new JButton("Iniciar sesión");

        // este sí lo dejo acá porque tiene que estar disponible ANTES de entrar:
        // es justamente para el que no se acuerda de la contraseña.
        JButton btnOlvide = new JButton("¿Olvidaste tu contraseña?");

        panel.add(btnLogin);
        panel.add(btnOlvide);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnOlvide.addActionListener(e -> new RecuperarContrasenaFrame().setVisible(true));

        add(panel);
    }

    private void iniciarSesion() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingresá usuario y contraseña.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // si la base no responde, el login revienta con una excepción que se sube
        // hasta Swing y solo se ve el stack trace en la consola. la agarro acá para
        // mostrar algo entendible en vez de dejar la ventana como si nada.
        Usuario usuario;
        try {
            usuario = usuarioDAO.login(username, password);
        } catch (RuntimeException e) {
            mostrarErrorDeConexion(e);
            return;
        }

        if (usuario != null) {
            // guardo el usuario en la sesión ANTES de abrir el menú, porque el menú
            // pregunta el rol para decidir qué botones muestra
            Sesion.iniciar(usuario);
            JOptionPane.showMessageDialog(this,
                "Bienvenido, " + usuario.getNombre() + "!");
            new MenuPrincipalFrame(usuario).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // las excepciones de JPA vienen envueltas en varias capas, así que bajo hasta
    // la última causa, que es la que de verdad dice qué pasó (contraseña mala,
    // base que no existe, MySQL apagado...)
    private void mostrarErrorDeConexion(RuntimeException e) {
        Throwable causa = e;
        while (causa.getCause() != null) {
            causa = causa.getCause();
        }

        JOptionPane.showMessageDialog(this,
            "No pude conectarme a la base de datos.\n\n"
            + "Revisá que el servicio de MySQL esté corriendo y que el usuario,\n"
            + "la contraseña y el nombre de la base en persistence.xml sean\n"
            + "los correctos.\n\n"
            + "Detalle: " + causa.getMessage(),
            "Error de conexión", JOptionPane.ERROR_MESSAGE);

        // igual lo dejo en la consola por si necesito el trace completo
        e.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // antes de mostrar el login dejo la base lista: siembro los 3 roles
            // (Hibernate crea la tabla pero vacía) y me aseguro de que exista un
            // administrador, si no nadie podría entrar a gestionar usuarios.
            // también aprovecho para hashear las contraseñas viejas de texto plano.
            try {
                new RolDAO().sembrarSiVacio();
                new CatalogoViviendaDAO().sembrarSiVacio();
                new UsuarioDAO().asegurarAdminInicial();

                // los registros que ya estaban en la base quedaron con las columnas
                // nuevas en NULL, esto se los acomoda una sola vez
                new ExpedienteDAO().normalizarRegistrosViejos();
                new PersonaDAO().normalizarRegistrosViejos();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null,
                    "No pude preparar la base de datos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            new LoginFrame().setVisible(true);
        });
    }
}