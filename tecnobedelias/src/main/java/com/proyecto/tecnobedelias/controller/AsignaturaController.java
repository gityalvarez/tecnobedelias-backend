package com.proyecto.tecnobedelias.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.service.CarreraService;

@RestController

@RequestMapping("/asignatura")
public class AsignaturaController{
	private AsignaturaRepository asignaturaRepository;	
	public AsignaturaController(AsignaturaRepository asignaturaRepository) {
		super();
		this.asignaturaRepository = asignaturaRepository;
	}
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE')")
	public List<Asignatura> listarAsignaturas(){
		return asignaturaRepository.findAll();
	}
	


    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void crearAsignatura(@RequestBody Asignatura asignatura){    	
    	asignaturaRepository.save(asignatura);
    }
    
    @GetMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean borrarAsignatura(HttpServletRequest request,
			@RequestParam(name = "asignatura", required = true) String asignatura) {
    	Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignatura);
    	if (asignaturaOpt.isPresent()) {
    		asignaturaRepository.delete(asignaturaOpt.get());
    		return true;    		
    	}
    	return false;
    }
    

}

