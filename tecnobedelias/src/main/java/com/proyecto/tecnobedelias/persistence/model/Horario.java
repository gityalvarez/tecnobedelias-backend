package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;



import javax.persistence.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="horarios")
public class Horario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="dia", nullable=false)
	private String dia;
	
	@Column(name="hora_inicio", nullable=false)	
	private String horaInicio;
	
	@Column(name="hora_fin", nullable=false)	
	private String horaFin;	
	
	@Column(name="activa", nullable=true)	
	private boolean activa;
	
	@JsonIgnore
	@ManyToMany(mappedBy="horarios", cascade={CascadeType.ALL})
    private List<Curso> cursos;
	
	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}
	
	public Horario() {		
	};	

	public Horario(String dia, String horaInicio, String horaFin) {
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.activa = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}	 

	/*@JsonIgnore
	@JoinTable(
	        name = "curso_horario",
	        joinColumns = @JoinColumn(
	                name = "id_horario",
	                referencedColumnName = "id",
	                foreignKey=@ForeignKey(name="curso_horario_horario_fkey")	                
	        ),
	        inverseJoinColumns = @JoinColumn(
	        		foreignKey=@ForeignKey(name="curso_horario_curso_fkey"),
	                name = "id_curso",
	                referencedColumnName = "id"
	        ),
	        indexes = {@Index(name = "horario_curso_horario_index", columnList = "id_horario"), @Index(name = "curso_curso_horario_index", columnList = "id_curso")}
	)	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Curso> cursos;	 
	
	public Set<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(Set<Curso> cursos) {
		this.cursos = cursos;
	}*/
	
}


