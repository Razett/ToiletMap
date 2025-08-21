package com.razett.toiletmap.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class GeocodingService {

    private final IpService ipService;
    private final HttpService httpService;
    private final NaverCloudClient naverCloudClient;
    private final NaverCloudService naverCloudService;

    private final String hostUrl = "https://maps.apigw.ntruss.com";
    private final String query = "/map-reversegeocode/v2/gc?coords=";

    public String reverseGeocoding(HttpServletRequest request, String coords) {
        String clientIp = ipService.getClientIp(request);
        String fullQuery = query + coords + "&output=json&orders=legalcode,admcode,addr";
        String requestUrl = hostUrl + fullQuery;

        Map<String, String> headers = new HashMap<>();
        headers.put("x-ncp-apigw-api-key-id", naverCloudClient.getId());
        headers.put("x-ncp-apigw-api-key", naverCloudClient.getSecret());

        HttpService.HttpResponse response = httpService.get(requestUrl, headers);

        try (InputStream body = response.body()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(body);
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                JsonNode region = results.get(0).path("region");
                String area1 = region.path("area1").path("name").asText();
                String area2 = region.path("area2").path("name").asText();
                String area3 = region.path("area3").path("name").asText();
                String area4 = region.path("area4").path("name").asText();

                return String.format("%s %s %s %s", area1, area2, area3, area4).trim();
            }
        } catch (IOException e) {
            return null;
        } finally {
            response.connection().disconnect();
        }

        return null;
    }
}
