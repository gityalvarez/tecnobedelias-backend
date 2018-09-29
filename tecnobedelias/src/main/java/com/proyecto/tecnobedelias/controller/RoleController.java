package com.proyecto.tecnobedelias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.tecnobedelias.persistence.model.Role;
import com.proyecto.tecnobedelias.persistence.repository.RoleRepository;

@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping
	public List<Role> listarRoles() {
		return roleRepository.findAll();
	}
	
	@PostMapping("/crear")
	public void crearRole(@RequestBody Role role) {
		roleRepository.save(role);
	}

}
