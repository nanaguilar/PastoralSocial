package ui;

import dao.BitacoraDAO;
import dao.UsuarioDAO;
import model.Bitacora;
import model.Rol;
import model.Usuario;
import util.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// pantalla exclusiva del administrador. cubre registrar, editar, restablecer
// contraseña y activar/desactivar en un solo lugar, para no hacer 4 pantallas
// casi iguales.
public class GestionUsuariosFrame extends JFrame {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

    // me guardo la lista aparte porque la tabla solo me devuelve el número de
    // fila, y así saco el usuario completo sin volver a consultar la base
    private List<Usuario> usuarios;

    private DefaultTableModel modelo = new DefaultTableModel(
            new Object[] { "ID", "Nombre", "Usuario", "Correo", "Rol", "Estado" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;   // la tabla es solo para ver, se edita con los botones
        }
    };

    private JTable tabla = new JTable(modelo);

    public GestionUsuariosFrame() {
        setTitle("Pastoral Social - Gestión de usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Usuarios del sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnPassword = new JButton("Restablecer contraseña");
        JButton btnEstado = new JButton("Activar / Desactivar");
        JButton btnCerrar = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> nuevo());
        btnEditar.addActionListener(e -> editar());
        btnPassword.addActionListener(e -> restablecerPassword());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnCerrar.addActionListener(e -> dispose());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        acciones.add(btnNuevo);
        acciones.add(btnEditar);
        acciones.add(btnPassword);
        acciones.add(btnEstado);
        acciones.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);

        add(panel);

        recargar();
    }

    private void recargar() {
        usuarios = usuarioDAO.listar();
        modelo.setRowCount(0);

        for (Usuario u : usuarios) {
            String nombreRol = (u.getRol() == null) ? "(sin rol)" : u.getRol().getNombre();
            String estado = u.isActivo() ? "Activo" : "Inactivo";
            // así se ve de una cuáles usuarios todavía no pueden recuperar su clave
            String correo = (u.getCorreo() == null || u.getCorreo().trim().isEmpty())
                          ? "(sin correo)" : u.getCorreo();
            modelo.addRow(new Object[] { u.getId(), u.getNombre(), u.getUsername(),
                                         correo, nombreRol, estado });
        }
    }

    // devuelve el usuario de la fila seleccionada, o null si no hay ninguna
    private Usuario seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un usuario de la lista.");
            return null;
        }
        return usuarios.get(fila);
    }

    private void nuevo() {
        UsuarioFormDialog dialog = new UsuarioFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.seGuardo()) {
            recargar();
        }
    }

    private void editar() {
        Usuario u = seleccionado();
        if (u == null) {
            return;
        }
        UsuarioFormDialog dialog = new UsuarioFormDialog(this, u);
        dialog.setVisible(true);
        if (dialog.seGuardo()) {
            recargar();
        }
    }

    private void restablecerPassword() {
        Usuario u = seleccionado();
        if (u == null) {
            return;
        }

        // armo un panelito con los dos campos para pedir la contraseña y la
        // confirmación en un solo JOptionPane, en vez de abrir dos seguidos
        JPasswordField txtNueva = new JPasswordField(15);
        JPasswordField txtConfirmar = new JPasswordField(15);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Contraseña nueva:"));
        panel.add(txtNueva);
        panel.add(new JLabel("Confirmar:"));
        panel.add(txtConfirmar);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
            "Restablecer contraseña de " + u.getUsername(),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        String nueva = new String(txtNueva.getPassword()).trim();
        String confirmar = new String(txtConfirmar.getPassword()).trim();

        if (nueva.isEmpty()) {
            error("La contraseña no puede quedar vacía.");
            return;
        }
        if (!nueva.equals(confirmar)) {
            error("Las contraseñas no coinciden.");
            return;
        }

        usuarioDAO.cambiarPassword(u.getId(), nueva);
        // no guardo la contraseña en la bitácora, obvio: solo que se restableció
        bitacoraDAO.registrar(Bitacora.RESTABLECER_CLAVE, "Usuario",
            u.getUsername(), null);

        JOptionPane.showMessageDialog(this,
            "Contraseña actualizada. Ya se la podés entregar al usuario.");
    }

    private void cambiarEstado() {
        Usuario u = seleccionado();
        if (u == null) {
            return;
        }

        boolean nuevoEstado = !u.isActivo();

        // misma regla del profe: no puedo desactivar al último administrador
        if (!nuevoEstado && u.isActivo() && esAdministrador(u.getRol())
                && usuarioDAO.contarOtrosAdminsActivos(u.getId()) == 0) {
            error("No podés desactivar al único administrador activo.\n"
                + "Dejá otro usuario con rol Administrador antes.");
            return;
        }

        // ojo: si me desactivo a mí mismo me quedo sin poder hacer nada,
        // así que al menos aviso antes
        if (!nuevoEstado && Sesion.getUsuarioActivo() != null
                && u.getId().equals(Sesion.getUsuarioActivo().getId())) {
            int r = JOptionPane.showConfirmDialog(this,
                "Te estás desactivando a vos mismo.\n"
                + "No vas a poder volver a entrar con este usuario. ¿Seguro?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) {
                return;
            }
        }

        usuarioDAO.cambiarEstado(u.getId(), nuevoEstado);
        bitacoraDAO.registrar(nuevoEstado ? Bitacora.ACTIVAR : Bitacora.DESACTIVAR,
            "Usuario", u.getUsername(), null);

        recargar();
    }

    private boolean esAdministrador(Rol rol) {
        return rol != null && "ADMINISTRADOR".equalsIgnoreCase(rol.getNombre());
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
