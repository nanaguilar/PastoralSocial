package pastoralsocial.main;

import java.util.Date;

import pastoralsocial.controller.RolController;
import pastoralsocial.controller.UsuarioController;
import pastoralsocial.modelo.Genero;
import pastoralsocial.modelo.Rol;
import pastoralsocial.modelo.Usuario;
import pastoralsocial.util.JPAUtil;
import pastoralsocial.controller.MaestroController;
import pastoralsocial.controller.DetalleController;
import pastoralsocial.modelo.Maestro;
import pastoralsocial.modelo.Detalle;

public class Main {

	public static void main(String[] args) {

		System.out.println("===== INICIANDO PASTORAL SOCIAL =====");

		try {

			RolController rolController = new RolController();
			UsuarioController usuarioController = new UsuarioController();

			// 1. Crear rol Administrador
			Rol rolAdministrador = new Rol("Administrador");

			rolController.registrar(rolAdministrador);

			System.out.println("Rol Administrador registrado correctamente.");

			// 2. Crear usuario administrador
			Usuario administrador = new Usuario("123456789", "Cédula", "Administrador", "SIGCAS", new Date(),
					"88888888", "admin@sigcas.com", "admin", "1234", Genero.FEMENINO, true, rolAdministrador);

			usuarioController.registrar(administrador);

			System.out.println("Usuario administrador registrado correctamente.");
			
			// 3. Crear catálogo "Tipo de Asistencia" con sus valores iniciales
			MaestroController maestroController = new MaestroController();
			DetalleController detalleController = new DetalleController();

			Maestro tipoAsistencia = new Maestro("Tipo de Asistencia");
			maestroController.registrar(tipoAsistencia);

			detalleController.registrar(new Detalle("Visita domiciliaria", tipoAsistencia));
			detalleController.registrar(new Detalle("Ayuda económica", tipoAsistencia));
			detalleController.registrar(new Detalle("Orientación psicológica", tipoAsistencia));
			detalleController.registrar(new Detalle("Donación de víveres", tipoAsistencia));

			System.out.println("Catálogo Tipo de Asistencia registrado correctamente.");
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