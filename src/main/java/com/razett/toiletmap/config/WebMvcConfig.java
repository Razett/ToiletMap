package com.razett.toiletmap.config;

import com.razett.toiletmap.properties.ResourceLocation;
import com.razett.toiletmap.properties.UriPath;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Resources를 정적 배포하기 위한 Config
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final UriPath uriPath;
    private final ResourceLocation resourceLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uriPath.getData() + "/**")
                .addResourceLocations("file:///" + resourceLocation.getData() + "/");
        registry.addResourceHandler(uriPath.getCss() + "/**")
                .addResourceLocations("file:///" + resourceLocation.getCss() + "/");
        registry.addResourceHandler(uriPath.getFont() + "/**")
                .addResourceLocations("file:///" + resourceLocation.getFont() + "/");
        registry.addResourceHandler(uriPath.getJs() + "/**")
                .addResourceLocations("file:///" + resourceLocation.getJs() + "/");
        registry.addResourceHandler(uriPath.getAsset() + "/**")
                .addResourceLocations("file:///" + resourceLocation.getAsset() + "/");
        registry.addResourceHandler(uriPath.getHtml() + "/**")
                .addResourceLocations("file:///" + resourceLocation.getHtml() + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*");
    }
}
