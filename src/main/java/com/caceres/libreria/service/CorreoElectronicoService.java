package com.caceres.libreria.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorreoElectronicoService {

    private final JavaMailSender remitenteMail;

    @Value("${aplicacion.correo.remitente}")
    private String correoRemitente;

    public void enviarCorreoRecuperacion(String correoDestino, String nombrePersona, String enlaceRecuperacion) {
        try {
            MimeMessage mensajeCorreo = remitenteMail.createMimeMessage();
            MimeMessageHelper ayudanteMensaje = new MimeMessageHelper(mensajeCorreo, true, "UTF-8");

            ayudanteMensaje.setFrom(correoRemitente);
            ayudanteMensaje.setTo(correoDestino);
            ayudanteMensaje.setSubject("Recuperacion de Contrasena - Libreria Caceres");

            String contenidoHtml =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<h2 style='color: #1F4E79; border-bottom: 2px solid #1F4E79; padding-bottom: 10px;'>Libreria Caceres</h2>" +
                "<h3>Recuperacion de Contrasena</h3>" +
                "<p>Hola <strong>" + nombrePersona + "</strong>,</p>" +
                "<p>Recibimos una solicitud para restablecer la contrasena de tu cuenta en el sistema de Libreria Caceres.</p>" +
                "<p>Haz clic en el siguiente boton para crear una nueva contrasena:</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "<a href='" + enlaceRecuperacion + "' style='background-color: #1F4E79; color: white; padding: 14px 28px; text-decoration: none; border-radius: 6px; font-size: 16px;'>Restablecer Contrasena</a>" +
                "</div>" +
                "<p>Si el boton no funciona, copia y pega el siguiente enlace en tu navegador:</p>" +
                "<p style='word-break: break-all; color: #1F4E79;'>" + enlaceRecuperacion + "</p>" +
                "<p style='margin-top: 20px;'>Este enlace expirara en <strong>1 hora</strong>.</p>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este correo con seguridad.</p>" +
                "<hr style='margin-top: 30px;'>" +
                "<p style='color: #888; font-size: 12px;'>Libreria Caceres - Sistema de Gestion</p>" +
                "</div>";

            ayudanteMensaje.setText(contenidoHtml, true);

System.out.println("Enviando correo a: " + correoDestino);

remitenteMail.send(mensajeCorreo);

System.out.println("Correo enviado correctamente");

        } catch (MessagingException excepcionMensaje) { 
            throw new RuntimeException("Error al enviar el correo electronico: " + excepcionMensaje.getMessage());
        }                                                                                                                                                                                                           
    }
}