package com.razett.toiletmap.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Web Resource 경로 Properties
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "toiletmap.uri-path")
@AllArgsConstructor
@NoArgsConstructor
public class UriPath {
    private String data;
    private String css;
    private String font;
    private String js;
    private String asset;
    private String html;
}
