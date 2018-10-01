package com.proyecto.tecnobedelias.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.Curso_EstudianteRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.ActaService;

public class ActaServiceImpl implements ActaService{

	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	Curso_EstudianteRepository cursoEstudianteRepository;
	
	@Override
	public boolean cargarCalificacionesCurso(Map<Long, Integer> calificaciones, Curso curso) {
		if(cursoRepository.findAll().contains(curso)) {
			/*List<Usuario> estudiantes = new ArrayList();
			for(Curso_Estudiante cursoEstudiante: curso.getCursoEstudiante()) {
				estudiantes.add(cursoEstudiante.getEstudiante());
			}*/
			calificaciones.forEach((k,v)->{
				cursoEstudianteRepository.findByCursoAndEstudiante(curso,usuarioRepository.findById(k).get()).get().setNota(v);
				
			});
			return true;
		}else return false;
	}

	@Override
	public boolean cargarCalificacionesExamen(Map<Long, Integer> calificaciones, Examen examen) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void imprimirActaCurso(Curso curso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprimirActaExamen(Examen examen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprimirEscolaridad(Usuario estudiante) {
		// TODO Auto-generated method stub
		
	}
	
	

}
