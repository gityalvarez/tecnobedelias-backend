package com.proyecto.tecnobedelias.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Usuario;

@Repository
public interface Curso_EstudianteRepository extends JpaRepository<Curso_Estudiante, Long>{
	
	public Curso_Estudiante findByCursoAndEstudiante(Curso curso, Usuario estudiante);

}
