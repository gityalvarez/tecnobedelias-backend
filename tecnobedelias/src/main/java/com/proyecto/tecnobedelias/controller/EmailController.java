package com.proyecto.tecnobedelias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

	@Autowired
	EmailService emailService;
	
	
	@GetMapping("/enviarprueba")
	public String enviarMailPrueba() {
		if (emailService.sendEmailPrueba()) {
			return "Fijate en el cel que te llegó un mailcito";
		}else return "no te llego nada";
		
	}
	
}
