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
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.Asignatura_CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.service.CarreraService;

@RestController

@RequestMapping("/carrera")
public class CarreraController{
	
	@Autowired
	CarreraService carreraService;
	
	private CarreraRepository carreraRepository;	
	private AsignaturaRepository asignaturaRepository;	
	private Asignatura_CarreraRepository asignaturaCarreraRepository;
	public CarreraController(CarreraRepository carreraRepository, AsignaturaRepository asignaturaRepository,Asignatura_CarreraRepository asignaturaCarreraRepository ) {
		super();
		this.carreraRepository = carreraRepository;
		this.asignaturaRepository = asignaturaRepository;
		this.asignaturaCarreraRepository = asignaturaCarreraRepository;
	}
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
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
    		carreraService.altaCarrera(carrera);
    		return true;
    	}else return false;
    }
    
    @PostMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void borrarCarrera(HttpServletRequest request,
			@RequestParam(name = "carreraId", required = true) Long carreraId) {
    	carreraRepository.deleteById(carreraId);
    }
    
    
    @GetMapping("/asignarasignatura")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void asignarAsignaturaCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	System.out.println("asi entro el parametro carreraNombre "+carreraNombre);
    	System.out.println("asi entro el parametro asignaturaNombre "+asignaturaNombre);
    	Optional<Carrera> carrera =  carreraRepository.findByNombre(carreraNombre);
    	System.out.println("obtuve la carrera "+carrera.get().getNombre());
    	Optional<Asignatura> asignatura = asignaturaRepository.findByNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignatura.get().getNombre());
    	System.out.println("entro al carreraService");
    	
    	carreraService.asignarAsignaturaCarrera(asignatura.get(), carrera.get());
    }
    
    
    
    @GetMapping("/desasignarasignatura")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void desasignarAsignaturaCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	System.out.println("asi entro el parametro carreraNombre "+carreraNombre);
    	System.out.println("asi entro el parametro asignaturaNombre "+asignaturaNombre);
    	Optional<Carrera> carrera =  carreraRepository.findByNombre(carreraNombre);
    	System.out.println("obtuve la carrera "+carrera.get().getNombre());
    	Optional<Asignatura> asignatura = asignaturaRepository.findByNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignatura.get().getNombre());
    	System.out.println("entro al carreraService");
    	
    	carreraService.desasignarAsignaturaCarrera(asignatura.get(), carrera.get());
    }
    
    @GetMapping("/listarasignaturas/{nombre}")
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
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
    	Optional<Carrera> carreraOpt = carreraRepository.findByNombre(carrera);
    	Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignatura);
    	Optional<Asignatura> asignaturaPreviaOpt = asignaturaRepository.findByNombre(asignaturaPrevia);
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
    	
    	Optional<Carrera> carreraOpt = carreraRepository.findByNombre(carrera);
    	Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignatura);
    	Optional<Asignatura> asignaturaPreviaOpt = asignaturaRepository.findByNombre(asignaturaPrevia);
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
		Optional<Asignatura>asignatura = asignaturaRepository.findByNombre(asignaturaNombre);
		Optional<Carrera>carrera = carreraRepository.findByNombre(carreraNombre);
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
		Optional<Asignatura>asignatura = asignaturaRepository.findByNombre(asignaturaNombre);
		Optional<Carrera>carrera = carreraRepository.findByNombre(carreraNombre);
		Optional<Asignatura_Carrera> asignaturaCarrera = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignatura.get(), carrera.get());
		System.out.println("obtuve la asignaturaCarrera "+asignaturaCarrera.get().getId());
		return carreraService.listarPreviasPosibles(asignaturaCarrera.get());
	}
    
    
    

}

