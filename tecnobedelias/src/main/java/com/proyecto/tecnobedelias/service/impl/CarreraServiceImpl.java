package com.proyecto.tecnobedelias.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.Asignatura_CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.service.CarreraService;

import ch.qos.logback.core.net.SyslogOutputStream;

@Service
public class CarreraServiceImpl implements CarreraService {

	@Autowired
	AsignaturaRepository asignaturaRepository;

	@Autowired
	CarreraRepository carreraRepository;

	@Autowired
	Asignatura_CarreraRepository asignaturaCarreraRepository;
	
	public List<Carrera> listarCarreras(){
		return carreraRepository.findAll();
	}
	
	public boolean existeCarrera(String nombre) {
		Optional<Carrera> carreraExistente = carreraRepository.findByNombre(nombre);
		if (carreraExistente.isPresent()) return true;
		else return false;
	}
	
	public void altaCarrera(Carrera carrera) {
		carreraRepository.save(carrera);
	}

	@Override
	public boolean asignarAsignaturaCarrera(Asignatura asignatura, Carrera carrera) {
		Optional<Asignatura_Carrera> asignaturaCarreraExistente = asignaturaCarreraRepository
				.findByAsignaturaAndCarrera(asignatura, carrera);
		if (asignaturaCarreraExistente.isPresent()) {
			return false;
		} else {
			Asignatura_Carrera asignaturaCarrera = new Asignatura_Carrera();
			System.out.println("Dentro del carrera service");
			asignaturaCarrera.setAsignatura(asignatura);
			asignaturaCarrera.setCarrera(carrera);
			carrera.getAsignaturaCarrera().add(asignaturaCarrera);
			asignatura.getAsignaturaCarrera().add(asignaturaCarrera);
			asignaturaCarreraRepository.save(asignaturaCarrera);
			return true;
		}
	}

	@Override
	public boolean desasignarAsignaturaCarrera(Asignatura asignatura, Carrera carrera) {
		Optional<Asignatura_Carrera> asignaturaCarrera = asignaturaCarreraRepository
				.findByAsignaturaAndCarrera(asignatura, carrera);
		if (asignaturaCarrera.isPresent()) {
			asignatura.getAsignaturaCarrera().remove(asignaturaCarrera.get());
			carrera.getAsignaturaCarrera().remove(asignaturaCarrera.get());
			asignaturaCarreraRepository.delete(asignaturaCarrera.get());
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean agregarPreviaAsignatura(Asignatura_Carrera asignatura, Asignatura_Carrera asignaturaPrevia) {
		if (asignatura.getCarrera() == asignaturaPrevia.getCarrera()) {
			//se evita la referencia circular
			if (!esPrevia(asignaturaPrevia,asignatura)) {				
				asignatura.getPrevias().add(asignaturaPrevia);
				asignaturaPrevia.getPreviaDe().add(asignatura);
				asignaturaCarreraRepository.save(asignatura);
				return true;
			}else return false;
		}
		else {
			return false;
		}
	}	
	
	private boolean esPrevia(Asignatura_Carrera asignatura, Asignatura_Carrera previaABuscar) {		
		List<Asignatura_Carrera> previas = asignatura.getPrevias();
		for(Asignatura_Carrera previa : previas) {
			if (previa.equals(previaABuscar)) {
				return true;
			}else {
				if(esPrevia(previa,previaABuscar)) {
					return true;
				}
			}		
		}
		return false;		
	}

	@Override
	public boolean eliminarPreviaAsignatura(Asignatura_Carrera asignatura, Asignatura_Carrera asignaturaPrevia) {
		if(asignatura.getPrevias().contains(asignaturaPrevia)) {
			asignatura.getPrevias().remove(asignaturaPrevia);
			asignaturaPrevia.getPreviaDe().remove(asignatura);
			asignaturaCarreraRepository.save(asignatura);
			return true;
		}else {
			return false;
		}
			
	}

	@Override
	public List<Asignatura> filtrarAsignatura() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asignatura_Carrera> listarPrevias(Asignatura_Carrera asignaturaCarrera) {

		return asignaturaCarrera.getPrevias();

	}

	@Override
	public void generarGrafoPrevias() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Asignatura> listarAsignaturasFaltantes(String carrera) {
		System.out.println("entre al listarAsignaturasFaltantes en el service");
		Optional<Carrera> carreraOpt = carreraRepository.findByNombre(carrera);
		List<Asignatura> listaAsignaturas = new ArrayList<>();

		if (carreraOpt.isPresent()) {
			listaAsignaturas = asignaturaRepository.findAll();			
			for(Asignatura_Carrera asignaturaCarrera : carreraOpt.get().getAsignaturaCarrera()) {
				listaAsignaturas.removeIf(asignatura -> (asignatura == asignaturaCarrera.getAsignatura()));
				System.out.println("removi la asignatura "+asignaturaCarrera.getAsignatura().getNombre());
			}
							
		}
		return listaAsignaturas;
	}

}
