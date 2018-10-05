package com.proyecto.tecnobedelias.persistence.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String username;
	
	private String password;
	
	private String nombre;
	
	private String apellido;
	
	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getApp_token() {
		return app_token;
	}

	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}



	private String cedula;
	
	private Date fechaNacimiento;
	
	private String email;
	
	private String app_token;
	
	private String resetToken;
	
	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}



	@JsonIgnore
	@JoinTable(
	        name = "Usuario_Roles",
	        joinColumns = @JoinColumn(
	                name = "usuarioId",
	                referencedColumnName = "id"
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "roleId",
	                referencedColumnName = "id"
	        )
	)	
	@ManyToMany(fetch = FetchType.EAGER)
	    private Set<Role> roles;

	
	 @OneToMany(mappedBy="estudiante",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 private List<Curso_Estudiante> cursoEstudiante;
	 
	
	 @OneToMany(mappedBy="estudiante",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 private List<Examen_Estudiante> examenEstudiante;

	 @ManyToMany(mappedBy="estudiantes")
	 private List<Carrera> carreras;
	 
	 
		private boolean activa;
		
		public boolean isActiva() {
			return activa;
		}

		public void setActiva(boolean activa) {
			this.activa = activa;
		}

	 
	 
	 
	 public List<Carrera> getCarreras() {
		return carreras;
	}

	public void setCarreras(List<Carrera> carreras) {
		this.carreras = carreras;
	}

	public List<Curso_Estudiante> getCursoEstudiante() {
		return cursoEstudiante;
	}

	public void setCursoEstudiante(List<Curso_Estudiante> cursoEstudiante) {
		this.cursoEstudiante = cursoEstudiante;
	}

	public List<Examen_Estudiante> getExamenEstudiante() {
		return examenEstudiante;
	}

	public void setExamenEstudiante(List<Examen_Estudiante> examenEstudiante) {
		this.examenEstudiante = examenEstudiante;
	}

	 
	 
	 
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Usuario() {};
	
	public Usuario(String username, String password) {

		this.username = username;
		this.password = password;
	}
	
	public Usuario(String username, String password, String nombre, String apellido) {
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.apellido = apellido;		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
}
