package com.proyecto.tecnobedelias.service.impl;

import java.util.Optional;

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
public class InscripcionServiceImpl implements InscripcionService {

	@Autowired
	CarreraRepository carreraRepository;

	@Autowired
	Curso_EstudianteRepository cursoEstudianteRepository;

	@Autowired
	Examen_EstudianteRepository examenEstudianteRepository;

	@Override
	public boolean inscripcionCarrera(Usuario usuario, Carrera carrera) {
		if (usuario.getCarreras().contains(carrera)) {
			return false;
		} else {

			carrera.getEstudiantes().add(usuario);
			usuario.getCarreras().add(carrera);
			carreraRepository.save(carrera);
			return true;
		}
	}

	@Override
	public boolean inscripcionCurso(Usuario usuario, Curso curso) {
		Optional<Curso_Estudiante> cursoEstudianteExistente = cursoEstudianteRepository.findByCursoAndEstudiante(curso,
				usuario);

		if (cursoEstudianteExistente.isPresent()) {
			return false;
		} else {

			Curso_Estudiante cursoEstudiante = new Curso_Estudiante();
			cursoEstudiante.setCurso(curso);
			cursoEstudiante.setEstudiante(usuario);
			usuario.getCursoEstudiante().add(cursoEstudiante);
			curso.getCursoEstudiante().add(cursoEstudiante);
			cursoEstudianteRepository.save(cursoEstudiante);
			return true;
		}
	}

	@Override
	public boolean inscripcionExamen(Usuario usuario, Examen examen) {
		Optional<Examen_Estudiante> examenEstudianteExistente = examenEstudianteRepository
				.findByExamenAndEstudiante(examen, usuario);

		if (examenEstudianteExistente.isPresent()) {
			return false;
		} else {
			Examen_Estudiante examenEstudiante = new Examen_Estudiante();
			examenEstudiante.setEstudiante(usuario);
			examenEstudiante.setExamen(examen);
			examenEstudianteRepository.save(examenEstudiante);
			return true;
		}
	}

	@Override
	public boolean desistirCurso(Usuario usuario, Curso curso) {
		Optional<Curso_Estudiante> cursoEstudianteExistente = cursoEstudianteRepository.findByCursoAndEstudiante(curso,
				usuario);
		if (cursoEstudianteExistente.isPresent()) {
			usuario.getCursoEstudiante().remove(cursoEstudianteExistente.get());
			curso.getCursoEstudiante().remove(cursoEstudianteExistente.get());
			cursoEstudianteRepository.delete(cursoEstudianteExistente.get());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean desistirExamen(Usuario usuario, Examen examen) {
		
		Optional<Examen_Estudiante> examenEstudianteExistente = examenEstudianteRepository.findByExamenAndEstudiante(examen,
				usuario);
		if (examenEstudianteExistente.isPresent()) {
			usuario.getExamenEstudiante().remove(examenEstudianteExistente.get());
			examen.getExamenEstudiante().remove(examenEstudianteExistente.get());
			examenEstudianteRepository.delete(examenEstudianteExistente.get());
			return true;
		}else {
			return false;
		}
		

	}

}
