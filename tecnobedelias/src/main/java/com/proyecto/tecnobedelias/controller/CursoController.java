package com.proyecto.tecnobedelias.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Horario;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.CursoService;

@RestController
@RequestMapping("/curso")
public class CursoController {

	@Autowired
	CursoRepository cursoRepository;

	@Autowired
	AsignaturaRepository asignaturaRepository;
	
	@Autowired
	CursoService cursoService;
	
	@Autowired
	AsignaturaService asignaturaService;

	@GetMapping("/listar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO') or hasRole('ROLE_ESTUDIANTE')")
	public List<Curso> listarCursos() {
		return cursoRepository.findAll();
	}

	@PostMapping("/crear")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean crearCurso(HttpServletRequest request, @RequestBody(required = true) Curso curso/*, @RequestBody List<Horario> horarios*/, 
			@RequestParam(name = "nombre", required = true) String nombreAsignatura) throws ParseException {
		System.out.println("entre a crearCurso");
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
				boolean fechasOk = true;
				SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd");
				String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(fechaActualDate);
				Date fechaActual = formateadorfecha.parse(fechaActualString);
			 	String fechaInicioString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaInicio());
			 	Date fechaInicio = formateadorfecha.parse(fechaInicioString);
			 	System.out.println("fecha actual: " + fechaActual);	
			 	System.out.println("fecha inicio: " + fechaInicio);	
			 	String fechaFinString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
			 	Date fechaFin = formateadorfecha.parse(fechaFinString);	
			 	System.out.println("fecha fin: " + fechaFin);	
			 	if (fechaInicio.before(fechaActual)) {
			 		fechasOk = false;
			 		System.out.println("fecha inicio < fecha actual");				
			 	}
			 	if (fechaFin.before(fechaInicio)) {
			 		fechasOk = false;
			 		System.out.println("fecha fin < fecha inicio");				
			 	}
			 	if (fechasOk) {
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
			 			System.out.println("horaInicioDate: "+ horaInicioDate);	
			 			horaInicio = horaInicioDate.getTime();
			 			horaFinDate = formateadorhora.parse(horario.getHoraFin());
			 			System.out.println("horaFinDate: "+ horaFinDate);	
			 			horaFin = horaFinDate.getTime();
			 			if (horaFin <= horaInicio) {
			 				horariosOk = false;
			 				System.out.println("hora fin <= hora inicio");	
			 			}
			 		}
			 		if (horariosOk) {
			 			curso.setNombreAsignatura(nombreAsignatura);
			 			cursoService.altaCurso(curso/*, horarios*/);
			 			Optional<Curso> cursoOpt = cursoService.obtenerCurso(curso.getAsignatura(), curso.getSemestre(), curso.getAnio());
			 			Asignatura asignatura = cursoOpt.get().getAsignatura();
			 			asignatura.getCursos().add(cursoOpt.get());
			 			asignaturaRepository.save(asignatura);
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
	
	
	@GetMapping("/modificar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean modificarCurso(HttpServletRequest request,  
			@RequestBody(required = true) Curso curso, 
			@RequestParam(name = "cursoId", required = true) String cursoIdStr) throws ParseException {
		long cursoId = Long.parseLong(cursoIdStr);		
		if (cursoService.existeCurso(cursoId)) {
			boolean fechasOk = true;
			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
			String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Date fechaActual = formateadorfecha.parse(fechaActualString);
		 	String fechaInicioString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaInicio());
		 	Date fechaInicio = formateadorfecha.parse(fechaInicioString);
		 	System.out.println("fecha actual: " + fechaActual);	
		 	System.out.println("fecha inicio: " + fechaInicio);	
		 	String fechaFinString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
		 	Date fechaFin = formateadorfecha.parse(fechaFinString);	
		 	System.out.println("fecha fin: " + fechaFin);	
		 	if (fechaInicio.before(fechaActual)) {
		 		fechasOk = false;
		 		System.out.println("fecha inicio < fecha actual");				
		 	}
		 	if (fechaFin.before(fechaInicio)) {
		 		fechasOk = false;
		 		System.out.println("fecha fin < fecha inicio");				
		 	}
		 	if (fechasOk) {
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
		 			System.out.println("horaInicioDate: "+ horaInicioDate);	
		 			horaInicio = horaInicioDate.getTime();
		 			horaFinDate = formateadorhora.parse(horario.getHoraFin());
		 			System.out.println("horaFinDate: "+ horaFinDate);	
		 			horaFin = horaFinDate.getTime();
		 			if (horaFin <= horaInicio) {
		 				horariosOk = false;
		 				System.out.println("hora fin <= hora inicio");	
		 			}
		 		}
		 		if (horariosOk) {
		 			Curso cursoExistente = cursoService.obtenerCurso(cursoId).get();
		 			if (cursoExistente.getSemestre() != curso.getSemestre()) {
		 				if (!cursoService.existeCurso(cursoExistente.getAsignatura(), curso.getSemestre(), cursoExistente.getAnio())) {
		 					cursoExistente.setFechaInicio(curso.getFechaInicio());
		 					cursoExistente.setFechaFin(curso.getFechaFin());
		 					cursoExistente.setHorarios(curso.getHorarios());
		 					cursoExistente.setSemestre(curso.getSemestre());
		 					cursoService.modificacionCurso(cursoExistente);
		 					return true;
		 				}
		 				else return false;
		 			}
		 			else {
		 				cursoExistente.setFechaInicio(curso.getFechaInicio());
	 					cursoExistente.setFechaFin(curso.getFechaFin());
	 					cursoExistente.setHorarios(curso.getHorarios());
	 					cursoService.modificacionCurso(cursoExistente);
	 					return true;
		 			}
		 		}
		 		else return false;
			}
		 	else return false;
		}
		else return false;
	}
	

	@GetMapping("/borrar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean borrarCurso(HttpServletRequest request,@RequestParam(name = "cursoId", required = true) String cursoIdStr) {
		long cursoId = Long.parseLong(cursoIdStr);
		if (cursoService.existeCurso(cursoId)) {
			Curso curso = cursoService.obtenerCurso(cursoId).get();
			if (curso.getCursoEstudiante().isEmpty()) {
				cursoService.bajaCurso(curso);
				return true;
			}
			else return false;
		}
		else return false;
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
