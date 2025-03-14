package com.Denuncias.denuncias.Controlador;

import com.Denuncias.denuncias.Entidad.Denuncia;
import com.Denuncias.denuncias.Entidad.Usuario;
import com.Denuncias.denuncias.Servicio.DenunciaServicio;
import com.Denuncias.denuncias.Servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/denuncias")
@CrossOrigin(origins = "*")
public class DenunciasControlador {

    @Autowired
    private DenunciaServicio denunciaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/test")
    public String test() {
        return "API de denuncias funcionando correctamente";
    }

    @GetMapping("/mostrar")
    public ResponseEntity<?> mostrarDenuncias() {
        try {
            List<Denuncia> denuncias = denunciaServicio.mostrarDenuncias();
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener denuncias: " + e.getMessage());
        }
    }

    @GetMapping("/mostrarSimple")
    public ResponseEntity<?> mostrarDenunciasSimple() {
        try {
            List<Denuncia> denuncias = denunciaServicio.mostrarDenuncias();
            List<Map<String, Object>> denunciasSimples = new ArrayList<>();

            for (Denuncia d : denuncias) {
                Map<String, Object> denunciaMap = new HashMap<>();
                denunciaMap.put("id", d.getId());
                denunciaMap.put("tipo", d.getTipo());
                denunciaMap.put("descripcion", d.getDescripcion());
                denunciaMap.put("ubicacion", d.getUbicacion());
                denunciaMap.put("contacto", d.getContacto());
                denunciaMap.put("estado", d.getEstado().toString());
                denunciaMap.put("fechaCreacion", d.getFechaCreacion().toString());

                if (d.getFechaActualizacion() != null) {
                    denunciaMap.put("fechaActualizacion", d.getFechaActualizacion().toString());
                }

                if (d.getUsuario() != null) {
                    Map<String, Object> usuarioMap = new HashMap<>();
                    usuarioMap.put("id", d.getUsuario().getId());
                    usuarioMap.put("nombre", d.getUsuario().getNombre());
                    denunciaMap.put("usuario", usuarioMap);
                }

                denunciasSimples.add(denunciaMap);
            }

            return ResponseEntity.ok(denunciasSimples);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener denuncias: " + e.getMessage());
        }
    }

    @GetMapping("/buscar/{tipo}")
    public ResponseEntity<?> buscarPorTipo(@PathVariable String tipo) {
        try {
            List<Denuncia> denuncias = denunciaServicio.buscarDenunciaPorTipo(tipo);
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar denuncias por tipo: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Denuncia> denuncias = denunciaServicio.buscarDenunciasPorUsuarioId(usuarioId);
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar denuncias por usuario: " + e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable Denuncia.EstadoDenuncia estado) {
        try {
            List<Denuncia> denuncias = denunciaServicio.buscarDenunciasPorEstado(estado);
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar denuncias por estado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Denuncia> denuncia = denunciaServicio.buscarDenunciaId(id);

            if (denuncia.isPresent()) {
                return ResponseEntity.ok(denuncia.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la denuncia con ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar denuncia: " + e.getMessage());
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarDenuncia(@RequestBody Denuncia denuncia) {
        try {
            // Verificar que el usuario existe
            if (denuncia.getUsuario() != null && denuncia.getUsuario().getId() != null) {
                Optional<Usuario> usuario = usuarioServicio.buscarUsuarioId(denuncia.getUsuario().getId());

                if (!usuario.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El usuario especificado no existe");
                }

                denuncia.setUsuario(usuario.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Se requiere un usuario válido para crear una denuncia");
            }

            // Establecer valores iniciales
            denuncia.setFechaCreacion(LocalDateTime.now());
            denuncia.setEstado(Denuncia.EstadoDenuncia.PENDIENTE);

            Denuncia denunciaGuardada = denunciaServicio.guardarDenuncia(denuncia);
            return ResponseEntity.status(HttpStatus.CREATED).body(denunciaGuardada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar denuncia: " + e.getMessage());
        }
    }

    @PostMapping("/guardarConEvidencia")
    public ResponseEntity<?> guardarDenunciaConEvidencia(
            @RequestParam("tipo") String tipo,
            @RequestParam("ubicacion") String ubicacion,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("contacto") String contacto,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam(value = "evidencia", required = false) MultipartFile evidencia) {
        try {
            // Verificar que el usuario existe
            Optional<Usuario> usuario = usuarioServicio.buscarUsuarioId(usuarioId);
            if (!usuario.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario especificado no existe");
            }

            Denuncia denuncia = new Denuncia();
            denuncia.setTipo(tipo);
            denuncia.setUbicacion(ubicacion);
            denuncia.setDescripcion(descripcion);
            denuncia.setContacto(contacto);
            denuncia.setUsuario(usuario.get());
            denuncia.setFechaCreacion(LocalDateTime.now());
            denuncia.setEstado(Denuncia.EstadoDenuncia.PENDIENTE);

            // Aquí se manejaría la lógica para guardar el archivo de evidencia
            // y asignar la URL correspondiente a la denuncia

            Denuncia denunciaGuardada = denunciaServicio.guardarDenuncia(denuncia);
            return ResponseEntity.status(HttpStatus.CREATED).body(denunciaGuardada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar denuncia con evidencia: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarDenuncia(@PathVariable Long id, @RequestBody Denuncia denunciaData) {
        try {
            Optional<Denuncia> denunciaOptional = denunciaServicio.buscarDenunciaId(id);

            if (!denunciaOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la denuncia con ID: " + id);
            }

            Denuncia denuncia = denunciaOptional.get();

            // Actualizar los campos
            denuncia.setTipo(denunciaData.getTipo());
            denuncia.setUbicacion(denunciaData.getUbicacion());
            denuncia.setDescripcion(denunciaData.getDescripcion());
            denuncia.setContacto(denunciaData.getContacto());

            // Si se está actualizando el estado
            if (denunciaData.getEstado() != null) {
                denuncia.setEstado(denunciaData.getEstado());
            }

            // Actualizar la fecha de actualización
            denuncia.setFechaActualizacion(LocalDateTime.now());

            Denuncia denunciaActualizada = denunciaServicio.guardarDenuncia(denuncia);
            return ResponseEntity.ok(denunciaActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar denuncia: " + e.getMessage());
        }
    }

    @PutMapping("/cambiarEstado/{id}")
    public ResponseEntity<?> cambiarEstadoDenuncia(@PathVariable Long id, @RequestParam Denuncia.EstadoDenuncia estado) {
        try {
            Optional<Denuncia> denunciaOptional = denunciaServicio.buscarDenunciaId(id);

            if (!denunciaOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la denuncia con ID: " + id);
            }

            Denuncia denuncia = denunciaOptional.get();
            denuncia.setEstado(estado);
            denuncia.setFechaActualizacion(LocalDateTime.now());

            Denuncia denunciaActualizada = denunciaServicio.guardarDenuncia(denuncia);
            return ResponseEntity.ok(denunciaActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar estado de denuncia: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarDenuncia(@PathVariable Long id) {
        try {
            Optional<Denuncia> denunciaOptional = denunciaServicio.buscarDenunciaId(id);

            if (!denunciaOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la denuncia con ID: " + id);
            }

            denunciaServicio.eliminarDenuncia(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Denuncia eliminada correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar denuncia: " + e.getMessage());
        }
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> generarPdfDenuncias() {
        try {
            byte[] pdfBytes = denunciaServicio.generarPdfDenuncias();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "listado-denuncias.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar PDF de denuncias: " + e.getMessage());
        }
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<?> generarPdfDenunciaDetalle(@PathVariable Long id) {
        try {
            byte[] pdfBytes = denunciaServicio.generarPdfDenunciaDetalle(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "denuncia-" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar PDF de detalle de denuncia: " + e.getMessage());
        }
    }
}