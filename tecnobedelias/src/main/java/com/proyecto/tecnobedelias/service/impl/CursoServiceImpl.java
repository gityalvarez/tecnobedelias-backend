package com.proyecto.tecnobedelias.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.service.CursoService;

@Service
public class CursoServiceImpl implements CursoService{

	@Autowired
	CursoRepository cursoRepository;
	
	@Override
	public boolean altaCurso(Curso curso) {
		cursoRepository.save(curso);
		return true;
	}

	@Override
	public List<Curso> listarCursos() {
		return cursoRepository.findAll();
	}

	@Override
	public boolean existeCurso(Curso curso) {
		return cursoRepository.existsById(curso.getId());
	}

	@Override
	public void bajaCurso(Curso curso) {
		
		cursoRepository.delete(curso);
		
	}
	
	

}
