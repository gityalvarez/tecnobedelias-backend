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
    public void crearCarrera(@RequestBody Carrera carrera){
    	carreraService.altaCarrera(carrera);
    }
    
    @PostMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void borrarCarrera(HttpServletRequest request,
			@RequestParam(name = "carreraId", required = true) Long carreraId) {
    	carreraRepository.deleteById(carreraId);
    }
    
    
    @PostMapping("/asignarasignatura")
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
    
    
    
    @PostMapping("/desasignarasignatura")
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
    
    
    @PostMapping("/asignarprevia")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void asignarPrevia(HttpServletRequest request,
			@RequestParam(name = "asignaturaCarreraId", required = true) Long asignaturaCarreraId,
			@RequestParam(name = "asignaturaCarreraPreviaId", required = true) Long asignaturaCarreraPreviaId) {
    	
    	Optional<Asignatura_Carrera> asignatura = asignaturaCarreraRepository.findById(asignaturaCarreraId);
    	Optional<Asignatura_Carrera> asignaturaPrevia = asignaturaCarreraRepository.findById(asignaturaCarreraPreviaId);
    	carreraService.agregarPreviaAsignatura(asignatura.get(), asignaturaPrevia.get());
    	
    }
    
    @PostMapping("/listarpreviaturas")
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
	public List<Asignatura_Carrera> listarPrevia(HttpServletRequest request,
			@RequestParam(name = "asignaturaCarreraId", required = true) Long asignaturaCarreraId) {
		Optional<Asignatura_Carrera> asignaturaCarrera = asignaturaCarreraRepository.findById(asignaturaCarreraId);
		return carreraService.listarPrevias(asignaturaCarrera.get());
	}
    
    
    @PostMapping("/desasignarprevia")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void eliminarPrevia(HttpServletRequest request,
			@RequestParam(name = "asignaturaCarreraId", required = true) Long asignaturaCarreraId,
			@RequestParam(name = "asignaturaCarreraPreviaId", required = true) Long asignaturaCarreraPreviaId) {
    	
    	Optional<Asignatura_Carrera> asignatura = asignaturaCarreraRepository.findById(asignaturaCarreraId);
    	Optional<Asignatura_Carrera> asignaturaPrevia = asignaturaCarreraRepository.findById(asignaturaCarreraPreviaId);
    	carreraService.eliminarPreviaAsignatura(asignatura.get(), asignaturaPrevia.get());
    	
    }
    

}

