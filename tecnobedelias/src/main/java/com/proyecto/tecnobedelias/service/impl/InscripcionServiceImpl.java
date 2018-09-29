package com.proyecto.tecnobedelias.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Examen_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.Curso_EstudianteRepository;
import com.proyecto.tecnobedelias.persistence.repository.Examen_EstudianteRepository;
import com.proyecto.tecnobedelias.service.InscripcionService;

@Service
public class InscripcionServiceImpl implements InscripcionService{
	
	@Autowired
	CarreraRepository carreraRepository;
	
	@Autowired
	Curso_EstudianteRepository cursoEstudianteRepository;
	
	@Autowired
	Examen_EstudianteRepository examenEstudianteRepository;

	@Override
	public void inscripcionCarrera(Usuario usuario, Carrera carrera) {
		carrera.getEstudiantes().add(usuario);
		usuario.getCarreras().add(carrera);
		carreraRepository.save(carrera);
		
	}

	@Override
	public void inscripcionCurso(Usuario usuario, Curso curso) {
		Curso_Estudiante cursoEstudiante = new Curso_Estudiante();
		cursoEstudiante.setCurso(curso);
		cursoEstudiante.setEstudiante(usuario);
		usuario.getCursoEstudiante().add(cursoEstudiante);
		curso.getCursoEstudiante().add(cursoEstudiante);
		cursoEstudianteRepository.save(cursoEstudiante);
		
	}

	@Override
	public void inscripcionExamen(Usuario usuario, Examen examen) {
		Examen_Estudiante examenEstudiante = new Examen_Estudiante();
		examenEstudiante.setEstudiante(usuario);
		examenEstudiante.setExamen(examen);
		examenEstudianteRepository.save(examenEstudiante);
		
	}

	@Override
	public void desistirCurso(Usuario usuario, Curso curso) {
		Curso_Estudiante cursoEstudiante = cursoEstudianteRepository.findByCursoAndEstudiante(curso, usuario);
		usuario.getCursoEstudiante().remove(cursoEstudiante);
		curso.getCursoEstudiante().remove(cursoEstudiante);
		cursoEstudianteRepository.delete(cursoEstudiante);
		
	}

	@Override
	public void desistirExamen(Usuario usuario, Examen examen) {
		Examen_Estudiante examenEstudiante = examenEstudianteRepository.findByExamenAndEstudiante(examen, usuario);
		usuario.getExamenEstudiante().remove(examenEstudiante);
		examen.getExamenEstudiante().remove(examenEstudiante);
		examenEstudianteRepository.delete(examenEstudiante);
		
	}
	
	

}
