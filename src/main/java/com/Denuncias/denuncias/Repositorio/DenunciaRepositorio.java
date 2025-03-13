package com.Denuncias.denuncias.Repositorio;

import com.Denuncias.denuncias.Entidad.Denuncia;
import com.Denuncias.denuncias.Entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DenunciaRepositorio extends JpaRepository<Denuncia, Long> {
    // Buscar por tipo de denuncia
    List<Denuncia> findByTipoContainingIgnoreCase(String tipo);

    // Buscar por ubicación
    List<Denuncia> findByUbicacionContainingIgnoreCase(String ubicacion);

    // Buscar por descripción
    List<Denuncia> findByDescripcionContainingIgnoreCase(String descripcion);

    // Buscar todas las denuncias de un usuario específico
    List<Denuncia> findByUsuario(Usuario usuario);

    // Buscar por usuario ID
    List<Denuncia> findByUsuarioId(Long usuarioId);

    // Buscar por estado
    List<Denuncia> findByEstado(Denuncia.EstadoDenuncia estado);
}