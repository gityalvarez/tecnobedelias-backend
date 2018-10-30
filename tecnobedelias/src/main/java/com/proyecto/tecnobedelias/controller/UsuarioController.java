package com.proyecto.tecnobedelias.controller;

import static com.proyecto.tecnobedelias.security.SecurityConstants.HEADER_STRING;
import static com.proyecto.tecnobedelias.security.SecurityConstants.SECRET;
import static com.proyecto.tecnobedelias.security.SecurityConstants.TOKEN_PREFIX;

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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.repository.RolRepository;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;
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
	
	
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RolRepository rolRepository;
    public UsuarioController(UsuarioRepository usuarioRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.rolRepository = rolRepository;
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
		return usuarioRepository.findByUsername(username);
		
    }
    
    @PostMapping("/crear/{rol}")
   // @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public boolean signUp(@RequestBody Usuario usuario,@PathVariable(value = "rol") String rol ) {
    	System.out.println("entre al crear con el usuario "+usuario.getUsername()+" y el rol "+rol);
    	if (!usuarioService.existeUsuario(usuario.getUsername())) {
    		if (!usuarioService.existeCedula(usuario.getCedula())) {
    			if (!usuarioService.existeEmail(usuario.getEmail())) {  
    				usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
    	    		usuarioService.altaUsuario(usuario);
    	    		if (usuarioService.existeUsuario(usuario.getUsername())) {
    	    			Usuario usuarioExistente = usuarioService.findUsuarioByUsername(usuario.getUsername()).get();
    	    		 	usuarioService.asignarRolUsuario(rol, usuarioExistente);
    	    			return true;
    	    		}
    	    		else return false;
    			}
    			else return false;
    		}
    		else return false;
    	}
    	else return false;
    }
    
    @PostMapping("/crearbien/{rol}")
    // @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
     public boolean crear(@RequestBody Usuario usuario,@PathVariable(value = "rol") String rol ) {
     	System.out.println("entre al crear con el usuario "+usuario.getUsername()+" y el rol "+rol);
     	if (!usuarioService.existeUsuario(usuario.getUsername())) {
    		if (!usuarioService.existeCedula(usuario.getCedula())) {
    			if (!usuarioService.existeEmail(usuario.getEmail())) { 
     		 		usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
     		 		usuarioService.altaBienUsuario(usuario);
     		 		if (usuarioService.existeUsuario(usuario.getUsername())) {
     		 			Usuario usuarioExistente = usuarioService.findUsuarioByUsername(usuario.getUsername()).get();
     		 			usuarioService.asignarRolUsuario(rol, usuarioExistente);
     		 			System.out.println("asigné el rol");
     		 			if (rol.equals("ESTUDIANTE")) {
     		 				System.out.println("voy a mandar el mail");
     		 				emailService.sendEmailToken(usuarioExistente.getResetToken());
     		 			}
     		 			return true;
     		 		}
     		 		else return false;
    			}
    			else return false;
     	   	}
    		else return false;
     	}
     	else return false;
     }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Optional<Usuario> getOne(@PathVariable(value = "id") Long id){
        return usuarioRepository.findById(id);
    }
    
    @PostMapping("/rol")
    //@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public void asignarRol(HttpServletRequest request,
			@RequestParam(name = "usuarioId", required = true) Long usuarioId,
			@RequestParam(name = "rol", required = true) String rol) {
    	Optional<Usuario> usuarioOpt =usuarioRepository.findById(usuarioId);
    	System.out.println("el rol es "+rol);
    	Usuario usuario = usuarioOpt.get();
    	System.out.println("el usuario es "+usuario.getUsername());
    	System.out.println("entro al usuario service");
    	//Optional<Role> roleOpt = roleRepository.findById(rolId);
    	//Role role = roleOpt.get();
    	usuarioService.asignarRolUsuario(rol, usuario);
    	//usuario.getRoles().add(role);
    	//usuarioRepository.save(usuario);
    }
    
    @PostMapping("/borrar")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public void borrarUsuario(@RequestBody Usuario usuario) {
    	System.out.println("entre al borrar usuario con el usuario "+usuario.getUsername()+" y el apellido "+usuario.getApellido());
    	usuarioService.bajaUsuario(usuario);
    }
    
    @GetMapping("/reset")
    public void cambiarPassword(HttpServletRequest request,
    		@RequestParam(name = "resetToken", required = true) String token,
			@RequestParam(name = "password", required = true) String password) {
    	Optional<Usuario> usuarioOpt= usuarioService.findUsuarioByResetToken(token);
    	if (usuarioOpt.isPresent()) {
    		Usuario usuario = usuarioOpt.get();
    		usuario.setPassword(bCryptPasswordEncoder.encode(password));
    		usuarioRepository.save(usuario);
    	}
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
    public boolean modificarUsuario(HttpServletRequest request, 
    		@RequestBody(required = true) Usuario usuario, 
    		@RequestParam(name = "usuarioId", required = true) String usuarioIdStr) {
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
    						usuarioExistente.get().setFechaNacimiento(usuario.getFechaNacimiento());
    						usuarioExistente.get().setFoto(usuario.getFoto());
    						usuarioService.modificacionUsuario(usuarioExistente.get());
    						return true;
    					}
    					else return false;
    				}
    				else {
    					usuarioExistente = usuarioService.obtenerUsuario(usuarioId);
    					usuarioExistente.get().setApellido(usuario.getApellido());
						usuarioExistente.get().setNombre(usuario.getNombre());
						usuarioExistente.get().setFechaNacimiento(usuario.getFechaNacimiento());
						usuarioExistente.get().setEmail(usuario.getEmail());
						usuarioExistente.get().setFoto(usuario.getFoto());
						usuarioService.modificacionUsuario(usuarioExistente.get());
						return true;
    				}
    			}
    	    	else return false;
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
    					return true;
    				}
    				else return false;
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
    				return true;
    			}
    		}
    	}
    	else return false;
    }
    
    
}
