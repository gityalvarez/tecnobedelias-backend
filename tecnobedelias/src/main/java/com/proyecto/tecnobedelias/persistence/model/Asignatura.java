package com.proyecto.tecnobedelias.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Asignatura {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long cod;
	
	private String nombre;
	
	private boolean electiva;
	@OneToMany(mappedBy="asignatura",fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<Asignatura_Carrera> asignaturaCarrera;
	
	
	@JoinTable(
	        name = "Asignatura_Curso",
	        joinColumns = @JoinColumn(
	                name = "asignaturaId",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "cursoId",
	                referencedColumnName = "id"
	        )
	)	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Curso> cursos;	
	
	@JoinTable(
	        name = "Asignatura_Examen",
	        joinColumns = @JoinColumn(
	                name = "asignaturaId",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "examenId",
	                referencedColumnName = "id"
	        )
	)	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Examen> examanes;
	
	private boolean activa;
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	
	
	
	
	public List<Examen> getExamanes() {
		return examanes;
	}

	public void setExamanes(List<Examen> examanes) {
		this.examanes = examanes;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCod() {
		return cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isElectiva() {
		return electiva;
	}

	public void setElectiva(boolean electiva) {
		this.electiva = electiva;
	}


	public List<Asignatura_Carrera> getAsignaturaCarrera() {
		return asignaturaCarrera;
	}

	public void setAsignaturaCarrera(List<Asignatura_Carrera> asignaturaCarrera) {
		this.asignaturaCarrera = asignaturaCarrera;
	}
	
	

}
