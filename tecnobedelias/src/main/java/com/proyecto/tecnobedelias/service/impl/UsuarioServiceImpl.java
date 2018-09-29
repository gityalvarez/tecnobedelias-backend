package com.proyecto.tecnobedelias.service.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Role;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.RoleRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	public void asignarRolUsuario(String rolName, Usuario usuario) {
		System.out.println("entre al usuario service");
		System.out.println("este es el rolName "+rolName);
		Role rol = roleRepository.findByName(rolName);
		System.out.println("este es el rol "+rol.getName());
		usuario.getRoles().add(rol);
		usuarioRepository.save(usuario);
		
	}
	
	
	public List<Usuario> filtrarEstudiantes() {
		return usuarioRepository.findAll().stream().filter(u -> u.getRoles().iterator().next().getName().equals("ESTUDIANTE")).collect(Collectors.toList());
	}
	
	

}
