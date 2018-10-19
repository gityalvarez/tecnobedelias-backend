package com.proyecto.tecnobedelias.service.impl;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmail(SimpleMailMessage email) {
		mailSender.send(email);
	}
	
	@Async
	public boolean sendEmailPrueba() {
		
		try {
			Properties pro = new Properties();

			pro.put("mail.smtp.host", "smtp.gmail.com");
			pro.setProperty("mail.smtp.starttls.enable", "true");
			pro.setProperty("mail.smtp.port", "587");
			pro.setProperty("mail.smtp.user", "proyecto.tecnobedelias@gmail.com");
			pro.setProperty("mail.smtp.auth", "true");
			pro.put("mail.smtp.ssl.trust", "smtp.gmail.com");

			Session session = Session.getDefaultInstance(pro, null);
			BodyPart texto = new MimeBodyPart();
			texto.setText("hola desde InfoBedelias");

			MimeMultipart m = new MimeMultipart();
			m.addBodyPart(texto);

			MimeMessage mensaje = new MimeMessage(session);
			mensaje.setFrom(new InternetAddress("proyecto.tecnobedelias@gmail.com"));
			mensaje.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress("yamandu.alvarez@gmail.com"));
			mensaje.setSubject("Saludo");
			mensaje.setContent(m);

			Transport t = session.getTransport("smtp");
			t.connect("proyecto.tecnobedelias@gmail.com", "HYFLM2018");
			t.sendMessage(mensaje, mensaje.getAllRecipients());
			t.close();
			return true;

		} catch (Exception e) {
			System.out.println("Error----" + e);
			return false;
		}

		
	}
		
		
		@Async
		public boolean sendEmailToken(String token) {
			System.out.println("entre al sendEmailToken con el token "+token);
			try {
				Properties pro = new Properties();

				pro.put("mail.smtp.host", "smtp.gmail.com");
				pro.setProperty("mail.smtp.starttls.enable", "true");
				pro.setProperty("mail.smtp.port", "587");
				pro.setProperty("mail.smtp.user", "proyecto.tecnobedelias@gmail.com");
				pro.setProperty("mail.smtp.auth", "true");
				pro.put("mail.smtp.ssl.trust", "smtp.gmail.com");

				Session session = Session.getDefaultInstance(pro, null);
				BodyPart texto = new MimeBodyPart();
				texto.setText("http://localhost:4200/reset?token="+token);

				MimeMultipart m = new MimeMultipart();
				m.addBodyPart(texto);

				MimeMessage mensaje = new MimeMessage(session);
				mensaje.setFrom(new InternetAddress("proyecto.tecnobedelias@gmail.com"));
				mensaje.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress("yamandu.alvarez@gmail.com"));
				mensaje.setSubject("Token");
				mensaje.setContent(m);

				Transport t = session.getTransport("smtp");
				t.connect("proyecto.tecnobedelias@gmail.com", "HYFLM2018");
				t.sendMessage(mensaje, mensaje.getAllRecipients());
				t.close();
				System.out.println("mande el mail!!!!!");
				return true;

			} catch (Exception e) {
				System.out.println("Error----" + e);
				return false;
			}
		}
		
		/*
		SimpleMailMessage mailPrueba = new SimpleMailMessage();
		mailPrueba.setFrom("proyecto.tecnobedelias@gmail.com");
		mailPrueba.setTo("yamandu.alvarez@gmail.com");
		mailPrueba.setSubject("Prueba");
		mailPrueba.setText("Este es un Mail de prueba, espero que llegue\n Gracias");
				
		mailSender.send(mailPrueba);
		*/
	
}