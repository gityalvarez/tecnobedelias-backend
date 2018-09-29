package com.proyecto.tecnobedelias.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Usuario findByUsername(String username);

}
