package com.proyecto.tecnobedelias.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;

public interface AsignaturaService {

	public void altaAsignatura(Asignatura asignatura);
	
	public List<Asignatura> listarAsignaturas();
	
	public boolean existeAsignatura(long asignaturaId);
	
	public boolean existeAsignaturaCodigo(String codigo);
	
	public boolean existeAsignaturaNombre(String nombre);
	
	public Optional<Asignatura> obtenerAsignatura(long asignaturaId);
	
	public Optional<Asignatura> obtenerAsignaturaNombre(String nombre);
	
	public Optional<Asignatura> obtenerAsignaturaCodigo(String codigo);
	
	public void bajaAsignatura(Asignatura asignatura);
	
	public void modificacionAsignatura(Asignatura asignatura);
	
}
