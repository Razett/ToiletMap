package com.razett.toiletmap.util;

import com.razett.toiletmap.model.GeoLocation;
import com.razett.toiletmap.properties.NaverCloudClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * GeoLocation 관련 도구
 *
 * @since 2025-06-07 v1.0.0
 * @author jeongjiwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private final IpService ipService;
    private final HttpService httpService;
    private final NaverCloudClient naverCloudClient;
    private final NaverCloudService naverCloudService;

    private final String hostUrl = "https://geolocation.apigw.ntruss.com";
    private final String query = "/geolocation/v2/geoLocation?";
    private final String ip = "ip=";
    private final String extOpt = "&ext=t";
    private final String responseFormat = "&responseFormatType=json";

    public GeoLocation geoLocationByIp(HttpServletRequest request) {
        String clientIp = ipService.getClientIp(request);
//        String fullQuery = uri + ip + clientIp + extOpt + responseFormat;
        String fullQuery = query + ip + "218.147.202.20" + extOpt + responseFormat;
        String requestUrl = hostUrl + fullQuery;
        String timestamp = String.valueOf(System.currentTimeMillis());

        String signature = naverCloudService.makeSignature(fullQuery, timestamp);

        if (signature != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("x-ncp-apigw-timestamp", timestamp);
            headers.put("x-ncp-iam-access-key", naverCloudClient.getAccessKey());
            headers.put("x-ncp-apigw-signature-v2", signature);

            HttpService.HttpResponse response = httpService.get(requestUrl, headers);

            try (InputStream body = response.body()) {
                GeoLocation geoLocation = httpService.readBodyDocument(body, GeoLocation.class).getGeoLocation();

                return geoLocation;
            } catch (IOException e) {
                return null;
            } finally {
                response.connection().disconnect(); // 여기서 수동으로 끊어줌
            }

        }
        return null;
    }
}
