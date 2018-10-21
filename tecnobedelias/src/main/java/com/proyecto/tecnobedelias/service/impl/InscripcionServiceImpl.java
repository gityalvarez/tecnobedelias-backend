package com.proyecto.tecnobedelias.service.impl;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Estudiante_Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.Curso_EstudianteRepository;
import com.proyecto.tecnobedelias.persistence.repository.Estudiante_ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.InscripcionService;

@Service
public class InscripcionServiceImpl implements InscripcionService {

	@Autowired
	CarreraRepository carreraRepository;

	@Autowired
	Curso_EstudianteRepository cursoEstudianteRepository;

	@Autowired
	Estudiante_ExamenRepository estudianteExamenRepository;

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
		Optional<Curso_Estudiante> cursoEstudianteExistente = cursoEstudianteRepository.findByCursoAndEstudiante(curso,	usuario);
		boolean matriculado = false;
		boolean puedeMatricularse = true; 
		// si ya está matriculado o salvo la asignatura correspondiente al curso no puede matricularse 
		if (cursoEstudianteExistente.isPresent())
			if (cursoEstudianteExistente.get().getEstado().equals("MATRICULADO") || cursoEstudianteExistente.get().getEstado().equals("SALVADO"))	
				puedeMatricularse = false;		 
		// si no recorro las carreras en las que el usuario esta inscripto y me fijo si la asignatura del curso pertenece a una de estas carreras
		if (puedeMatricularse) {
			boolean asignaturaEnCarrera = false;			
			Iterator<Carrera> itCarreras = usuario.getCarreras().iterator();
			while (itCarreras.hasNext() && !asignaturaEnCarrera) {
				Iterator<Asignatura_Carrera> itAsignCarreras = itCarreras.next().getAsignaturaCarrera().iterator();
				while (itAsignCarreras.hasNext() && !asignaturaEnCarrera) {
					if (itAsignCarreras.next().getAsignatura().equals(curso.getAsignatura()))
						asignaturaEnCarrera = true;
				}
			}			 
			/*for (Carrera carrera : usuario.getCarreras()) {
				for (Asignatura_Carrera asign_carrera : carrera.getAsignaturaCarrera()) {
					if (asign_carrera.getAsignatura().equals(curso.getAsignatura()))
						encontrada = true;
				}				
			}*/			
			if (asignaturaEnCarrera) {
				Curso_Estudiante cursoEstudiante = new Curso_Estudiante();
				cursoEstudiante.setCurso(curso);
				cursoEstudiante.setEstudiante(usuario);
				cursoEstudiante.setEstado("MATRICULADO");
				usuario.getCursoEstudiante().add(cursoEstudiante);
				curso.getCursoEstudiante().add(cursoEstudiante);
				cursoEstudianteRepository.save(cursoEstudiante);
				matriculado = true;
			}			
		}
		return matriculado;
	}	

	@Override
	public boolean inscripcionExamen(Usuario usuario, Examen examen) {		
		Optional<Estudiante_Examen> estudianteExamenExistente = estudianteExamenRepository.findByExamenAndEstudiante(examen, usuario);
		// si ya esta inscripto al examen retorno false
		boolean anotado = false;
		boolean puedeAnotarse = true;
		if (estudianteExamenExistente.isPresent()) // si ya está matriculado o salvo la asignatura correspondiente al curso no puede matricularse 
			if (estudianteExamenExistente.get().getEstado().equals("ANOTADO") || estudianteExamenExistente.get().getEstado().equals("APROBADO"))	
				puedeAnotarse = false;	
		if (puedeAnotarse) {
			//corroboro que se haya inscripto al curso de esa asignatura y que llegue a la nota de examen	
			boolean asignaturaEnCarrera = false;			
			Iterator<Carrera> itCarreras = usuario.getCarreras().iterator();
			while (itCarreras.hasNext() && !asignaturaEnCarrera) {
				Iterator<Asignatura_Carrera> itAsignCarreras = itCarreras.next().getAsignaturaCarrera().iterator();
				while (itAsignCarreras.hasNext() && !asignaturaEnCarrera) {
					if (itAsignCarreras.next().getAsignatura().equals(examen.getAsignatura()))
						asignaturaEnCarrera = true;
				}
			}			 
			if (asignaturaEnCarrera) {
				Iterator<Curso_Estudiante> itCursoEst = usuario.getCursoEstudiante().iterator();
				while (itCursoEst.hasNext() && !anotado) {
					if (itCursoEst.next().getCurso().getAsignatura().equals(examen.getAsignatura())) {
						if (itCursoEst.next().getEstado().equals("EXAMEN")) {
							Estudiante_Examen examenEstudiante = new Estudiante_Examen();
							examenEstudiante.setEstudiante(usuario);
							examenEstudiante.setExamen(examen);
							examenEstudiante.setEstado("ANOTADO");
							estudianteExamenRepository.save(examenEstudiante);
							anotado = true;						
						}
					}
				}
				/*for (Curso_Estudiante cursoEstudiante: usuario.getCursoEstudiante()) {
					if (cursoEstudiante.getCurso().getAsignatura().equals(examen.getAsignatura())) {
						if (cursoEstudiante.getEstado().equals("EXAMEN")) {
							Estudiante_Examen examenEstudiante = new Estudiante_Examen();
							examenEstudiante.setEstudiante(usuario);
							examenEstudiante.setExamen(examen);
							examenEstudiante.setEstado("ANOTADO");
							estudianteExamenRepository.save(examenEstudiante);
							anotado = true;						
						}
					}
				}*/
			}
		}
		return anotado;			
	}
	

	@Override
	public boolean desistirCurso(Usuario usuario, Curso curso) {
		Optional<Curso_Estudiante> cursoEstudianteExistente = cursoEstudianteRepository.findByCursoAndEstudiante(curso,	usuario);
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
		
		Optional<Estudiante_Examen> estudianteExamenExistente = estudianteExamenRepository.findByExamenAndEstudiante(examen, usuario);
		if (estudianteExamenExistente.isPresent()) {
			usuario.getEstudianteExamen().remove(estudianteExamenExistente.get());
			examen.getEstudianteExamen().remove(estudianteExamenExistente.get());
			estudianteExamenRepository.delete(estudianteExamenExistente.get());
			return true;
		}else {
			return false;
		}
		

	}

	@Override
	public boolean desistirCarrera(Usuario usuario, Carrera carrera) {
		if (usuario.getCarreras().contains(carrera)) {
			usuario.getCarreras().remove(carrera);
			carrera.getEstudiantes().remove(usuario);
			carreraRepository.save(carrera);
			return true;
		}
		return false;
	}

}
