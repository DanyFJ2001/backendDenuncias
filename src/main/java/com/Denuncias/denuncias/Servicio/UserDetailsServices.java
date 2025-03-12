package com.Denuncias.denuncias.Servicio;

import com.Denuncias.denuncias.Entidad.Usuario;
import com.Denuncias.denuncias.Repositorio.UsuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByUsername(username);
        if (usuario != null) {
            return new User(usuario.getUsername(), usuario.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString())));
        }

        throw new UsernameNotFoundException("Usuario no encontrado: " + username);

    }
}

