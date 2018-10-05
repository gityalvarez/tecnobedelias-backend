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
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.service.CursoService;

@RestController
@RequestMapping("/curso")
@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
public class CursoController {

	@Autowired
	CursoRepository cursoRepository;

	@Autowired
	AsignaturaRepository asignaturaRepository;
	
	@Autowired
	CursoService cursoService;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public List<Curso> listarCursos() {
		return cursoRepository.findAll();
	}

	@PostMapping("/crear")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void crearCurso(@RequestBody Curso curso) {
		cursoService.altaCurso(curso);
	}
	
	

	@PostMapping("/borrar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void borrarCurso(Curso curso) {
		cursoService.bajaCurso(curso);
	}

	@PostMapping("/asignarAsignatura")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void asignarAsignaturaCurso(HttpServletRequest request,
			@RequestParam(name = "curso", required = true) Long cursoId,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre) {
		System.out.println("asi entro el parametro cursoId " + cursoId);
		System.out.println("asi entro el parametro asignaturaNombre " + asignaturaNombre);
		Optional<Curso> curso = cursoRepository.findById(cursoId);
		System.out.println("obtuve la carrera " + curso.get().getId());
		Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignaturaNombre);
		System.out.println("obtuve la asignatura " + asignaturaOpt.get().getNombre());

		Asignatura asignatura = asignaturaOpt.get();
		asignatura.getCursos().add(curso.get());
		asignaturaRepository.save(asignatura);
	}
	
	@PostMapping("/desasignarAsignatura")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public void desasignarAsignaturaCurso(HttpServletRequest request,
			@RequestParam(name = "curso", required = true) Long cursoId,
			@RequestParam(name = "asignatura", required = true) String asignaturaNombre) {
		System.out.println("asi entro el parametro cursoId " + cursoId);
		System.out.println("asi entro el parametro asignaturaNombre " + asignaturaNombre);
		Optional<Curso> curso = cursoRepository.findById(cursoId);
		System.out.println("obtuve el curso " + curso.get().getId());
		Optional<Asignatura> asignaturaOpt = asignaturaRepository.findByNombre(asignaturaNombre);
		System.out.println("obtuve la asignatura " + asignaturaOpt.get().getNombre());

		Asignatura asignatura = asignaturaOpt.get();
		asignatura.getCursos().remove(curso.get());
		asignaturaRepository.save(asignatura);
	}

}
