package com.proyecto.tecnobedelias.Util;

public class Response {
	
	
	
	public Response(boolean estado, String mensaje) {
		super();
		this.estado = estado;
		this.mensaje = mensaje;
	}

	private boolean estado;
	
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	private String mensaje;

}
