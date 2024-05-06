
package comunicaciones;


import config.Config;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

import static jakarta.mail.Transport.send;

    public class Comunicaciones {
        public static final String host = "smtp.gmail.com";
        public static final String user = "fernanpaaqenviacorreos@gmail.com";
        public static final String pass = "sous xwjs bnhl hmht";
        public static boolean enviarMensaje(String destino, String asunto, String mensaje){
            String contenido;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.mtp.socketFactory.port", "465");
            props.put("mail.mtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });

            try{
                Multipart multipart = new MimeMultipart();
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
                message.setSubject(asunto);
                //message.setText(mensaje);
                MimeBodyPart parteTexto = new MimeBodyPart();
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                parteTexto.setContent(mensaje, "text/html; charset=utf-8");
                multipart.addBodyPart(parteTexto);
                mimeBodyPart.attachFile(Config.rutaPATH_RECEIPT());
                multipart.addBodyPart(mimeBodyPart);
                message.setContent(multipart);

                send(message);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        public static boolean enviarMensajeSinFactura(String destino, String asunto, String mensaje){
            String contenido;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.mtp.socketFactory.port", "465");
            props.put("mail.mtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });

            try{
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
                message.setSubject(asunto);
                //message.setText(mensaje);
                message.setContent(mensaje, "text/html; charset=utf-8");
                send(message);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        public static boolean enviaExcelAlAdmin(){
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.mtp.socketFactory.port", "465");
            props.put("mail.mtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });

            try{
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("fernanpaaqenviacorreos@gmail.com"));
                message.setSubject("Envia resumen de paquetes en formato excel");
                //message.setText(mensaje);
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.attachFile(Config.rutaPATH_EXCEL());
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);
                message.setContent(multipart);

                send(message);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }


