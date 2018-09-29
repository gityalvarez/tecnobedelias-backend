package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Carrera;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long>{
	Optional<Carrera> findByNombre(String nombre);
}



