package com.proyecto.tecnobedelias.service;

import com.proyecto.tecnobedelias.persistence.model.Horario;

public interface HorarioService {
	
	public boolean altaHorario(Horario horario);
	
	public boolean existeHorario(String dia, String horaInicio, String horaFin);
	
	public void bajaHorario(Horario horario);

}
