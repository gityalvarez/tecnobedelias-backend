package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="examenes", indexes = {@Index(name="asignatura_examen_index", columnList="id_asignatura")})
public class Examen implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="fecha", nullable=true)	
	private Date fecha;
	
	@Column(name="hora", nullable=true)	
	private String hora;
	
	@Column(name="nombreAsignatura", nullable=false)
	private String nombreAsignatura;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_asignatura", foreignKey = @ForeignKey(name="examen_asignatura_fkey"))
	private Asignatura asignatura;
	
	/*@Column(name="activa", nullable=true)	
	private boolean activa;*/
	
	public Examen() {
	}
	
	public Examen(Date fecha, String hora) {
		this.fecha = fecha;
		this.hora = hora;
		//this.activa = true;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public String getNombreAsignatura() {
		return nombreAsignatura;
	}

	public void setNombreAsignatura(String nombreAsignatura) {
		this.nombreAsignatura = nombreAsignatura;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@OneToMany(mappedBy="examen",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 private List<Estudiante_Examen> estudianteExamen;

	public List<Estudiante_Examen> getEstudianteExamen() {
		return estudianteExamen;
	}

	public void setEstudianteExamen(List<Estudiante_Examen> estudianteExamen) {
		this.estudianteExamen = estudianteExamen;
	}
	
}

