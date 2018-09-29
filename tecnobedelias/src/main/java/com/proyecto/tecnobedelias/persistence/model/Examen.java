package com.proyecto.tecnobedelias.persistence.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Examen {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Date fecha;
	
	@ManyToOne
	private Asignatura asignatura;
	
	private boolean activa;
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	
	
	
	 public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@OneToMany(mappedBy="examen",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 private List<Examen_Estudiante> examenEstudiante;

	public List<Examen_Estudiante> getExamenEstudiante() {
		return examenEstudiante;
	}

	public void setExamenEstudiante(List<Examen_Estudiante> examenEstudiante) {
		this.examenEstudiante = examenEstudiante;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
