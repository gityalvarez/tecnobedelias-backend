package com.proyecto.tecnobedelias.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.service.ExamenService;

@RestController
@RequestMapping("/examen")
public class ExamenController {

	@Autowired
	ExamenService examenService;
	
	@Autowired
	AsignaturaRepository asignaturaRepository;
	
	@Autowired
	ExamenRepository examenRepository;
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public List<Examen> listarExamenes() {
		return examenRepository.findAll();
	}
	
	@PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
    public void crearExamen(HttpServletRequest request, @RequestBody Examen examen, @RequestParam(name = "codigo", required = true) String codigoAsignatura) throws ParseException{
		Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByCodigo(codigoAsignatura);
		examen.setAsignatura(asignaturaOpt.get());
		if (!examenService.existeExamen(examen.getAsignatura(), examen.getFecha())){
			/*List<Curso> cursosAsignatura = asignaturaOpt.get().getCursos();
			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
			Date fechaActualDate = new Date();
			long fechaActual = fechaActualDate.getTime();
			Date fechaFinCursoDate;
			long fechaFinCurso;
			long diferencia = ;
			Curso ultimocurso;
			for (Curso curso : cursosAsignatura) {
				String fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
			 	fechaFinCursoDate = formateadorfecha.parse(fechaFinCursoString);
			 	fechaFinCurso = fechaFinCursoDate.getTime();
			 	if (fechaActual - fechaFinCurso < diferencia)
			}*/			
			examenService.altaExamen(examen);
			Optional<Examen> examenOpt = examenService.obtenerExamen(examen.getAsignatura(), examen.getFecha());
			Asignatura asignatura = examenOpt.get().getAsignatura();
	    	asignatura.getExamenes().add(examenOpt.get());
	    	asignaturaRepository.save(asignatura);
		}
		
		
    	
    }
	
	@PostMapping("/borrar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void borrarExamen(HttpServletRequest request,
			@RequestParam(name = "examenId", required = true) Long examenId) {
		examenRepository.deleteById(examenId);
	}
	
	/*@PostMapping("/asignarAsignatura")
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
    	asignatura.getExamenes().add(examen.get());
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
		asignatura.getExamenes().remove(examen.get());
		asignaturaRepository.save(asignatura);
	}*/
	
	
}
