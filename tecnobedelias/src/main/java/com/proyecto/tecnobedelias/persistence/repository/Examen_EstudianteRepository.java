package com.proyecto.tecnobedelias.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Examen_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

@Repository
public interface Examen_EstudianteRepository extends JpaRepository<Examen_Estudiante, Long>{
	
	public Examen_Estudiante findByExamenAndEstudiante(Examen examen, Usuario estudiante);
	
}
