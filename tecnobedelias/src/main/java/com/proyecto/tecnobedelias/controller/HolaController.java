package com.proyecto.tecnobedelias.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/hola")
public class HolaController {

	
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String dimeHola(){
        String message = "Hola Amigo!";
        return message;
    }
}
