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

import com.proyecto.tecnobedelias.Util.Response;
import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Horario;
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.CursoService;
import com.proyecto.tecnobedelias.service.HorarioService;

@RestController
@RequestMapping("/curso")
public class CursoController {

	@Autowired
	CursoService cursoService;
	
	@Autowired
	HorarioService horarioService;
	
	@Autowired
	AsignaturaService asignaturaService;

	@GetMapping("/listar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO') or hasRole('ROLE_ESTUDIANTE')")
	public List<Curso> listarCursos() {
		return cursoService.listarCursos();
	}

	@PostMapping("/crear")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response crearCurso(HttpServletRequest request, @RequestBody(required = true) Curso curso, 
			@RequestParam(name = "nombre", required = true) String nombreAsignatura) throws ParseException {
		System.out.println("entre a crearCurso");
		Response respuesta;
		Optional<Asignatura> asignaturaOpt = asignaturaService.obtenerAsignaturaNombre(nombreAsignatura);
		curso.setAsignatura(asignaturaOpt.get());
		curso.setNombreAsignatura(asignaturaOpt.get().getNombre());
		//curso.setHorarios(horarios);
		if (!cursoService.existeCurso(curso.getAsignatura(), curso.getSemestre(), curso.getAnio())) {
			Date fechaActualDate = new Date();
			String anioActualString = new SimpleDateFormat("yyyy").format(fechaActualDate);
			int anioActual = Integer.parseInt(anioActualString);
			System.out.println("anio actual: " + anioActual);	
			if (curso.getAnio() >= anioActual) {				
				boolean fechaInicioOk = true;
				boolean fechaFinOk = true;
				SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd");
				String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(fechaActualDate);
				Date fechaActual = formateadorfecha.parse(fechaActualString);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(curso.getFechaInicio());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			 	//String fechaInicioString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaInicio());
				String fechaInicioString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			 	Date fechaInicio = formateadorfecha.parse(fechaInicioString);
			 	//System.out.println("fecha actual: " + fechaActual);	
			 	//System.out.println("fecha inicio: " + fechaInicio);
			 	calendar.setTime(curso.getFechaFin());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			 	//String fechaFinString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
				String fechaFinString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			 	Date fechaFin = formateadorfecha.parse(fechaFinString);	
			 	//System.out.println("fecha fin: " + fechaFin);	
			 	if (fechaInicio.before(fechaActual)) {
			 		fechaInicioOk = false;			 					
			 	}
			 	if (fechaFin.before(fechaInicio)) {
			 		fechaFinOk = false;			 					
			 	}
			 	if (fechaInicioOk) {
			 		if (fechaFinOk) {
			 			boolean horariosOk = true;
			 			Date horaInicioDate;
			 			Date horaFinDate;
			 			long horaInicio;
			 			long horaFin;
			 			SimpleDateFormat formateadorhora = new SimpleDateFormat("HH:mm");
			 			Iterator<Horario> itHorarios = curso.getHorarios().iterator();
			 			Horario horario;
			 			while (itHorarios.hasNext() && horariosOk) {
			 				horario = itHorarios.next();
			 				horaInicioDate = formateadorhora.parse(horario.getHoraInicio());
			 				//System.out.println("horaInicioDate: "+ horaInicioDate);	
			 				horaInicio = horaInicioDate.getTime();
			 				horaFinDate = formateadorhora.parse(horario.getHoraFin());
			 				//System.out.println("horaFinDate: "+ horaFinDate);	
			 				horaFin = horaFinDate.getTime();
			 				if (horaFin <= horaInicio) {
			 					horariosOk = false;
			 					//return new Response(false, "El curso no pudo ser creado, algun horario tiene hora de fin anterior a la hora de inicio");	
			 				}
			 			}
			 			if (horariosOk) {
			 				curso.setNombreAsignatura(nombreAsignatura);
			 				curso.setFechaInicio(fechaInicio);
			 				curso.setFechaFin(fechaFin);
			 				cursoService.altaCurso(curso/*, horarios*/);
			 				Optional<Curso> cursoOpt = cursoService.obtenerCurso(curso.getAsignatura(), curso.getSemestre(), curso.getAnio());
			 				Asignatura asignatura = cursoOpt.get().getAsignatura();
			 				asignatura.getCursos().add(cursoOpt.get());
			 				asignaturaService.modificacionAsignatura(asignatura);
			 				respuesta = new Response(true, "El curso fue creado con exito para la asignatura " + nombreAsignatura);
			 			}
			 			else respuesta = new Response(false, "El curso no pudo ser creado, algun horario tiene hora de fin anterior a la hora de inicio");
			 		}
			 		else respuesta = new Response(false, "El curso no pudo ser creado, la fecha de finalizacion es anterior a la fecha de inicio");
			 	}
			 	else respuesta = new Response(false, "El curso no pudo ser creado, la fecha de inicio es anterior a la fecha actual");
			}
			else respuesta = new Response(false, "El curso no pudo ser creado, el anio es anterior al actual");
		}
		else respuesta = new Response(false, "El curso no pudo ser creado, ya existe un curso para esta asignatura en el mismo semestre del mismo anio");
		return respuesta;
	}
	
	
	@PostMapping("/modificar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response modificarCurso(HttpServletRequest request,  
			@RequestBody(required = true) Curso curso, 
			@RequestParam(name = "cursoId", required = true) String cursoIdStr) throws ParseException {
		Response respuesta;
		long cursoId = Long.parseLong(cursoIdStr);		
		if (cursoService.existeCurso(cursoId)) {
			boolean fechaInicioOk = true;
			boolean fechaFinOk = true;
			boolean fechaFinExistenteOk = true;
			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
			String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Date fechaActual = formateadorfecha.parse(fechaActualString);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(curso.getFechaInicio());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		 	//String fechaInicioString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaInicio());
			String fechaInicioString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		 	Date fechaInicio = formateadorfecha.parse(fechaInicioString);
		 	curso.setFechaInicio(fechaInicio);
		 	//System.out.println("fecha actual: " + fechaActual);	
		 	//System.out.println("fecha inicio: " + fechaInicio);	
		 	calendar.setTime(curso.getFechaFin());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		 	//String fechaFinString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
			String fechaFinString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		 	Date fechaFin = formateadorfecha.parse(fechaFinString);	
		 	curso.setFechaFin(fechaFin);
		 	System.out.println("fecha fin: " + fechaFin);	
		 	if (fechaInicio.before(fechaActual)) {
		 		fechaInicioOk = false;		 		
		 		System.out.println("fecha inicio < fecha actual");				
		 	}
		 	if (fechaFin.before(fechaInicio)) {
		 		fechaFinOk = false;
		 		System.out.println("fecha fin < fecha inicio");				
		 	}
		 	Curso cursoExistente = cursoService.obtenerCurso(cursoId).get();
		 	if (cursoExistente.getFechaFin().before(fechaActual)) {
		 		fechaFinExistenteOk = false;
		 		System.out.println("fecha fin curso existente < fecha actual");				
		 	}
		 	if (fechaInicioOk) {
		 		if (fechaFinOk) {
		 			if (fechaFinExistenteOk) {			 		
		 				boolean horariosOk = true;
		 				Date horaInicioDate;
		 				Date horaFinDate;
		 				long horaInicio;
		 				long horaFin;
		 				SimpleDateFormat formateadorhora = new SimpleDateFormat("HH:mm");
		 				Iterator<Horario> itHorarios = curso.getHorarios().iterator();
		 				Horario horario;
		 				while (itHorarios.hasNext() && horariosOk) {
		 					horario = itHorarios.next();
		 					horaInicioDate = formateadorhora.parse(horario.getHoraInicio());
		 					//System.out.println("horaInicioDate: "+ horaInicioDate);	
		 					horaInicio = horaInicioDate.getTime();
		 					horaFinDate = formateadorhora.parse(horario.getHoraFin());
		 					//System.out.println("horaFinDate: "+ horaFinDate);	
		 					horaFin = horaFinDate.getTime();
		 					if (horaFin <= horaInicio) {
		 						horariosOk = false;
		 						System.out.println("hora fin <= hora inicio");	
		 					}
		 				}
		 				if (horariosOk) {		 			
		 					if (cursoExistente.getSemestre() != curso.getSemestre()) {
		 						if (!cursoService.existeCurso(cursoExistente.getAsignatura(), curso.getSemestre(), cursoExistente.getAnio())) {
		 							cursoExistente.setFechaInicio(curso.getFechaInicio());
		 							cursoExistente.setFechaFin(curso.getFechaFin());
		 							List<Horario> horarios = cursoExistente.getHorarios();
		 							cursoExistente.setSemestre(curso.getSemestre());
		 							cursoExistente.setHorarios(curso.getHorarios());
		 							cursoService.modificacionCurso(cursoExistente);	
		 							for (Horario diayhoras : horarios) {
		 								//System.out.println("dia "+diayhoras.getDia());	
		 								horarioService.bajaHorario(diayhoras);
		 							}
		 							respuesta = new Response(true, "El curso fue modificado con exito");
		 						}
		 						else respuesta = new Response(false, "El curso no pudo ser modificado, ya hay un curso para esa asignatura ese semestre de ese anio");
		 					}
		 					else {
		 						cursoExistente.setFechaInicio(curso.getFechaInicio());
		 						cursoExistente.setFechaFin(curso.getFechaFin());
		 						List<Horario> horarios = cursoExistente.getHorarios();
		 						cursoExistente.setHorarios(curso.getHorarios());
		 						cursoService.modificacionCurso(cursoExistente);	
		 						for (Horario diayhoras : horarios) {
		 							//System.out.println("dia "+diayhoras.getDia());	
		 							horarioService.bajaHorario(diayhoras);
		 						}
		 						respuesta = new Response(true, "El curso fue modificado con exito");
		 					}
		 				}
		 				else respuesta = new Response(false, "El curso no pudo ser modificado, algun horario tiene hora de fin anterior a la hora de inicio");
		 			}
		 			else respuesta = new Response(false, "El curso no pudo ser modificado, el curso ya ha finalizado");
		 		}
		 		else respuesta = new Response(false, "El curso no pudo ser modificado, la fecha de finalizacion es anterior a la fecha de inicio");
			}
			else respuesta = new Response(false, "El curso no pudo ser modificado, la fecha de inicio es anterior a la fecha actual");
		}
		else respuesta = new Response(false, "El curso no pudo ser modificado, el curso no existe");
		return respuesta;
	}
	

	@GetMapping("/borrar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public Response borrarCurso(HttpServletRequest request,@RequestParam(name = "cursoId", required = true) String cursoIdStr) {
		long cursoId = Long.parseLong(cursoIdStr);
		Response respuesta;
		if (cursoService.existeCurso(cursoId)) {
			Curso curso = cursoService.obtenerCurso(cursoId).get();
			if (curso.getCursoEstudiante().isEmpty()) {
				cursoService.bajaCurso(curso);
				respuesta = new Response(true, "El curso fue borrado con exito");
			}
			else respuesta = new Response(false, "El curso no pudo ser borrado, tiene algun estudiante registrado");
		}
		else respuesta = new Response(false, "El curso no pudo ser borrado, no existe el curso");
		return respuesta;
	}

	/*@PostMapping("/asignarAsignatura")
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
		curso.get().setAsignatura(asignatura);
		asignaturaRepository.save(asignatura);
		cursoRepository.save(curso.get());
	}*/
	
	/*@PostMapping("/desasignarAsignatura")
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
	}*/

}
