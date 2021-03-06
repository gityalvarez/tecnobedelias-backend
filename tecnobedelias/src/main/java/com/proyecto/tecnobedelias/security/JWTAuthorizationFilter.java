package com.proyecto.tecnobedelias.security;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static com.proyecto.tecnobedelias.security.SecurityConstants.HEADER_STRING;
import static com.proyecto.tecnobedelias.security.SecurityConstants.SECRET;
import static com.proyecto.tecnobedelias.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("entre al filtro Authorization");
		
		String header = req.getHeader(HEADER_STRING);
		System.out.println(header);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			System.out.println("header null o no empieza con Bearer");
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			System.out.println("entre al getAuthentication con el token "+token);
			Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody();

			String user = claims.getSubject();
			if (user != null) {

				Set<SimpleGrantedAuthority> authorities = new HashSet<>();

				ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");

				roles.forEach(r -> {
					authorities.add(new SimpleGrantedAuthority(r.get("authority")));
				});
				System.out.println("esta es la authority en el filter: " + authorities.iterator().next().getAuthority());
				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}
			return null;
		}
		return null;
	}
}