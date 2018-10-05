package com.proyecto.tecnobedelias.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	public void sendEmail(SimpleMailMessage email);
	public boolean sendEmailPrueba();
}
