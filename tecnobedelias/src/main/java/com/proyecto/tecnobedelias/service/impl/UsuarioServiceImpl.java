package com.proyecto.tecnobedelias.service.impl;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Rol;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.RolRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	RolRepository rolRepository;
	
	@Autowired
	BCryptPasswordEncoder bcrypt;
	
	public List<Usuario> listarUsuarios(){
		return usuarioRepository.findAll();
	}
	
	
	public boolean existeUsuario(String username) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByUsername(username);
		if(usuarioExistente.isPresent()) return true;
		else return false;
		
	}
	
	public void altaUsuario(Usuario usuario) {
		usuarioRepository.save(usuario);
	}
	
	public List<Rol> listarRoles(){
		return rolRepository.findAll();
	}
	
	public void asignarRolUsuario(String rolName, Usuario usuario) {
		System.out.println("entre al usuario service");
		System.out.println("este es el rolName "+rolName);
		Rol rol = rolRepository.findByNombre(rolName);
		System.out.println("este es el rol "+rol.getNombre());
		//Optional<Usuario> usuarioOpt = usuarioRepository.
		usuario.getRoles().add(rol);
		
		/*if (usuario.getRoles() != null) {
			usuario.getRoles().add(rol);			
		}else usuario.setRol(rol);
		*/
		usuarioRepository.save(usuario);
		
	}
	
	
	public List<Usuario> filtrarEstudiantes() {
		return usuarioRepository.findAll().stream().filter(u -> u.getRoles().iterator().next().getNombre().equals("ESTUDIANTE")).collect(Collectors.toList());
	}
	
	
	public void bajaUsuario(Usuario usuario) {
		usuarioRepository.delete(usuario);
	}
	
	@Override
	public Optional findUsuarioByResetToken(String resetToken) {
		return usuarioRepository.findByResetToken(resetToken);
	}


	@Override
	public Optional<Usuario> findUsuarioByUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}


	@Override
	public void altaBienUsuario(Usuario usuario) {
		usuario.setResetToken(UUID.randomUUID().toString());
		usuarioRepository.save(usuario);
		
		
		
		
	}


	@Override
	public void inicializar() {
		Rol rolAdministrador = rolRepository.findByNombre("ADMINISTRADOR");
		Rol rolEstudiante = rolRepository.findByNombre("ESTUDIANTE");
		Rol rolDirector = rolRepository.findByNombre("DIRECTOR");
		Rol rolFuncionario = rolRepository.findByNombre("FUNCIONARIO");
		Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername("administrador");
		
		
		if(rolAdministrador==null) {
			Rol rol = new Rol();
			rol.setNombre("ADMINISTRADOR");
			rolRepository.save(rol);
		}
		if(rolFuncionario==null) {
			Rol rol = new Rol();
			rol.setNombre("FUNCIONARIO");
			rolRepository.save(rol);
		}
		if(rolDirector ==null) {
			Rol rol = new Rol();
			rol.setNombre("DIRECTOR");
			rolRepository.save(rol);
		}
		if(rolEstudiante ==null) {
			Rol rol = new Rol();
			rol.setNombre("ESTUDIANTE");
			rolRepository.save(rol);
		}
		
		if(!usuarioOpt.isPresent()) {
			Usuario usuario = new Usuario();
			usuario.setUsername("administrador");
			usuario.setNombre("administrador");
			usuario.setApellido("administrador");
			usuario.setEmail("admin@gmail.com");
			usuario.setPassword(bcrypt.encode("123456"));
			usuario.setCedula("12345678");
			Rol rol = rolRepository.findByNombre("ADMINISTRADOR");
			usuario.getRoles().add(rol);
			usuarioRepository.save(usuario);
			
		}
		
		
	}
	
	
	

}
