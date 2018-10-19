package com.proyecto.tecnobedelias.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.InscripcionService;


import  com.proyecto.tecnobedelias.Util.TokenUtil;

@RestController

@RequestMapping("/inscripcion")
public class InscripcionController {

	@Autowired
	InscripcionService inscripcionService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	CarreraRepository carreraRepository;
	
	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	ExamenRepository examenRepository;
	
	@Autowired
	TokenUtil token;
	
	
	@GetMapping("/carrera")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean inscribirCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre/*,
			@RequestParam(name = "usuario", required = true) String username*/) {
		System.out.println("entre al inscripcion carrera");
		Optional<Carrera> carrera = carreraRepository.findByNombre(carreraNombre);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (inscripcionService.inscripcionCarrera(usuario.get(), carrera.get())) {
			return true;
		}
		return false;
	}
	
	@PostMapping("/curso")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public void inscribirCurso(HttpServletRequest request,
			@RequestParam(name = "curso", required = true) Long cursoId,
			@RequestParam(name = "usuario", required = true) Long usuarioId) {
		Optional<Curso> curso = cursoRepository.findById(cursoId);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		inscripcionService.inscripcionCurso(usuario.get(), curso.get());
	}
	
	@PostMapping("/examen")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public void inscribirExamen(HttpServletRequest request,
			@RequestParam(name = "examen", required = true) Long examenId,
			@RequestParam(name = "usuario", required = true) Long usuarioId) {
		Optional<Examen> examen = examenRepository.findById(examenId);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		inscripcionService.inscripcionExamen(usuario.get(), examen.get());
	}
	
	@GetMapping("/desistircarrera")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean desistirCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre/*,
			@RequestParam(name = "usuario", required = true) String username*/) {
		System.out.println("entre al inscripcion desistircarrera");
		Optional<Carrera> carrera = carreraRepository.findByNombre(carreraNombre);
		String username = token.getUsername(request);
		
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (inscripcionService.desistirCarrera(usuario.get(), carrera.get())) {
			return true;
		}
		return false;
	}
	
	
	@PostMapping("/desistircurso")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public void desistirCurso(HttpServletRequest request,
			@RequestParam(name = "curso", required = true) Long cursoId,
			@RequestParam(name = "usuario", required = true) Long usuarioId) {
		Optional<Curso> curso = cursoRepository.findById(cursoId);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		inscripcionService.desistirCurso(usuario.get(), curso.get());
	}
	
	@PostMapping("/desistirexamen")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public void desistirExamen(HttpServletRequest request,
			@RequestParam(name = "examen", required = true) Long examenId,
			@RequestParam(name = "usuario", required = true) Long usuarioId) {
		Optional<Examen> examen = examenRepository.findById(examenId);
		Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
		inscripcionService.desistirExamen(usuario.get(), examen.get());
	}
	
}
