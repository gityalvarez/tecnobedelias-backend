package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="carreras", uniqueConstraints = @UniqueConstraint(name="nombre_carrera_ukey", columnNames={"nombre"}))
public class Carrera implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="nombre", /*unique=true,*/ nullable=false)
	private String nombre;
	
	@Column(name="descripcion", nullable=true)
	private String descripcion;
	
	@OneToMany(mappedBy="carrera", fetch=FetchType.LAZY, cascade=CascadeType.ALL)	
	private List<Asignatura_Carrera> asignaturaCarrera;
	
	@JoinTable(
	        name = "carrera_estudiante",
	        joinColumns = @JoinColumn(
	                name = "id_carrera",
	                referencedColumnName = "id",
	                foreignKey = @ForeignKey(name = "carrera_estudiante_carrera_fkey")
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "id_usuario",
	                referencedColumnName = "id",
	                foreignKey = @ForeignKey(name = "carrera_estudiante_usuario_fkey")
	        ),
	        indexes = {@Index(name = "carrera_carrera_estudiante_index", columnList = "id_carrera"), @Index(name = "usuario_carrera_estudiante_index", columnList = "id_usuario")}
	)	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Usuario> estudiantes;
	
	@Column(name="creditosMinimos", nullable=true)
	private int creditosMinimos;

	/*@Column(name="activa", nullable=true)
	private boolean activa;*/
	
	public Carrera() {
	}	
	
	public Carrera(String nombre, String descripcion, int creditosMinimos) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.creditosMinimos = creditosMinimos;
		//this.activa = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getCreditosMinimos() {
		return creditosMinimos;
	}

	public void setCreditosMinimos(int creditosMinimos) {
		this.creditosMinimos = creditosMinimos;
	}	
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/*public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}*/

	public List<Usuario> getEstudiantes() {
		return estudiantes;
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

}

