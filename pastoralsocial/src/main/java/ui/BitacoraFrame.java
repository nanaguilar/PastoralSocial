package ui;

import dao.BitacoraDAO;
import model.Bitacora;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// consulta de la bitácora, exclusiva del administrador. es solo lectura:
// no hay botones de editar ni de borrar a propósito, si se pudiera modificar
// no serviría de nada para auditar.
public class BitacoraFrame extends JFrame {

    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

    private JTextField txtDesde = new JTextField(10);
    private JTextField txtHasta = new JTextField(10);
    private JTextField txtUsuario = new JTextField(12);
    private JComboBox<String> cmbAccion = new JComboBox<String>();

    private DefaultTableModel modelo = new DefaultTableModel(
            new Object[] { "Fecha y hora", "Usuario", "Acción", "Entidad",
                           "Referencia", "Detalle" }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private JTable tabla = new JTable(modelo);

    private SimpleDateFormat soloFecha = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public BitacoraFrame() {
        setTitle("Pastoral Social - Bitácora de acciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880, 460);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Bitácora de acciones sensibles");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel arriba = new JPanel(new BorderLayout(10, 10));
        arriba.add(titulo, BorderLayout.NORTH);
        arriba.add(panelFiltros(), BorderLayout.SOUTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.add(btnCerrar);

        panel.add(arriba, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(pie, BorderLayout.SOUTH);

        add(panel);

        cargarAcciones();
        recargar();
    }

    private JPanel panelFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        p.add(new JLabel("Desde:"));
        p.add(txtDesde);
        p.add(new JLabel("Hasta:"));
        p.add(txtHasta);
        p.add(new JLabel("Usuario:"));
        p.add(txtUsuario);
        p.add(new JLabel("Acción:"));
        p.add(cmbAccion);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> recargar());
        p.add(btnBuscar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());
        p.add(btnLimpiar);

        p.add(new JLabel("(fechas dd/mm/aaaa)"));
        return p;
    }

    private void cargarAcciones() {
        cmbAccion.addItem("Todas");
        for (String accion : bitacoraDAO.listarAcciones()) {
            cmbAccion.addItem(accion);
        }
    }

    private void recargar() {
        Date desde;
        Date hasta;
        try {
            desde = leerFecha(txtDesde, "fecha \"desde\"");
            hasta = leerFecha(txtHasta, "fecha \"hasta\"");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (desde != null && hasta != null && hasta.before(desde)) {
            JOptionPane.showMessageDialog(this,
                "La fecha \"hasta\" no puede ser anterior a la de \"desde\".",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String accion = (String) cmbAccion.getSelectedItem();
        if ("Todas".equals(accion)) {
            accion = "";   // vacío significa no filtrar por acción
        }

        List<Bitacora> registros = bitacoraDAO.buscar(
            desde, hasta, txtUsuario.getText(), accion);

        modelo.setRowCount(0);
        for (Bitacora b : registros) {
            modelo.addRow(new Object[] {
                (b.getFechaHora() == null) ? "" : fechaHora.format(b.getFechaHora()),
                textoSeguro(b.getUsuario()),
                textoSeguro(b.getAccion()),
                textoSeguro(b.getEntidad()),
                textoSeguro(b.getReferencia()),
                textoSeguro(b.getDetalle())
            });
        }

        if (registros.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay registros que coincidan con esos filtros.");
        }
    }

    private void limpiar() {
        txtDesde.setText("");
        txtHasta.setText("");
        txtUsuario.setText("");
        cmbAccion.setSelectedItem("Todas");
        recargar();
    }

    private Date leerFecha(JTextField campo, String nombre) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            soloFecha.setLenient(false);
            return soloFecha.parse(texto);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                "La " + nombre + " tiene que ir en formato dd/mm/aaaa.");
        }
    }

    private String textoSeguro(String valor) {
        return (valor == null) ? "" : valor;
    }
}
