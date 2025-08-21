package com.razett.toiletmap.util;

import com.razett.toiletmap.properties.NaverCloudClient;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Naver Cloud 관련 도구
 *
 * @since 2025-06-03 v1.0.0
 * @author jeongjiwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class NaverCloudService {

    private final NaverCloudClient naverCloudClient;

    public String makeSignature(String url, String timestamp) {
        String space = " "; // one space
        String newLine = "\n"; // new line
        String method = "GET"; // method
        String accessKey = naverCloudClient.getAccessKey(); // access key id (from portal or Sub Account)
        String secretKey = naverCloudClient.getAccessSecret();

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));

            return Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
