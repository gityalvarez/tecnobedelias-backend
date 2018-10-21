package com.proyecto.tecnobedelias.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.service.ExamenService;

@Service
public class ExamenServiceImpl implements ExamenService {

	@Autowired
	ExamenRepository examenRepository;	
	
	@Autowired
	AsignaturaRepository asignaturaRepository;	
	
	@Override
	public boolean altaExamen(Examen examen) {
		System.out.println("Entro a altaExamen");
		examenRepository.save(examen);		
		return true;
	}
	
	@Override
	public List<Examen> listarExamenes(){
		return examenRepository.findAll();
	}
	
	@Override
	public boolean existeExamen(long examenId) {
		return examenRepository.existsById(examenId);
	}
	
	@Override
	public Optional<Examen> obtenerExamen(Asignatura asignatura, Date fecha) {
		return examenRepository.findByAsignaturaAndFecha(asignatura, fecha);
	}
	
	@Override
	public Optional<Examen> obtenerExamen(long id) {
		return examenRepository.findById(id);
	}
	
	@Override
	public void bajaExamen(Examen examen) {
		examenRepository.delete(examen);
	}

	@Override
	public boolean existeExamen(Asignatura asignatura, Date fecha) {
		Optional<Examen> examenExistente = examenRepository.findByAsignaturaAndFecha(asignatura, fecha);
		if (examenExistente.isPresent())
			return true;
		else return false;	
	}

	
}
