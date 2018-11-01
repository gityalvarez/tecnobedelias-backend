package com.proyecto.tecnobedelias.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

import  com.proyecto.tecnobedelias.Util.TokenUtil;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Estudiante_Examen;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.CursoService;
import com.proyecto.tecnobedelias.service.ExamenService;
import com.proyecto.tecnobedelias.service.InscripcionService;
import com.proyecto.tecnobedelias.service.UsuarioService;

@RestController

@RequestMapping("/inscripcion")
public class InscripcionController {

	@Autowired
	InscripcionService inscripcionService;
	
	@Autowired
	ExamenService examenService;
	
	@Autowired
	CursoService cursoService;
	
	@Autowired
	UsuarioService usuarioService;
	
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
	
	@GetMapping("/ingresarcalificacionexamen")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean cargarCalificacionExamen(HttpServletRequest request,
			@RequestParam(name = "usuarioId", required = true) String usuarioIdStr,
			@RequestParam(name = "examenId", required = true) String examenIdStr,
			@RequestParam(name = "nota", required = true) String notaStr) throws ParseException {
		long usuarioId = Long.parseLong(usuarioIdStr);
		long examenId = Long.parseLong(examenIdStr);
		int nota = Integer.parseInt(notaStr);
		Optional<Examen> examen = examenService.obtenerExamen(examenId);
		Optional<Usuario> usuario = usuarioService.obtenerUsuario(usuarioId);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String fechaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(examen.get().getFecha());
		Date fechaExamen = formateadorfecha.parse(fechaExamenString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede ingresar la nota del examen luego de que fue rendido
	 	//if (fechaActual.after(fechaExamen)) {
	 		return inscripcionService.ingresarCalificacionExamen(usuario.get(), examen.get(), nota);
	 	/*}
	 		else return false;*/
	}
	
	
	@PostMapping("/ingresarcalificacionesexamen")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean cargarCalificacionesExamen(HttpServletRequest request,
			@RequestBody(required = true) List<Estudiante_Examen> estudiantesExamen,
			@RequestParam(name = "examenId", required = true) String examenIdStr) throws ParseException {
		long examenId = Long.parseLong(examenIdStr);
		Optional<Examen> examen = examenService.obtenerExamen(examenId);		
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String fechaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(examen.get().getFecha());
		Date fechaExamen = formateadorfecha.parse(fechaExamenString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede ingresar las notas del examen luego de que fue rendido
	 	boolean calificacionCargada = true;
	 	//if (fechaActual.after(fechaExamen)) {	 		
	 		Estudiante_Examen estudianteExamen;
	 		Iterator<Estudiante_Examen> itEstudianteExamen = estudiantesExamen.iterator();
	 		Usuario estudiante;
	 		while (itEstudianteExamen.hasNext() && calificacionCargada) {	
	 			estudianteExamen = itEstudianteExamen.next();
	 			estudiante = inscripcionService.obtenerEstudianteEstudianteExamen(estudianteExamen.getId());
	 			calificacionCargada = inscripcionService.ingresarCalificacionExamen(estudiante, examen.get(), estudianteExamen.getNota());
	 		}
	 	/*}
	 	else calificacionCargada = false;*/
	 	return calificacionCargada;
	}
	
	@GetMapping("/ingresarcalificacioncurso")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean cargarCalificacionCurso(HttpServletRequest request,
			@RequestParam(name = "usuarioId", required = true) String usuarioIdStr,
			@RequestParam(name = "cursoId", required = true) String cursoIdStr,
			@RequestParam(name = "nota", required = true) String notaStr) throws ParseException {
		long usuarioId = Long.parseLong(usuarioIdStr);
		long cursoId = Long.parseLong(cursoIdStr);
		int nota = Integer.parseInt(notaStr);
		Optional<Curso> curso = cursoService.obtenerCurso(cursoId);
		Optional<Usuario> usuario = usuarioService.obtenerUsuario(usuarioId);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(curso.get().getFechaFin());
		Date fechaFinCurso = formateadorfecha.parse(fechaFinCursoString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede ingresar la nota del curso luego de que ha finalizado
	 	//if (fechaActual.after(fechaFinCurso)) {
	 		return inscripcionService.ingresarCalificacionCurso(usuario.get(), curso.get(), nota);
	 	/*}
	 	else return false;*/
	}
	
	
	@PostMapping("/ingresarcalificacionescurso")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean cargarCalificacionesCurso(HttpServletRequest request,
			@RequestBody(required = true) List<Curso_Estudiante> estudiantesCurso,
			@RequestParam(name = "cursoId", required = true) String cursoIdStr) throws ParseException {
		long cursoId = Long.parseLong(cursoIdStr);
		Optional<Curso> curso = cursoService.obtenerCurso(cursoId);		
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(curso.get().getFechaFin());
		Date fechaFinCurso = formateadorfecha.parse(fechaFinCursoString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se pueden ingresar las notas del cusro luego de que finalizo
	 	boolean calificacionCargada = true;
	 	System.out.println("Entro a cargarCalificacionesCurso");
	 	//if (fechaActual.after(fechaFinCurso)) {	 		
	 		Curso_Estudiante estudianteCurso;
	 		Iterator<Curso_Estudiante> itEstudianteCurso = estudiantesCurso.iterator();
	 		Usuario estudiante;
	 		while (itEstudianteCurso.hasNext() && calificacionCargada) {
	 			estudianteCurso = itEstudianteCurso.next();
	 			estudiante = inscripcionService.obtenerEstudianteCursoEstudiante(estudianteCurso.getId());
	 			calificacionCargada = inscripcionService.ingresarCalificacionCurso(estudiante, curso.get(), estudianteCurso.getNota());
	 		}
	 	/*}
	 	else calificacionCargada = false;*/
	 	return calificacionCargada;
	}
	
	
}
