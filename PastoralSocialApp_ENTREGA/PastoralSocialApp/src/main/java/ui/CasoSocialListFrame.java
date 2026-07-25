package ui;

import dao.ExpedienteDAO;
import model.Expediente;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Único punto de acceso al módulo "Caso Social": lista los casos ya registrados
 * para reabrirlos y editarlos (reutilizando su información) en lugar de crearlos
 * de nuevo. Concentra aquí las acciones del flujo para no duplicar enlaces.
 */
public class CasoSocialListFrame extends JFrame {

    private ExpedienteDAO expedienteDAO = new ExpedienteDAO();
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    private List<Expediente> casos = new ArrayList<>();
    private CasosTableModel tableModel = new CasosTableModel();
    private JTable tabla = new JTable(tableModel);

    public CasoSocialListFrame() {
        setTitle("Pastoral Social - Casos Sociales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Casos Sociales");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnNuevo = new JButton("Nuevo caso");
        JButton btnEditar = new JButton("Abrir / Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCatalogo = new JButton("Parroquias y sectores");
        JButton btnVolver = new JButton("Volver");

        btnNuevo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnCatalogo.addActionListener(e -> new CatalogoParroquiasFrame().setVisible(true));
        btnVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCatalogo);
        panelBotones.add(btnVolver);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
        recargar();
    }

    private void abrirFormulario(Integer id) {
        setEnabled(false);
        CasoSocialFormFrame form = new CasoSocialFormFrame(id, () -> {
            setEnabled(true);
            toFront();
            recargar();
        });
        form.setVisible(true);
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un caso social.");
            return;
        }
        abrirFormulario(casos.get(fila).getId());
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un caso social.");
            return;
        }
        Expediente caso = casos.get(fila);
        int r = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el caso social N° " + caso.getNumeroFicha() + " y sus familiares?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            expedienteDAO.eliminar(caso.getId());
            recargar();
        }
    }

    private void recargar() {
        casos = expedienteDAO.listar();
        tableModel.fireTableDataChanged();
    }

    private class CasosTableModel extends AbstractTableModel {
        private String[] cols = {"N° ficha", "Parroquia", "Sector", "Familiares", "Fecha inicio"};

        public int getRowCount() { return casos.size(); }
        public int getColumnCount() { return cols.length; }
        public String getColumnName(int c) { return cols[c]; }

        public Object getValueAt(int r, int c) {
            Expediente e = casos.get(r);
            switch (c) {
                case 0: return e.getNumeroFicha();
                case 1: return e.getParroquia() != null ? e.getParroquia().getNombre() : "";
                case 2: return e.getSectorFilial() != null ? e.getSectorFilial().getNombre() : "";
                case 3: return e.getPersonas() != null ? e.getPersonas().size() : 0;
                case 4: return e.getFechaInicio() != null ? fmt.format(e.getFechaInicio()) : "";
                default: return "";
            }
        }
    }
}
