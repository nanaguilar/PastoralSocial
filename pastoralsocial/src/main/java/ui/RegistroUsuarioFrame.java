package ui;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class RegistroUsuarioFrame extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtUsername = new JTextField(15);
    private JPasswordField txtPassword = new JPasswordField(15);
    private JPasswordField txtConfirmar = new JPasswordField(15);
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public RegistroUsuarioFrame() {
        setTitle("Registrar nuevo usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nombre completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(txtConfirmar);

        JButton btnGuardar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmar = new String(txtConfirmar.getPassword()).trim();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Todos los campos son obligatorios.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmar)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuarioDAO.existeUsername(username)) {
            JOptionPane.showMessageDialog(this,
                "Ese nombre de usuario ya existe, elegí otro.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setUsername(username);
        nuevo.setPassword(password);
        nuevo.setActivo(true);

        usuarioDAO.guardar(nuevo);

        JOptionPane.showMessageDialog(this,
            "Usuario registrado con éxito. Ya podés iniciar sesión.");
        dispose();
    }
}