package com.proyecto.tecnobedelias.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hola2")
public class Hola2Controller {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ESTUDIANTE')")
    public String dimeHola(){
        String message = "Hola Amigo, vos de nuevo?";
        return message;
    }
}
