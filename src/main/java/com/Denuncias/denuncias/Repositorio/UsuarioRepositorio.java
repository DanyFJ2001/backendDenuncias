package com.Denuncias.denuncias.Repositorio;

import com.Denuncias.denuncias.Entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    Usuario findByUsername(String username);
}
