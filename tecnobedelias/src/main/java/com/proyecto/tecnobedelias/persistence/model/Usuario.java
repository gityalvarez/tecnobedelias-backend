package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="usuarios", 
	uniqueConstraints = { @UniqueConstraint(name = "cedula_usuario_ukey", columnNames= { "cedula" }), @UniqueConstraint(name = "email_usuario_ukey", columnNames= { "email" }), @UniqueConstraint(name = "username_usuario_ukey", columnNames= { "username" } ) },
	indexes = {@Index(name="nombre_usuario_index", columnList="nombre"), @Index(name="apellido_usuario_index", columnList="apellido")})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="cedula", nullable=false)
	private String cedula;
	
	@Column(name="nombre", nullable=false)	
	private String nombre;
	
	@Column(name="apellido", nullable=false)	
	private String apellido;
	
	@Column(name="fechaNacimiento", nullable=true)	
	private Date fechaNacimiento;
	
	@Column(name="email", nullable=false)	
	private String email;
	
	@Column(name="username", nullable=false)	
	private String username;
	
	@Column(name="password", nullable=false)	
	private String password;
	
	@Column(name="app_token", nullable=true)	
	private String app_token;
	
	@Column(name="resetToken", nullable=true)	
	private String resetToken;	
	
	@Column(name="activa", nullable=true)	
	private boolean activa;
	
	public Usuario() {		
		this.roles = new HashSet<Rol>();
	}
	
	public Usuario(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Usuario(String cedula, String nombre, String apellido, String email, String username, String password) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.username = username;
		this.password = password;
		this.activa = true;
		

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
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

	public String getApp_token() {
		return app_token;
	}

	public void setApp_token(String app_token) {
		this.app_token = app_token;
	}	
	
	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}	

	@JsonIgnore
	@JoinTable(
	        name = "rol_usuario",
	        joinColumns = @JoinColumn(
	                name = "id_usuario",
	                referencedColumnName = "id",
	                foreignKey = @ForeignKey(name = "rol_usuario_usuario_fkey")
	        ),
	        inverseJoinColumns = @JoinColumn(
	                name = "id_rol",
	                referencedColumnName = "id",
	                foreignKey = @ForeignKey(name = "rol_usuario_rol_fkey")
	        ),
	        indexes = {@Index(name = "rol_rol_usuario_index", columnList = "id_rol"), @Index(name = "usuario_rol_usuario_index", columnList = "id_usuario")}
	)	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Rol> roles;

	@JsonIgnore
	@OneToMany(mappedBy="estudiante",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Curso_Estudiante> cursoEstudiante;
	 
	
	@OneToMany(mappedBy="estudiante",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Estudiante_Examen> examenEstudiante;
	
	@ManyToMany(mappedBy="estudiantes")
	private List<Carrera> carreras;
	 
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

	public List<Estudiante_Examen> getExamenEstudiante() {
		return examenEstudiante;
	}

	public void setExamenEstudiante(List<Estudiante_Examen> examenEstudiante) {
		this.examenEstudiante = examenEstudiante;
	}	 
	 
	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}	


	/*public void setRol(Rol rol) {
		if (this.roles == null) {
			System.out.println("el set de roles es vacio");
			this.roles = new HashSet<Rol>();
			System.out.println("pude crear el Hashset");
			System.out.println("voy a agregar el rol "+rol.getNombre());
			this.roles.add(rol);		
			System.out.println("pude agregar el rol");
			
		}
	}
	*/
	
}
