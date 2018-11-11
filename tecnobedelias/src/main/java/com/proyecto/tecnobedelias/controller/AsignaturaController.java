package com.proyecto.tecnobedelias.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.Util.Response;
import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.service.AsignaturaService;

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
    public Response crearAsignatura(@RequestBody(required = true) Asignatura asignatura){  
    	if (!asignaturaService.existeAsignaturaNombre(asignatura.getNombre())) {    		
    		asignaturaService.altaAsignatura(asignatura);
    		return new Response(true,"La asignatura " + asignatura.getNombre() + " fue creada con exito");
    	}
    	else return new Response(false, "No se pudo crear la asignatura, ya existe una asignatura con ese nombre");
    }
    
    @GetMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response borrarAsignatura(HttpServletRequest request,
			@RequestParam(name = "asignaturaId", required = true) String asignaturaIdStr) {
    	long asignaturaId = Long.parseLong(asignaturaIdStr);
    	if (asignaturaService.existeAsignatura(asignaturaId)) { 
    		Asignatura asignaturaExistente = asignaturaService.obtenerAsignatura(asignaturaId).get();
    		if (asignaturaExistente.getAsignaturaCarrera().isEmpty()) {
    			if (asignaturaExistente.getCursos().isEmpty()) {
    				if (asignaturaExistente.getExamenes().isEmpty()) {
    					asignaturaService.bajaAsignatura(asignaturaExistente);
    					return new Response(true, "La asignatura fue borrada con exito");  
    				}
    				else return new Response(false, "No se pudo borrar la asignatura, cuenta con algun examen");
    			}
    			else return new Response(false, "No se pudo borrar la asignatura, cuenta con algun curso");
    		}
    		else return new Response(false, "No se pudo borrar la asignatura, esta asignada a alguna carrera");
    	}
    	else return new Response(false, "No se pudo borrar la asignatura, no existe esa asignatura");
    }
    
    
    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Response modificarAsignatura(HttpServletRequest request,
    		@RequestBody(required = true) Asignatura asignatura, 
    		@RequestParam(name = "asignaturaId", required = true) String asignaturaIdStr) {
    	long asignaturaId = Long.parseLong(asignaturaIdStr);
    	if (asignaturaService.existeAsignatura(asignaturaId)) {
    		Optional<Asignatura> asignaturaExistente = asignaturaService.obtenerAsignatura(asignaturaId);
    		asignaturaExistente.get().setDescripcion(asignatura.getDescripcion());
    		asignaturaService.modificacionAsignatura(asignaturaExistente.get());
    		return new Response(true, "La asignatura " + asignaturaExistente.get().getNombre() + " fue modificada con exito");
    	}
    	else return new Response(false, "La asignatura no fue modificada, la asignatura no existe");
    }
    
    @GetMapping("/obtenerasignaturacarrera")
    @PreAuthorize("hasRole('ROLE_DIRECTOR')")
    public Asignatura_Carrera obtenerAsignaturaCarrera(HttpServletRequest request,
    		@RequestParam(name="asignaturaId",required = true) String asignaturaIdStr){
    	System.out.println("entre al obtener asignaturacarrera con la asignatura "+asignaturaIdStr);
    	long asignaturaId = Long.parseLong(asignaturaIdStr);
    	if (asignaturaService.existeAsignatura(asignaturaId)) {    		
    		return asignaturaService.obtenerAsignaturaCarrera(asignaturaId);
    	}
    	else return null;	   	
    }
    

}

