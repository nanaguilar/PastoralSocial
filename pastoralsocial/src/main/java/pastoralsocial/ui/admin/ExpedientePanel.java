package pastoralsocial.ui.admin;

import java.awt.Desktop;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import pastoralsocial.controller.AdendumController;
import pastoralsocial.controller.AsistenciaController;
import pastoralsocial.controller.IngresoFamiliarController;
import pastoralsocial.controller.IntegranteFamiliarController;
import pastoralsocial.controller.DocumentoCasoController;
import pastoralsocial.modelo.Adendum;
import pastoralsocial.modelo.Asistencia;
import pastoralsocial.modelo.CasoSocial;
import pastoralsocial.modelo.IngresoFamiliar;
import pastoralsocial.modelo.IntegranteFamiliar;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.modelo.DocumentoCaso;

public class ExpedientePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final CasoSocial casoSocial;

	private final IntegranteFamiliarController integranteController;
	private final IngresoFamiliarController ingresoController;
	private final AsistenciaController asistenciaController;
	private final AdendumController adendumController;
	private final DocumentoCasoController documentoController;

	private Usuario usuarioActual;

	private JPanel panelContenido;
	private int yActual = 0;

	private JTable tablaDocumentos;
	private JButton btnAgregarDocumento;
	private JButton btnAbrirDocumento;
	private JButton btnEditarDocumento;
	private JButton btnEliminarDocumento;

	public ExpedientePanel(CasoSocial casoSocial, Usuario usuarioActual) {

		this.casoSocial = casoSocial;
		this.usuarioActual = usuarioActual;

		this.integranteController = new IntegranteFamiliarController();
		this.ingresoController = new IngresoFamiliarController();
		this.asistenciaController = new AsistenciaController();
		this.adendumController = new AdendumController();
		this.documentoController = new DocumentoCasoController();

		inicializarInterfaz();
		cargarExpediente();
		configurarPermisosDocumentos();
	}

	private void inicializarInterfaz() {

		setBackground(new Color(245, 247, 250));
		setLayout(null);

		JLabel lblTitulo = new JLabel("EXPEDIENTE DIGITAL");
		lblTitulo.setForeground(new Color(0, 45, 102));
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		lblTitulo.setBounds(30, 20, 500, 40);
		add(lblTitulo);

		String numeroFicha = "";

		if (casoSocial != null && casoSocial.getNumeroFicha() != null) {
			numeroFicha = casoSocial.getNumeroFicha();
		}

		JLabel lblCaso = new JLabel("Caso social N.º " + numeroFicha);
		lblCaso.setForeground(new Color(90, 90, 90));
		lblCaso.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblCaso.setBounds(30, 60, 500, 25);
		add(lblCaso);

		JPanel panelResumen = new JPanel();
		panelResumen.setLayout(null);
		panelResumen.setBackground(Color.WHITE);
		panelResumen.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 222), 1));
		panelResumen.setBounds(30, 95, 850, 75);
		add(panelResumen);

		agregarDatoResumen(panelResumen, "ESTADO", obtenerEstado(), 20, 15, 170);
		agregarSeparadorResumen(panelResumen, 215);
		agregarDatoResumen(panelResumen, "RESPONSABLE", obtenerResponsable(), 235, 15, 170);
		agregarSeparadorResumen(panelResumen, 430);
		agregarDatoResumen(panelResumen, "FECHA DE INICIO", formatearFecha(casoSocial.getFechaInicio()), 450, 15, 170);
		agregarSeparadorResumen(panelResumen, 645);
		agregarDatoResumen(panelResumen, "FECHA FIN", formatearFecha(casoSocial.getFechaPrevistaFin()), 665, 15, 170);

		panelContenido = new JPanel();
		panelContenido.setBackground(new Color(245, 247, 250));
		panelContenido.setLayout(null);

		JScrollPane scroll = new JScrollPane(panelContenido);
		scroll.setBounds(20, 180, 900, 505);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		add(scroll);
	}

	private void cargarExpediente() {

		if (casoSocial == null) {
			return;
		}

		yActual = 10;

		// INFORMACIÓN DEL REGISTRO
		agregarTituloSeccion("DATOS DEL REGISTRO");

		Object[][] datosRegistro = { { "N.º de ficha", safe(casoSocial.getNumeroFicha()) },
				{ "Fecha de inicio", formatearFecha(casoSocial.getFechaInicio()) },
				{ "Fecha prevista de conclusión", formatearFecha(casoSocial.getFechaPrevistaFin()) } };

		agregarTablaDatos(new String[] { "Campo", "Información" }, datosRegistro, 390, 850);

		// DATOS PARROQUIALES
		agregarTituloSeccion("DATOS PARROQUIALES");

		Object[][] datosParroquiales = { { "Parroquia", safe(casoSocial.getParroquia()) },
				{ "Sector o filial", safe(casoSocial.getSectorFilial()) } };

		agregarTablaDatos(new String[] { "Campo", "Información" }, datosParroquiales, 390, 850);

		// DATOS GENERALES
		agregarTituloSeccion("DATOS GENERALES");

		Object[][] datosGenerales = { { "Dirección", safe(casoSocial.getDireccion()) },
				{ "Provincia", safe(casoSocial.getProvincia()) }, { "Cantón", safe(casoSocial.getCanton()) },
				{ "Distrito", safe(casoSocial.getDistrito()) } };

		agregarTablaDatos(new String[] { "Campo", "Información" }, datosGenerales, 390, 850);

		// GRUPO FAMILIAR
		agregarTituloSeccion("GRUPO FAMILIAR");

		List<IntegranteFamiliar> integrantes = obtenerIntegrantes();

		String[] columnasFamilia = { "Nombre / Identificación", "Sexo", "Jefatura", "Relación", "Edad", "País",
				"Migración", "Educación", "Seguro" };

		DefaultTableModel modeloFamilia = crearModelo(columnasFamilia);

		for (IntegranteFamiliar integrante : integrantes) {

			String nombre = safe(integrante.getNombre()) + " " + safe(integrante.getApellidos());

			String identificacion = safe(integrante.getTipoIdentificacion()) + " "
					+ safe(integrante.getIdentificacion());

			nombre = nombre.trim() + "\n" + identificacion.trim();

			String sexo = "";

			if (integrante.getDetalleGenero() != null) {
				sexo = safe(integrante.getDetalleGenero().getDescripcion());
			}

			String jefatura = integrante.isJefeHogar() ? "Sí" : "No";

			String relacion = "";

			if (integrante.getDetalleParentesco() != null) {
				relacion = safe(integrante.getDetalleParentesco().getDescripcion());
			}

			String educacion = "";

			if (integrante.getDetalleNivelEducativo() != null) {
				educacion = safe(integrante.getDetalleNivelEducativo().getDescripcion());
			}

			String seguro = "";

			if (integrante.getDetalleSeguro() != null) {
				seguro = safe(integrante.getDetalleSeguro().getDescripcion());
			}

			modeloFamilia.addRow(
					new Object[] { nombre, sexo, jefatura, relacion, calcularEdad(integrante.getFechaNacimiento()),
							safe(integrante.getPais()), safe(integrante.getMigracion()), educacion, seguro });
		}

		if (integrantes.isEmpty()) {
			modeloFamilia.addRow(new Object[] { "No hay integrantes registrados.", "", "", "", "", "", "", "", "" });
		}

		agregarTabla(modeloFamilia, 850, 180);

		// DATOS DE LA VIVIENDA
		agregarTituloSeccion("DATOS DE LA VIVIENDA");

		String tipoVivienda = "";

		if (casoSocial.getDetalleTipoVivienda() != null) {
			tipoVivienda = safe(casoSocial.getDetalleTipoVivienda().getDescripcion());
		}

		String tenencia = "";

		if (casoSocial.getDetalleTenenciaVivienda() != null) {
			tenencia = safe(casoSocial.getDetalleTenenciaVivienda().getDescripcion());
		}

		String condicion = "";

		if (casoSocial.getDetalleCondicionVivienda() != null) {
			condicion = safe(casoSocial.getDetalleCondicionVivienda().getDescripcion());
		}

		Object[][] datosVivienda = { { "Tipo de vivienda", tipoVivienda }, { "Tenencia", tenencia },
				{ "Condición", condicion } };

		agregarTablaDatos(new String[] { "Campo", "Información" }, datosVivienda, 390, 850);

		// INGRESOS FAMILIARES
		agregarTituloSeccion("INGRESOS FAMILIARES");

		List<IngresoFamiliar> ingresos = obtenerIngresos();

		String[] columnasIngresos = { "Persona", "Ocupación / Pensión", "¿Trabaja?", "Ingreso mensual" };

		DefaultTableModel modeloIngresos = crearModelo(columnasIngresos);

		for (IngresoFamiliar ingreso : ingresos) {

			String persona = "";

			if (ingreso.getIntegrante() != null) {
				persona = ingreso.getIntegrante().toString();
			}

			String ingresoMensual = "";

			if (ingreso.getIngresoMensual() != null) {
				ingresoMensual = ingreso.getIngresoMensual().toString();
			}

			modeloIngresos.addRow(new Object[] { persona, safe(ingreso.getOcupacion()),
					ingreso.isTrabaja() ? "Sí" : "No", ingresoMensual });
		}

		if (ingresos.isEmpty()) {
			modeloIngresos.addRow(new Object[] { "No hay ingresos registrados.", "", "", "" });
		}

		agregarTabla(modeloIngresos, 850, 180);

		// ASISTENCIAS
		agregarTituloSeccion("ASISTENCIAS BRINDADAS");

		List<Asistencia> asistencias = obtenerAsistencias();

		String[] columnasAsistencias = { "Fecha", "Tipo", "Modalidad", "Frecuencia", "Duración", "Valor",
				"Observaciones" };

		DefaultTableModel modeloAsistencias = crearModelo(columnasAsistencias);

		for (Asistencia asistencia : asistencias) {

			String tipo = "";

			if (asistencia.getDetalleTipoAsistencia() != null) {
				tipo = safe(asistencia.getDetalleTipoAsistencia().getDescripcion());
			}

			String valor = "";

			if (asistencia.getValor() != null) {
				valor = asistencia.getValor().toString();
			}

			modeloAsistencias.addRow(new Object[] { formatearFecha(asistencia.getFecha()), tipo,
					safe(asistencia.getModalidad()), safe(asistencia.getFrecuencia()), safe(asistencia.getDuracion()),
					valor, safe(asistencia.getObservaciones()) });
		}

		if (asistencias.isEmpty()) {
			modeloAsistencias.addRow(new Object[] { "No hay asistencias registradas.", "", "", "", "", "", "" });
		}

		agregarTabla(modeloAsistencias, 850, 180);

		// ESTADO Y RESPONSABLE
		agregarTituloSeccion("ESTADO Y RESPONSABLE");

		String estado = "";

		if (casoSocial.getDetalleEstado() != null) {
			estado = casoSocial.getDetalleEstado().toString();
		}

		String responsable = "";

		if (casoSocial.getUsuario() != null) {
			responsable = safe(casoSocial.getUsuario().getNombre()) + " "
					+ safe(casoSocial.getUsuario().getApellidos());
		}

		Object[][] datosEstado = { { "Estado", estado }, { "Responsable", responsable } };

		agregarTablaDatos(new String[] { "Campo", "Información" }, datosEstado, 390, 850);
		;

		// OBSERVACIONES DEL CASO
		agregarTituloSeccion("OBSERVACIONES DEL CASO");

		Object[][] datosObservaciones = { { "Observaciones", mostrar(casoSocial.getObservaciones()) } };

		agregarTablaDatos(new String[] { "Campo", "Información" }, datosObservaciones, 390, 850);

		// ADENDUMS
		agregarTituloSeccion("ADENDUMS");

		List<Adendum> adendums = obtenerAdendums();

		String[] columnasAdendums = { "ID", "Fecha de registro", "Observaciones" };

		DefaultTableModel modeloAdendums = crearModelo(columnasAdendums);

		for (Adendum adendum : adendums) {

			modeloAdendums.addRow(new Object[] { adendum.getIdAdendum(), formatearFecha(adendum.getFechaRegistro()),
					safe(adendum.getObservaciones()) });
		}

		if (adendums.isEmpty()) {
			modeloAdendums.addRow(new Object[] { "", "", "No hay adendums registrados." });
		}

		agregarTabla(modeloAdendums, 850, 180);

		panelContenido.setPreferredSize(new java.awt.Dimension(880, yActual + 30));

		// DOCUMENTOS
		agregarTituloSeccion("DOCUMENTOS");

		List<DocumentoCaso> documentos = obtenerDocumentos();

		String[] columnasDocumentos = { "Tipo", "Documento", "Fecha de registro" };

		DefaultTableModel modeloDocumentos = crearModelo(columnasDocumentos);

		for (DocumentoCaso documento : documentos) {

			String tipo = "";

			if (documento.getDetalleTipoDocumento() != null) {
				tipo = safe(documento.getDetalleTipoDocumento().getDescripcion());
			}

			modeloDocumentos.addRow(new Object[] { tipo, safe(documento.getNombreArchivo()),
					formatearFecha(documento.getFechaRegistro()) });
		}

		if (documentos.isEmpty()) {

			modeloDocumentos.addRow(new Object[] { "No hay documentos registrados.", "", "" });
		}

		tablaDocumentos = new JTable(modeloDocumentos);
		tablaDocumentos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tablaDocumentos.setRowHeight(30);
		tablaDocumentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaDocumentos.setShowGrid(true);
		tablaDocumentos.setGridColor(new Color(205, 215, 225));
		tablaDocumentos.setFillsViewportHeight(true);
		tablaDocumentos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tablaDocumentos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tablaDocumentos.getTableHeader().setForeground(Color.WHITE);
		tablaDocumentos.getTableHeader().setBackground(new Color(0, 55, 120));
		tablaDocumentos.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollDocumentos = new JScrollPane(tablaDocumentos);
		scrollDocumentos.setBounds(10, yActual, 850, 180);
		scrollDocumentos.setBorder(BorderFactory.createLineBorder(new Color(195, 205, 215)));
		scrollDocumentos.getViewport().setBackground(Color.WHITE);

		panelContenido.add(scrollDocumentos);

		yActual += 200;

		btnAgregarDocumento = new JButton("Agregar documento");
		btnAbrirDocumento = new JButton("Abrir documento");
		btnEditarDocumento = new JButton("Editar documento");
		btnEliminarDocumento = new JButton("Eliminar documento");

		btnAgregarDocumento.setBounds(130, yActual, 180, 35);
		btnAbrirDocumento.setBounds(320, yActual, 170, 35);
		btnEditarDocumento.setBounds(500, yActual, 170, 35);
		btnEliminarDocumento.setBounds(680, yActual, 180, 35);

		panelContenido.add(btnAgregarDocumento);
		panelContenido.add(btnAbrirDocumento);
		panelContenido.add(btnEditarDocumento);
		panelContenido.add(btnEliminarDocumento);

///////////////

		yActual += 55;

		btnAgregarDocumento.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				agregarDocumento();
			}
		});

		btnAbrirDocumento.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				abrirDocumento();
			}
		});

		btnEditarDocumento.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				editarDocumento();
			}
		});

		btnEliminarDocumento.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				eliminarDocumento();
			}
		});

		panelContenido.setPreferredSize(new java.awt.Dimension(880, yActual + 30));
	}

	private void configurarPermisosDocumentos() {

		String rol = "";

		if (usuarioActual != null && usuarioActual.getDetalleRol() != null
				&& usuarioActual.getDetalleRol().getDescripcion() != null) {

			rol = usuarioActual.getDetalleRol().getDescripcion().trim();
		}

		boolean puedeGestionarDocumentos = "Administrador".equalsIgnoreCase(rol)
				|| "Trabajador Social".equalsIgnoreCase(rol);

		boolean puedeConsultarDocumentos = puedeGestionarDocumentos || "Coordinador".equalsIgnoreCase(rol);

		btnAbrirDocumento.setVisible(puedeConsultarDocumentos);
		btnAgregarDocumento.setVisible(puedeGestionarDocumentos);
		btnEditarDocumento.setVisible(puedeGestionarDocumentos);
		btnEliminarDocumento.setVisible(puedeGestionarDocumentos);
	}

	// TABLAS
	private DefaultTableModel crearModelo(String[] columnas) {

		return new DefaultTableModel(columnas, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private void agregarTabla(DefaultTableModel modelo, int ancho, int alto) {

		JTable tabla = new JTable(modelo);

		// FUENTE Y FILAS
		tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tabla.setRowHeight(30);

		// COMPORTAMIENTO
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.setShowGrid(true);
		tabla.setGridColor(new Color(205, 215, 225));
		tabla.setFillsViewportHeight(true);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabla.setIntercellSpacing(new java.awt.Dimension(1, 1));

		// ENCABEZADO
		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.getTableHeader().setBackground(new Color(0, 55, 120));
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 27));

		// CUERPO
		tabla.setBackground(Color.WHITE);
		tabla.setForeground(new Color(20, 50, 90));
		tabla.setSelectionBackground(new Color(220, 230, 242));
		tabla.setSelectionForeground(new Color(20, 50, 90));

		// ALTURA SEGÚN CONTENIDO
		int alturaTabla = Math.min(250, 27 + (modelo.getRowCount() * 30) + 2);

		JScrollPane scrollTabla = new JScrollPane(tabla);
		scrollTabla.setBounds(10, yActual, 850, alturaTabla);
		scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(195, 205, 215)));
		scrollTabla.getViewport().setBackground(Color.WHITE);
		panelContenido.add(scrollTabla);

		yActual += alturaTabla + 20;
	}

	private String mostrar(String valor) {

		if (valor == null || valor.trim().isEmpty()) {
			return "—";
		}

		return valor;
	}

	private void agregarTablaDatos(String[] columnas, Object[][] datos, int anchoPrimeraColumna, int anchoTotal) {

		DefaultTableModel modelo = crearModelo(columnas);

		for (Object[] fila : datos) {
			modelo.addRow(fila);
		}

		JTable tabla = new JTable(modelo);

		// FUENTE
		tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tabla.setRowHeight(30);

		// ENCABEZADO
		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.getTableHeader().setBackground(new Color(0, 55, 120));
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 27));

		// CUERPO
		tabla.setBackground(Color.WHITE);
		tabla.setForeground(new Color(20, 50, 90));
		tabla.setShowGrid(true);
		tabla.setGridColor(new Color(205, 215, 225));
		tabla.setSelectionBackground(new Color(220, 230, 242));
		tabla.setSelectionForeground(new Color(20, 50, 90));
		tabla.setFillsViewportHeight(true);

		// ANCHOS DE COLUMNAS
		tabla.getColumnModel().getColumn(0).setPreferredWidth(anchoPrimeraColumna);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(anchoTotal - anchoPrimeraColumna);

		// TABLA
		int alturaTabla = 27 + (datos.length * 30) + 2;

		JScrollPane scrollTabla = new JScrollPane(tabla);
		scrollTabla.setBounds(10, yActual, 850, alturaTabla);
		scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(195, 205, 215)));
		scrollTabla.getViewport().setBackground(Color.WHITE);
		panelContenido.add(scrollTabla);

		yActual += alturaTabla + 20;
	}

	private void agregarTituloSeccion(String texto) {

		JPanel barra = new JPanel();
		barra.setLayout(null);
		barra.setBackground(new Color(235, 240, 247));
		barra.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(0, 45, 102)));
		barra.setBounds(10, yActual, 850, 34);

		JLabel titulo = new JLabel(texto);
		titulo.setForeground(new Color(0, 45, 102));
		titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
		titulo.setBounds(15, 0, 820, 34);
		barra.add(titulo);
		panelContenido.add(barra);

		yActual += 44;
	}

	private void agregarSeparadorResumen(JPanel panel, int x) {

		JPanel separador = new JPanel();
		separador.setBackground(new Color(215, 220, 228));
		separador.setBounds(x, 20, 1, 45);
		panel.add(separador);
	}

	// CONSULTAS
	private List<IntegranteFamiliar> obtenerIntegrantes() {

		try {

			return integranteController.buscarPorCasoSocial(casoSocial);

		} catch (Exception e) {

			e.printStackTrace();

			return java.util.Collections.emptyList();
		}
	}

	private List<IngresoFamiliar> obtenerIngresos() {

		try {

			return ingresoController.buscarPorCasoSocial(casoSocial);

		} catch (Exception e) {

			e.printStackTrace();

			return java.util.Collections.emptyList();
		}
	}

	private List<Asistencia> obtenerAsistencias() {

		try {

			return asistenciaController.buscarPorCasoSocial(casoSocial);

		} catch (Exception e) {

			e.printStackTrace();

			return java.util.Collections.emptyList();
		}
	}

	private List<Adendum> obtenerAdendums() {

		try {

			return adendumController.listarPorCaso(casoSocial);

		} catch (Exception e) {

			e.printStackTrace();

			return java.util.Collections.emptyList();
		}
	}

	// UTILIDADES
	private String safe(String texto) {

		return texto != null ? texto : "";
	}

	private String formatearFecha(Date fecha) {

		if (fecha == null) {
			return "";
		}

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		return formato.format(fecha);
	}

	private String calcularEdad(Date fechaNacimiento) {

		if (fechaNacimiento == null) {
			return "";
		}

		Calendar nacimiento = Calendar.getInstance();
		nacimiento.setTime(fechaNacimiento);

		Calendar hoy = Calendar.getInstance();

		int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);

		if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {

			edad--;
		}

		return String.valueOf(edad);
	}

	private void agregarDatoResumen(JPanel panel, String titulo, String valor, int x, int y, int ancho) {

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 10));
		lblTitulo.setForeground(new Color(110, 110, 110));
		lblTitulo.setBounds(x, y, ancho, 18);

		panel.add(lblTitulo);

		JLabel lblValor = new JLabel(valor == null || valor.trim().isEmpty() ? "—" : valor);

		lblValor.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblValor.setForeground(new Color(0, 45, 102));
		lblValor.setBounds(x, y + 20, ancho, 25);

		panel.add(lblValor);
	}

	private String obtenerEstado() {

		if (casoSocial.getDetalleEstado() == null) {
			return "—";
		}

		return casoSocial.getDetalleEstado().toString();
	}

	private String obtenerResponsable() {

		if (casoSocial.getUsuario() == null) {
			return "—";
		}

		return safe(casoSocial.getUsuario().getNombre()) + " " + safe(casoSocial.getUsuario().getApellidos());
	}

	private void agregarDocumento() {

		DocumentoCasoFormDialog dialog = new DocumentoCasoFormDialog(null, casoSocial);

		dialog.setVisible(true);

		cargarExpediente();
	}

	private List<DocumentoCaso> obtenerDocumentos() {

		try {

			return documentoController.listarPorCaso(casoSocial);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "No fue posible cargar los documentos.", "Error",
					JOptionPane.ERROR_MESSAGE);

			return new java.util.ArrayList<DocumentoCaso>();
		}
	}

	private void abrirDocumento() {

		int fila = tablaDocumentos.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Seleccione un documento.", "Documento", JOptionPane.WARNING_MESSAGE);

			return;
		}

		try {

			List<DocumentoCaso> documentos = documentoController.listarPorCaso(casoSocial);

			if (fila >= documentos.size()) {
				return;
			}

			DocumentoCaso documento = documentos.get(fila);

			File archivo = new File(documento.getRutaArchivo());

			if (!archivo.exists()) {

				JOptionPane.showMessageDialog(this, "El archivo no existe:\n" + archivo.getAbsolutePath(),
						"Archivo no encontrado", JOptionPane.ERROR_MESSAGE);

				return;
			}

			if (!Desktop.isDesktopSupported()) {

				JOptionPane.showMessageDialog(this, "El sistema no permite abrir archivos.", "No disponible",
						JOptionPane.ERROR_MESSAGE);

				return;
			}

			Desktop.getDesktop().open(archivo);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "No se pudo abrir el documento:\n" + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editarDocumento() {

		int fila = tablaDocumentos.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Seleccione un documento.", "Documento", JOptionPane.WARNING_MESSAGE);

			return;
		}

		try {

			List<DocumentoCaso> documentos = documentoController.listarPorCaso(casoSocial);

			if (fila >= documentos.size()) {
				return;
			}

			DocumentoCaso documento = documentos.get(fila);

			Frame parent = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this);

			DocumentoCasoFormDialog dialog = new DocumentoCasoFormDialog(parent, casoSocial, documento);

			dialog.setVisible(true);

			cargarExpediente();

		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "No se pudo editar el documento:\n" + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void eliminarDocumento() {

		int fila = tablaDocumentos.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(this, "Seleccione un documento.", "Documento", JOptionPane.WARNING_MESSAGE);

			return;
		}

		try {

			List<DocumentoCaso> documentos = documentoController.listarPorCaso(casoSocial);

			if (fila >= documentos.size()) {
				return;
			}

			DocumentoCaso documento = documentos.get(fila);

			int respuesta = JOptionPane.showConfirmDialog(this,
					"¿Está seguro de eliminar este documento?\n\n" + documento.getNombreArchivo(),
					"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (respuesta != JOptionPane.YES_OPTION) {
				return;
			}

			// ELIMINAR REGISTRO DE LA BASE DE DATOS
			documentoController.eliminar(documento);

			JOptionPane.showMessageDialog(this, "El documento fue eliminado correctamente.", "Documento eliminado",
					JOptionPane.INFORMATION_MESSAGE);

			cargarExpediente();

		} catch (Exception e) {

			e.printStackTrace();

			JOptionPane.showMessageDialog(this, "No se pudo eliminar el documento:\n" + obtenerMensajeError(e), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String obtenerMensajeError(Exception e) {

		Throwable causa = e;

		while (causa.getCause() != null) {
			causa = causa.getCause();
		}

		return causa.getMessage() != null ? causa.getMessage() : e.getMessage();
	}

}