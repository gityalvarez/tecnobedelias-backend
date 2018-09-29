package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura,Long> {
	
	Optional<Asignatura> findByNombre(String nombre);
}
