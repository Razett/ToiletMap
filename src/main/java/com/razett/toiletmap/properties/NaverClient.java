package com.razett.toiletmap.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Naver API 호출을 위한 Client Properties
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "com.naver.api.client")
@AllArgsConstructor
@NoArgsConstructor
public class NaverClient {

    private String id;
    private String secret;
}
