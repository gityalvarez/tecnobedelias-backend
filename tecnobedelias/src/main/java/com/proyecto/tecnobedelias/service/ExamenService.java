package com.proyecto.tecnobedelias.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Examen;

public interface ExamenService {
	
	public boolean altaExamen(Examen examen);
	
	public List<Examen> listarExamenes();
	
	public boolean existeExamen(long examenId);
	
	public Optional<Examen> obtenerExamen(Asignatura asignatura, Date fecha);
	
	public Optional<Examen> obtenerExamen(long id);
	
	public void bajaExamen(Examen examen);

	public boolean existeExamen(Asignatura asignatura, Date fecha);


}
