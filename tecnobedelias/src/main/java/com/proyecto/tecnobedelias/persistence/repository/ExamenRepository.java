package com.proyecto.tecnobedelias.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Examen;
@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long>{

}
