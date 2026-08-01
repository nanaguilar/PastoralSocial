package ui;

import dao.CatalogoViviendaDAO;
import model.CatalogoSimple;
import model.CondicionVivienda;
import model.Tenencia;
import model.TipoVivienda;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// esta es la pantalla que pidió el profe para que los valores de vivienda se
// puedan agregar, editar y eliminar en vez de estar fijos en un enum.
// las tres pestañas son iguales por dentro, por eso hice una sola clase de panel
// y solo le cambio la entidad con la que trabaja.
public class CatalogosFrame extends JFrame {

    private CatalogoViviendaDAO dao = new CatalogoViviendaDAO();

    public CatalogosFrame() {
        setTitle("Pastoral Social - Catálogos de vivienda");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 420);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Catálogos de vivienda");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Tipo de vivienda", new PanelCatalogo(TipoVivienda.class));
        pestanas.addTab("Tenencia", new PanelCatalogo(Tenencia.class));
        pestanas.addTab("Condición", new PanelCatalogo(CondicionVivienda.class));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(pestanas, BorderLayout.CENTER);
        panel.add(pie, BorderLayout.SOUTH);

        add(panel);
    }

    // ------------------------------------------------------------- utilidades

    private String pedirNombre(Component padre, String titulo, String valorActual) {
        String valor = (String) JOptionPane.showInputDialog(padre, "Nombre:", titulo,
            JOptionPane.PLAIN_MESSAGE, null, null, valorActual);

        if (valor == null) {
            return null;   // le dio cancelar
        }

        valor = valor.trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(padre, "El nombre no puede quedar vacío.");
            return null;
        }
        return valor;
    }

    private void aviso(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // ------------------------------------------------------------ el panel que
    // se repite en las tres pestañas

    private class PanelCatalogo extends JPanel {

        private Class<? extends CatalogoSimple> clase;

        private DefaultListModel<CatalogoSimple> modelo = new DefaultListModel<CatalogoSimple>();
        private JList<CatalogoSimple> lista = new JList<CatalogoSimple>(modelo);

        private PanelCatalogo(Class<? extends CatalogoSimple> clase) {
            super(new BorderLayout(10, 10));
            this.clase = clase;
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            add(new JScrollPane(lista), BorderLayout.CENTER);

            JButton btnAgregar = new JButton("Agregar");
            JButton btnEditar = new JButton("Editar");
            JButton btnEliminar = new JButton("Eliminar");

            btnAgregar.addActionListener(e -> agregar());
            btnEditar.addActionListener(e -> editar());
            btnEliminar.addActionListener(e -> eliminar());

            JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            acciones.add(btnAgregar);
            acciones.add(btnEditar);
            acciones.add(btnEliminar);
            add(acciones, BorderLayout.SOUTH);

            recargar();
        }

        private void recargar() {
            modelo.clear();
            // uso "? extends" porque listar() me devuelve la lista del tipo concreto
            // (TipoVivienda, Tenencia...) y yo la quiero tratar como CatalogoSimple
            List<? extends CatalogoSimple> valores = dao.listar(clase);
            for (CatalogoSimple valor : valores) {
                modelo.addElement(valor);
            }
        }

        private void agregar() {
            String nombre = pedirNombre(this, "Agregar valor", "");
            if (nombre == null) {
                return;
            }
            if (dao.existeNombre(clase, nombre, null)) {
                aviso(this, "Ya existe un valor con ese nombre.");
                return;
            }
            dao.guardar(dao.crear(clase, nombre));
            recargar();
        }

        private void editar() {
            CatalogoSimple sel = lista.getSelectedValue();
            if (sel == null) {
                aviso(this, "Seleccioná un valor de la lista.");
                return;
            }

            String nombre = pedirNombre(this, "Editar valor", sel.getNombre());
            if (nombre == null) {
                return;
            }
            if (dao.existeNombre(clase, nombre, sel.getId())) {
                aviso(this, "Ya existe un valor con ese nombre.");
                return;
            }

            sel.setNombre(nombre);
            dao.guardar(sel);
            recargar();
        }

        private void eliminar() {
            CatalogoSimple sel = lista.getSelectedValue();
            if (sel == null) {
                aviso(this, "Seleccioná un valor de la lista.");
                return;
            }

            // no lo dejo borrar si algún caso lo está usando, si no esos casos
            // quedarían apuntando a una fila que ya no existe
            if (dao.contarUsoEnExpedientes(clase, sel.getId()) > 0) {
                aviso(this, "No podés eliminar \"" + sel.getNombre() + "\".\n"
                    + "Hay casos sociales que lo están usando.");
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Eliminar \"" + sel.getNombre() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            dao.eliminar(clase, sel.getId());
            recargar();
        }
    }
}
