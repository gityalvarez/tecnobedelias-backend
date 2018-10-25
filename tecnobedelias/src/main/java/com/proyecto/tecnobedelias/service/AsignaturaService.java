package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;

public interface AsignaturaService {

	public void altaAsignatura(Asignatura asignatura);
	
	public List<Asignatura> listarAsignaturas();
	
	public boolean existeAsignatura(long asignaturaId);
	
	public boolean existeAsignaturaCodigo(String codigo);
	
	public boolean existeAsignaturaNombre(String nombre);
	
	public void bajaAsignatura(Asignatura asignatura);
	
}
