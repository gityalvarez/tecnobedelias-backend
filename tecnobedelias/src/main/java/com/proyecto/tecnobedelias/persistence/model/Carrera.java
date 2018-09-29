package com.proyecto.tecnobedelias.persistence.model;

import java.util.List;
import java.util.Set;

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
public class Carrera {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nombre;
	
	@OneToMany(mappedBy="carrera",fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<Asignatura_Carrera> asignaturaCarrera;
	
	@JoinTable(
	        name = "Carrera_Estudiante",
	        joinColumns = @JoinColumn(
	                name = "carreraId",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "estudianteId",
	                referencedColumnName = "id"
	        )
	)	
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Usuario> estudiantes;
	
	private int creaditosMinimos;

	private boolean activa;
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public List<Usuario> getEstudiantes() {
		return estudiantes;
	}

	public int getCreaditosMinimos() {
		return creaditosMinimos;
	}

	public void setCreaditosMinimos(int creaditosMinimos) {
		this.creaditosMinimos = creaditosMinimos;
	}

	public void setEstudiantes(List<Usuario> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public List<Asignatura_Carrera> getAsignaturaCarrera() {
		return asignaturaCarrera;
	}

	public void setAsignaturaCarrera(List<Asignatura_Carrera> asignaturaCarrera) {
		this.asignaturaCarrera = asignaturaCarrera;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	

}
