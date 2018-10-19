package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
	Optional<Curso> findByAsignaturaAndSemestreAndAnio(Asignatura asignatura, int semestre, int anio);
}
