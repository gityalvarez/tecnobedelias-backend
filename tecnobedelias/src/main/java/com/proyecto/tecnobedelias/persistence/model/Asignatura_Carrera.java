package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="asignatura_carrera", indexes={@Index(name="carrera_asignatura_carrera_index", columnList="id_carrera"), @Index(name="asignatura_asignatura_carrera_index", columnList="id_asignatura")})
public class Asignatura_Carrera implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="notaSalvaExamen")
	private int notaSalvaExamen;
	
	@Column(name="notaMinimaExamen")
	private int notaMinimaExamen;
	
	@Column(name="notaMinimaExonera")
	private int notaMinimaExonera;
	
	@Column(name="notaMaxima")
	private int notaMaxima;
	
	@Column(name="creditos")
	private int creditos;
	
	//@Column(name="electiva", nullable=true)
	//private boolean electiva;
	
	/*@Column(name="activa", nullable=true)
	private boolean activa;*/
	
	public Asignatura_Carrera() {
	}
	
	public Asignatura_Carrera(int notaSalvaExamen, int notaMinimaExamen, int notaMinimaExonera, int notaMaxima,	int creditos, boolean electiva) {
		this.notaSalvaExamen = notaSalvaExamen;
		this.notaMinimaExamen = notaMinimaExamen;
		this.notaMinimaExonera = notaMinimaExonera;
		this.notaMaxima = notaMaxima;
		this.creditos = creditos;
		//this.electiva = electiva;
		//this.activa = true;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	/*public boolean isElectiva() {
		return electiva;
	}

	public void setElectiva(boolean electiva) {
		this.electiva = electiva;
	}*/
	
	/*public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}*/
	
	@ManyToOne
	@JsonIgnore	
	@JoinColumn(name="id_carrera", foreignKey = @ForeignKey(name="asignatura_carrera_carrera_fkey"))
	private Carrera carrera;
	
	@ManyToOne
	//@JsonIgnore
	@JoinColumn(name="id_asignatura", foreignKey = @ForeignKey(name="asignatura_carrera_asignatura_fkey"))
	private Asignatura asignatura;
	
	@JsonIgnore
	@JoinTable(
	        name = "previaturas",
	        joinColumns = @JoinColumn(
	                name = "id_asignatura_carrera",
	                referencedColumnName = "id",
	                foreignKey=@ForeignKey(name="previatura_asignatura_carrera_fkey")
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "id_asignatura_carrera_previa",
	                referencedColumnName = "id",
	                foreignKey=@ForeignKey(name="previatura_asignatura_carrera_previa_fkey")
	        ),
	        indexes = {@Index(name = "asignatura_carrera_previatura_index", columnList = "id_asignatura_carrera"), @Index(name = "asignatura_carrera_previa_previatura_index", columnList = "id_asignatura_carrera_previa")}
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

}
