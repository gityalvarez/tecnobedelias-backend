package com.proyecto.tecnobedelias.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Rol;
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {	
	Rol findByNombre(String nombre);

}
