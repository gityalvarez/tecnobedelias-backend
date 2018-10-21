package com.proyecto.tecnobedelias.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Horario;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.HorarioRepository;
import com.proyecto.tecnobedelias.service.CursoService;
import com.proyecto.tecnobedelias.service.HorarioService;

@Service
public class CursoServiceImpl implements CursoService {

	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	AsignaturaRepository asignaturaRepository;	
	
	@Autowired
	HorarioRepository horarioRepository;
	
	@Autowired
	HorarioService horarioService;
	
	@Override
	public boolean altaCurso(Curso curso/*, List<Horario> horarios*/) {		
		System.out.println("Entro a altaCurso");
		cursoRepository.save(curso);		
		return true;		
	}
	
	
	/*public void asignarHorarioCurso(Horario horario, Curso curso) {
		curso.getHorarios().add(horario);
		cursoRepository.save(curso);
	}*/
	

	@Override
	public List<Curso> listarCursos() {
		return cursoRepository.findAll();
	}

	@Override
	public boolean existeCurso(long cursoId) {
		return cursoRepository.existsById(cursoId);
	}
	
	@Override
	public boolean existeCurso(Asignatura asignatura, int semestre, int anio) {		
		Optional<Curso> cursoExistente = cursoRepository.findByAsignaturaAndSemestreAndAnio(asignatura, semestre, anio);
		if (cursoExistente.isPresent())
			return true;
		else return false;		
	}
	
	@Override
	public Optional<Curso> obtenerCurso(Asignatura asignatura, int semestre, int anio) {		
		return cursoRepository.findByAsignaturaAndSemestreAndAnio(asignatura, semestre, anio);				
	}
	
	@Override
	public Optional<Curso> obtenerCurso(long id) {		
		return cursoRepository.findById(id);				
	}

	@Override
	public void bajaCurso(Curso curso) {		
		cursoRepository.delete(curso);
		
	}
	
	

}
