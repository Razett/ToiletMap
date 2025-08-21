package com.razett.toiletmap.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Kakao API 호출을 위한 App Key Properties
 *
 * @since 2025-06-04 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "com.kakao.app.key")
@AllArgsConstructor
@NoArgsConstructor
public class KakaoAppKey {
    private String admin;
    private String rest;
}
