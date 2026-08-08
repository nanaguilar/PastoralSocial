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

            // Obtener el rol Administrador existente
            Rol rolAdministrador = rolController.buscarPorId(1);

            if (rolAdministrador == null) {
                System.out.println("ERROR: No existe el rol con ID 1.");
                return;
            }

            // Crear usuario administrador
            Usuario administrador = new Usuario(
                    "123456789",
                    "Cédula",
                    "Administrador",
                    "SIGCAS",
                    new Date(),
                    "88888888",
                    "admin@sigcas.com",
                    "admin",
                    "1234",
                    Genero.FEMENINO,
                    true,
                    rolAdministrador
            );

            usuarioController.registrar(administrador);

            System.out.println("Usuario administrador registrado correctamente.");

            // ===== CATALOGOS MAESTRO / DETALLE =====

            MaestroController maestroController = new MaestroController();
            DetalleController detalleController = new DetalleController();

            // Catalogo: Tipo de Asistencia
            Maestro tipoAsistencia = new Maestro("Tipo de Asistencia");
            maestroController.registrar(tipoAsistencia);

            detalleController.registrar(new Detalle("Visita domiciliaria", tipoAsistencia));
            detalleController.registrar(new Detalle("Ayuda económica", tipoAsistencia));
            detalleController.registrar(new Detalle("Orientación psicológica", tipoAsistencia));
            detalleController.registrar(new Detalle("Donación de víveres", tipoAsistencia));

            System.out.println("Catálogo Tipo de Asistencia registrado correctamente.");

            // Catalogo: Tipo de Vivienda
            Maestro tipoVivienda = new Maestro("Tipo de Vivienda");
            maestroController.registrar(tipoVivienda);

            detalleController.registrar(new Detalle("Casa", tipoVivienda));
            detalleController.registrar(new Detalle("Apartamento", tipoVivienda));
            detalleController.registrar(new Detalle("Rancho", tipoVivienda));
            detalleController.registrar(new Detalle("Cuarto", tipoVivienda));

            // Catalogo: Tenencia de Vivienda
            Maestro tenenciaVivienda = new Maestro("Tenencia de Vivienda");
            maestroController.registrar(tenenciaVivienda);

            detalleController.registrar(new Detalle("Propia", tenenciaVivienda));
            detalleController.registrar(new Detalle("Alquilada", tenenciaVivienda));
            detalleController.registrar(new Detalle("Prestada", tenenciaVivienda));
            detalleController.registrar(new Detalle("Precaria", tenenciaVivienda));

            // Catalogo: Condicion de Vivienda
            Maestro condicionVivienda = new Maestro("Condición de Vivienda");
            maestroController.registrar(condicionVivienda);

            detalleController.registrar(new Detalle("Buena", condicionVivienda));
            detalleController.registrar(new Detalle("Regular", condicionVivienda));
            detalleController.registrar(new Detalle("Mala", condicionVivienda));

            System.out.println("Catálogos de vivienda registrados correctamente.");

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