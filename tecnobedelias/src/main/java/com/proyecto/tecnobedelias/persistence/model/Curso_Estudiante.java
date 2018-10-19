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
@Table(name="curso_estudiante", indexes = {@Index(name="curso_curso_estudiante_index", columnList="id_curso"), @Index(name="usuario_curso_estudiante_index", columnList="id_usuario")})
public class Curso_Estudiante implements Serializable {
		private static final long serialVersionUID = 1L;	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name="id")
		private long id;
		
		@Column(name="nota", nullable=true)
		private int nota;
		
		@Column(name="estado", nullable=true)
		/*
		MATRICULADO - si el estudiante ya esta matriculado en el curso 
		SALVADO - si el estudiante ya tiene salvada la asignatura correspondiente al curso 
		EXAMEN - si el estudiante ya tiene aprobado el curso
		RECURSA - si el estudiante perdio el curso y debe recursar
		 */
		private String estado;
		
		/*@Column(name="activa", nullable=true)
		private boolean activa;*/
		
		@ManyToOne
		@JsonIgnore
		@JoinColumn(name = "id_curso", foreignKey = @ForeignKey(name = "curso_estudiante_curso_fkey"))
		private Curso curso;
		
		@ManyToOne
		@JsonIgnore
		@JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "curso_estudiante_usuario_fkey"))
		private Usuario estudiante;
		
		public Curso_Estudiante() {
		}		
		
		public Curso_Estudiante(int nota, String estado) {
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

		public Curso getCurso() {
			return curso;
		}

		public void setCurso(Curso curso) {
			this.curso = curso;
		}

		public Usuario getEstudiante() {
			return estudiante;
		}

		public void setEstudiante(Usuario estudiante) {
			this.estudiante = estudiante;
		}		
}

