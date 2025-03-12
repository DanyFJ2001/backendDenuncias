package com.Denuncias.denuncias.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class AutorizUsuario {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));  // Permite todos los orígenes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));  // Permite todos los headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/formularioCliente", "/registrarCliente", "/login", "/formularioLogin", "/formularioPrestaciones").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/cliente/**").permitAll()
                        .requestMatchers("/usuarios/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
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