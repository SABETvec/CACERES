package com.caceres.libreria.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private final JwtUtilidad jwtUtilidad;
    private final ImplementacionServicioDetallesUsuario implementacionServicioDetallesUsuario;

    @Override
    protected void doFilterInternal(HttpServletRequest solicitud,
                                    HttpServletResponse respuesta,
                                    FilterChain cadenaFiltros)
            throws ServletException, IOException {

        String encabezadoAutorizacion = solicitud.getHeader("Authorization");

        if (encabezadoAutorizacion != null && encabezadoAutorizacion.startsWith("Bearer ")) {
            String tokenJwt = encabezadoAutorizacion.substring(7);
            if (jwtUtilidad.validarToken(tokenJwt)) {
                String nombreUsuario = jwtUtilidad.extraerNombreUsuario(tokenJwt);
                UserDetails detallesUsuario = implementacionServicioDetallesUsuario.loadUserByUsername(nombreUsuario);
                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(
                                detallesUsuario, null, detallesUsuario.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        }

        cadenaFiltros.doFilter(solicitud, respuesta);
    }
}