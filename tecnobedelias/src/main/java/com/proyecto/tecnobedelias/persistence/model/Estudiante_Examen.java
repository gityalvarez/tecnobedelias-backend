package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="estudiante_examen", indexes = {@Index(name="examen_estudiante_examen_index", columnList="id_examen"), @Index(name="usuario_estudiante_examen_index", columnList="id_usuario")})
public class Estudiante_Examen implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="nota", nullable=true)
	private int nota;
	
	@Column(name="estado", nullable=true)
	/*
	ANOTADO - si el estudiante ya esta anotado para rendir el examen 
	APROBADO - si el estudiante ya tiene salvada la asignatura correspondiente al examen 
	REPROBADO - si el estudiante perdio el examen	
	 */
	private String estado;
	
	@Column(name="nombre", nullable=true)
	private String nombre;
	
	@Column(name="apellido", nullable=true)
	private String apellido;
	
	@Column(name="cedula", nullable=true)
	private String cedula;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_examen", foreignKey = @ForeignKey(name = "estudiante_examen_examen_fkey"))
	private Examen examen;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "estudiante_examen_usuario_fkey"))
	private Usuario estudiante;
	
	public Estudiante_Examen() {
	}
		
	public Estudiante_Examen(int nota, String estado) {
		this.nota = nota;
		this.estado = estado;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}	

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

	public Usuario getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Usuario estudiante) {
		this.estudiante = estudiante;
	}	
	
}

