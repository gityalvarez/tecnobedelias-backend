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
	private String estado;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_examen", foreignKey = @ForeignKey(name = "estudiante_examen_examen_fkey"))
	private Examen examen;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "estudiante_examen_usuario_fkey"))
	private Usuario estudiante;
	
	/*@Column(name="activa", nullable=true)
	private boolean activa;*/
	
	public Estudiante_Examen() {
	}
		
	public Estudiante_Examen(int nota, String estado) {
		this.nota = nota;
		this.estado = estado;
		//this.activa = true;
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
	
	/*public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}*/

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

