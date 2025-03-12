package com.Denuncias.denuncias.Servicio;

import com.Denuncias.denuncias.Entidad.Usuario;
import com.Denuncias.denuncias.Repositorio.UsuarioRepositorio;
import com.Denuncias.denuncias.Roles.Rol;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder; // Para encriptar la contraseña si es necesario

    // Mostrar todos los usuarios
    public List<Usuario> mostrarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    // Buscar usuarios por nombre
    public List<Usuario> buscarUsuarioNombre(String buscarUsuario) {
        if (buscarUsuario == null || buscarUsuario.isEmpty()) {
            return usuarioRepositorio.findAll();
        } else {
            return usuarioRepositorio.findByNombreContainingIgnoreCase(buscarUsuario);
        }
    }

    // Guardar o actualizar usuario
    public Usuario guardarUsuario(Long id, String nombre, String apellido, String email, String username, String password, Rol rol) {
        Usuario usuario;

        if (id != null) {
            usuario = usuarioRepositorio.findById(id).orElse(new Usuario());
        } else {
            usuario = new Usuario();
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setUsername(username);

        if (password != null && !password.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(password)); // Encriptamos la contraseña
        }

        // Aquí podríamos agregar el rol si es necesario
        usuario.setRol(rol);

        usuarioRepositorio.save(usuario);

        return usuario;
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    // Buscar usuario por ID
    public Optional<Usuario> buscarUsuarioId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    // Método para buscar usuario por username
    public Usuario buscarPorUsername(String username) {
        return usuarioRepositorio.findByUsername(username);
    }

    @PostConstruct
    public void initAdmin() {
        crearAdmin("Administrador","root" ,"admin@email.com","admin", "admin123");
    }

    @Transactional
    public void crearAdmin(String nombre, String apellido, String email, String username, String password) {
        if (usuarioRepositorio.findByUsername(username) == null) {
            System.out.println("Creando administrador...");
            Usuario admin = new Usuario();
            admin.setNombre(nombre);
            admin.setApellido(apellido);
            admin.setEmail(email);
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRol(Rol.ADMIN);
            usuarioRepositorio.save(admin);
        } else {
            System.out.println("El administrador ya existe.");
        }
    }
}