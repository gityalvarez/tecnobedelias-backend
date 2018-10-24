package com.proyecto.tecnobedelias.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import  com.proyecto.tecnobedelias.Util.TokenUtil;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.InscripcionService;

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
	public boolean inscribirCarrera(HttpServletRequest request, @RequestParam(name = "carrera", required = true) String carreraNombre) {
		System.out.println("entre al inscripcion carrera");
		Optional<Carrera> carrera = carreraRepository.findByNombre(carreraNombre);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (inscripcionService.inscripcionCarrera(usuario.get(), carrera.get())) {
			return true;
		}
		return false;
	}
	
	@GetMapping("/curso")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean inscribirCurso(HttpServletRequest request, @RequestParam(name = "curso", required = true) String cursoIdStr) throws ParseException {
		long cursoId = Long.parseLong(cursoIdStr);
		Optional<Curso> curso = cursoRepository.findById(cursoId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curso.get().getFechaInicio());
		calendar.add(Calendar.DAY_OF_YEAR, 10);
		String fechaInicioMas10DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaInicioMas10Dias = formateadorfecha.parse(fechaInicioMas10DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede anotar hasta 10 dias despues del inicio del curso
	 	if (fechaActual.before(fechaInicioMas10Dias)) {
	 		return inscripcionService.inscripcionCurso(usuario.get(), curso.get());
	 	}
	 	else return false;
	}
	
	@GetMapping("/curso/consulta")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public List<Curso> consultaCursos(HttpServletRequest request) {		
		String username = token.getUsername(request);
		Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
		return inscripcionService.consultaCursos(usuarioOpt.get());
	}
	
	@GetMapping("/examen")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean inscribirExamen(HttpServletRequest request,	@RequestParam(name = "examen", required = true) String examenIdStr) throws ParseException {
		long examenId = Long.parseLong(examenIdStr);
		Optional<Examen> examen = examenRepository.findById(examenId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(examen.get().getFecha());
		calendar.add(Calendar.DAY_OF_YEAR, -5);
		String fechaExamenMenos5DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaExamenMenos5Dias = formateadorfecha.parse(fechaExamenMenos5DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede anotar hasta 5 dias antes del examen
	 	if (fechaActual.before(fechaExamenMenos5Dias)) {
	 		return inscripcionService.inscripcionExamen(usuario.get(), examen.get());
	 	}
	 	else return false;
	}
	
	@GetMapping("/examen/consulta")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public List<Examen> consultaExamenes(HttpServletRequest request) {		
		String username = token.getUsername(request);
		Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
		return inscripcionService.consultaExamenes(usuarioOpt.get());
	}
	
	/*@GetMapping("/desistircarrera")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean desistirCarrera(HttpServletRequest request,
			@RequestParam(name = "carrera", required = true) String carreraNombre) {
		System.out.println("entre al inscripcion desistircarrera");
		Optional<Carrera> carrera = carreraRepository.findByNombre(carreraNombre);
		String username = token.getUsername(request);		
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		if (inscripcionService.desistirCarrera(usuario.get(), carrera.get())) {
			return true;
		}
		return false;
	}*/	
	
	@GetMapping("/desistircurso")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean desistirCurso(HttpServletRequest request,
			@RequestParam(name = "curso", required = true) String cursoIdStr) throws ParseException {
		long cursoId = Long.parseLong(cursoIdStr);
		Optional<Curso> curso = cursoRepository.findById(cursoId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curso.get().getFechaInicio());
		calendar.add(Calendar.DAY_OF_YEAR, 15);
		String fechaInicioMas15DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaInicioMas15Dias = formateadorfecha.parse(fechaInicioMas15DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede desistir hasta 15 dias despues del inicio del curso
	 	if (fechaActual.before(fechaInicioMas15Dias)) {		
	 		return inscripcionService.desistirCurso(usuario.get(), curso.get());
	 	}
	 	else return false;
	}
	
	@GetMapping("/desistirexamen")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public boolean desistirExamen(HttpServletRequest request,
			@RequestParam(name = "examen", required = true) String examenIdStr) throws ParseException {
		long examenId = Long.parseLong(examenIdStr);
		Optional<Examen> examen = examenRepository.findById(examenId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(examen.get().getFecha());
		calendar.add(Calendar.DAY_OF_YEAR, -3);
		String fechaExamenMenos3DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaExamenMenos3Dias = formateadorfecha.parse(fechaExamenMenos3DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede desistir hasta 3 dias antes del examen
	 	if (fechaActual.before(fechaExamenMenos3Dias)) {
	 		return inscripcionService.desistirExamen(usuario.get(), examen.get());
	 	}
	 	else return false;
	}
	
}
