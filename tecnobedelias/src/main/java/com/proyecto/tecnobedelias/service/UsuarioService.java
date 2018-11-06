package com.proyecto.tecnobedelias.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.tecnobedelias.persistence.model.Actividad;
import com.proyecto.tecnobedelias.persistence.model.Rol;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface UsuarioService {
	
	//Usuario loadUsuarioByUsername(String username);
	public List<Usuario> listarUsuarios();
	
	public boolean existeUsuario(String username);
	
	public boolean existeCedula(String cedula);
	
	public boolean existeEmail(String username);
	
	public void altaUsuario(Usuario usuario);
	
	public void altaBienUsuario(Usuario usuario);
	
	public List<Rol> listarRoles();
	
	public void asignarRolUsuario(String rolName, Usuario usuario);
	
	public List<Usuario> filtrarEstudiantes();
	
	public void bajaUsuario(Usuario usuario);
	
	public Optional<Usuario> findUsuarioByResetToken(String resetToken);
	
	public Optional<Usuario> findUsuarioByUsername(String username);
	
	public Optional<Usuario> obtenerUsuarioCedula(String cedula);	
	
	public Optional<Usuario> obtenerUsuarioEmail(String email);
	
	public Optional<Usuario> obtenerUsuario(long usuarioId);
	
	public void inicializar();
	
	public void modificacionUsuario(Usuario usuario);
	
	public List<Actividad> escolaridad(Usuario usuario);
}
