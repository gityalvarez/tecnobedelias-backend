package com.proyecto.tecnobedelias.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.tecnobedelias.persistence.model.Rol;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface UsuarioService {
	
	//Usuario loadUsuarioByUsername(String username);
	public List<Usuario> listarUsuarios();
	
	public boolean existeUsuario(String username);
	
	public void altaUsuario(Usuario usuario);
	
	public void altaBienUsuario(Usuario usuario);
	
	public List<Rol> listarRoles();
	
	public void asignarRolUsuario(String rolName, Usuario usuario);
	
	public List<Usuario> filtrarEstudiantes();
	
	public void bajaUsuario(Usuario usuario);
	
	public Optional<Usuario> findUsuarioByResetToken(String resetToken);
	
	public Optional<Usuario> findUsuarioByUsername(String username);
	
	public void inicializar();
}
