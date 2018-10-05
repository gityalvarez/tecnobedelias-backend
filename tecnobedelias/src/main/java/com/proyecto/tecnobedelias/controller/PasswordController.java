package com.proyecto.tecnobedelias.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.service.UsuarioService;

@RestController
@RequestMapping("/password")
public class PasswordController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

}
