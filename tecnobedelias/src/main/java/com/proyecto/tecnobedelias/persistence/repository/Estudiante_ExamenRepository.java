package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Estudiante_Examen;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

@Repository
public interface Estudiante_ExamenRepository extends JpaRepository<Estudiante_Examen, Long>{
	
	public Optional<Estudiante_Examen> findByExamenAndEstudiante(Examen examen, Usuario estudiante);
	
}
