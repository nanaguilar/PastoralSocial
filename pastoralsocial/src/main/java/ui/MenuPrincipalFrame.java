package ui;

import model.Usuario;
import util.Sesion;

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

        // le puse 0 filas para que el panel crezca solo según los botones que le
        // agregue, porque la cantidad depende del rol del que entró
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnExpedientes = new JButton("Gestionar expedientes");
        JButton btnCerrarSesion = new JButton("Cerrar sesión");

        panelBotones.add(btnExpedientes);

        // el profe pidió que el botón NO se vea si no sos administrador, no que se
        // vea y después rechace la acción. por eso ni siquiera lo agrego al panel.
        if (Sesion.esAdmin()) {
            JButton btnUsuarios = new JButton("Gestionar usuarios");
            btnUsuarios.addActionListener(e -> new GestionUsuariosFrame().setVisible(true));
            panelBotones.add(btnUsuarios);

            // la bitácora también es exclusiva del administrador
            JButton btnBitacora = new JButton("Bitácora");
            btnBitacora.addActionListener(e -> new BitacoraFrame().setVisible(true));
            panelBotones.add(btnBitacora);
        }

        // los catálogos los administra el admin y el trabajador social.
        // el coordinador es solo lectura, así que a él ni se los muestro.
        if (Sesion.puedeEditar()) {
            JButton btnCatalogos = new JButton("Catálogos de vivienda");
            btnCatalogos.addActionListener(e -> new CatalogosFrame().setVisible(true));
            panelBotones.add(btnCatalogos);

            JButton btnParroquias = new JButton("Parroquias y sectores");
            btnParroquias.addActionListener(e -> new CatalogoParroquiasFrame().setVisible(true));
            panelBotones.add(btnParroquias);
        }

        panelBotones.add(btnCerrarSesion);

        // acá antes salía el mensaje de "próximamente", ahora sí abre el módulo
        btnExpedientes.addActionListener(e -> {
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
            // limpio la sesión para que el siguiente que entre no herede mi rol
            Sesion.cerrar();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}