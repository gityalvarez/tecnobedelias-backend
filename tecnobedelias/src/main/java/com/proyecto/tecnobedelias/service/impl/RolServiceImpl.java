package com.proyecto.tecnobedelias.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Rol;
import com.proyecto.tecnobedelias.persistence.repository.RolRepository;
import com.proyecto.tecnobedelias.service.RolService;

@Service
public class RolServiceImpl implements RolService {
	
	@Autowired
	RolRepository rolRepository;
	
	public void altaRol(Rol rol) {
		rolRepository.save(rol);
	}
	
	public List<Rol> listarRoles() {
		return rolRepository.findAll();
	}

}
