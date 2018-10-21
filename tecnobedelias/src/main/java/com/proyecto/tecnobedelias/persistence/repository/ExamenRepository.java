package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Examen;
@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long>{
	Optional<Examen> findByAsignaturaAndFecha(Asignatura asignatura, Date fecha);
}
