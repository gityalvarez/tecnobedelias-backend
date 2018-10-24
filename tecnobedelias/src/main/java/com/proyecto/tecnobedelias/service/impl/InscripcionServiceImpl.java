package com.proyecto.tecnobedelias.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Carrera;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Curso_Estudiante;
import com.proyecto.tecnobedelias.persistence.model.Asignatura_Carrera;
import com.proyecto.tecnobedelias.persistence.model.Examen;
import com.proyecto.tecnobedelias.persistence.model.Estudiante_Examen;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.Asignatura_CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CarreraRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.Curso_EstudianteRepository;
import com.proyecto.tecnobedelias.persistence.repository.Estudiante_ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.ExamenRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
import com.proyecto.tecnobedelias.service.InscripcionService;

@Service
public class InscripcionServiceImpl implements InscripcionService {

	@Autowired
	CarreraRepository carreraRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	ExamenRepository examenRepository;

	@Autowired
	Curso_EstudianteRepository cursoEstudianteRepository;

	@Autowired
	Estudiante_ExamenRepository estudianteExamenRepository;
	
	@Autowired
	Asignatura_CarreraRepository asignaturaCarreraRepository;

	@Override
	public boolean inscripcionCarrera(Usuario usuario, Carrera carrera) {
		if (usuario.getCarreras().contains(carrera)) {
			return false;
		} else {
			usuario.getCarreras().add(carrera);
			//usuarioRepository.save(usuario);
			carrera.getEstudiantes().add(usuario);
			carreraRepository.save(carrera);						
			return true;
		}
	}
	
	/*private boolean isElectivaAsignaturaCarrera(Asignatura asignatura, Carrera carrera) {
		Optional<Asignatura_Carrera> asign_carreraExistente = asignaturaCarreraRepository.findByAsignaturaAndCarrera(asignatura, carrera);
		if (asign_carreraExistente.isPresent()) 
			if (asign_carreraExistente.get().isElectiva())
				return true;
			else return false;
		else return false;			
	}*/
	
	private boolean isAsignaturaEnCarreraEstudiante(Asignatura asignatura, Usuario estudiante) {
		boolean asignaturaEnCarrera = false;			
		Iterator<Carrera> itCarreras = estudiante.getCarreras().iterator();
		while (itCarreras.hasNext() && !asignaturaEnCarrera) {
			Iterator<Asignatura_Carrera> itAsignCarreras = itCarreras.next().getAsignaturaCarrera().iterator();
			while (itAsignCarreras.hasNext() && !asignaturaEnCarrera) {
				if (itAsignCarreras.next().getAsignatura().equals(asignatura))
					asignaturaEnCarrera = true;
			}
		}
		return asignaturaEnCarrera;
	}
	
	
	@Override
	public boolean inscripcionCurso(Usuario usuario, Curso curso) {
		Optional<Curso_Estudiante> cursoEstudianteExistente = cursoEstudianteRepository.findByCursoAndEstudiante(curso,	usuario);
		boolean matriculado = false;
		boolean puedeMatricularse = true; 
		// si ya está matriculado o salvo la asignatura correspondiente al curso no puede matricularse 
		if (cursoEstudianteExistente.isPresent()) {
			//if (cursoEstudianteExistente.get().getEstado().equals("MATRICULADO") || cursoEstudianteExistente.get().getEstado().equals("SALVADO")) {	
				puedeMatricularse = false;
				System.out.println("El estudiante ya esta matriculado");
			//}
		}
		// si no recorro las carreras en las que el usuario esta inscripto y me fijo si la asignatura del curso pertenece a una de estas carreras
		if (puedeMatricularse) {
			System.out.println("El estudiante no esta matriculado");
			/*for (Carrera carrera : usuario.getCarreras()) {
				for (Asignatura_Carrera asign_carrera : carrera.getAsignaturaCarrera()) {
					if (asign_carrera.getAsignatura().equals(curso.getAsignatura()))
						encontrada = true;
				}				
			}*/			
			if (isAsignaturaEnCarreraEstudiante(curso.getAsignatura(), usuario)) {
				System.out.println("La asignatura pertenece a una carrera en la que esta inscripto el usuario");
				boolean cumplePreviaturas = true;
				// para cada carrera en la que el estudiante está inscripto
				if (!usuario.getCarreras().isEmpty()) {					
					for (Carrera carrera : usuario.getCarreras()) {
						System.out.println("carrera: "+carrera.getNombre());
						// para cada asignatura en una carrera en la que el estudiante está inscripto
						//if (!carrera.getAsignaturaCarrera().isEmpty()) {
							for (Asignatura_Carrera asign_carrera : carrera.getAsignaturaCarrera()) {
								// si la asignatura en la carrera es la del curso al que el usuario intenta matricularse
								if (asign_carrera.getAsignatura().equals(curso.getAsignatura())) {
									System.out.println("asignatura: "+asign_carrera.getAsignatura().getNombre());
									// para cada asignatura en la carrera previa a la del curso al que el usuario intenta matricularse
									for (Asignatura_Carrera previa_asign_carrera : asign_carrera.getPrevias()) {
										System.out.println("asignatura previa: "+previa_asign_carrera.getAsignatura().getNombre());
										// si la asignatura previa es electiva en la carrera
										if (previa_asign_carrera.getAsignatura().isTaller()) {
											// para cada curso de dicha asignatura previa en la carrera 
											System.out.println("La asignatura previa es taller");
											if (!previa_asign_carrera.getAsignatura().getCursos().isEmpty()) {
												for (Curso curso_previa : previa_asign_carrera.getAsignatura().getCursos()) {
													System.out.println("asignatura previa: "+previa_asign_carrera.getAsignatura().getNombre());
													// para cada estudiante del curso de la asignatura previa en la carrera
													if (!curso_previa.getCursoEstudiante().isEmpty()) {
														for (Curso_Estudiante curso_estudiante : curso_previa.getCursoEstudiante()) {											
															// si es el estudiante que quiere matricularse
															if (curso_estudiante.getEstudiante().equals(usuario)) {
																// si se verifica que no está salvado el curso de la asignatura previa
																if (!curso_estudiante.getEstado().equals("SALVADO")) {
																	// no cumple las previaturas
																	cumplePreviaturas = false;	
																}												
															}											
														}
													}
													else cumplePreviaturas = false;
												}
											}
											else cumplePreviaturas = false;
										}
										// si la asignatura previa no es electiva en la carrera
										else {
											System.out.println("La asignatura previa no es taller");
											// para cada curso de la asignatura previa en la carrera 
											if (!previa_asign_carrera.getAsignatura().getCursos().isEmpty()) {
												for (Curso curso_previa : previa_asign_carrera.getAsignatura().getCursos()) {
													// para cada estudiante del curso de la asignatura previa
													System.out.println("asignatura previa: "+previa_asign_carrera.getAsignatura().getNombre());
													if (!curso_previa.getCursoEstudiante().isEmpty()) {
														for (Curso_Estudiante curso_estudiante : curso_previa.getCursoEstudiante()) {
															// si es el estudiante que quiere matricularse
															if (curso_estudiante.getEstudiante().equals(usuario)) {
																// si se verifica que no tiene salvado el curso de la asignatura previa
																if (!curso_estudiante.getEstado().equals("SALVADO")) {
																	System.out.println("no tiene salvado el curso de la asignatura previa");
																	// si se verifica que tiene aprobado el curso de la asignatura previa (ganado el derecho a examen)
																	if (curso_estudiante.getEstado().equals("EXAMEN")) {
																		System.out.println("tiene aprobado el curso de la asignatura previa");
																		// para cada examen del estudiante que pretende inscribirse al curso
																		if (!usuario.getEstudianteExamen().isEmpty()) {
																			for (Estudiante_Examen estudiante_examen: usuario.getEstudianteExamen()) {
																				// si la asignatura del examen coincide con la asignatura previa del curso
																				if (estudiante_examen.getExamen().getAsignatura().equals(curso_previa.getAsignatura())) {
																					// si el examen no esta aprobado
																					if (!estudiante_examen.getEstado().equals("APROBADO")) {
																						System.out.println("no tiene aprobado el examen de la asignatura previa");
																						// no cumple la previatura
																						cumplePreviaturas = false;
																					}																		
																				}
																			}
																		}
																		else cumplePreviaturas = false;
																	}
																	else cumplePreviaturas = false;
																}
															}
														}											
													}
													else cumplePreviaturas = false;
												}
											}
											else cumplePreviaturas = false;
										}
									}							
								}
							}
						//}
						//else cumplePreviaturas = false;
					}
				}
				else cumplePreviaturas = false;
				if (cumplePreviaturas) {				
					// anoto al estudiante en el curso
					Curso_Estudiante cursoEstudiante = new Curso_Estudiante();
					cursoEstudiante.setCurso(curso);
					cursoEstudiante.setEstudiante(usuario);
					cursoEstudiante.setEstado("MATRICULADO");
					usuario.getCursoEstudiante().add(cursoEstudiante);
					//usuarioRepository.save(usuario);
					curso.getCursoEstudiante().add(cursoEstudiante);
					//cursoRepository.save(curso);
					cursoEstudianteRepository.save(cursoEstudiante);
					matriculado = true;				 				
				}
			}
		}		
		return matriculado;
	}	

	
	@Override
	public boolean inscripcionExamen(Usuario usuario, Examen examen) {		
		Optional<Estudiante_Examen> estudianteExamenExistente = estudianteExamenRepository.findByExamenAndEstudiante(examen, usuario);
		boolean anotado = false;
		boolean puedeAnotarse = true;
		// si ya esta inscripto al examen no puede volver a anotarse
		if (!examen.getAsignatura().isTaller()) {
			if (estudianteExamenExistente.isPresent()) {// si ya está matriculado o salvo la asignatura correspondiente al curso no puede matricularse 
				//if (estudianteExamenExistente.get().getEstado().equals("ANOTADO") || estudianteExamenExistente.get().getEstado().equals("APROBADO")) {
					System.out.println("El estudiante ya esta anotado");
					puedeAnotarse = false;	
				//}
			}
			if (puedeAnotarse) {			
				// si la asignatura del examen al cual se quiere anotar pertenece a una carrera en que esté matriculado	
				if (isAsignaturaEnCarreraEstudiante(examen.getAsignatura(), usuario)) {
					System.out.println("La asignatura del examen pertenece a una carrera del estudiante");
					Iterator<Curso_Estudiante> itCursoEst = usuario.getCursoEstudiante().iterator();
					// reviso todos los cursos donde esta matriculado el estudiante hasta que no haya mas o quede anotado al examen 
					while (itCursoEst.hasNext() && !anotado) {
						// si la asignatura del curso coincide con la del examen
						Curso_Estudiante curso_est = itCursoEst.next();
						if (curso_est.getCurso().getAsignatura().equals(examen.getAsignatura())) {
							System.out.println("El estudiante esta matriculado en un curso de la asignatura del examen");
							// si el estudiante tiene ganado el curso para el que se encuentra matriculado en la asignatura del examen
							if (curso_est.getEstado().equals("EXAMEN")) {
								System.out.println("El estudiante tiene ganado el curso");
								// anoto al estudiante en el examen
								Estudiante_Examen estudianteExamen = new Estudiante_Examen();
								estudianteExamen.setEstudiante(usuario);
								usuario.getEstudianteExamen().add(estudianteExamen);
								//usuarioRepository.save(usuario);
								estudianteExamen.setExamen(examen);
								examen.getEstudianteExamen().add(estudianteExamen);
								//examenRepository.save(examen);
								estudianteExamen.setEstado("ANOTADO");
								estudianteExamenRepository.save(estudianteExamen);
								anotado = true;						 	
							}
						}
					}
				/*for (Curso_Estudiante cursoEstudiante: usuario.getCursoEstudiante()) {
					if (cursoEstudiante.getCurso().getAsignatura().equals(examen.getAsignatura())) {
						if (cursoEstudiante.getEstado().equals("EXAMEN")) {
							Estudiante_Examen examenEstudiante = new Estudiante_Examen();
							examenEstudiante.setEstudiante(usuario);
							examenEstudiante.setExamen(examen);
							examenEstudiante.setEstado("ANOTADO");
							estudianteExamenRepository.save(examenEstudiante);
							anotado = true;						
						}
					}
				}*/
				}
			}
		}		
		return anotado;			
	}
	

	@Override
	public boolean desistirCurso(Usuario usuario, Curso curso) {
		Optional<Curso_Estudiante> cursoEstudianteExistente = cursoEstudianteRepository.findByCursoAndEstudiante(curso,	usuario);
		if (cursoEstudianteExistente.isPresent()) {	
			System.out.println("existe el estudiante en el curso");
			if (cursoEstudianteExistente.get().getEstado().equals("MATRICULADO")) {
				System.out.println("estado matriculado");
				usuario.getCursoEstudiante().remove(cursoEstudianteExistente.get());
				curso.getCursoEstudiante().remove(cursoEstudianteExistente.get());
				cursoEstudianteRepository.delete(cursoEstudianteExistente.get());
				return true;
			}
			else return false;
		} 
		else return false;		
	}

	@Override
	public boolean desistirExamen(Usuario usuario, Examen examen) {		
		Optional<Estudiante_Examen> estudianteExamenExistente = estudianteExamenRepository.findByExamenAndEstudiante(examen, usuario);
		if (estudianteExamenExistente.isPresent()) {
			System.out.println("existe el estudiante en el examen");
			if (estudianteExamenExistente.get().getEstado().equals("ANOTADO")) {
				System.out.println("estado anotado");
				usuario.getEstudianteExamen().remove(estudianteExamenExistente.get());
				examen.getEstudianteExamen().remove(estudianteExamenExistente.get());
				estudianteExamenRepository.delete(estudianteExamenExistente.get());
				return true;
			}
			else return false;
		}
		else return false;
	}

	/*@Override
	public boolean desistirCarrera(Usuario usuario, Carrera carrera) {
		if (usuario.getCarreras().contains(carrera)) {
			usuario.getCarreras().remove(carrera);
			carrera.getEstudiantes().remove(usuario);
			carreraRepository.save(carrera);
			return true;
		}
		return false;
	}*/

	@Override
	public List<Curso> consultaCursos(Usuario usuario) {
		List<Curso> listaCursos = new ArrayList<>();
		for(Curso_Estudiante cursoEstudiante : usuario.getCursoEstudiante()) {
			listaCursos.add(cursoEstudiante.getCurso());
			//System.out.println(agregue el curso );
		}
		return listaCursos;
	}
	
	
	@Override
	public List<Examen> consultaExamenes(Usuario usuario) {
		List<Examen> listaExamenes = new ArrayList<>();
		for(Estudiante_Examen examenEstudiante : usuario.getEstudianteExamen()) {
			listaExamenes.add(examenEstudiante.getExamen());
			//System.out.println(agregue el curso );
		}
		return listaExamenes;
	}


}
