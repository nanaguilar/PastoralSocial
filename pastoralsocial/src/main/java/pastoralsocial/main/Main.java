package pastoralsocial.main;

import pastoralsocial.util.JPAUtil;

public class Main {

	public static void main(String[] args) {

		System.out.println("===== INICIANDO PASTORAL SOCIAL =====");

		try {

//			UsuarioController usuarioController = new UsuarioController();
//			MaestroController maestroController = new MaestroController();
//			DetalleController detalleController = new DetalleController();
//
//			// Rol
//			Maestro maestroRol = new Maestro("Rol");
//			maestroController.registrar(maestroRol);
//			Detalle administrador = new Detalle("Administrador", maestroRol);
//			Detalle trabajadorSocial = new Detalle("Trabajador Social", maestroRol);
//			Detalle coordinador = new Detalle("Coordinador", maestroRol);
//			detalleController.registrar(administrador);
//			detalleController.registrar(trabajadorSocial);
//			detalleController.registrar(coordinador);
//
//			// Crear usuario administrador
//			Usuario usuarioAdministrador = new Usuario("123456789", "Cédula", "Administrador", "SIGCAS", new Date(),
//					"88888888", "darianazel@gmai.com", "admin", PasswordUtil.hash("123"), Genero.FEMENINO, true, administrador);
//
//			usuarioController.registrar(usuarioAdministrador);
//
//			System.out.println("Usuario administrador registrado correctamente.");
//
//			// Tipo de Asistencia
//			Maestro tipoAsistencia = new Maestro("Tipo de Asistencia");
//			maestroController.registrar(tipoAsistencia);
//			detalleController.registrar(new Detalle("Alimentos", tipoAsistencia));
//			detalleController.registrar(new Detalle("Aseo, higiene y limpieza", tipoAsistencia));
//			detalleController.registrar(new Detalle("Indumentaria", tipoAsistencia));
//			detalleController.registrar(new Detalle("Alquiler", tipoAsistencia));
//			detalleController.registrar(new Detalle("Servicios publicos", tipoAsistencia));
//			detalleController.registrar(new Detalle("Medicamentos", tipoAsistencia));
//			detalleController.registrar(new Detalle("Aparatos ortopedicos", tipoAsistencia));
//			detalleController.registrar(new Detalle("Otros (especifique)", tipoAsistencia));
//
//			System.out.println("Catálogo Tipo de Asistencia registrado correctamente.");
//
//			// Tipo de Vivienda
//			Maestro tipoVivienda = new Maestro("Tipo de Vivienda");
//			maestroController.registrar(tipoVivienda);
//			detalleController.registrar(new Detalle("Casa", tipoVivienda));
//			detalleController.registrar(new Detalle("Apartamento", tipoVivienda));
//			detalleController.registrar(new Detalle("Cuarto", tipoVivienda));
//			detalleController.registrar(new Detalle("Tugurio", tipoVivienda));
//			detalleController.registrar(new Detalle("Refugio", tipoVivienda));
//			detalleController.registrar(new Detalle("Ninguna", tipoVivienda));
//
//			// Tenencia de Vivienda
//			Maestro tenenciaVivienda = new Maestro("Tenencia de Vivienda");
//			maestroController.registrar(tenenciaVivienda);
//			detalleController.registrar(new Detalle("Propia", tenenciaVivienda));
//			detalleController.registrar(new Detalle("Paga a plazos", tenenciaVivienda));
//			detalleController.registrar(new Detalle("Alquilada", tenenciaVivienda));
//			detalleController.registrar(new Detalle("Prestada", tenenciaVivienda));
//			detalleController.registrar(new Detalle("Precario", tenenciaVivienda));
//
//			// Condicion de Vivienda
//			Maestro condicionVivienda = new Maestro("Condición de Vivienda");
//			maestroController.registrar(condicionVivienda);
//			detalleController.registrar(new Detalle("Buena", condicionVivienda));
//			detalleController.registrar(new Detalle("Regular", condicionVivienda));
//			detalleController.registrar(new Detalle("Mala", condicionVivienda));
//			detalleController.registrar(new Detalle("No aplica", condicionVivienda));
//
//			System.out.println("Catálogos de vivienda registrados correctamente.");
//
//			// Parentesco
//			Maestro parentesco = new Maestro("Parentesco");
//			maestroController.registrar(parentesco);
//			detalleController.registrar(new Detalle("Cónyuge", parentesco));
//			detalleController.registrar(new Detalle("Hijo/a", parentesco));
//			detalleController.registrar(new Detalle("Padre/Madre", parentesco));
//			detalleController.registrar(new Detalle("Hermano/a", parentesco));
//			detalleController.registrar(new Detalle("Abuelo/a", parentesco));
//			detalleController.registrar(new Detalle("Nieto/a", parentesco));
//			detalleController.registrar(new Detalle("Otro familiar", parentesco));
//			detalleController.registrar(new Detalle("Sin parentesco", parentesco));
//
//			// Nivel educativo
//			Maestro nivelEducativo = maestroController.buscarPorNombre("Nivel Educativo");
//			detalleController.registrar(new Detalle("Sin escolaridad", nivelEducativo));
//			detalleController.registrar(new Detalle("Primaria incompleta", nivelEducativo));
//			detalleController.registrar(new Detalle("Primaria completa", nivelEducativo));
//			detalleController.registrar(new Detalle("Secundaria incompleta", nivelEducativo));
//			detalleController.registrar(new Detalle("Secundaria completa", nivelEducativo));
//			detalleController.registrar(new Detalle("Técnico", nivelEducativo));
//			detalleController.registrar(new Detalle("Universitaria incompleta", nivelEducativo));
//			detalleController.registrar(new Detalle("Universitaria completa", nivelEducativo));
//
//			// Seguro
//			Maestro seguro = new Maestro("Seguro");
//			maestroController.registrar(seguro);
//			detalleController.registrar(new Detalle("Seguro Social", seguro));
//			detalleController.registrar(new Detalle("Seguro voluntario", seguro));
//			detalleController.registrar(new Detalle("Seguro por trabajo", seguro));
//			detalleController.registrar(new Detalle("Sin seguro", seguro));

			System.out.println("Catálogos nuevos registrados correctamente.");

			System.out.println("===== DATOS DE PRUEBA CREADOS =====");

		} catch (Exception e) {

			System.err.println("ERROR: " + e.getMessage());
			e.printStackTrace();

		} finally {

			JPAUtil.closeEntityManagerFactory();

			System.out.println("===== ENTITY MANAGER FACTORY CERRADO =====");
		}
	}
}