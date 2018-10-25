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
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.CarreraService;

@RestController

@RequestMapping("/asignatura")
public class AsignaturaController{
	private AsignaturaRepository asignaturaRepository;	
	public AsignaturaController(AsignaturaRepository asignaturaRepository) {
		super();
		this.asignaturaRepository = asignaturaRepository;
	}
	
	@Autowired
	AsignaturaService asignaturaService;	
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('DIRECTOR') or hasRole('ESTUDIANTE')")
	public List<Asignatura> listarAsignaturas(){
		return asignaturaService.listarAsignaturas();
	}

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean crearAsignatura(@RequestBody(required = true) Asignatura asignatura){  
    	if (!asignaturaService.existeAsignaturaCodigo(asignatura.getCodigo())) {
    		asignaturaRepository.save(asignatura);
    		return true;
    	}
    	else return false;
    }
    
    @GetMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean borrarAsignatura(HttpServletRequest request,
			@RequestParam(name = "asignatura", required = true) String asignatura) {
    	Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignatura);
    	if (asignaturaOpt.isPresent()) {
    		if (asignaturaOpt.get().getAsignaturaCarrera().isEmpty()) {
    			if (asignaturaOpt.get().getCursos().isEmpty()) {
    				if (asignaturaOpt.get().getExamenes().isEmpty()) {
    					asignaturaService.bajaAsignatura(asignaturaOpt.get());
    					return true;  
    				}
    				else return false;
    			}
    			else return false;
    		}
    		else return false;
    	}
    	else return false;
    }
    

}

