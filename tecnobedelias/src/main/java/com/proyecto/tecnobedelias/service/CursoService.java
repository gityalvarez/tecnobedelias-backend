package com.proyecto.tecnobedelias.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;

public interface CursoService {
	
	public boolean altaCurso(Curso curso/*, List<Horario> horarios*/);
	
	public List<Curso> listarCursos();
	
	public boolean existeCurso(long cursoId);
	
	public Optional<Curso> obtenerCurso(Asignatura asignatura, int semestre, int anio);
	
	public Optional<Curso> obtenerCurso(long id);
	
	public void bajaCurso(Curso curso);
	
	public void modificacionCurso(Curso curso);

	public boolean existeCurso(Asignatura asignatura, int semestre, int anio);

}
