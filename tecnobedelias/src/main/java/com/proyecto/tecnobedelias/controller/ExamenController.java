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

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;

@RestController
@RequestMapping("/examen")
@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
public class ExamenController {

	@Autowired
	AsignaturaRepository asignaturaRepository;
	
	@Autowired
	ExamenRepository examenRepository;
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public List<Examen> listarExamenes() {
		return examenRepository.findAll();
	}
	
	@PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
    public void crearExamen(@RequestBody Examen examen){
    	examenRepository.save(examen);
    }
	
	@PostMapping("/borrar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void borrarExamen(HttpServletRequest request,
			@RequestParam(name = "examenId", required = true) Long examenId) {
		examenRepository.deleteById(examenId);
	}
	
	@PostMapping("/asignarAsignatura")
    @PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
    public void asignarAsignaturaExamen(HttpServletRequest request,
			@RequestParam(name = "examen", required = true) Long examenId,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre){
    	System.out.println("asi entro el parametro examenId "+examenId);
    	System.out.println("asi entro el parametro asignaturaNombre "+asignaturaNombre);
    	Optional<Examen> examen =  examenRepository.findById(examenId);
    	System.out.println("obtuve el examen "+examen.get().getId());
    	Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignaturaNombre);
    	System.out.println("obtuve la asignatura "+asignaturaOpt.get().getNombre());
    	
    	Asignatura asignatura = asignaturaOpt.get();
    	asignatura.getExamanes().add(examen.get());
    	asignaturaRepository.save(asignatura);
    }
	
	@PostMapping("/desasignarAsignatura")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void desasignarAsignaturaExamen(HttpServletRequest request,
			@RequestParam(name = "examenId", required = true) Long examenId,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre) {
		System.out.println("asi entro el parametro examenId " + examenId);
		System.out.println("asi entro el parametro asignaturaNombre " + asignaturaNombre);
		Optional<Examen> examen = examenRepository.findById(examenId);
		System.out.println("obtuve el examen " + examen.get().getId());
		Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignaturaNombre);
		System.out.println("obtuve la asignatura " + asignaturaOpt.get().getNombre());

		Asignatura asignatura = asignaturaOpt.get();
		asignatura.getExamanes().remove(examen.get());
		asignaturaRepository.save(asignatura);
	}
	
	
}
