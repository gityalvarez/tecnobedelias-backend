package com.proyecto.tecnobedelias.Util;

import static com.proyecto.tecnobedelias.security.SecurityConstants.HEADER_STRING;
import static com.proyecto.tecnobedelias.security.SecurityConstants.SECRET;
import static com.proyecto.tecnobedelias.security.SecurityConstants.TOKEN_PREFIX;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class TokenUtil {
	
	public String getUsername(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody();

		String username = claims.getSubject();
		return username;
	}

}
