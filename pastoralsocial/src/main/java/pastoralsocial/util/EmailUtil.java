package pastoralsocial.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	private static final String REMITENTE = "sigcas.ps@gmail.com";
	private static final String CLAVE_APLICACION = "ebib dfux eqzq ztwn";

	public static boolean enviarCodigoRecuperacion(String correoDestino, String codigo) {
		Properties props = new Properties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(REMITENTE, CLAVE_APLICACION);
			}
		});

		try {
			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(REMITENTE, "Pastoral Social"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
			message.setSubject("SIGCAS - Código de Recuperación de Contraseña");

			String cuerpoHtml = "<!DOCTYPE html>" + "<html>"
					+ "<body style='margin:0;padding:0;background:#f5f7fb;font-family:Segoe UI,Arial,sans-serif;'>"
					+ "<table width='100%' cellpadding='0' cellspacing='0' style='padding:30px;'>"
					+ "<tr><td align='center'>" + "<table width='600' cellpadding='0' cellspacing='0' "
					+ "style='background:#ffffff;border-radius:10px;overflow:hidden;" + "border:1px solid #dfe6ee;'>"

					// Encabezado
					+ "<tr>" + "<td style='background:#002d66;padding:28px;text-align:center;'>"
					+ "<h1 style='margin:0;color:white;font-size:28px;'>SIGCAS</h1>"
					+ "<p style='margin:6px 0 0;color:#d9e6f7;font-size:14px;'>"
					+ "Sistema de Gestión de Asistencia Social" + "</p>" + "</td>" + "</tr>"

					// Contenido
					+ "<tr>" + "<td style='padding:35px;'>"
					+ "<h2 style='margin-top:0;color:#002d66;'>Recuperación de contraseña</h2>"
					+ "<p style='font-size:15px;color:#444;'>" + "Hola," + "</p>"
					+ "<p style='font-size:15px;color:#444;line-height:1.6;'>"
					+ "Recibimos una solicitud para restablecer la contraseña de su cuenta en "
					+ "<strong>SIGCAS</strong>." + "</p>" + "<p style='font-size:15px;color:#444;'>"
					+ "Utilice el siguiente código de verificación:" + "</p>"

					// Código
					+ "<div style='text-align:center;margin:35px 0;'>" + "<span style='display:inline-block;"
					+ "padding:16px 42px;" + "background:#eef4ff;" + "border:2px solid #002d66;" + "border-radius:8px;"
					+ "font-size:32px;" + "font-weight:bold;" + "letter-spacing:8px;" + "color:#002d66;'>" + codigo
					+ "</span>" + "</div>"

					// Aviso
					+ "<div style='background:#f7f9fc;" + "border-left:5px solid #002d66;" + "padding:15px;"
					+ "margin:25px 0;'>" + "<strong style='color:#002d66;'>Importante</strong><br>"
					+ "<span style='color:#555;font-size:14px;'>"
					+ "Este código es válido durante <strong>15 minutos</strong>." + "</span>" + "</div>"
					+ "<p style='font-size:14px;color:#666;line-height:1.6;'>"
					+ "Si usted no solicitó este cambio, puede ignorar este mensaje. "
					+ "Su contraseña permanecerá sin modificaciones." + "</p>" + "</td>" + "</tr>"

					// Pie
					+ "<tr>" + "<td style='background:#f4f6f9;" + "padding:20px;" + "text-align:center;"
					+ "font-size:12px;" + "color:#777;'>" + "<strong>SIGCAS</strong><br>"
					+ "Sistema de Gestión de Asistencia Social<br><br>"
					+ "Este es un correo automático. Por favor, no responda a este mensaje." + "</td>" + "</tr>"
					+ "</table>" + "</td></tr>" + "</table>" + "</body>" + "</html>";

			message.setContent(cuerpoHtml, "text/html; charset=utf-8");

			Transport.send(message);
			return true;

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
