package com.proyecto.tecnobedelias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.persistence.model.Rol;
import com.proyecto.tecnobedelias.persistence.repository.RolRepository;

@RestController
@RequestMapping("/rol")
public class RolController {
	
	@Autowired
	RolRepository rolRepository;
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	public List<Rol> listarRoles() {
		return rolRepository.findAll();
	}
	
	@PostMapping("/crear")
	//@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	public void crearRol(@RequestBody Rol rol) {
		rolRepository.save(rol);
	}

}
