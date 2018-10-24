package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="asignaturas", uniqueConstraints=@UniqueConstraint(name="codigo_asignatura_ukey", columnNames={"codigo"}), indexes = {@Index(name="nombre_asignatura_index", columnList="nombre")})
public class Asignatura implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;

	@Column(name="codigo", nullable=false)
	private String codigo;
	
	@Column(name="nombre", nullable=false)
	private String nombre;
	
	@Column(name="descripcion", nullable=true)
	private String descripcion;
	
	@Column(name="taller", nullable=true)
	private boolean taller;
	
	//@JsonIgnore
	@OneToMany(mappedBy="asignatura", fetch=FetchType.LAZY, cascade=CascadeType.ALL)	
	private List<Asignatura_Carrera> asignaturaCarrera;	
	
	/*@JoinTable(
	        name = "Asignatura_Curso",
	        joinColumns = @JoinColumn(
	                name = "asignaturaId",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "cursoId",
	                referencedColumnName = "id"
	        )
	)*/
	
	//@JsonIgnore
	@OneToMany(mappedBy="asignatura", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Curso> cursos;	
	
	/*@JoinTable(
	        name = "Asignatura_Examen",
	        joinColumns = @JoinColumn(
	                name = "idAsignatura",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "idExamen",
	                referencedColumnName = "id"
	        )
	)*/	
	@JsonIgnore
	@OneToMany(mappedBy="asignatura", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Examen> examenes;
	
	/*@Column(name="activa", nullable=true)
	private boolean activa;*/
	
	public Asignatura() {
	}
	
	public Asignatura(String codigo, String nombre, String descripcion) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		//this.activa = true;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

	/*public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}*/	
	
	public boolean isTaller() {
		return taller;
	}

	public void setTaller(boolean taller) {
		this.taller = taller;
	}

	public List<Examen> getExamenes() {
		return examenes;
	}

	public void setExamenes(List<Examen> examenes) {
		this.examenes = examenes;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}
	
	public List<Asignatura_Carrera> getAsignaturaCarrera() {
		return asignaturaCarrera;
	}

	public void setAsignaturaCarrera(List<Asignatura_Carrera> asignaturaCarrera) {
		this.asignaturaCarrera = asignaturaCarrera;
	}	

}

