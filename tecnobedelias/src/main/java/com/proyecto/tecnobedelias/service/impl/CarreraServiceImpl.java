package com.proyecto.tecnobedelias.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Link;
import com.proyecto.tecnobedelias.persistence.model.Nodo;
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
	
	@Override
	public List<Carrera> listarCarreras(){
		return carreraRepository.findAll();
	}
	
	@Override
	public boolean existeCarrera(String nombre) {
		Optional<Carrera> carreraExistente = carreraRepository.findByNombre(nombre);
		if (carreraExistente.isPresent()) return true;
		else return false;
	}
	
	@Override
	public void altaCarrera(Carrera carrera) {
		carreraRepository.save(carrera);
	}
	
	@Override
	public Optional<Carrera> obtenerCarrera(long carreraId) {
		return carreraRepository.findById(carreraId);
	}
	
	@Override
	public Optional<Carrera> obtenerCarreraNombre(String nombre) {
		return carreraRepository.findByNombre(nombre);
	}
	
	@Override
	public void modificacionCarrera(Carrera carrera) {
		carreraRepository.save(carrera);
	}
				
				
	@Override
	public boolean asignarAsignaturaCarrera(Asignatura_Carrera asigncarrera) {
		Optional<Asignatura_Carrera> asignaturaCarreraExistente = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asigncarrera.getAsignatura(), asigncarrera.getCarrera());
		if (asignaturaCarreraExistente.isPresent()) {
			return false;
		} 
		if (asigncarrera.getCreditos() >= 0) {
			asigncarrera.getCarrera().getAsignaturaCarrera().add(asigncarrera);
			asigncarrera.getAsignatura().getAsignaturaCarrera().add(asigncarrera);
			asignaturaCarreraRepository.save(asigncarrera);
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean modificarAsignaturaCarrera(Asignatura_Carrera asign_carrera) {
		Optional<Asignatura_Carrera> asignaturaCarreraExistente = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asign_carrera.getAsignatura(), asign_carrera.getCarrera());
		if (!asignaturaCarreraExistente.isPresent()) {
			return false;
		} 
		if (asign_carrera.getCreditos() >= 0) {
			asign_carrera.setId(asignaturaCarreraExistente.get().getId());
			asignaturaCarreraRepository.save(asign_carrera);
			return true;
		}
		else return true;
	}
	

	@Override
	public boolean desasignarAsignaturaCarrera(Asignatura asignatura, Carrera carrera) {
		Optional<Asignatura_Carrera> asignaturaCarrera = asignaturaCarreraRepository
				.findByAsignaturaAndCarrera(asignatura, carrera);
		if (asignaturaCarrera.isPresent() && asignaturaCarrera.get().getPreviaDe().isEmpty()) {
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
			if (!esPrevia(asignaturaPrevia,asignatura) && !esPrevia(asignatura,asignaturaPrevia) ) {				
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
	public List<Asignatura> listarPrevias(Asignatura_Carrera asignaturaCarrera) {
		List<Asignatura> asignaturasPrevias = new ArrayList<>();
		for (Asignatura_Carrera previa : asignaturaCarrera.getPrevias()) {
			asignaturasPrevias.add(previa.getAsignatura());
		}
		return asignaturasPrevias;

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

	@Override
	public List<Asignatura> listarAsingaturas(String carrera) {
		Optional<Carrera> carreraOpt = carreraRepository.findByNombre(carrera);
		List<Asignatura> asignaturas = new ArrayList<>();
		if (carreraOpt.isPresent()) {
			
			for(Asignatura_Carrera asignaturaCarrera : carreraOpt.get().getAsignaturaCarrera()) {
				asignaturas.add(asignaturaCarrera.getAsignatura());
			}	
		}
		return asignaturas;
	}

	@Override
	public List<Asignatura> listarPreviasPosibles(Asignatura_Carrera asignaturaCarrera) {
		List<Asignatura> asignaturasPreviasPosibles = new ArrayList<>();
		asignaturasPreviasPosibles = this.listarAsingaturas(asignaturaCarrera.getCarrera().getNombre());
		for (Asignatura_Carrera previa : asignaturaCarrera.getPrevias()) {
			asignaturasPreviasPosibles.remove(previa.getAsignatura());
		}
		asignaturasPreviasPosibles.remove(asignaturaCarrera.getAsignatura());
		return asignaturasPreviasPosibles;
	}
	
	@Override
	public void bajaCarrera(Carrera carrera) {
		carreraRepository.delete(carrera);		
	}

	@Override
	public List<Nodo> listarNodosGrafo(Carrera carrera) {
		List<Nodo> listaNodos = new ArrayList<>();
		List<Asignatura_Carrera> asignaturasCarrera = carrera.getAsignaturaCarrera();
		// Random random = new Random();
		// String[] colores = {"blue", "red", "green", "darkorange", "purple"};
		List<String> colores = new ArrayList<>();
		colores.add("darkred");
		colores.add("tomato");
		colores.add("darkgreen");
		colores.add("darkcyan");
		colores.add("steelblue");
		colores.add("indigo");
		colores.add("purple");
		colores.add("deeppink");
		ListIterator<String> listaColores = colores.listIterator();
		for (Asignatura_Carrera asignaturaCarrera : asignaturasCarrera) {
			// int i = 0;
			Asignatura asignatura = asignaturaCarrera.getAsignatura();
			Nodo nodo = new Nodo();
			String stringLong = ""+asignatura.getId();
			nodo.setKey(stringLong);
			nodo.setName(asignatura.getNombre());
			
	        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
	        // int nextInt = random.nextInt(4);
			// int nextInt = random.nextInt(colores.size()-1);
	        // format it as hexadecimal string (with hashtag and leading zeros)
	        // String colorCode = colores.get(nextInt);
			
			if (!listaColores.hasNext()) listaColores = colores.listIterator();	
			String colorCode = listaColores.next();

			nodo.setColor(colorCode);
			listaNodos.add(nodo);
		}
		return listaNodos;
	}

	@Override
	public List<Link> listarLinkGrafo(Carrera carrera) {
		List<Link> listaLinks = new ArrayList<>();
		List<Asignatura_Carrera> asignaturasCarrera = carrera.getAsignaturaCarrera();
		for (Asignatura_Carrera asignaturaCarrera : asignaturasCarrera) {
			List<Asignatura_Carrera> previas = asignaturaCarrera.getPrevias();
			for(Asignatura_Carrera previa : previas) {
				Link link = new Link();
				String fromString = ""+previa.getAsignatura().getId();
				link.setFrom(fromString);
				String toString = ""+asignaturaCarrera.getAsignatura().getId();
				link.setTo(toString);
				listaLinks.add(link);				
			}
		}
		return listaLinks;
	}

}
