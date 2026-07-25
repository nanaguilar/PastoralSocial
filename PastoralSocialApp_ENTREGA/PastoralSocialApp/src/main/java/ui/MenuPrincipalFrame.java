package ui;

import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private Usuario usuarioActivo;

    public MenuPrincipalFrame(Usuario usuario) {
        this.usuarioActivo = usuario;

        setTitle("Pastoral Social - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblBienvenido = new JLabel(
            "Bienvenido, " + usuarioActivo.getNombre(),
            SwingConstants.CENTER);
        lblBienvenido.setFont(new Font("Arial", Font.BOLD, 16));

        // Un único acceso al flujo (Caso Social) para evitar enlaces redundantes.
        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton btnCasosSociales = new JButton("Casos Sociales");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");

        panelBotones.add(btnCasosSociales);
        panelBotones.add(btnCerrarSesion);

        btnCasosSociales.addActionListener(e -> {
            new CasoSocialListFrame().setVisible(true);
        });

        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panel.add(lblBienvenido, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);

        add(panel);
    }

    private void cerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Seguro que querés cerrar sesión?",
            "Cerrar sesión",
            JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
