package com.proyecto.tecnobedelias.controller;

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
import com.proyecto.tecnobedelias.service.UsuarioService;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
	
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
    
    @PostMapping("/crear/{rol}")
   // @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public boolean signUp(@RequestBody Usuario usuario,@PathVariable(value = "rol") String rol ) {
    	System.out.println("entre al crear con el usuario "+usuario.getUsername()+" y el rol "+rol);
    	if (usuarioService.existeUsuario(usuario.getUsername())) return false;
    	else {
    		
    		usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
    		usuarioService.altaUsuario(usuario);
    		Optional<Usuario>usuarioOpt = usuarioRepository.findByUsername(usuario.getUsername());
    		if (usuarioOpt.isPresent()) {
    			usuarioService.asignarRolUsuario(rol, usuarioOpt.get());
    			return true;
    		}
    	}return false;
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
    
    
}
