package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Rol;

public interface RolService {

	public void altaRol(Rol rol);
	
	public List<Rol> listarRoles();
	
}
