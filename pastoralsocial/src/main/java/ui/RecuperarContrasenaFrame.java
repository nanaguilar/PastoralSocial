package ui;

import dao.BitacoraDAO;
import dao.UsuarioDAO;
import model.Bitacora;
import model.Usuario;
import util.EmailUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

// pantalla de "olvidé mi contraseña". son 3 pasos y los armé con CardLayout para
// no abrir tres ventanas seguidas: pedir el correo, verificar el código que llegó
// y escribir la contraseña nueva.
// esta pantalla se abre SIN sesión iniciada, así que no puede usar nada de Sesion
// ni pedir permisos por rol.
public class RecuperarContrasenaFrame extends JFrame {

    private JTextField txtCorreo = new JTextField(15);
    private JTextField txtCodigo = new JTextField(15);
    private JPasswordField txtNuevaClave = new JPasswordField(15);
    private JPasswordField txtConfirmarClave = new JPasswordField(15);

    private JButton btnEnviarCodigo = new JButton("Enviar código");
    private JButton btnValidarCodigo = new JButton("Verificar código");
    private JButton btnCambiarClave = new JButton("Guardar contraseña");

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

    // me guardo el correo del paso 1 porque el código se valida contra ese correo,
    // no suelto contra toda la tabla
    private String correoSolicitado;
    private Usuario usuarioEnRecuperacion;

    public RecuperarContrasenaFrame() {
        setTitle("Pastoral Social - Recuperar contraseña");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new CardLayout());

        getContentPane().add(panelCorreo(), "PasoCorreo");
        getContentPane().add(panelCodigo(), "PasoCodigo");
        getContentPane().add(panelClave(), "PasoClave");

        btnEnviarCodigo.addActionListener(e -> enviarCodigo());
        btnValidarCodigo.addActionListener(e -> validarCodigo());
        btnCambiarClave.addActionListener(e -> cambiarContrasena());
    }

    // --- paso 1: a qué correo le mando el código ---
    private JPanel panelCorreo() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Ingresá el correo con el que estás registrado:"));
        panel.add(txtCorreo);
        panel.add(btnEnviarCodigo);
        return panel;
    }

    // --- paso 2: el código que llegó al correo ---
    private JPanel panelCodigo() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Escribí el código de 6 dígitos que te llegó:"));
        panel.add(txtCodigo);
        panel.add(btnValidarCodigo);
        return panel;
    }

    // --- paso 3: la contraseña nueva ---
    private JPanel panelClave() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Contraseña nueva:"));
        panel.add(txtNuevaClave);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(txtConfirmarClave);
        panel.add(btnCambiarClave);
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

        // código de 6 dígitos. el +100000 es para que nunca empiece en cero y
        // siempre queden los 6 dígitos completos.
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

        // el envío del correo tarda unos segundos, así que lo hago en otro hilo.
        // si lo dejo en el hilo de Swing la ventana se queda congelada y parece
        // que se colgó. después vuelvo al hilo de la interfaz con invokeLater,
        // que es el único que puede tocar los componentes.
        new Thread(() -> {
            boolean enviado = EmailUtil.enviarCodigoRecuperacion(correo, codigo);
            SwingUtilities.invokeLater(() -> {
                btnEnviarCodigo.setEnabled(true);
                btnEnviarCodigo.setText("Enviar código");

                if (enviado) {
                    JOptionPane.showMessageDialog(this,
                        "Te mandamos un código a " + correo + ".\n"
                        + "Vence en 15 minutos.");
                    mostrarPaso("PasoCodigo");
                } else {
                    error("No pude enviar el correo.\n"
                        + "Revisá la conexión a internet y volvé a intentar.");
                }
            });
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
            // a propósito no le digo cuál de las dos cosas falló
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
            actualizada = usuarioDAO.restablecerConToken(usuarioEnRecuperacion.getId(), nueva);
        } catch (RuntimeException e) {
            errorDeBase(e);
            return;
        }

        if (!actualizada) {
            error("No pude actualizar la contraseña.");
            return;
        }

        // dejo rastro en la bitácora. como no hay sesión iniciada, el usuario del
        // registro queda como "desconocido", por eso aclaro en el detalle que fue
        // el mismo dueño de la cuenta desde la pantalla de recuperación.
        bitacoraDAO.registrar(Bitacora.RESTABLECER_CLAVE, "Usuario",
            usuarioEnRecuperacion.getUsername(),
            "Recuperación por código enviado al correo");

        JOptionPane.showMessageDialog(this,
            "Listo, tu contraseña quedó actualizada.\nYa podés iniciar sesión.");
        dispose();
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atención", JOptionPane.WARNING_MESSAGE);
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // mismo criterio que en el login: bajo hasta la última causa, que es la que
    // de verdad dice qué pasó, en vez de mostrar el envoltorio de JPA
    private void errorDeBase(RuntimeException e) {
        Throwable causa = e;
        while (causa.getCause() != null) {
            causa = causa.getCause();
        }
        JOptionPane.showMessageDialog(this,
            "No pude conectarme a la base de datos.\n\nDetalle: " + causa.getMessage(),
            "Error de conexión", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
