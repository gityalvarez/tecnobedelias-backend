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
import com.proyecto.tecnobedelias.service.AsignaturaService;
import com.proyecto.tecnobedelias.service.ExamenService;

@RestController
@RequestMapping("/examen")
public class ExamenController {

	@Autowired
	ExamenService examenService;
	
	@Autowired
	AsignaturaService asignaturaService;
	
	@GetMapping("/listar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public List<Examen> listarExamenes() {
		return examenService.listarExamenes();
	}
	
	@PostMapping("/crear")
    @PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
    public boolean crearExamen(HttpServletRequest request, 
    		@RequestBody(required = true) Examen examen, 
    		@RequestParam(name = "nombre", required = true) String nombreAsignatura) throws ParseException{
		System.out.println("entre a crearExamen");
		Optional<Asignatura> asignaturaOpt = asignaturaService.obtenerAsignaturaNombre(nombreAsignatura);
		if (!asignaturaOpt.get().isTaller()) {
			examen.setAsignatura(asignaturaOpt.get());
			examen.setNombreAsignatura(asignaturaOpt.get().getNombre());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(examen.getFecha());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
			String fechaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			Date fechaExamen = formateadorfecha.parse(fechaExamenString);
			examen.setFecha(fechaExamen);
			System.out.println("fecha examen "+examen.getFecha());
			if (!examenService.existeExamen(examen.getAsignatura(), examen.getFecha())){
				System.out.println("no existe el examen");
				boolean fechaOk = true;
				List<Curso> cursosAsignatura = asignaturaOpt.get().getCursos();
				if (!cursosAsignatura.isEmpty()) {
					String fechaFinCursoString;
					String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					Date fechaActualDate = formateadorfecha.parse(fechaActualString);
					long fechaActual = fechaActualDate.getTime();
					Date fechaFinCursoDate;
					long fechaFinCurso;
					long diferencia = Long.MAX_VALUE;
					Curso ultimocurso = null;
					// obtengo el ultimo curso de la asignatura para la que se quiere crear el examen
					for (Curso curso : cursosAsignatura) {
						fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
						fechaFinCursoDate = formateadorfecha.parse(fechaFinCursoString);
						fechaFinCurso = fechaFinCursoDate.getTime();
						if (fechaActual - fechaFinCurso < diferencia) {
							diferencia = fechaActual - fechaFinCurso;
							ultimocurso = curso;
						}
					}	
					// si existe el ultimo curso
					if (ultimocurso != null) {
						System.out.println("encuentro ultimo curso");
						fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(ultimocurso.getFechaFin());
						fechaFinCursoDate = formateadorfecha.parse(fechaFinCursoString);
						System.out.println("fecha fin ultimo curso " + fechaFinCursoDate);
						//if (fechaActualDate.after(fechaFinCursoDate)) {
							fechaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(examen.getFecha());
							Date fechaExamenDate = formateadorfecha.parse(fechaExamenString);
							if (fechaExamenDate.before(fechaFinCursoDate)) {
								System.out.println("fecha examen < fecha fin curso");
								fechaOk = false;
							}
						/*}
						//else fechaOk = false;*/
					}
					else fechaOk = false;
					if (fechaOk) {
						examen.setNombreAsignatura(nombreAsignatura);
						examenService.altaExamen(examen);
						Optional<Examen> examenOpt = examenService.obtenerExamen(examen.getAsignatura(), examen.getFecha());
						Asignatura asignatura = examenOpt.get().getAsignatura();
						asignatura.getExamenes().add(examenOpt.get());
						asignaturaService.modificacionAsignatura(asignatura);
						return true;
					}
					else return false;
				}
				else {
					System.out.println("La asignatura no tiene cursos");
					return false;			
				}
			}
			else return false; 
		}
		else return false;
    }
	
	
	@PostMapping("/modificar")
    @PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
    public boolean modificarExamen(HttpServletRequest request, 
    		@RequestBody(required = true) Examen examen, 
    		@RequestParam(name = "examenId", required = true) String examenIdStr) throws ParseException {
		System.out.println("entre a modificarExamen");
		long examenId = Long.parseLong(examenIdStr);
		if (examenService.existeExamen(examenId)) {			
			boolean fechaOk = true;
			Examen examenExistente = examenService.obtenerExamen(examenId).get();
			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
			String fechaActualString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Date fechaActualDate = formateadorfecha.parse(fechaActualString);
			String fechaActualExamenString = new SimpleDateFormat("yyyy-MM-dd").format(examenExistente.getFecha());
			Date fechaActualExamenDate = formateadorfecha.parse(fechaActualExamenString);
			System.out.println("fecha actual examen "+fechaActualExamenDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(examen.getFecha());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			//String fechaNuevaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(examen.getFecha());
			String fechaNuevaExamenString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			Date fechaNuevaExamenDate = formateadorfecha.parse(fechaNuevaExamenString);
			examen.setFecha(fechaNuevaExamenDate);
			//System.out.println("fecha nueva examen "+examen.getFecha());
			if (fechaNuevaExamenDate.after(fechaActualDate) && fechaActualDate.before(fechaActualExamenDate)) {
				List<Curso> cursosAsignatura = examenExistente.getAsignatura().getCursos();
				if (!cursosAsignatura.isEmpty()) {
					String fechaFinCursoString;				
					long fechaActual = fechaActualDate.getTime();
					Date fechaFinCursoDate;
					long fechaFinCurso;
					long diferencia = Long.MAX_VALUE;
					Curso ultimocurso = null;
					// obtengo el ultimo curso de la asignatura para la que se quiere modificar el examen
					for (Curso curso : cursosAsignatura) {
						fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(curso.getFechaFin());
						fechaFinCursoDate = formateadorfecha.parse(fechaFinCursoString);
						fechaFinCurso = fechaFinCursoDate.getTime();
						if (fechaActual - fechaFinCurso < diferencia) {
							diferencia = fechaActual - fechaFinCurso;
							ultimocurso = curso;
						}
					}	
					// si existe el ultimo curso
					if (ultimocurso != null) {
						//System.out.println("encuentro ultimo curso");
						fechaFinCursoString = new SimpleDateFormat("yyyy-MM-dd").format(ultimocurso.getFechaFin());
						fechaFinCursoDate = formateadorfecha.parse(fechaFinCursoString);
						//System.out.println("fecha fin ultimo curso " + fechaFinCursoString);
						//if (fechaActualDate.after(fechaFinCursoDate)) {
							if (fechaNuevaExamenDate.before(fechaFinCursoDate)) {
								System.out.println("fecha examen < fecha fin curso");
								fechaOk = false;
							}
						/*}
						//else fechaOk = false;*/
					}
					else fechaOk = false;
					if (fechaOk) {
						//System.out.println("fechaOk");
						//System.out.println("asignatura "+examenExistente.getAsignatura().getNombre());
						//System.out.println("nueva fecha "+examen.getFecha());
						if (examenService.existeExamen(examenExistente.getAsignatura(), examen.getFecha())) {
							Examen examenExistenteAsignFecha = examenService.obtenerExamen(examenExistente.getAsignatura(), examen.getFecha()).get();
							if (examenExistenteAsignFecha.getId() == examenExistente.getId()) {
								System.out.println("fecha examen: "+ examen.getFecha());
								examenExistente.setFecha(examen.getFecha());
								examenExistente.setHora(examen.getHora());
								examenService.modificacionExamen(examenExistente);
								return true;
							}
							else return false;
						}
						else {
							System.out.println("fecha examen: "+examen.getFecha());
							examenExistente.setFecha(examen.getFecha());
							examenExistente.setHora(examen.getHora());
							examenService.modificacionExamen(examenExistente);
							return true;
						}
					}
					else return false;
				}
				else {
					System.out.println("La asignatura no tiene cursos");
					return false;			
				}
			}
			else return false;
		}
		else return false;
    }
	
	
	@GetMapping("/borrar")
	@PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
	public boolean borrarExamen(HttpServletRequest request, @RequestParam(name = "examenId", required = true) String examenIdStr) {
		long examenId = Long.parseLong(examenIdStr);
		if (examenService.existeExamen(examenId)) {
			Examen examen = examenService.obtenerExamen(examenId).get();
			if (examen.getEstudianteExamen().isEmpty()) {
				examenService.bajaExamen(examen);
				return true;
			}
			else return false;	
		}
		else return false;
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
