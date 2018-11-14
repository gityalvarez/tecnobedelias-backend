package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.Util.Response;
import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface InscripcionService {
	
	public Response inscripcionCarrera(Usuario usuario, Carrera carrera);
	
	public Response inscripcionCurso(Usuario usuario, Curso curso);

	public Response inscripcionExamen(Usuario usuario, Examen examen);
	
	public Response desistirCurso(Usuario usuario, Curso curso);
	
	public Response desistirExamen(Usuario usuario, Examen examen);
	
	//public boolean desistirCarrera(Usuario usuario, Carrera carrera);
	
	public List<Curso> consultaCursos(Usuario usuario);
	
	public List<Curso> consultaCursosDisponibles(Usuario usuario);
	
	public List<Examen> consultaExamenes(Usuario usuario);
	
	public List<Examen> consultaExamenesDisponibles(Usuario usuario);
	
	public Usuario obtenerEstudianteCursoEstudiante(long id_curso_est);
	
	public Usuario obtenerEstudianteEstudianteExamen(long id_est_examen);
	
	public Response ingresarCalificacionExamen(Usuario usuario, Examen examen, int nota);
	
	public Response ingresarCalificacionCurso(Usuario usuario, Curso curso, int nota);
}
