package com.proyecto.tecnobedelias.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Link;
import com.proyecto.tecnobedelias.persistence.model.Nodo;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.Asignatura_CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.CarreraService;

@RestController

@RequestMapping("/carrera")
public class CarreraController{
	
	@Autowired
	CarreraService carreraService;
	
	@Autowired
	AsignaturaService asignaturaService;
	
	private Asignatura_CarreraRepository asignaturaCarreraRepository;
	public CarreraController(Asignatura_CarreraRepository asignaturaCarreraRepository ) {
		super();
		this.asignaturaCarreraRepository = asignaturaCarreraRepository;
	}
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE')")
	public List<Carrera> listarCarreras() {
		return carreraService.listarCarreras();
	}
	
    @PostMapping("/verificar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean verificarCarrera(HttpServletRequest request,
			@RequestParam(name = "nombre", required = true) String carreraNombre) {
    	return carreraService.existeCarrera(carreraNombre);
    }

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean crearCarrera(@RequestBody Carrera carrera){
    	if (!carreraService.existeCarrera(carrera.getNombre())) {
    		if (carrera.getCreditosMinimos() >= 0) {
    			carreraService.altaCarrera(carrera);
    			return true;
    		}
    		else return false;
    	}
    	else return false;
    }
    
    @GetMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean borrarCarrera(HttpServletRequest request,
			@RequestParam(name = "carreraId", required = true) String carreraIdStr) {
    	long carreraId = Long.parseLong(carreraIdStr);
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarrera(carreraId);
    	if (carreraOpt.isPresent()) {
    		if (carreraOpt.get().getAsignaturaCarrera().isEmpty()) {
    			if (carreraOpt.get().getEstudiantes().isEmpty()) {    		
    				carreraService.bajaCarrera(carreraOpt.get());
    				return true;
    			}
    			else return false;
    		}
    		else return false;  
    	}
    	else return false;
    }
    
    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean modificarCarrera(HttpServletRequest request,
    		@RequestBody(required = true) Carrera carrera,
			@RequestParam(name = "carreraId", required = true) String carreraIdStr) {
    	long carreraId = Long.parseLong(carreraIdStr);
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarrera(carreraId);
    	if (carreraOpt.isPresent()) {
    		carreraOpt.get().setDescripcion(carrera.getDescripcion());
    		carreraOpt.get().setCreditosMinimos(carrera.getCreditosMinimos());
    		carreraService.modificacionCarrera(carreraOpt.get());
    		return true;
    	}
    	else return false;
    }    
    
    @PostMapping("/asignarasignatura")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean asignarAsignaturaCarrera(HttpServletRequest request, @RequestBody(required = true) Asignatura_Carrera asign_carrera,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	System.out.println("asi entro el parametro carreraNombre "+carreraNombre);
    	System.out.println("asi entro el parametro asignaturaNombre "+asignaturaNombre);
    	Optional<Carrera> carrera =  carreraService.obtenerCarreraNombre(carreraNombre);
    	System.out.println("obtuve la carrera "+carrera.get().getNombre());
    	Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignatura.get().getNombre());
    	System.out.println("entro al carreraService");  
    	asign_carrera.setAsignatura(asignatura.get());
    	asign_carrera.setCarrera(carrera.get());
    	return carreraService.asignarAsignaturaCarrera(asign_carrera);
    }  
    
    @PostMapping("/modificarasignaturacarrera")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean modificarAsignaturaCarrera(HttpServletRequest request, @RequestBody(required = true) Asignatura_Carrera asign_carrera,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	Optional<Carrera> carrera =  carreraService.obtenerCarreraNombre(carreraNombre);
    	System.out.println("obtuve la carrera "+carrera.get().getNombre());
    	Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignatura.get().getNombre());
    	System.out.println("entro al carreraService");  
    	asign_carrera.setAsignatura(asignatura.get());
    	asign_carrera.setCarrera(carrera.get());
    	return carreraService.modificarAsignaturaCarrera(asign_carrera);
    }
    
    @GetMapping("/desasignarasignatura")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean desasignarAsignaturaCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	System.out.println("asi entro el parametro carreraNombre "+carreraNombre);
    	System.out.println("asi entro el parametro asignaturaNombre "+asignaturaNombre);
    	Optional<Carrera> carrera =  carreraService.obtenerCarreraNombre(carreraNombre);
    	System.out.println("obtuve la carrera "+carrera.get().getNombre());
    	Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignatura.get().getNombre());
    	System.out.println("entro al carreraService");
    	if (carreraService.desasignarAsignaturaCarrera(asignatura.get(), carrera.get())) {
    		return true;
    	}
    	else return false;
    }
    
    @GetMapping("/listarasignaturas/{nombre}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE')")
	public List<Asignatura> listarAsignaturas(@PathVariable(value = "nombre") String nombre){
		System.out.println("entre al listarAsignaturas con la carrera "+nombre);
		return carreraService.listarAsingaturas(nombre);		
	}
    
	@GetMapping("/listarasignaturasfaltantes/{nombre}")
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
	public List<Asignatura> listarAsignaturasFaltantes(@PathVariable(value = "nombre") String nombre){
		System.out.println("entre al listaFaltantes con la carrera "+nombre);
		return carreraService.listarAsignaturasFaltantes(nombre);		
	}
    
    
    @GetMapping("/asignarprevia")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean asignarPrevia(HttpServletRequest request,
    		@RequestParam(name = "carrera", required = true) String carrera,
			@RequestParam(name = "asignatura", required = true) String asignatura,
			@RequestParam(name = "asignaturaPrevia", required = true) String asignaturaPrevia) {
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarreraNombre(carrera);
    	Optional<Asignatura> asignaturaOpt = asignaturaService.obtenerAsignaturaNombre(asignatura);
    	Optional<Asignatura> asignaturaPreviaOpt = asignaturaService.obtenerAsignaturaNombre(asignaturaPrevia);
    	if (carreraOpt.isPresent() && asignaturaOpt.isPresent()) {
        	Optional<Asignatura_Carrera> asignaturaCarreraOpt = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignaturaOpt.get(), carreraOpt.get());
        	Optional<Asignatura_Carrera> asignaturaCarreraPreviaOpt = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignaturaPreviaOpt.get(), carreraOpt.get());
        	if (asignaturaCarreraOpt.isPresent() && asignaturaCarreraPreviaOpt.isPresent()) {
        		if(carreraService.agregarPreviaAsignatura(asignaturaCarreraOpt.get(), asignaturaCarreraPreviaOpt.get())) {
        			return true;
        		};
        	}

    	}
    	return false;
    }
    
    @GetMapping("/desasignarprevia")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean eliminarPrevia(HttpServletRequest request,
    		@RequestParam(name = "carrera", required = true) String carrera,
			@RequestParam(name = "asignatura", required = true) String asignatura,
			@RequestParam(name = "asignaturaPrevia", required = true) String asignaturaPrevia) {    	
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarreraNombre(carrera);
    	Optional<Asignatura> asignaturaOpt = asignaturaService.obtenerAsignaturaNombre(asignatura);
    	Optional<Asignatura> asignaturaPreviaOpt = asignaturaService.obtenerAsignaturaNombre(asignaturaPrevia);
    	if (carreraOpt.isPresent() && asignaturaOpt.isPresent()) {
        	Optional<Asignatura_Carrera> asignaturaCarreraOpt = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignaturaOpt.get(), carreraOpt.get());
        	Optional<Asignatura_Carrera> asignaturaCarreraPreviaOpt = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignaturaPreviaOpt.get(), carreraOpt.get());
        	if (asignaturaCarreraOpt.isPresent() && asignaturaCarreraPreviaOpt.isPresent()) {
        		if (carreraService.eliminarPreviaAsignatura(asignaturaCarreraOpt.get(), asignaturaCarreraPreviaOpt.get())) {
        			return true;
        		}
        	}
    	}
    	return false;
    }
    
    @GetMapping("/listarpreviaturas")
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
	public List<Asignatura> listarPrevia(HttpServletRequest request,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre,
			@RequestParam(name = "carrera", required = true) String carreraNombre) {
		System.out.println("entre al listarPrevia con "+asignaturaNombre+" y "+carreraNombre);
		Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
		Optional<Carrera> carrera = carreraService.obtenerCarreraNombre(carreraNombre);
		Optional<Asignatura_Carrera> asignaturaCarrera = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignatura.get(), carrera.get());
		System.out.println("obtuve la asignaturaCarrera "+asignaturaCarrera.get().getId());
		return carreraService.listarPrevias(asignaturaCarrera.get());
	}
	
	@GetMapping("/listarpreviaturasposibles")
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
	public List<Asignatura> listarPreviasPosibles(HttpServletRequest request,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre,
			@RequestParam(name = "carrera", required = true) String carreraNombre) {
		System.out.println("entre al listarPreviaPosibles con "+asignaturaNombre+" y "+carreraNombre);
		Optional<Asignatura>asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
		Optional<Carrera>carrera = carreraService.obtenerCarreraNombre(carreraNombre);
		Optional<Asignatura_Carrera> asignaturaCarrera = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignatura.get(), carrera.get());
		System.out.println("obtuve la asignaturaCarrera "+asignaturaCarrera.get().getId());
		return carreraService.listarPreviasPosibles(asignaturaCarrera.get());
	}
	
	@GetMapping("/asignaturasgrafo")
	@PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE')")
	public List<Nodo> listarNodosGrafo(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre){
		Optional<Carrera> carreraOpt = carreraService.obtenerCarreraNombre(carreraNombre);
		if (carreraOpt.isPresent()) {
			return carreraService.listarNodosGrafo(carreraOpt.get());
		}
		else return null;			
	}
	
	@GetMapping("/linksgrafo")
	@PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE')")
	public List<Link> listarLinkGrafo(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre){
		Optional<Carrera> carreraOpt = carreraService.obtenerCarreraNombre(carreraNombre);
		if (carreraOpt.isPresent()) {
			return carreraService.listarLinkGrafo(carreraOpt.get());
		}
		else return null;
		
	}
	
    
    
    

}

