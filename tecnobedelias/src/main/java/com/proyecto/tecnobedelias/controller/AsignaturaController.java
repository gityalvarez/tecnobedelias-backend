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
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.CarreraService;

@RestController

@RequestMapping("/asignatura")
public class AsignaturaController{
	public AsignaturaController(AsignaturaRepository asignaturaRepository) {
		super();
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
    	if (!asignaturaService.existeAsignaturaNombre(asignatura.getNombre())) {    		
    		asignaturaService.altaAsignatura(asignatura);
    		return true;
    	}
    	else return false;
    }
    
    @GetMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean borrarAsignatura(HttpServletRequest request,
			@RequestParam(name = "asignaturaId", required = true) String asignaturaIdStr) {
    	long asignaturaId = Long.parseLong(asignaturaIdStr);
    	if (asignaturaService.existeAsignatura(asignaturaId)) { 
    		Asignatura asignaturaExistente = asignaturaService.obtenerAsignatura(asignaturaId).get();
    		if (asignaturaExistente.getAsignaturaCarrera().isEmpty()) {
    			if (asignaturaExistente.getCursos().isEmpty()) {
    				if (asignaturaExistente.getExamenes().isEmpty()) {
    					asignaturaService.bajaAsignatura(asignaturaExistente);
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
    
    
    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public boolean modificarAsignatura(HttpServletRequest request,
    		@RequestBody(required = true) Asignatura asignatura, 
    		@RequestParam(name = "asignaturaId", required = true) String asignaturaIdStr) {
    	long asignaturaId = Long.parseLong(asignaturaIdStr);
    	if (asignaturaService.existeAsignatura(asignaturaId)) {
    		Optional<Asignatura> asignaturaExistente = asignaturaService.obtenerAsignatura(asignaturaId);
    		asignaturaExistente.get().setDescripcion(asignatura.getDescripcion());
    		asignaturaService.modificacionAsignatura(asignaturaExistente.get());
    		return true;
    	}
    	else return false;
    }
    

}

