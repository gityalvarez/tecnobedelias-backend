package com.proyecto.tecnobedelias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.proyecto.tecnobedelias.service.ActaService;

@RestController
@RequestMapping("/acta")
//@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")



public class ActaController {
	
	@Autowired
	ActaService actaService;
	
	@GetMapping("/downloadPdf")
	public ModelAndView downloadPdf() {
		System.out.println("entre al actaController");
	return actaService.downloadPdf();
	}
	
}
