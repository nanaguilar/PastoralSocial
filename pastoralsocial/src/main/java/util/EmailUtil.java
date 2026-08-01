package util;

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

// esto manda el correo con el código de recuperación. usa la cuenta de Gmail del
// proyecto con una "contraseña de aplicación" (no la contraseña normal de la
// cuenta: Google ya no deja entrar con esa desde programas externos).
// necesita los jars javax.mail-1.6.2.jar y activation-1.1.1.jar del lib.
public class EmailUtil {

    // cuenta desde la que sale el correo
    private static final String REMITENTE = "pastoralsocial.proyecto@gmail.com";
    // contraseña de aplicación generada en la configuración de Gmail
    private static final String CLAVE_APLICACION = "phjgsopzteckxbrl";

    // devuelve true si el correo salió, false si algo falló (credenciales malas,
    // sin internet...). el que llama decide qué mostrarle al usuario.
    public static boolean enviarCodigoRecuperacion(String correoDestino, String codigo) {
        Properties props = new Properties();

        // configuración del servidor SMTP de Gmail
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

            // el segundo parámetro es el nombre que ve el destinatario en la bandeja
            message.setFrom(new InternetAddress(REMITENTE, "Pastoral Social"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            message.setSubject("Código de Recuperación de Contraseña - Pastoral Social");

            String cuerpoHtml = "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333333; line-height: 1.6;'>"
                    + "<div style='max-width: 500px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                    + "<h2 style='color: #2c3e50; text-align: center;'>Pastoral Social</h2>"
                    + "<p>Hola,</p>"
                    + "<p>Has solicitado restablecer tu contraseña en el sistema de Pastoral Social.</p>"
                    + "<p>Tu código de verificación es:</p>"
                    + "<div style='text-align: center; margin: 25px 0;'>"
                    + "  <span style='background-color: #f2f4f7; color: #1a73e8; font-size: 24px; font-weight: bold; letter-spacing: 4px; padding: 12px 24px; border-radius: 6px; border: 1px solid #d0d7de; display: inline-block;'>"
                    +      codigo
                    + "  </span>"
                    + "</div>"
                    + "<p style='font-size: 13px; color: #666666;'>Este código vencerá en <b>15 minutos</b>.</p>"
                    + "<p style='font-size: 13px; color: #666666;'>Si no solicitaste este cambio, puedes ignorar este correo de forma segura.</p>"
                    + "<hr style='border: none; border-top: 1px solid #eeeeee; margin: 20px 0;'>"
                    + "<p style='font-size: 12px; color: #888888; text-align: center;'>Atentamente,<br><b>Equipo de Soporte de Pastoral Social</b></p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            // le pongo el charset a mano porque si no las tildes llegan rotas
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
