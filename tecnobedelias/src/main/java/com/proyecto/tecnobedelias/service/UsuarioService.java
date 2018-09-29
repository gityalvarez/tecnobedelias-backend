package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface UsuarioService {
	
	//Usuario loadUsuarioByUsername(String username);
	public void asignarRolUsuario(String rolName, Usuario usuario);
	
	public List<Usuario> filtrarEstudiantes();
}
