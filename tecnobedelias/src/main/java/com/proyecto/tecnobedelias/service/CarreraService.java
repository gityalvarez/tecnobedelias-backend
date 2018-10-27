package com.proyecto.tecnobedelias.service;

import java.util.List;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Link;
import com.proyecto.tecnobedelias.persistence.model.Nodo;


public interface CarreraService {
	
	public List<Carrera> listarCarreras();
	
	public boolean existeCarrera(String nombre);
	
	public void altaCarrera(Carrera carrera);
	
	public boolean asignarAsignaturaCarrera(Asignatura_Carrera asigncarrera);
	
	public boolean modificarAsignaturaCarrera(Asignatura_Carrera asigncarrera);
	
	public boolean desasignarAsignaturaCarrera(Asignatura asignatura, Carrera carrera);
	
	public boolean agregarPreviaAsignatura(Asignatura_Carrera asignatura, Asignatura_Carrera previa);
	
	public boolean eliminarPreviaAsignatura(Asignatura_Carrera asignatura, Asignatura_Carrera previa);
	
	public List<Asignatura> listarAsingaturas(String carrera);
	
	public List<Asignatura> listarAsignaturasFaltantes(String carrera);
	
	public List<Asignatura> filtrarAsignatura();
	
	public List<Asignatura> listarPrevias(Asignatura_Carrera asignaturaCarrera);
	
	public List<Asignatura> listarPreviasPosibles(Asignatura_Carrera asignaturaCarrera);
	
	public void generarGrafoPrevias();
	
	public void bajaCarrera(Carrera carrera);
	
	public List<Nodo> listarNodosGrafo(Carrera carrera);
	
	public List<Link> listarLinkGrafo(Carrera carrera);
}
