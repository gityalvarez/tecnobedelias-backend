package com.proyecto.tecnobedelias.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.tecnobedelias.persistence.model.Horario;
@Repository 
public interface HorarioRepository extends JpaRepository<Horario, Long> {
	Optional<Horario> findByDiaAndHoraInicioAndHoraFin(String dia, String horaInicio, String horaFin);
}
