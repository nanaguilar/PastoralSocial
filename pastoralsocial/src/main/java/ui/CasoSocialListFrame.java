package ui;

import dao.BitacoraDAO;
import dao.ExpedienteDAO;
import model.Bitacora;
import model.Expediente;
import util.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// lista de casos sociales con búsqueda y filtro por estado.
// los botones que modifican algo se los escondo al Coordinador, que es solo lectura.
public class CasoSocialListFrame extends JFrame {

    private ExpedienteDAO expedienteDAO = new ExpedienteDAO();
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

    // igual que en la lista de usuarios: me guardo los objetos aparte porque la
    // tabla solo me da el número de fila
    private List<Expediente> casos;

    private JTextField txtBuscar = new JTextField(18);
    private JComboBox<String> cmbEstado = new JComboBox<String>(
        new String[] { "Todos", Expediente.ESTADO_ACTIVO, Expediente.ESTADO_CERRADO });

    private DefaultTableModel modelo = new DefaultTableModel(
            new Object[] { "Ficha", "Fecha inicio", "Parroquia", "Sector",
                           "Entrevistador", "Estado" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private JTable tabla = new JTable(modelo);

    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public CasoSocialListFrame() {
        setTitle("Pastoral Social - Casos sociales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 460);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Casos sociales");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel arriba = new JPanel(new BorderLayout(10, 10));
        arriba.add(titulo, BorderLayout.NORTH);
        arriba.add(panelBusqueda(), BorderLayout.SOUTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(arriba, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(panelAcciones(), BorderLayout.SOUTH);

        add(panel);

        recargar();
    }

    private JPanel panelBusqueda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        p.add(new JLabel("Buscar:"));
        p.add(txtBuscar);
        p.add(new JLabel("Estado:"));
        p.add(cmbEstado);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> recargar());
        p.add(btnBuscar);

        // con Enter en el campo también busca, es más cómodo que ir al botón
        txtBuscar.addActionListener(e -> recargar());

        JLabel ayuda = new JLabel("(por ficha, parroquia o entrevistador)");
        p.add(ayuda);
        return p;
    }

    private JPanel panelAcciones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // el Coordinador solo consulta, así que a él solo le muestro "Consultar"
        if (Sesion.puedeEditar()) {
            JButton btnNuevo = new JButton("Nuevo caso");
            btnNuevo.addActionListener(e -> nuevo());
            p.add(btnNuevo);
        }

        JButton btnAbrir = new JButton(Sesion.puedeEditar() ? "Ver / Editar" : "Consultar");
        btnAbrir.addActionListener(e -> abrir());
        p.add(btnAbrir);

        if (Sesion.puedeEditar()) {
            JButton btnCerrarCaso = new JButton("Cerrar caso");
            btnCerrarCaso.addActionListener(e -> cerrarCaso());
            p.add(btnCerrarCaso);
        }

        // reabrir es solo del administrador
        if (Sesion.esAdmin()) {
            JButton btnReabrir = new JButton("Reabrir caso");
            btnReabrir.addActionListener(e -> reabrirCaso());
            p.add(btnReabrir);
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        p.add(btnCerrar);
        return p;
    }

    private void recargar() {
        String texto = txtBuscar.getText().trim();

        // "Todos" significa no filtrar por estado, por eso lo mando vacío
        String estado = (String) cmbEstado.getSelectedItem();
        if ("Todos".equals(estado)) {
            estado = "";
        }

        casos = expedienteDAO.buscar(texto, estado);
        modelo.setRowCount(0);

        for (Expediente e : casos) {
            modelo.addRow(new Object[] {
                e.getNumeroFicha(),
                formatear(e.getFechaInicio()),
                e.getParroquia() == null ? "" : e.getParroquia().getNombre(),
                e.getSectorFilial() == null ? "" : e.getSectorFilial().getNombre(),
                textoSeguro(e.getEntrevistador()),
                textoSeguro(e.getEstado())
            });
        }
    }

    private Expediente seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un caso de la lista.");
            return null;
        }
        return casos.get(fila);
    }

    private void nuevo() {
        CasoSocialFormDialog dialog = new CasoSocialFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.seGuardo()) {
            recargar();
        }
    }

    private void abrir() {
        Expediente e = seleccionado();
        if (e == null) {
            return;
        }
        CasoSocialFormDialog dialog = new CasoSocialFormDialog(this, e);
        dialog.setVisible(true);
        if (dialog.seGuardo()) {
            recargar();
        }
    }

    private void cerrarCaso() {
        Expediente e = seleccionado();
        if (e == null) {
            return;
        }
        if (!e.estaActivo()) {
            aviso("Ese caso ya está cerrado.");
            return;
        }

        String motivo = JOptionPane.showInputDialog(this,
            "¿Por qué se cierra el caso " + e.getNumeroFicha() + "?",
            "Cerrar caso", JOptionPane.PLAIN_MESSAGE);

        if (motivo == null) {
            return;   // le dio cancelar
        }
        motivo = motivo.trim();
        if (motivo.isEmpty()) {
            aviso("Tenés que escribir el motivo del cierre.");
            return;
        }

        expedienteDAO.cerrar(e.getId(), motivo);
        bitacoraDAO.registrar(Bitacora.CERRAR, "Caso social",
            e.getNumeroFicha(), "Motivo: " + motivo);

        recargar();
        JOptionPane.showMessageDialog(this,
            "Caso cerrado. El historial se conserva completo.");
    }

    private void reabrirCaso() {
        Expediente e = seleccionado();
        if (e == null) {
            return;
        }
        if (e.estaActivo()) {
            aviso("Ese caso ya está activo.");
            return;
        }

        String justificacion = JOptionPane.showInputDialog(this,
            "¿Por qué se reabre el caso " + e.getNumeroFicha() + "?",
            "Reabrir caso", JOptionPane.PLAIN_MESSAGE);

        if (justificacion == null) {
            return;
        }
        justificacion = justificacion.trim();
        if (justificacion.isEmpty()) {
            aviso("Tenés que escribir la justificación.");
            return;
        }

        // reabro el mismo caso, no creo uno nuevo, así no se duplica el historial
        expedienteDAO.reabrir(e.getId(), justificacion);
        bitacoraDAO.registrar(Bitacora.REABRIR, "Caso social",
            e.getNumeroFicha(), "Justificación: " + justificacion);

        recargar();
        JOptionPane.showMessageDialog(this, "Caso reabierto.");
    }

    private String formatear(Date fecha) {
        return (fecha == null) ? "" : formato.format(fecha);
    }

    private String textoSeguro(String valor) {
        return (valor == null) ? "" : valor;
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
