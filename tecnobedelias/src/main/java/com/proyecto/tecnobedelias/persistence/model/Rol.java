package com.proyecto.tecnobedelias.persistence.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="roles", uniqueConstraints = @UniqueConstraint(name="nombre_rol_ukey", columnNames={"nombre"}))
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;	
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="id")
    private long id;

	@Column(name="nombre", nullable=false)
    private String nombre;

	@Column(name="descripcion", nullable=true)
    private String descripcion;
	
	/*@Column(name="activa", nullable=true)
	private boolean activa; */
    
	@JsonIgnore
    @ManyToMany(mappedBy="roles")
    private List<Usuario> usuarios;
    
    public Rol() {    	
    }
    
    public Rol(String nombre, String descripcion) {
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}		
	    
}
