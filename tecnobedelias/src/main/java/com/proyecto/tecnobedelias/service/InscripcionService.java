package com.proyecto.tecnobedelias.service;

import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface InscripcionService {
	
	public void inscripcionCarrera(Usuario usuario, Carrera carrera);
	
	public void inscripcionCurso(Usuario usuario, Curso curso);

	public void inscripcionExamen(Usuario usuario, Examen examen);
	
	public void desistirCurso(Usuario usuario, Curso curso);
	
	public void desistirExamen(Usuario usuario, Examen examen);
	
	
}
