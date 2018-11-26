package com.proyecto.tecnobedelias.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.Util.Response;
import  com.proyecto.tecnobedelias.Util.TokenUtil;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Estudiante_Examen;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.service.AndroidPushNotificationsService;
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.CarreraService;
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
	CarreraService carreraService;
	
	@Autowired
	AsignaturaService asignaturaService;
	
	@Autowired
	TokenUtil token;
	
	@Autowired
	AndroidPushNotificationsService androidPushNotificationsService;
	
	
	@GetMapping("/carrera")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public Response inscribirCarrera(HttpServletRequest request, @RequestParam(name = "carrera", required = true) String carreraNombre) {
		System.out.println("entre al inscripcion carrera");
		Optional<Carrera> carrera = carreraService.obtenerCarreraNombre(carreraNombre);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioService.findUsuarioByUsername(username);
		return inscripcionService.inscripcionCarrera(usuario.get(), carrera.get());
	}
	
	@GetMapping("/curso")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public Response inscribirCurso(HttpServletRequest request, @RequestParam(name = "curso", required = true) String cursoIdStr) throws ParseException {
		long cursoId = Long.parseLong(cursoIdStr);
		Optional<Curso> curso = cursoService.obtenerCurso(cursoId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioService.findUsuarioByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curso.get().getFechaInicio());
		calendar.add(Calendar.DAY_OF_YEAR, 10);
		String fechaInicioMas10DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaInicioMas10Dias = formateadorfecha.parse(fechaInicioMas10DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede anotar hasta 10 dias despues del inicio del curso
	 	if (!fechaActual.after(fechaInicioMas10Dias)) {
	 		return inscripcionService.inscripcionCurso(usuario.get(), curso.get());
	 	}
	 	else return new Response(false, "No se realizo la inscripcion, finalizo la fecha limite para la matriculacion");
	}
	
	@GetMapping("/curso/consulta")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public List<Curso> consultaCursos(HttpServletRequest request) {		
		String username = token.getUsername(request);
		Optional<Usuario> usuarioOpt = usuarioService.findUsuarioByUsername(username);
		return inscripcionService.consultaCursos(usuarioOpt.get());
	}
	
	@GetMapping("/curso/disponibles")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public List<Curso> consultaCursosDisponibles(HttpServletRequest request) {			
		String username = token.getUsername(request);
		Optional<Usuario> usuarioOpt = usuarioService.findUsuarioByUsername(username);
		return inscripcionService.consultaCursosDisponibles(usuarioOpt.get());		
	}
	
	
	@GetMapping("/examen")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public Response inscribirExamen(HttpServletRequest request,	@RequestParam(name = "examen", required = true) String examenIdStr) throws ParseException {
		long examenId = Long.parseLong(examenIdStr);
		Optional<Examen> examen = examenService.obtenerExamen(examenId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioService.findUsuarioByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(examen.get().getFecha());
		calendar.add(Calendar.DAY_OF_YEAR, -5);
		String fechaExamenMenos5DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaExamenMenos5Dias = formateadorfecha.parse(fechaExamenMenos5DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede anotar hasta 5 dias antes del examen
	 	if (!fechaActual.after(fechaExamenMenos5Dias)) {
	 		return inscripcionService.inscripcionExamen(usuario.get(), examen.get());
	 	}
	 	else return new Response(false, "No se realizo la anotacion, finalizo la fecha limite para la anotacion");
	}
	
	@GetMapping("/examen/consulta")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public List<Examen> consultaExamenes(HttpServletRequest request) {		
		String username = token.getUsername(request);
		Optional<Usuario> usuarioOpt = usuarioService.findUsuarioByUsername(username);
		return inscripcionService.consultaExamenes(usuarioOpt.get());
	}
	
	@GetMapping("/examen/disponibles")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public List<Examen> consultaExamenesDisponibles(HttpServletRequest request) {		
		String username = token.getUsername(request);		
		Optional<Usuario> usuarioOpt = usuarioService.findUsuarioByUsername(username);
		return inscripcionService.consultaExamenesDisponibles(usuarioOpt.get());
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
	public Response desistirCurso(HttpServletRequest request,
			@RequestParam(name = "curso", required = true) String cursoIdStr) throws ParseException {
		long cursoId = Long.parseLong(cursoIdStr);
		Optional<Curso> curso = cursoService.obtenerCurso(cursoId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioService.findUsuarioByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curso.get().getFechaInicio());
		calendar.add(Calendar.DAY_OF_YEAR, 15);
		String fechaInicioMas15DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaInicioMas15Dias = formateadorfecha.parse(fechaInicioMas15DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede desistir hasta 15 dias despues del inicio del curso
	 	if (!fechaActual.after(fechaInicioMas15Dias)) {		
	 		return inscripcionService.desistirCurso(usuario.get(), curso.get());
	 	}
	 	else return new Response(false,"No pudo desistir al curso, finalizo la fecha limite para desistir");
	}
	
	@GetMapping("/desistirexamen")
	@PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
	public Response desistirExamen(HttpServletRequest request,
			@RequestParam(name = "examen", required = true) String examenIdStr) throws ParseException {
		long examenId = Long.parseLong(examenIdStr);
		Optional<Examen> examen = examenService.obtenerExamen(examenId);
		String username = token.getUsername(request);
		Optional<Usuario> usuario = usuarioService.findUsuarioByUsername(username);
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(examen.get().getFecha());
		calendar.add(Calendar.DAY_OF_YEAR, -3);
		String fechaExamenMenos3DiasString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Date fechaExamenMenos3Dias = formateadorfecha.parse(fechaExamenMenos3DiasString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede desistir hasta 3 dias antes del examen
	 	if (!fechaActual.after(fechaExamenMenos3Dias)) {
	 		return inscripcionService.desistirExamen(usuario.get(), examen.get());
	 	}
	 	else return new Response(false, "No pudo desistir al examen, finalizo la fecha limite para desistir");
	}	
	
	@PostMapping("/ingresarcalificacionexamen")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response cargarCalificacionExamen(HttpServletRequest request,
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
	 	if (!fechaActual.before(fechaExamen)) {
	 		return inscripcionService.ingresarCalificacionExamen(usuario.get(), examen.get(), nota); 	 		
	 	}
	 	else return new Response(false, "La calificacion no pudo ser ingresada, aun no ha sido tomado el examen");
	}
	
	
	@PostMapping("/ingresarcalificacionesexamen")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response cargarCalificacionesExamen(HttpServletRequest request,
			@RequestBody(required = true) List<Estudiante_Examen> estudiantesExamen,
			@RequestParam(name = "examenId", required = true) String examenIdStr) throws ParseException {
		long examenId = Long.parseLong(examenIdStr);
		Response respuesta;
		Optional<Examen> examen = examenService.obtenerExamen(examenId);		
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String fechaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(examen.get().getFecha());
		Date fechaExamen = formateadorfecha.parse(fechaExamenString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se puede ingresar las notas del examen luego de que fue rendido
	 	boolean calificacionCargada = true;
	 	String mensaje = null;
	 	if (!fechaActual.before(fechaExamen)) {	 		
	 		Estudiante_Examen estudianteExamen;
	 		Iterator<Estudiante_Examen> itEstudianteExamen = estudiantesExamen.iterator();
	 		Usuario estudiante;
	 		while (itEstudianteExamen.hasNext() && calificacionCargada) {	
	 			estudianteExamen = itEstudianteExamen.next();
	 			estudiante = inscripcionService.obtenerEstudianteEstudianteExamen(estudianteExamen.getId());
	 			respuesta = inscripcionService.ingresarCalificacionExamen(estudiante, examen.get(), estudianteExamen.getNota());
	 			calificacionCargada = respuesta.isEstado();
	 			mensaje = respuesta.getMensaje();
	 		}
	 		String topico = examen.get().getAsignatura().getNombre().replace(" ","_") +"-examen-"+String.valueOf(examen.get().getId());
	 		this.send(topico);
	 	}
	 	else return new Response(false,"Las calificaciones no pudieron ser ingresadas, aun no ha sido tomado el examen");
	 	if (calificacionCargada) {
	 		return new Response(true, "Las calificaciones fueron ingresadas con exito");
	 	}
	 	else return new Response(false, mensaje);
	}
	
	@PostMapping("/ingresarcalificacioncurso")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response cargarCalificacionCurso(HttpServletRequest request,
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
	 	if (!fechaActual.before(fechaFinCurso)) {
	 		return inscripcionService.ingresarCalificacionCurso(usuario.get(), curso.get(), nota);
	 	}
	 	else return new Response(false, "La calificacion no pudo ser ingresada, el curso aun no ha finalizado");
	}
	
	
	@PostMapping("/ingresarcalificacionescurso")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response cargarCalificacionesCurso(HttpServletRequest request,
			@RequestBody(required = true) List<Curso_Estudiante> estudiantesCurso,
			@RequestParam(name = "cursoId", required = true) String cursoIdStr) throws ParseException {
		Response respuesta;
		long cursoId = Long.parseLong(cursoIdStr);
		Optional<Curso> curso = cursoService.obtenerCurso(cursoId);		
		SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(curso.get().getFechaFin());
		Date fechaFinCurso = formateadorfecha.parse(fechaFinCursoString);
		String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	 	Date fechaActual = formateadorfecha.parse(fechaActualString);
	 	// se pueden ingresar las notas del cusro luego de que finalizo
	 	boolean calificacionCargada = true;
	 	String mensaje = null;
	 	System.out.println("Entro a cargarCalificacionesCurso");
	 	if (!fechaActual.before(fechaFinCurso)) {	 		
	 		Curso_Estudiante estudianteCurso;
	 		Iterator<Curso_Estudiante> itEstudianteCurso = estudiantesCurso.iterator();
	 		Usuario estudiante;
	 		while (itEstudianteCurso.hasNext() && calificacionCargada) {
	 			estudianteCurso = itEstudianteCurso.next();
	 			estudiante = inscripcionService.obtenerEstudianteCursoEstudiante(estudianteCurso.getId());
	 			respuesta = inscripcionService.ingresarCalificacionCurso(estudiante, curso.get(), estudianteCurso.getNota());
	 			calificacionCargada = respuesta.isEstado();
	 			mensaje = respuesta.getMensaje();
	 		}
	 		String topico = curso.get().getAsignatura().getNombre().replace(" ","_") +"-"+String.valueOf(curso.get().getSemestre()+"-"+String.valueOf(curso.get().getAnio()));
	 		this.send(topico);
	 	}
	 	else return new Response(false, "Las calificaciones no pudieron ser ingresadas, el curso aun no ha finalizado");
	 	if (calificacionCargada) {
	 		return new Response(true, "Las calificaciones fueron ingresadas con exito");
	 	}
	 	else return new Response(false, mensaje);
	}
	
	@GetMapping(value = "/send", produces = "application/json")
	public ResponseEntity<String> send(String topico) throws JSONException {
		 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/"+topico);
		body.put("priority", "high");
 
		JSONObject notification = new JSONObject();
		notification.put("title", "TecnoBedelias");
		notification.put("body", "Usted tiene una nueva Calificacion!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");
 
		body.put("notification", notification);
		body.put("data", data);
 
		HttpEntity<String> request = new HttpEntity<>(body.toString());
 
		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
		CompletableFuture.allOf(pushNotification).join();
 
		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
 
		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/examen/estudiantesinscriptos")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public List<Usuario> consultaAnotadosExamen(HttpServletRequest request, 
			@RequestParam(name = "examenId", required = true) String examenIdStr) {		
		long examenId = Long.parseLong(examenIdStr);		
		Optional<Examen> examenOpt = examenService.obtenerExamen(examenId);
		return inscripcionService.consultaAnotadosExamen(examenOpt.get());
	}
	
	@GetMapping("/curso/estudiantesinscriptos")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public List<Usuario> consultaMatriculadosCurso(HttpServletRequest request, 
			@RequestParam(name = "cursoId", required = true) String cursoIdStr) {		
		long cursoId = Long.parseLong(cursoIdStr);		
		Optional<Curso> cursoOpt = cursoService.obtenerCurso(cursoId);
		return inscripcionService.consultaMatriculadosCurso(cursoOpt.get());
	}
	
}
