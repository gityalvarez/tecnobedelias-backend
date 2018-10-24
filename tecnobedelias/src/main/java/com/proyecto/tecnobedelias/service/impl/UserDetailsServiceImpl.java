package com.proyecto.tecnobedelias.service.impl;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.tecnobedelias.persistence.model.Usuario;
import com.proyecto.tecnobedelias.persistence.model.Rol;
import com.proyecto.tecnobedelias.persistence.repository.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UsuarioRepository usuarioRepository;

	public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("entre al loadUser del userDetails");
		Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
		if (!usuarioOpt.isPresent()) {
			throw new UsernameNotFoundException(username);
		}
		else {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			Usuario usuario = usuarioOpt.get();
			for (Rol role : usuario.getRoles()) {
				grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role.getNombre()));
				System.out.println("estos son los roles: "+role.getNombre());
				System.out.println("estos son los roles por el repository: "+usuario.getRoles());
			}
			System.out.println(grantedAuthorities.iterator().next().getAuthority());
			return new User(usuario.getUsername(), usuario.getPassword(), grantedAuthorities);
		}
	}

}
