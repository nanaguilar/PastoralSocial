package ui;

import dao.ParroquiaDAO;
import model.Parroquia;
import model.SectorFilial;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// este es el catálogo maestro-detalle de verdad: a la izquierda las parroquias
// (maestro) y a la derecha los sectores de la parroquia que tengo seleccionada
// (detalle). al cambiar de parroquia se recarga la lista de la derecha sola.
public class CatalogoParroquiasFrame extends JFrame {

    private ParroquiaDAO dao = new ParroquiaDAO();

    private DefaultListModel<Parroquia> modeloParroquias = new DefaultListModel<Parroquia>();
    private JList<Parroquia> listaParroquias = new JList<Parroquia>(modeloParroquias);

    private DefaultListModel<SectorFilial> modeloSectores = new DefaultListModel<SectorFilial>();
    private JList<SectorFilial> listaSectores = new JList<SectorFilial>(modeloSectores);

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
        listaSectores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // cada vez que cambio de parroquia recargo sus sectores. el getValueIsAdjusting
        // es para que no se dispare dos veces mientras estoy soltando el clic.
        listaParroquias.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                recargarSectores();
            }
        });

        recargarParroquias();
    }

    // ------------------------------------------------------------------ maestro

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
        // me guardo cuál estaba seleccionada para volver a dejarla marcada después
        // de recargar, si no la lista salta al inicio y es incómodo
        Parroquia seleccionada = listaParroquias.getSelectedValue();

        modeloParroquias.clear();
        for (Parroquia p : dao.listar()) {
            modeloParroquias.addElement(p);
        }

        if (seleccionada != null) {
            for (int i = 0; i < modeloParroquias.size(); i++) {
                if (modeloParroquias.get(i).getId().equals(seleccionada.getId())) {
                    listaParroquias.setSelectedIndex(i);
                    break;
                }
            }
        }
        recargarSectores();
    }

    private void nuevaParroquia() {
        String nombre = pedirNombre("Nueva parroquia", "");
        if (nombre == null) {
            return;
        }
        if (dao.existeParroquia(nombre, null)) {
            aviso("Ya existe una parroquia con ese nombre.");
            return;
        }
        dao.guardarParroquia(new Parroquia(nombre));
        recargarParroquias();
    }

    private void editarParroquia() {
        Parroquia sel = listaParroquias.getSelectedValue();
        if (sel == null) {
            aviso("Seleccioná una parroquia.");
            return;
        }

        String nombre = pedirNombre("Editar parroquia", sel.getNombre());
        if (nombre == null) {
            return;
        }
        if (dao.existeParroquia(nombre, sel.getId())) {
            aviso("Ya existe una parroquia con ese nombre.");
            return;
        }

        sel.setNombre(nombre);
        dao.guardarParroquia(sel);
        recargarParroquias();
    }

    private void eliminarParroquia() {
        Parroquia sel = listaParroquias.getSelectedValue();
        if (sel == null) {
            aviso("Seleccioná una parroquia.");
            return;
        }

        // reviso el uso antes de borrar, contando también los casos que la usan
        // a través de alguno de sus sectores
        if (dao.contarUsoParroquia(sel.getId()) > 0) {
            aviso("No podés eliminar \"" + sel.getNombre() + "\".\n"
                + "Hay casos sociales que la están usando.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Eliminar la parroquia \"" + sel.getNombre() + "\" y todos sus sectores?",
            "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        dao.eliminarParroquia(sel.getId());
        recargarParroquias();
    }

    // ------------------------------------------------------------------ detalle

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
        if (sel == null) {
            return;   // sin parroquia seleccionada no hay detalle que mostrar
        }
        List<SectorFilial> sectores = dao.listarSectores(sel);
        for (SectorFilial s : sectores) {
            modeloSectores.addElement(s);
        }
    }

    private void nuevoSector() {
        Parroquia parroquia = listaParroquias.getSelectedValue();
        if (parroquia == null) {
            aviso("Seleccioná primero la parroquia a la que pertenece el sector.");
            return;
        }

        String nombre = pedirNombre("Nuevo sector", "");
        if (nombre == null) {
            return;
        }

        dao.guardarSector(new SectorFilial(nombre, parroquia));
        recargarSectores();
    }

    private void editarSector() {
        SectorFilial sel = listaSectores.getSelectedValue();
        if (sel == null) {
            aviso("Seleccioná un sector.");
            return;
        }

        String nombre = pedirNombre("Editar sector", sel.getNombre());
        if (nombre == null) {
            return;
        }

        sel.setNombre(nombre);
        dao.guardarSector(sel);
        recargarSectores();
    }

    private void eliminarSector() {
        SectorFilial sel = listaSectores.getSelectedValue();
        if (sel == null) {
            aviso("Seleccioná un sector.");
            return;
        }

        if (dao.contarUsoSector(sel.getId()) > 0) {
            aviso("No podés eliminar \"" + sel.getNombre() + "\".\n"
                + "Hay casos sociales que lo están usando.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el sector \"" + sel.getNombre() + "\"?",
            "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        dao.eliminarSector(sel.getId());
        recargarSectores();
    }

    // ---------------------------------------------------------------- utilidades

    private String pedirNombre(String titulo, String valorActual) {
        String valor = (String) JOptionPane.showInputDialog(this, "Nombre:", titulo,
            JOptionPane.PLAIN_MESSAGE, null, null, valorActual);

        if (valor == null) {
            return null;
        }

        valor = valor.trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede quedar vacío.");
            return null;
        }
        return valor;
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
