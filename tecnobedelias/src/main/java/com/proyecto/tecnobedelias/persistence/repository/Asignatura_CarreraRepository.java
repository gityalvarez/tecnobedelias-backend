package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;

@Repository
public interface Asignatura_CarreraRepository extends JpaRepository<Asignatura_Carrera,Long>{
	
	public Optional<Asignatura_Carrera> findByAsignaturaAndCarrera(Asignatura asignatura,Carrera carrera);

}
