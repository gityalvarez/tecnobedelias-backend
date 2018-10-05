package com.proyecto.tecnobedelias.service.impl;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<Usuario> listarUsuarios(){
		return usuarioRepository.findAll();
	}
	
	
	public boolean existeUsuario(Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(usuario.getUsername());
		if(usuarioExistente.isPresent()) return true;
		else return false;
		
	}
	
	public void altaUsuario(Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	public List<Role> listarRoles(){
		return roleRepository.findAll();
	}
	
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
	
	
	public void bajaUsuario(Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
	@Override
	public Optional findUsuarioByResetToken(String resetToken) {
		return usuarioRepository.findByResetToken(resetToken);
	}
	

}
