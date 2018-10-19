package com.proyecto.tecnobedelias.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Asignatura;
import com.proyecto.tecnobedelias.persistence.model.Curso;
import com.proyecto.tecnobedelias.persistence.model.Horario;
import com.proyecto.tecnobedelias.persistence.repository.AsignaturaRepository;
import com.proyecto.tecnobedelias.persistence.repository.CursoRepository;
import com.proyecto.tecnobedelias.persistence.repository.HorarioRepository;
import com.proyecto.tecnobedelias.service.CursoService;
import com.proyecto.tecnobedelias.service.HorarioService;

@Service
public class CursoServiceImpl implements CursoService {

	@Autowired
	CursoRepository cursoRepository;
	
	@Autowired
	AsignaturaRepository asignaturaRepository;	
	
	@Autowired
	HorarioRepository horarioRepository;
	
	@Autowired
	HorarioService horarioService;
	
	@Override
	public boolean altaCurso(Curso curso/*, List<Horario> horarios*/) {	
		
		if (!existeCurso(curso.getAsignatura(), curso.getSemestre(), curso.getAnio())) {
			/*List<Horario> horarios = new ArrayList<Horario>();*/			
			/*for (Horario h : curso.getHorarios()) {
				System.out.println("dia: " + h.getDia());
				//horarioService.altaHorario(h);
				curso.getHorarios().remove(o)
				Optional<Horario> horarioOpt = horarioRepository.findByDiaAndHoraInicioAndHoraFin(h.getDia(), h.getHoraInicio(), h.getHoraFin());
				if (!horarioOpt.isPresent()) {
					horarioRepository.save(horarioOpt.get());
				}
				
			}				
				System.out.println("id Horario: "+horarioOpt.get().getId());
				//asignarHorarioCurso(horarioOpt.get(), cursoOpt.get());
				horarios.add(horarioOpt.get());
				//curso.getHorarios().remove(h);
				//curso.getHorarios().add(horarioOpt.get());
				System.out.println("paso addHorario");
			}
			System.out.println("salgo del segundo for");
			for (Horario h : horarios) {
				System.out.println("id: " + h.getId());
				System.out.println("dia: " + h.getDia());			
			}*/
			//curso.setHorarios(null);
			cursoRepository.save(curso);
			/*Optional<Curso> cursoOpt = cursoRepository.findByAsignaturaAndSemestreAndAnio(curso.getAsignatura(), curso.getSemestre(), curso.getAnio());
			//cursoOpt.get().setHorarios(horarios);
			System.out.println("antes del tercer for");
			for (Horario horario : horarios) {
				System.out.println("id: " + horario.getId());
				System.out.println("dia: " + horario.getDia());
				//cursoOpt.get().getHorarios().add(horario);
			//horariosaux.add(horario.get());
			//curso.getHorarios().remove(h);
			//curso.setId(cursoOpt.get().getId());
			//curso.getHorarios().add(horario);
				System.out.println("paso signarHorario");
			}	
			cursoRepository.save(cursoOpt.get());
			System.out.println("salgo del tercer for");*/
		//curso.setHorarios(horariosaux);		
			return true;
		}
		else return false;
	}
	
	
	public void asignarHorarioCurso(Horario horario, Curso curso) {
		curso.getHorarios().add(horario);
		cursoRepository.save(curso);
	}
	

	@Override
	public List<Curso> listarCursos() {
		return cursoRepository.findAll();
	}

	@Override
	public boolean existeCurso(Curso curso) {
		return cursoRepository.existsById(curso.getId());
	}
	
	@Override
	public boolean existeCurso(Asignatura asignatura, int semestre, int anio) {		
		Optional<Curso> cursoExistente = cursoRepository.findByAsignaturaAndSemestreAndAnio(asignatura, semestre, anio);
		if (cursoExistente.isPresent())
			return true;
		else return false;		
	}

	@Override
	public void bajaCurso(Curso curso) {		
		cursoRepository.delete(curso);
		
	}
	
	

}
