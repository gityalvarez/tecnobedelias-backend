package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;


public interface CarreraService {
	
	public boolean asignarAsignaturaCarrera(Asignatura asignatura, Carrera carrera);
	
	public boolean desasignarAsignaturaCarrera(Asignatura asignatura, Carrera carrera);
	
	public boolean agregarPreviaAsignatura(Asignatura_Carrera asignatura, Asignatura_Carrera previa);
	
	public boolean eliminarPreviaAsignatura(Asignatura_Carrera asignatura, Asignatura_Carrera previa);
	
	public List<Asignatura> filtrarAsignatura();
	
	public List<Asignatura_Carrera> listarPrevias(Asignatura_Carrera asignaturaCarrera);
	
	public void generarGrafoPrevias();
	
}
