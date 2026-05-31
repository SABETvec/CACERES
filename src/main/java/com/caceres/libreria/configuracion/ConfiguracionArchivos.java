package com.caceres.libreria.configuracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionArchivos implements WebMvcConfigurer {

    @Value("${aplicacion.directorio.subidas:uploads}")
    private String directorioSubidas;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry manejadorRecursos) {
        manejadorRecursos.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + directorioSubidas + "/");
    }
}