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

import com.proyecto.tecnobedelias.Util.Response;
import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Link;
import com.proyecto.tecnobedelias.persistence.model.Nodo;
import com.proyecto.tecnobedelias.persistence.repository.Asignatura_CarreraRepository;
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
	@PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE') or hasRole('FUNCIONARIO')")
	public List<Carrera> listarCarreras() {
		return carreraService.listarCarreras();
	}
	
    @PostMapping("/verificar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response verificarCarrera(HttpServletRequest request,
			@RequestParam(name = "nombre", required = true) String carreraNombre) {
    	if( carreraService.existeCarrera(carreraNombre)) {
    		return new Response(true,"La carrera existe");
    	}else return new Response(false,"La carrera no existe");
    }

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response crearCarrera(@RequestBody Carrera carrera){
    	if (!carreraService.existeCarrera(carrera.getNombre())) {
    		if (carrera.getCreditosMinimos() >= 0) {
    			carreraService.altaCarrera(carrera);
    			return new Response(true,"La carrera " + carrera.getNombre() + " fue creada con exito");
    		}
    		else return new Response(false, "No se pudo crear la carrera, los creditos minimos deben ser mayor o igual a cero");
    	}
    	else return new Response(false, "No se pudo crear la carrera, ya existe una carrera con ese nombre");
    }
    
    @GetMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response borrarCarrera(HttpServletRequest request,
			@RequestParam(name = "carreraId", required = true) String carreraIdStr) {
    	long carreraId = Long.parseLong(carreraIdStr);
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarrera(carreraId);
    	if (carreraOpt.isPresent()) {
    		if (carreraOpt.get().getAsignaturaCarrera().isEmpty()) {
    			if (carreraOpt.get().getEstudiantes().isEmpty()) {    		
    				carreraService.bajaCarrera(carreraOpt.get());
    				return new Response(true, "La carrera " + carreraOpt.get().getNombre() + " fue borrada con exito");
    			}
    			else return new Response(false,"No se pudo borrar la carrera, cuenta con algun estudiante inscripto");
    		}
    		else return new Response(false,"No se pudo borrar la carrera, cuenta con alguna asignatura asignada");  
    	}
    	else return new Response(false,"No se pudo borrar la carrera, no existe esa carrera");
    }
    
    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response modificarCarrera(HttpServletRequest request,
    		@RequestBody(required = true) Carrera carrera,
			@RequestParam(name = "carreraId", required = true) String carreraIdStr) {
    	long carreraId = Long.parseLong(carreraIdStr);
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarrera(carreraId);
    	if (carreraOpt.isPresent()) {
    		carreraOpt.get().setDescripcion(carrera.getDescripcion());
    		carreraOpt.get().setCreditosMinimos(carrera.getCreditosMinimos());
    		carreraService.modificacionCarrera(carreraOpt.get());
    		return new Response(true, "La carrera " + carreraOpt.get().getNombre() + " fue modificada con exito");
    	}
    	else return new Response(false, "La carrera no fue modificada, la carrera no existe");
    }    
    
    @PostMapping("/asignarasignatura")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response asignarAsignaturaCarrera(HttpServletRequest request, @RequestBody(required = true) Asignatura_Carrera asign_carrera,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	Optional<Carrera> carrera =  carreraService.obtenerCarreraNombre(carreraNombre);
    	Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
    	asign_carrera.setAsignatura(asignatura.get());
    	asign_carrera.setCarrera(carrera.get());
    	return carreraService.asignarAsignaturaCarrera(asign_carrera);     	
    }  
    
    @PostMapping("/modificarasignaturacarrera")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response modificarAsignaturaCarrera(HttpServletRequest request, @RequestBody(required = true) Asignatura_Carrera asign_carrera,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	Optional<Carrera> carrera =  carreraService.obtenerCarreraNombre(carreraNombre);
    	System.out.println("obtuve la carrera "+carrera.get().getNombre());
    	Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignatura.get().getNombre());
    	System.out.println("entro al carreraService");  
    	asign_carrera.setAsignatura(asignatura.get());
    	asign_carrera.setCarrera(carrera.get());
    	if (carreraService.modificarAsignaturaCarrera(asign_carrera)) {
    		return new Response(true, "La asignaturaCarrera fue modificada con exito");
    	}else return new Response(false, "La asignaturaCarrera no fue modificada");
    }
    
    @GetMapping("/desasignarasignatura")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response desasignarAsignaturaCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	Optional<Carrera> carrera =  carreraService.obtenerCarreraNombre(carreraNombre);
    	Optional<Asignatura> asignatura = asignaturaService.obtenerAsignaturaNombre(asignaturaNombre);
    	return carreraService.desasignarAsignaturaCarrera(asignatura.get(), carrera.get()); 
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
    public Response asignarPrevia(HttpServletRequest request,
    		@RequestParam(name = "carrera", required = true) String carrera,
			@RequestParam(name = "asignatura", required = true) String asignatura,
			@RequestParam(name = "asignaturaPrevia", required = true) String asignaturaPrevia) {
    	Optional<Carrera> carreraOpt = carreraService.obtenerCarreraNombre(carrera);
    	Optional<Asignatura> asignaturaOpt = asignaturaService.obtenerAsignaturaNombre(asignatura);
    	Optional<Asignatura> asignaturaPreviaOpt = asignaturaService.obtenerAsignaturaNombre(asignaturaPrevia);
    	if (carreraOpt.isPresent() && asignaturaOpt.isPresent() && asignaturaPreviaOpt.isPresent()) {
        	Optional<Asignatura_Carrera> asignaturaCarreraOpt = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignaturaOpt.get(), carreraOpt.get());
        	Optional<Asignatura_Carrera> asignaturaCarreraPreviaOpt = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignaturaPreviaOpt.get(), carreraOpt.get());
        	if (asignaturaCarreraOpt.isPresent() && asignaturaCarreraPreviaOpt.isPresent()) {
        		return carreraService.agregarPreviaAsignatura(asignaturaCarreraOpt.get(), asignaturaCarreraPreviaOpt.get());
        	}
        	else return new Response(false, "La previa no pudo ser asignada, alguna asignatura no esta asignada a la carrera");
    	}
    	return new Response(false, "La previa no pudo ser asignada, la carrera, la asignatura o la asignatura prvia no existen");
    }
    
    @GetMapping("/desasignarprevia")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response eliminarPrevia(HttpServletRequest request,
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
        		return carreraService.eliminarPreviaAsignatura(asignaturaCarreraOpt.get(), asignaturaCarreraPreviaOpt.get());        			
        	}
        	else return new Response(false, "La previa no pudo ser desasignada, alguna asignatura no esta asignada a la carrera");
    	}
    	else return new Response(false, "La previa no pudo ser desasignada, la carrera, la asignatura o la asignatura prvia no existen");
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

