package com.proyecto.tecnobedelias.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	public void sendEmail(SimpleMailMessage email);
	public boolean sendEmailPrueba();
	public boolean sendEmailToken(String token,String email, String username);
	public boolean sendEmailCalifiacion(String tipo, String email, String nombreAsignatura);
}
