package com.proyecto.tecnobedelias.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.Curso_EstudianteRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.Estudiante_ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.ActaService;

@Service
public class ActaServiceImpl implements ActaService{

	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	Curso_EstudianteRepository cursoEstudianteRepository;
	
	@Autowired
	ExamenRepository examenRepository;
	
	@Autowired
	Estudiante_ExamenRepository examenEstudianteRepository;
	
	
	
	
	@Override
	public boolean cargarCalificacionesCurso(Map<Long, Integer> calificaciones, Curso curso) {
		if(cursoRepository.findAll().contains(curso)) {
			calificaciones.forEach((key,value)->{
				cursoEstudianteRepository.findByCursoAndEstudiante(curso,usuarioRepository.findById(key).get()).get().setNota(value);
				
			});
			return true;
		}else return false;
	}

	@Override
	public boolean cargarCalificacionesExamen(Map<Long, Integer> calificaciones, Examen examen) {
		if(examenRepository.findAll().contains(examen)) {
			calificaciones.forEach((key,value)->{
				examenEstudianteRepository.findByExamenAndEstudiante(examen, usuarioRepository.findById(key).get()).get().setNota(value);;
			});
			return true;
		}else return false;
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
	
	public ModelAndView downloadPdf() {
		System.out.println("entre al actaService");
		return new ModelAndView("PdfView", "usuarios", usuarioRepository.findAll());
	}
	

}
