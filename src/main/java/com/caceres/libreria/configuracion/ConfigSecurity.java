package com.caceres.libreria.configuracion;

import com.caceres.libreria.seguridad.FiltroAutenticacionJwt;
import com.caceres.libreria.seguridad.ImplementacionServicioDetallesUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigSecurity {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;
    private final ImplementacionServicioDetallesUsuario implementacionServicioDetallesUsuario;

    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity seguridadHttp) throws Exception {
        seguridadHttp
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(fuenteConfiguracionCors()))
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorizacion -> autorizacion
                        .requestMatchers("/api/autenticacion/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(proveedorAutenticacion())
                .addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class);

        return seguridadHttp.build();
    }

    @Bean
    public CorsConfigurationSource fuenteConfiguracionCors() {
        CorsConfiguration configuracionCors = new CorsConfiguration();
        configuracionCors.setAllowedOriginPatterns(List.of("*"));
        configuracionCors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuracionCors.setAllowedHeaders(List.of("*"));
        configuracionCors.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource fuenteBasadaEnUrl = new UrlBasedCorsConfigurationSource();
        fuenteBasadaEnUrl.registerCorsConfiguration("/**", configuracionCors);
        return fuenteBasadaEnUrl;
    }

    @Bean
    public AuthenticationProvider proveedorAutenticacion() {
        DaoAuthenticationProvider proveedorDao = new DaoAuthenticationProvider();
        proveedorDao.setUserDetailsService(implementacionServicioDetallesUsuario);
        proveedorDao.setPasswordEncoder(codificadorContrasena());
        return proveedorDao;
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration configuracionAutenticacion) throws Exception {
        return configuracionAutenticacion.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }
}