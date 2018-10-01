package com.proyecto.tecnobedelias.service;

import java.util.Map;

import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface ActaService {
	
	public boolean cargarCalificacionesCurso(Map<Long,Integer> calificaciones, Curso curso);
	
	public boolean cargarCalificacionesExamen(Map<Long,Integer> calificaciones, Examen examen);
	
	public void imprimirActaCurso(Curso curso);
	
	public void imprimirActaExamen(Examen examen);
	
	public void imprimirEscolaridad(Usuario estudiante);
	
	
}
