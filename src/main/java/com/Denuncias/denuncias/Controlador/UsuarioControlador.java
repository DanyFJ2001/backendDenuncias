package com.Denuncias.denuncias.Controlador;

import com.Denuncias.denuncias.Entidad.Usuario;
import com.Denuncias.denuncias.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {
    @Autowired
    UsuarioServicio usuarioServicio;
    @GetMapping("/test")
    public String test() {
        return "API funcionando correctamente";
    }

    @GetMapping("/mostrar")
    public List<Usuario> mostrar() {
        return usuarioServicio.mostrarUsuarios();
    }
    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> getUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioServicio.buscarUsuarioId(id);
        if(usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/guardarUsuario")
    public Usuario guardar(@RequestBody Usuario usuario) {
        return usuarioServicio.guardarUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(), // Añadí esta línea que faltaba
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRol()  // Añadí esta línea que faltaba
        );
    }

    @DeleteMapping("/eliminarUsuario/{id}")
    public ResponseEntity<Boolean> eliminar(@PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/actualizarUsuario/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuarioData) {
        Optional<Usuario> usuarioOptional = usuarioServicio.buscarUsuarioId(id);
        Usuario usuario = usuarioOptional.get();
        //actualizar
        usuario.setNombre(usuarioData.getNombre());
        usuario.setApellido(usuarioData.getApellido());
        usuario.setEmail(usuarioData.getEmail());
        usuario.setUsername(usuarioData.getUsername());
        usuario.setPassword(usuarioData.getPassword());

        Usuario usuarioInsertado = usuarioServicio.guardarUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRol()
        );

        return ResponseEntity.ok(usuarioInsertado);
    }
}