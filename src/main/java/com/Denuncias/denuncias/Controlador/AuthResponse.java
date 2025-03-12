package com.Denuncias.denuncias.Controlador;

public class AuthResponse {
    private String accessToken;
    private String rol;
    private String username;

    public AuthResponse(String accessToken, String rol, String username) {
        this.accessToken = accessToken;
        this.rol = rol;
        this.username = username;
    }

    // Getters y setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
