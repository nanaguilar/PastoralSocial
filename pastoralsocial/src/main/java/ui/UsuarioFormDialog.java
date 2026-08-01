package ui;

import dao.BitacoraDAO;
import dao.RolDAO;
import dao.UsuarioDAO;
import model.Bitacora;
import model.Rol;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// este mismo dialog me sirve para crear y para editar: si le paso un usuario lo
// edita, si le paso null crea uno nuevo. lo hice modal para que no se pueda seguir
// usando la lista de atrás mientras estoy escribiendo.
public class UsuarioFormDialog extends JDialog {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtUsername = new JTextField(15);
    private JTextField txtCorreo = new JTextField(15);
    private JPasswordField txtPassword = new JPasswordField(15);
    private JPasswordField txtConfirmar = new JPasswordField(15);
    private JComboBox<Rol> cmbRol = new JComboBox<Rol>();
    private JCheckBox chkActivo = new JCheckBox("Usuario activo");

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private RolDAO rolDAO = new RolDAO();
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

    private Usuario usuario;          // queda en null cuando estoy creando
    private boolean esNuevo;
    private boolean guardado = false; // la lista lee esto para saber si recarga

    public UsuarioFormDialog(JFrame padre, Usuario usuario) {
        super(padre, true);
        this.usuario = usuario;
        this.esNuevo = (usuario == null);

        setTitle(esNuevo ? "Nuevo usuario" : "Editar usuario");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, esNuevo ? 360 : 290);
        setLocationRelativeTo(padre);
        setResizable(false);

        // uso GridLayout(0,2) para no andar contando filas a mano cada vez que
        // el formulario cambia entre "nuevo" y "editar"
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nombre completo:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsername);

        // el correo lo pido siempre, tanto al crear como al editar: sin correo el
        // usuario no puede usar la recuperación de contraseña. los que ya estaban
        // en la base lo tienen vacío, así que la primera vez que se les edite la
        // ficha hay que llenárselo.
        panel.add(new JLabel("Correo electrónico:"));
        panel.add(txtCorreo);

        // la contraseña solo la pido al crear. para cambiarla después está el
        // botón "Restablecer contraseña" en la lista, así no la muestro de gratis.
        if (esNuevo) {
            panel.add(new JLabel("Contraseña:"));
            panel.add(txtPassword);
            panel.add(new JLabel("Confirmar contraseña:"));
            panel.add(txtConfirmar);
        }

        panel.add(new JLabel("Rol:"));
        panel.add(cmbRol);
        panel.add(new JLabel("Estado:"));
        panel.add(chkActivo);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panel.add(btnGuardar);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);

        cargarRoles();
        if (esNuevo) {
            chkActivo.setSelected(true);
        } else {
            cargarDatosDelUsuario();
        }
    }

    // la lista me pregunta esto al cerrar el dialog para saber si tiene que recargar
    public boolean seGuardo() {
        return guardado;
    }

    private void cargarRoles() {
        List<Rol> roles = rolDAO.listar();
        for (Rol r : roles) {
            cmbRol.addItem(r);
        }
    }

    private void cargarDatosDelUsuario() {
        txtNombre.setText(usuario.getNombre());
        txtUsername.setText(usuario.getUsername());
        // los usuarios viejos tienen el correo en null, no le puedo pasar eso al
        // setText porque me deja el campo con la palabra "null" escrita
        txtCorreo.setText(usuario.getCorreo() == null ? "" : usuario.getCorreo());
        chkActivo.setSelected(usuario.isActivo());

        // no puedo usar setSelectedItem() directo porque los Rol del combo vienen
        // de otra consulta y son objetos distintos aunque sean la misma fila.
        // por eso los comparo por id.
        if (usuario.getRol() != null) {
            for (int i = 0; i < cmbRol.getItemCount(); i++) {
                if (cmbRol.getItemAt(i).getId().equals(usuario.getRol().getId())) {
                    cmbRol.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String correo = txtCorreo.getText().trim();
        Rol rol = (Rol) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || username.isEmpty()) {
            error("El nombre y el usuario son obligatorios.");
            return;
        }

        if (correo.isEmpty()) {
            error("El correo es obligatorio.\n"
                + "Sin correo el usuario no va a poder recuperar su contraseña.");
            return;
        }

        if (!correoValido(correo)) {
            error("Ese correo no tiene un formato válido.");
            return;
        }

        if (rol == null) {
            error("Tenés que asignarle un rol al usuario.");
            return;
        }

        Integer idActual = esNuevo ? null : usuario.getId();
        if (usuarioDAO.existeUsername(username, idActual)) {
            error("Ese nombre de usuario ya existe, elegí otro.");
            return;
        }

        if (usuarioDAO.existeCorreo(correo, idActual)) {
            error("Ese correo ya está registrado en otro usuario.");
            return;
        }

        if (esNuevo) {
            String password = new String(txtPassword.getPassword()).trim();
            String confirmar = new String(txtConfirmar.getPassword()).trim();

            if (password.isEmpty()) {
                error("Tenés que ponerle una contraseña.");
                return;
            }
            if (!password.equals(confirmar)) {
                error("Las contraseñas no coinciden.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setUsername(username);
            nuevo.setCorreo(correo);
            nuevo.setRol(rol);
            nuevo.setActivo(chkActivo.isSelected());
            usuarioDAO.guardarConHash(nuevo, password);
            bitacoraDAO.registrar(Bitacora.CREAR, "Usuario", username,
                "Rol asignado: " + rol.getNombre());

        } else {
            // acá está la regla del profe: siempre tiene que quedar al menos un
            // administrador activo. si este es el último y lo estoy degradando o
            // desactivando, no lo dejo.
            if (dejariaSinAdmins(rol)) {
                error("No podés dejar el sistema sin administradores activos.\n"
                    + "Asigná el rol de Administrador a otro usuario primero.");
                return;
            }

            usuario.setNombre(nombre);
            usuario.setUsername(username);
            usuario.setCorreo(correo);
            usuario.setRol(rol);
            usuario.setActivo(chkActivo.isSelected());
            usuarioDAO.actualizar(usuario);
            bitacoraDAO.registrar(Bitacora.EDITAR, "Usuario", username,
                "Rol: " + rol.getNombre()
                + " / " + (chkActivo.isSelected() ? "activo" : "inactivo"));
        }

        guardado = true;
        JOptionPane.showMessageDialog(this, "Usuario guardado.");
        dispose();
    }

    // me dice si el cambio que estoy por hacer deja el sistema sin ningún admin activo
    private boolean dejariaSinAdmins(Rol rolNuevo) {
        boolean eraAdminActivo = usuario.isActivo() && esAdministrador(usuario.getRol());
        if (!eraAdminActivo) {
            return false;   // si no era admin activo, lo que le haga no quita admins
        }

        boolean sigueSiendoAdminActivo = chkActivo.isSelected() && esAdministrador(rolNuevo);
        if (sigueSiendoAdminActivo) {
            return false;
        }

        return usuarioDAO.contarOtrosAdminsActivos(usuario.getId()) == 0;
    }

    // validación básica: que tenga un solo arroba, algo antes, algo después y un
    // punto en el dominio. no me pongo a hacer la expresión regular completa del
    // estándar porque igual el que confirma que el correo existe es el envío.
    private boolean correoValido(String correo) {
        int arroba = correo.indexOf('@');
        if (arroba <= 0 || arroba != correo.lastIndexOf('@')) {
            return false;
        }
        String dominio = correo.substring(arroba + 1);
        return dominio.contains(".")
            && !dominio.startsWith(".")
            && !dominio.endsWith(".")
            && !correo.contains(" ");
    }

    private boolean esAdministrador(Rol rol) {
        return rol != null && "ADMINISTRADOR".equalsIgnoreCase(rol.getNombre());
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
