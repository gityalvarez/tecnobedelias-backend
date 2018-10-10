package com.proyecto.tecnobedelias.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.proyecto.tecnobedelias.persistence.model.Usuario;

import static com.proyecto.tecnobedelias.security.SecurityConstants.EXPIRATION_TIME;
import static com.proyecto.tecnobedelias.security.SecurityConstants.HEADER_STRING;
import static com.proyecto.tecnobedelias.security.SecurityConstants.SECRET;
import static com.proyecto.tecnobedelias.security.SecurityConstants.TOKEN_PREFIX;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        
    	System.out.println("entre al attemptAuthentication");
    	try {
            Usuario creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Usuario.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	System.out.println("entre al succesfulAuthentication");
    	
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles",((User) auth.getPrincipal()).getAuthorities())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        
        System.out.println("devuelvo el string : "+TOKEN_PREFIX + token);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        
        
        
        //res.addHeader("Access-Control-Allow-Origin", "*");
       
    }
}
