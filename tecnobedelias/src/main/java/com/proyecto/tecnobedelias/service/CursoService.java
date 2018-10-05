package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Curso;

public interface CursoService {
	
	public boolean altaCurso(Curso curso);
	
	public List<Curso> listarCursos();
	
	public boolean existeCurso(Curso curso);
	
	public void bajaCurso(Curso curso);

}
