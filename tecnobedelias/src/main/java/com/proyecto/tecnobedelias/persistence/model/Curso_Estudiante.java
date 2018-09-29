package com.proyecto.tecnobedelias.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Curso_Estudiante {

	
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
		
		private int nota;
		
		private String estado;
		
		private boolean activa;
		
		public boolean isActiva() {
			return activa;
		}

		public void setActiva(boolean activa) {
			this.activa = activa;
		}

		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
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

		@ManyToOne
		@JsonIgnore
		private Curso curso;
		
		@ManyToOne
		@JsonIgnore
		private Usuario estudiante;
		
	
	
	
	
	
	
	
}
