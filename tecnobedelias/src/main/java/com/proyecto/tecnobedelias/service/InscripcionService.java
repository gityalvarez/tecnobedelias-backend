package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Estudiante_Examen;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

public interface InscripcionService {
	
	public boolean inscripcionCarrera(Usuario usuario, Carrera carrera);
	
	public boolean inscripcionCurso(Usuario usuario, Curso curso);

	public boolean inscripcionExamen(Usuario usuario, Examen examen);
	
	public boolean desistirCurso(Usuario usuario, Curso curso);
	
	public boolean desistirExamen(Usuario usuario, Examen examen);
	
	//public boolean desistirCarrera(Usuario usuario, Carrera carrera);
	
	public List<Curso> consultaCursos(Usuario usuario);
	
	public List<Examen> consultaExamenes(Usuario usuario);
	
	public Usuario obtenerEstudianteCursoEstudiante(long id_curso_est);
	
	public Usuario obtenerEstudianteEstudianteExamen(long id_est_examen);
	
	public boolean ingresarCalificacionExamen(Usuario usuario, Examen examen, int nota);
	
	public boolean ingresarCalificacionCurso(Usuario usuario, Curso curso, int nota);
}
