package com.Denuncias.denuncias.Controlador;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DenunciasApiFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;

    public DenunciasApiFilter(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Si la solicitud coincide con nuestra ruta de denuncias
        if (requestMatcher.matches(request)) {
            // Configurar encabezados CORS para esta solicitud específica
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");

            // Si es una solicitud OPTIONS (preflight), responder directamente
            if (request.getMethod().equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }

            // Continuar con la cadena de filtros para procesar esta solicitud normalmente
            filterChain.doFilter(request, response);
            return;
        }

        // Para otras solicitudes, continuar con el procesamiento normal
        filterChain.doFilter(request, response);
    }
}
