package com.proyecto.tecnobedelias.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;

@RestController

@RequestMapping("/asignatura")
public class AsignaturaController{
	private AsignaturaRepository asignaturaRepository;	
	public AsignaturaController(AsignaturaRepository asignaturaRepository) {
		super();
		this.asignaturaRepository = asignaturaRepository;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_DIRECTOR')")
	public List<Asignatura> listarAsignaturas(){
		return asignaturaRepository.findAll();
	}

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void crearAsignatura(@RequestBody Asignatura asignatura){
    	asignaturaRepository.save(asignatura);
    }
    
    @PostMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public void borrarAsignatura(HttpServletRequest request,
			@RequestParam(name = "asignaturaId", required = true) Long asignaturaId) {
    	asignaturaRepository.deleteById(asignaturaId);
    }
    

}

