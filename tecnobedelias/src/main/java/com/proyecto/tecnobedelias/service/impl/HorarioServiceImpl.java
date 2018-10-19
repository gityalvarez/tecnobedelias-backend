package com.proyecto.tecnobedelias.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Horario;
import com.proyecto.tecnobedelias.persistence.repository.HorarioRepository;
import com.proyecto.tecnobedelias.service.HorarioService;

@Service
public class HorarioServiceImpl implements HorarioService {

	@Autowired
	HorarioRepository horarioRepository;
	
	public boolean altaHorario(Horario horario) {
		if (existeHorario(horario.getDia(), horario.getHoraInicio(), horario.getHoraFin())) {
			System.out.println("horario encontrado");
			return false;
		}
		else {
			horarioRepository.save(horario);
			System.out.println("horario guardado");
			return true;
		}		
	}
	
	public boolean existeHorario(String dia, String horaInicio, String horaFin) {
		List<Horario> horarios = horarioRepository.findAll();
		for (Horario h : horarios) {
			if (h.getDia().equals(dia) && h.getHoraInicio().equals(horaInicio) && h.getHoraFin().equals(horaFin))
				return true;
		}			
		return false;
	}
	
}
