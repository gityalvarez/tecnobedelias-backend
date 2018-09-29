package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
	
	Role findByName(String name);

}
