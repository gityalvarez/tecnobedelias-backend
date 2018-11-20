package com.proyecto.tecnobedelias.controller;

import static com.proyecto.tecnobedelias.security.SecurityConstants.HEADER_STRING;
import static com.proyecto.tecnobedelias.security.SecurityConstants.SECRET;
import static com.proyecto.tecnobedelias.security.SecurityConstants.TOKEN_PREFIX;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.Util.Response;
import com.proyecto.tecnobedelias.Util.TokenUtil;
import com.proyecto.tecnobedelias.persistence.model.Actividad;
import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.service.EmailService;
import com.proyecto.tecnobedelias.service.UsuarioService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TokenUtil token;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UsuarioController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	public List<Usuario> obtenerUsuarios() {
		return usuarioService.listarUsuarios();
	} 
    
    @GetMapping("/estudiante")
    @PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
    public Optional<Usuario> obtenerEstudiante(HttpServletRequest request) {
    	String token = request.getHeader(HEADER_STRING);
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody();

		String username = claims.getSubject();
		System.out.println(("obtuve al estudiante "+username));
		return usuarioService.findUsuarioByUsername(username);
		
    }
    
    @PostMapping("/crear/{rol}")
   // @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Response signUp(@RequestBody Usuario usuario,@PathVariable(value = "rol") String rol ) {
    	System.out.println("entre al crear con el usuario "+usuario.getUsername()+" y el rol "+rol);
    	if (!usuarioService.existeUsuario(usuario.getUsername())) {
    		if (!usuarioService.existeCedula(usuario.getCedula())) {
    			if (!usuarioService.existeEmail(usuario.getEmail())) {  
    				usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
    	    		usuarioService.altaUsuario(usuario);
    	    		if (usuarioService.existeUsuario(usuario.getUsername())) {
    	    			Usuario usuarioExistente = usuarioService.findUsuarioByUsername(usuario.getUsername()).get();
    	    		 	usuarioService.asignarRolUsuario(rol, usuarioExistente);
    	    			return new Response(true, "Se creo el usuario "+usuario.getUsername()+" con exito");
    	    		}
    	    		else return new Response(false,"no se pudo crear el usuario");
    			}
    			else return new Response(false,"No se pudo crear el usuario, ya existe un usuario con ese email");
    		}
    		else return new Response(false,"No se pudo crear el usuario, ya existe un usuario con esa cedula");
    	}
    	else return new Response(false,"No se pudo crear el usuario, ya existe un usuario con ese username");
    }
    
    @PostMapping("/crearbien/{rol}")
    // @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
     public Response crear(@RequestBody Usuario usuario,@PathVariable(value = "rol") String rol ) throws ParseException {
     	System.out.println("entre al crear con el usuario "+usuario.getUsername()+" y el rol "+rol);
     	if (!usuarioService.existeUsuario(usuario.getUsername())) {
    		if (!usuarioService.existeCedula(usuario.getCedula())) {
    			if (!usuarioService.existeEmail(usuario.getEmail())) { 
     		 		usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
     		 		if (usuario.getFechaNacimiento() != null) {
     		 			Calendar calendar = Calendar.getInstance();     		 		
     		 			calendar.setTime(usuario.getFechaNacimiento());
     		 			calendar.add(Calendar.DAY_OF_YEAR, 1);
     		 			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
     		 			String fechaNacimientoString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
     		 			Date fechaNacimiento = formateadorfecha.parse(fechaNacimientoString);
     		 			usuario.setFechaNacimiento(fechaNacimiento);
     		 		}
     		 		usuarioService.altaBienUsuario(usuario);
     		 		if (usuarioService.existeUsuario(usuario.getUsername())) {
     		 			Usuario usuarioExistente = usuarioService.findUsuarioByUsername(usuario.getUsername()).get();
     		 			usuarioService.asignarRolUsuario(rol, usuarioExistente);
     		 			System.out.println("asigné el rol");
     		 			if (rol.equals("ESTUDIANTE")) {
     		 				System.out.println("voy a mandar el mail");
     		 				emailService.sendEmailToken(usuarioExistente.getResetToken(),usuarioExistente.getEmail(),usuarioExistente.getUsername());
     		 			}
     		 			return new Response(true,"Se creo el usuario "+usuario.getUsername()+" con exito");
     		 		}
     		 		else return new Response(false,"No se pudo crear el usuario");
    			}
    			else return new Response(false,"No se pudo crear el usuario, ya existe un usuario con ese email");
     	   	}
    		else return new Response(false,"No se pudo crear el usuario, ya existe un usuario con esa cedula");
     	}
     	else return new Response(false,"No se pudo crear el usuario, ya existe un usuario con ese username");
     }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Optional<Usuario> getOne(@PathVariable(value = "id") long id){
        return usuarioService.obtenerUsuario(id);
    }
    
    @PostMapping("/rol")
    //@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public void asignarRol(HttpServletRequest request,
			@RequestParam(name = "usuarioId", required = true) Long usuarioId,
			@RequestParam(name = "rol", required = true) String rol) {
    	Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuario(usuarioId);
    	System.out.println("el rol es "+rol);
    	Usuario usuario = usuarioOpt.get();
    	System.out.println("el usuario es "+usuario.getUsername());
    	System.out.println("entro al usuario service");
    	usuarioService.asignarRolUsuario(rol, usuario);
    }
    
    @PostMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Response borrarUsuario(@RequestBody Usuario usuario) {
    	if (usuarioService.existeUsuario(usuario.getUsername())) {
    		usuarioService.bajaUsuario(usuario);
    		return new Response(true, "El usuario fue borrado con exito");
    	}else return new Response(false, "No se pudo borrar al usuario");
    }
    
    @GetMapping("/reset")
    public void cambiarPassword(HttpServletRequest request,
    		@RequestParam(name = "resetToken", required = true) String token,
			@RequestParam(name = "password", required = true) String password) {
    	Optional<Usuario> usuarioOpt= usuarioService.findUsuarioByResetToken(token);
    	if (usuarioOpt.isPresent()) {
    		Usuario usuario = usuarioOpt.get();
    		usuario.setPassword(bCryptPasswordEncoder.encode(password));
    		usuario.setResetToken(null);
    		usuarioService.modificacionUsuario(usuario);
    	}
    }    
    
    @GetMapping("/cambiarpassword")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_FUNCIONARIO') or hasRole('ROLE_ESTUDIANTE') or hasRole('ROLE_DIRECTOR')")
    public Response modificarPassword(HttpServletRequest request,
    		@RequestParam(name = "actualpassword", required = true) String actualpassword,
    		@RequestParam(name = "newpassword", required = true) String newpassword,
    		@RequestParam(name = "newpasswordrepeat", required = true) String newpasswordrepeat) {
    	String username = token.getUsername(request);
		Optional<Usuario> usuarioOpt = usuarioService.findUsuarioByUsername(username); 
    	if (usuarioOpt.isPresent()) {
    		Usuario usuario = usuarioOpt.get();    		
    		if (bCryptPasswordEncoder.matches(actualpassword, usuario.getPassword())) {
    			if (!bCryptPasswordEncoder.matches(newpassword, usuario.getPassword())) {
    				if (newpassword.equals(newpasswordrepeat)) {
    					usuario.setPassword(bCryptPasswordEncoder.encode(newpassword));
    					usuarioService.modificacionUsuario(usuario);
    					return new Response(true, "La password ha sido cambiada con exito");
    				}
    				else return new Response(false, "La nueva password ingresada no coincide con su confirmacion");
    			}
    			else return new Response(false, "La nueva password ingresada coincide con la password actual");
    		}
    		else return new Response(false, "La password actual ingresada no es correcta");
    	}
    	else return new Response(false, "El usuario no existe");
    }
    
    
    
    @GetMapping("/inicializar")
    public void inicializar(HttpServletRequest request,
    		@RequestParam(name = "clave", required = true) String clave) {
    	if (clave.equals("magico")) {
    		usuarioService.inicializar();
    	}
    }
    
    @PostMapping("/modificar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Response modificarUsuario(HttpServletRequest request, 
    		@RequestBody(required = true) Usuario usuario, 
    		@RequestParam(name = "usuarioId", required = true) String usuarioIdStr) throws ParseException {
    	long usuarioId = Long.parseLong(usuarioIdStr);
    	Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
    	if (usuarioExistente.isPresent()) {
    		if (usuarioService.existeCedula(usuario.getCedula())) {
    			usuarioExistente = usuarioService.obtenerUsuarioCedula(usuario.getCedula());
    			if (usuarioExistente.get().getId() == usuarioId) {
    				if (usuarioService.existeEmail(usuario.getEmail())) {
    					usuarioExistente = usuarioService.obtenerUsuarioEmail(usuario.getEmail());
    					if (usuarioExistente.get().getId() == usuarioId) {
    						usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
    						usuarioExistente.get().setApellido(usuario.getApellido());
    						usuarioExistente.get().setNombre(usuario.getNombre());
    						if (usuario.getFechaNacimiento() != null) {
    	     		 			Calendar calendar = Calendar.getInstance();     		 		
    	     		 			calendar.setTime(usuario.getFechaNacimiento());
    	     		 			calendar.add(Calendar.DAY_OF_YEAR, 1);
    	     		 			SimpleDateFormat formateadorfecha = new SimpleDateFormat("yyyy-MM-dd"); 
    	     		 			String fechaNacimientoString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    	     		 			Date fechaNacimiento = formateadorfecha.parse(fechaNacimientoString);
    	     		 			usuario.setFechaNacimiento(fechaNacimiento);
    	     		 		}
    						usuarioExistente.get().setFechaNacimiento(usuario.getFechaNacimiento());
    						usuarioExistente.get().setFoto(usuario.getFoto());
    						usuarioService.modificacionUsuario(usuarioExistente.get());
    						return new Response(true,"El usuario se modifico con exito");
    					}
    					else return new Response(false,"No se pudo modificar el usuario, ya existe un usuario con el email ingresado");
    				}
    				else {
    					usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
    					usuarioExistente.get().setApellido(usuario.getApellido());
						usuarioExistente.get().setNombre(usuario.getNombre());
						usuarioExistente.get().setFechaNacimiento(usuario.getFechaNacimiento());
						usuarioExistente.get().setEmail(usuario.getEmail());
						usuarioExistente.get().setFoto(usuario.getFoto());
						usuarioService.modificacionUsuario(usuarioExistente.get());
						return new Response(true,"El usuario se modifico con exito");
    				}
    			}
    			else return new Response(false,"No se pudo modificar el usuario, ya existe un usuario con la cedula ingresada");
    		}
    		else {
    			if (usuarioService.existeEmail(usuario.getEmail())) {
    				usuarioExistente = usuarioService.obtenerUsuarioEmail(usuario.getEmail());
    				if (usuarioExistente.get().getId() == usuarioId) {
    					usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
    					usuarioExistente.get().setApellido(usuario.getApellido());
						usuarioExistente.get().setNombre(usuario.getNombre());
						usuarioExistente.get().setFechaNacimiento(usuario.getFechaNacimiento());
						usuarioExistente.get().setCedula(usuario.getCedula());
						usuarioExistente.get().setFoto(usuario.getFoto());
    					usuarioService.modificacionUsuario(usuarioExistente.get());
    					return new Response(true,"El usuario se modifico con exito");
    				}
    				else return new Response(false,"No se pudo modificar el usuario, ya existe un usuario con el email ingresado");
    			}
    			else {
    				usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
    				usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
					usuarioExistente.get().setApellido(usuario.getApellido());
					usuarioExistente.get().setNombre(usuario.getNombre());
					usuarioExistente.get().setFechaNacimiento(usuario.getFechaNacimiento());
					usuarioExistente.get().setCedula(usuario.getCedula());
					usuarioExistente.get().setEmail(usuario.getEmail());
					usuarioExistente.get().setFoto(usuario.getFoto());
    				usuarioService.modificacionUsuario(usuarioExistente.get());
    				return new Response(true,"El usuario se modifico con exito");
    			}
    		}
    	}
    	else return new Response(false,"No se pudo modificar el usuario, el usuario no existe");
    }
    
    @GetMapping("/escolaridad")
    @PreAuthorize("hasRole('ROLE_FUNCIONARIO')")
    public List<Actividad> escolaridad(HttpServletRequest request,
    		@RequestParam(name = "cedula", required = true) String cedula) {
    	Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioCedula(cedula);
    	if(usuarioOpt.isPresent()) {
    		return usuarioService.escolaridad(usuarioOpt.get());
    	}else return null;    	

    }
    
    
    
    
}
