package com.Denuncias.denuncias.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class AutorizUsuario {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    // Configuración para la API REST
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**", "/usuarios/**", "/denuncias/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/usuarios/**").permitAll()
                        .requestMatchers("/denuncias/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // Configuración para la aplicación web
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/formularioCliente", "/registrarCliente", "/login", "/formularioLogin", "/formularioPrestaciones").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/cliente/**").permitAll()
                        .requestMatchers("/administrador/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/vistaCliente").hasAuthority("ROLE_CLIENTE")
                        .requestMatchers("/vistaProveedor").hasAuthority("ROLE_PROVEEDOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/formularioLogin")
                        .permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/postLogin", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/formularioLogin?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}