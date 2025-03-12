package com.Denuncias.denuncias.Entidad;

import com.Denuncias.denuncias.Roles.Rol;
import jakarta.persistence.*;
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    private Rol rol;  // Rol del cliente, si es necesario

    public Usuario() {
    }

    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
