package com.proyecto.tecnobedelias.persistence.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Asignatura_Carrera {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int notaSalvaExamen;
	
	private int notaMinimaExamen;
	
	private int notaMinimaExonera;
	
	private int notaMaxima;
	
	private int creditos;
	
	private boolean electiva;
	
	private boolean activa;
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	
	@ManyToOne
	@JsonIgnore
	private Carrera carrera;
	
	@ManyToOne
	@JsonIgnore
	private Asignatura asignatura;
	

	@JsonIgnore
	@JoinTable(
	        name = "Previas",
	        joinColumns = @JoinColumn(
	                name = "id",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "previasId",
	                referencedColumnName = "id"
	        )
	)	
	@ManyToMany
	private List<Asignatura_Carrera> previas;
	
	@JsonIgnore	
	@ManyToMany(mappedBy="previas")
	private List<Asignatura_Carrera> previaDe;
	
	
	public List<Asignatura_Carrera> getPrevias() {
		return previas;
	}
	
	public void setPrevias(List<Asignatura_Carrera> previas) {
		this.previas = previas;
		
	}
	
	public List<Asignatura_Carrera> getPreviaDe() {
		return previaDe;
	}
	
	public void setPreviaDe(List<Asignatura_Carrera> previaDe) {
		this.previaDe = previaDe;
	}
	



	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	public Carrera getCarrera() {
		return carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public int getNotaSalvaExamen() {
		return notaSalvaExamen;
	}

	public void setNotaSalvaExamen(int notaSalvaExamen) {
		this.notaSalvaExamen = notaSalvaExamen;
	}

	public int getNotaMinimaExamen() {
		return notaMinimaExamen;
	}

	public void setNotaMinimaExamen(int notaMinimaExamen) {
		this.notaMinimaExamen = notaMinimaExamen;
	}

	public int getNotaMinimaExonera() {
		return notaMinimaExonera;
	}

	public void setNotaMinimaExonera(int notaMinimaExonera) {
		this.notaMinimaExonera = notaMinimaExonera;
	}

	public int getNotaMaxima() {
		return notaMaxima;
	}

	public void setNotaMaxima(int notaMaxima) {
		this.notaMaxima = notaMaxima;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}

	public boolean isElectiva() {
		return electiva;
	}

	public void setElectiva(boolean electiva) {
		this.electiva = electiva;
	}

	

}
