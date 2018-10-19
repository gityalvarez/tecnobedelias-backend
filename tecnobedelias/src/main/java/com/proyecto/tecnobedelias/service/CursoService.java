package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Horario;

public interface CursoService {
	
	public boolean altaCurso(Curso curso/*, List<Horario> horarios*/);
	
	public List<Curso> listarCursos();
	
	public boolean existeCurso(Curso curso);
	
	public void bajaCurso(Curso curso);

	boolean existeCurso(Asignatura asignatura, int semestre, int anio);

}
