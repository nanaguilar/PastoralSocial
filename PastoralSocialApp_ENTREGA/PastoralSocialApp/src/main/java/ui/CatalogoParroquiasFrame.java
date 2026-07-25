package ui;

import dao.ParroquiaDAO;
import model.Parroquia;
import model.SectorFilial;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Mantenimiento del catálogo de parroquias y sus sectores/filiales.
 *
 * <p>Pantalla maestro-detalle: a la izquierda las parroquias (maestro) y a la
 * derecha los sectores de la parroquia seleccionada (detalle). Alimenta los
 * combos del Caso Social y evita valores libres duplicados.
 */
public class CatalogoParroquiasFrame extends JFrame {

    private ParroquiaDAO dao = new ParroquiaDAO();

    private DefaultListModel<Parroquia> modeloParroquias = new DefaultListModel<>();
    private JList<Parroquia> listaParroquias = new JList<>(modeloParroquias);

    private DefaultListModel<SectorFilial> modeloSectores = new DefaultListModel<>();
    private JList<SectorFilial> listaSectores = new JList<>(modeloSectores);

    public CatalogoParroquiasFrame() {
        setTitle("Pastoral Social - Parroquias y sectores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 420);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Catálogo de parroquias y sectores");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
        centro.add(panelParroquias());
        centro.add(panelSectores());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        panel.add(pie, BorderLayout.SOUTH);
        add(panel);

        listaParroquias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaParroquias.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) recargarSectores();
        });
        listaSectores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        recargarParroquias();
    }

    // ------------------------------------------------------------------ Maestro

    private JPanel panelParroquias() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createTitledBorder("Parroquias"));
        p.add(new JScrollPane(listaParroquias), BorderLayout.CENTER);

        JButton btnNueva = new JButton("Nueva");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        btnNueva.addActionListener(e -> nuevaParroquia());
        btnEditar.addActionListener(e -> editarParroquia());
        btnEliminar.addActionListener(e -> eliminarParroquia());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        acciones.add(btnNueva);
        acciones.add(btnEditar);
        acciones.add(btnEliminar);
        p.add(acciones, BorderLayout.SOUTH);
        return p;
    }

    private void recargarParroquias() {
        Parroquia sel = listaParroquias.getSelectedValue();
        modeloParroquias.clear();
        for (Parroquia p : dao.listar()) {
            modeloParroquias.addElement(p);
        }
        if (sel != null) {
            for (int i = 0; i < modeloParroquias.size(); i++) {
                if (modeloParroquias.get(i).getId().equals(sel.getId())) {
                    listaParroquias.setSelectedIndex(i);
                    break;
                }
            }
        }
        recargarSectores();
    }

    private void nuevaParroquia() {
        String nombre = pedirNombre("Nueva parroquia", "");
        if (nombre == null) return;
        if (dao.existeParroquia(nombre, null)) {
            avisoDuplicado();
            return;
        }
        dao.guardarParroquia(new Parroquia(nombre));
        recargarParroquias();
    }

    private void editarParroquia() {
        Parroquia sel = listaParroquias.getSelectedValue();
        if (sel == null) { avisoSeleccion("una parroquia"); return; }
        String nombre = pedirNombre("Editar parroquia", sel.getNombre());
        if (nombre == null) return;
        if (dao.existeParroquia(nombre, sel.getId())) {
            avisoDuplicado();
            return;
        }
        sel.setNombre(nombre);
        dao.guardarParroquia(sel);
        recargarParroquias();
    }

    private void eliminarParroquia() {
        Parroquia sel = listaParroquias.getSelectedValue();
        if (sel == null) { avisoSeleccion("una parroquia"); return; }
        int r = JOptionPane.showConfirmDialog(this,
            "¿Eliminar la parroquia \"" + sel.getNombre() + "\" y todos sus sectores?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            dao.eliminarParroquia(sel.getId());
            recargarParroquias();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                "No se puede eliminar: la parroquia o alguno de sus sectores\n"
                + "está siendo usado por un caso social.",
                "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ------------------------------------------------------------------ Detalle

    private JPanel panelSectores() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createTitledBorder("Sectores / filiales"));
        p.add(new JScrollPane(listaSectores), BorderLayout.CENTER);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        btnNuevo.addActionListener(e -> nuevoSector());
        btnEditar.addActionListener(e -> editarSector());
        btnEliminar.addActionListener(e -> eliminarSector());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        acciones.add(btnNuevo);
        acciones.add(btnEditar);
        acciones.add(btnEliminar);
        p.add(acciones, BorderLayout.SOUTH);
        return p;
    }

    private void recargarSectores() {
        modeloSectores.clear();
        Parroquia sel = listaParroquias.getSelectedValue();
        if (sel == null) return;
        List<SectorFilial> sectores = dao.listarSectores(sel);
        for (SectorFilial s : sectores) {
            modeloSectores.addElement(s);
        }
    }

    private void nuevoSector() {
        Parroquia parroquia = listaParroquias.getSelectedValue();
        if (parroquia == null) { avisoSeleccion("una parroquia primero"); return; }
        String nombre = pedirNombre("Nuevo sector", "");
        if (nombre == null) return;
        dao.guardarSector(new SectorFilial(nombre, parroquia));
        recargarSectores();
    }

    private void editarSector() {
        SectorFilial sel = listaSectores.getSelectedValue();
        if (sel == null) { avisoSeleccion("un sector"); return; }
        String nombre = pedirNombre("Editar sector", sel.getNombre());
        if (nombre == null) return;
        sel.setNombre(nombre);
        dao.guardarSector(sel);
        recargarSectores();
    }

    private void eliminarSector() {
        SectorFilial sel = listaSectores.getSelectedValue();
        if (sel == null) { avisoSeleccion("un sector"); return; }
        int r = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el sector \"" + sel.getNombre() + "\"?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            dao.eliminarSector(sel.getId());
            recargarSectores();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                "No se puede eliminar: el sector está siendo usado por un caso social.",
                "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ---------------------------------------------------------------- Utilidades

    private String pedirNombre(String titulo, String valorActual) {
        String v = (String) JOptionPane.showInputDialog(this,
            "Nombre:", titulo, JOptionPane.PLAIN_MESSAGE, null, null, valorActual);
        if (v == null) return null;
        v = v.trim();
        if (v.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return null;
        }
        return v;
    }

    private void avisoSeleccion(String que) {
        JOptionPane.showMessageDialog(this, "Seleccioná " + que + ".");
    }

    private void avisoDuplicado() {
        JOptionPane.showMessageDialog(this,
            "Ya existe una parroquia con ese nombre.",
            "Error", JOptionPane.WARNING_MESSAGE);
    }
}
