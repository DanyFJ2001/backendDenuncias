package com.Denuncias.denuncias.Controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControlador {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}