package com.razett.toiletmap.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Naver Cloud API 호출을 위한 Client Properties
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "com.ncloud.api.client")
@AllArgsConstructor
@NoArgsConstructor
public class NaverCloudClient {

    private String id;
    private String secret;
    private String accessKey;
    private String accessSecret;
}
