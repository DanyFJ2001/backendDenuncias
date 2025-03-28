package com.Denuncias.denuncias.Controlador;

import com.Denuncias.denuncias.Entidad.Usuario;
import com.Denuncias.denuncias.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthControlador {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("email");
            String password = loginRequest.get("password");

            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Buscar usuario para obtener su rol
            Usuario usuario = usuarioServicio.buscarPorUsername(username);

            // Generar token
            String token = UUID.randomUUID().toString();

            // Crear respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", token);
            response.put("rol", usuario.getRol().toString());
            response.put("username", usuario.getUsername());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Error: Usuario o contraseña incorrectos");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}