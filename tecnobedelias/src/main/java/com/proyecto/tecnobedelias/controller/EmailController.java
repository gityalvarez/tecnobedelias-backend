package com.proyecto.tecnobedelias.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.service.EmailService;
//import com.proyecto.tecnobedelias.service.impl.MailService;

@RestController
@RequestMapping("/email")
public class EmailController {

private Logger logger = LoggerFactory.getLogger(EmailController.class);
	
	//@Autowired
	//private MailService notificationService;
	
	@Autowired
	private EmailService emailService;
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	@RequestMapping("/asincrono")
	public String signupSuccess(){
		
		
		
		// send a notification
		try {
			emailService.sendEmailPrueba();
		}catch( Exception e ){
			// catch error
			logger.info("Error Sending Email: " + e.getMessage());
		}
		
		return "Thank you for registering with us.";
	}
	
	
}
