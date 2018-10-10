package com.proyecto.tecnobedelias.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.tecnobedelias.persistence.model.Role;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface UsuarioService {
	
	//Usuario loadUsuarioByUsername(String username);
	public List<Usuario> listarUsuarios();
	
	public boolean existeUsuario(Usuario usuario);
	
	public void altaUsuario(Usuario usuario);
	
	public List<Role> listarRoles();
	
	public void asignarRolUsuario(String rolName, Usuario usuario);
	
	public List<Usuario> filtrarEstudiantes();
	
	public void bajaUsuario(Usuario usuario);
	
	public Optional<Usuario> findUsuarioByResetToken(String resetToken);
	
	public Optional<Usuario> findUsuarioByUsername(String username);
}
