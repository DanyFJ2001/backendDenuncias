package com.Denuncias.denuncias.Repositorio;

import com.Denuncias.denuncias.Entidad.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepositorio extends JpaRepository<Admin, Long> {
    List<Admin> findByNombreContainingIgnoreCase(String nombre);
    Admin findByUsername(String username);
}
