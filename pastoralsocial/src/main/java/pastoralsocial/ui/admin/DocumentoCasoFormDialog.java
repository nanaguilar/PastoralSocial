package pastoralsocial.ui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pastoralsocial.controller.DetalleController;
import pastoralsocial.controller.DocumentoCasoController;
import pastoralsocial.controller.MaestroController;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.Detalle;
import pastoralsocial.modelo.DocumentoCaso;
import pastoralsocial.modelo.Maestro;
import pastoralsocial.util.DocumentoUtil;

public class DocumentoCasoFormDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final CasoSocial casoSocial;
	private final DocumentoCasoController controller;
	private DocumentoCaso documento;

	private JComboBox<Detalle> comboTipo;
	private JTextField txtArchivo;

	private Path archivoSeleccionado;

	private final DetalleController detalleController;
	private final MaestroController maestroController;

	private static final String CATALOGO_TIPO_DOCUMENTO = "TiposDocumento";

	// CONSTRUCTOR PARA AGREGAR
	public DocumentoCasoFormDialog(java.awt.Window parent, CasoSocial casoSocial) {

		super(parent, "Agregar documento", ModalityType.APPLICATION_MODAL);

		this.casoSocial = casoSocial;
		this.controller = new DocumentoCasoController();
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();

		inicializar();
		cargarTiposDocumento();
	}

	// CONSTRUCTOR PARA EDITAR
	public DocumentoCasoFormDialog(java.awt.Window parent, CasoSocial casoSocial, DocumentoCaso documento) {

		super(parent, "Editar documento", ModalityType.APPLICATION_MODAL);

		this.casoSocial = casoSocial;
		this.controller = new DocumentoCasoController();
		this.detalleController = new DetalleController();
		this.maestroController = new MaestroController();
		this.documento = documento;

		inicializar();
		cargarTiposDocumento();
	}

	private void inicializar() {

		setSize(600, 300);
		setLocationRelativeTo(getParent());
		setResizable(false);

		JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// TÍTULO
		JLabel titulo = new JLabel(documento == null ? "AGREGAR DOCUMENTO" : "EDITAR DOCUMENTO");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
		titulo.setForeground(new Color(0, 45, 102));
		panelPrincipal.add(titulo, BorderLayout.NORTH);

		// FORMULARIO
		JPanel formulario = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(8, 5, 8, 5);

		gbc.fill = GridBagConstraints.HORIZONTAL;

		// TIPO DE DOCUMENTO
		JLabel lblTipo = new JLabel("Tipo de documento:");

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;

		formulario.add(lblTipo, gbc);

		comboTipo = new JComboBox<Detalle>();

		gbc.gridx = 1;
		gbc.weightx = 1;

		formulario.add(comboTipo, gbc);

		// ARCHIVO
		JLabel lblArchivo = new JLabel("Archivo:");

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;

		formulario.add(lblArchivo, gbc);

		JPanel panelArchivo = new JPanel(new BorderLayout(5, 0));

		txtArchivo = new JTextField();
		txtArchivo.setEditable(false);

		JButton btnExaminar = new JButton("Examinar");

		btnExaminar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				seleccionarArchivo();
			}
		});

		panelArchivo.add(txtArchivo, BorderLayout.CENTER);
		panelArchivo.add(btnExaminar, BorderLayout.EAST);

		gbc.gridx = 1;
		gbc.weightx = 1;

		formulario.add(panelArchivo, gbc);

		panelPrincipal.add(formulario, BorderLayout.CENTER);

		// BOTONES
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setForeground(new Color(0, 45, 102));
		btnCancelar.setBackground(Color.WHITE);
		btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCancelar.setFocusPainted(false);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setBackground(new Color(0, 55, 120));
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setFocusPainted(false);

		// EVENTOS
		btnCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});

		btnGuardar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				guardarDocumento();
			}
		});

		panelBotones.add(btnCancelar);
		panelBotones.add(btnGuardar);

		panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
		add(panelPrincipal);
	}

	private void cargarTiposDocumento() {

		try {

			comboTipo.removeAllItems();

			Maestro maestro = maestroController.buscarPorNombre(CATALOGO_TIPO_DOCUMENTO);

			if (maestro == null) {

				throw new IllegalArgumentException("No se encontró el maestro: " + CATALOGO_TIPO_DOCUMENTO);
			}

			System.out.println("MAESTRO ENCONTRADO: " + maestro.getNombre());
			System.out.println("ID MAESTRO: " + maestro.getIdMaestro());

			List<Detalle> detalles = detalleController.listarPorMaestro(maestro.getIdMaestro());

			System.out.println("CANTIDAD DE DETALLES: " + detalles.size());

			for (Detalle detalle : detalles) {

				System.out.println("DETALLE: " + detalle);

				comboTipo.addItem(detalle);
			}

			// Si estamos editando, seleccionar el tipo actual
			if (documento != null && documento.getDetalleTipoDocumento() != null) {

				int idDetalleSeleccionado = documento.getDetalleTipoDocumento().getIdDetalle();

				for (int i = 0; i < comboTipo.getItemCount(); i++) {

					Detalle detalleCombo = comboTipo.getItemAt(i);

					if (detalleCombo.getIdDetalle() == idDetalleSeleccionado) {

						comboTipo.setSelectedIndex(i);
						break;
					}
				}

				// Mostrar el nombre del archivo actual
				if (documento.getNombreArchivo() != null) {

					txtArchivo.setText(documento.getNombreArchivo());
				}
			}

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No fue posible cargar los tipos de documento:\n\n" + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void seleccionarArchivo() {

		JFileChooser chooser = new JFileChooser();

		chooser.setDialogTitle("Seleccionar documento");

		int resultado = chooser.showOpenDialog(this);

		if (resultado == JFileChooser.APPROVE_OPTION) {

			File archivo = chooser.getSelectedFile();

			archivoSeleccionado = archivo.toPath();

			txtArchivo.setText(archivo.getAbsolutePath());
		}
	}

	private void guardarDocumento() {

		if (casoSocial == null) {

			JOptionPane.showMessageDialog(this, "No se encontró el caso social.", "Error", JOptionPane.ERROR_MESSAGE);

			return;
		}

		try {

			Detalle detalleTipo = (Detalle) comboTipo.getSelectedItem();

			if (detalleTipo == null) {

				JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de documento.", "Validación",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			// EDITAR
			if (documento != null) {

				documento.setDetalleTipoDocumento(detalleTipo);

				if (archivoSeleccionado != null) {

					String numeroFicha = casoSocial.getNumeroFicha();

					String ruta = DocumentoUtil.guardarArchivo(archivoSeleccionado, numeroFicha);

					documento.setNombreArchivo(archivoSeleccionado.getFileName().toString());

					documento.setRutaArchivo(ruta);
				}

				documento.setFechaRegistro(new Date());

				controller.actualizar(documento);

				JOptionPane.showMessageDialog(this, "El documento se actualizó correctamente.", "Documento actualizado",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();

				return;
			}

			// AGREGAR
			if (archivoSeleccionado == null) {

				JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo.", "Validación",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			String numeroFicha = casoSocial.getNumeroFicha();

			String ruta = DocumentoUtil.guardarArchivo(archivoSeleccionado, numeroFicha);

			DocumentoCaso nuevoDocumento = new DocumentoCaso();

			nuevoDocumento.setCasoSocial(casoSocial);
			nuevoDocumento.setDetalleTipoDocumento(detalleTipo);
			nuevoDocumento.setNombreArchivo(archivoSeleccionado.getFileName().toString());
			nuevoDocumento.setRutaArchivo(ruta);
			nuevoDocumento.setFechaRegistro(new Date());

			controller.registrar(nuevoDocumento);

			JOptionPane.showMessageDialog(this, "El documento se agregó correctamente.", "Documento guardado",
					JOptionPane.INFORMATION_MESSAGE);

			dispose();

		} catch (Exception ex) {

			ex.printStackTrace();

			JOptionPane.showMessageDialog(this, "No se pudo guardar el documento:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}