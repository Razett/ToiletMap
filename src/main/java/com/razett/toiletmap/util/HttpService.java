package com.razett.toiletmap.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razett.toiletmap.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * API 요청을 위한 Http 관련 도구
 *
 * @since 2025-06-03 v1.0.0
 * @author jeongjiwon
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class HttpService {

    /**
     * Http 요청의 결과 (body)를 JSON 에서 Java Object로 매핑하기 위한 객체
     */
    private final ObjectMapper objectMapper;

    /**
     * Http Get 요청을 보내고, 응답을 Java Object로 변환하여 반환합니다.
     * @param apiUrl Http 요청을 보낼 URL
     * @param requestHeaders 요청 시 포함할 헤더
     * @param responseType 응답 (JSON)을 Java Object로 변환할 Class Type
     * @return {@literal <T>}
     *
     * @since 2025-06-03 v1.0.0
     * @author jeongjiwon
     */
    public <T> T get(String apiUrl, Map<String, String> requestHeaders, Class<T> responseType) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            InputStream responseStream = responseCode == HttpURLConnection.HTTP_OK ? con.getInputStream() : con.getErrorStream();

            return readBody(responseStream, responseType);
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public record HttpResponse(HttpURLConnection connection, InputStream body) {}

    public HttpResponse get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            InputStream responseStream = responseCode == HttpURLConnection.HTTP_OK
                    ? con.getInputStream()
                    : con.getErrorStream();

            return new HttpResponse(con, responseStream);

        } catch (IOException e) {
            con.disconnect(); // 실패 시 연결 끊기
            throw new RuntimeException("API 요청과 응답 실패", e);
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);

            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    public <T> T readBody(InputStream body, Class<T> valueType) {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return objectMapper.readValue(responseBody.toString(), valueType);
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    public <T> List<T> readListBody(InputStream body, Class<T> valueType) {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, valueType);

            return objectMapper.readValue(responseBody.toString(), listType);
        } catch (IOException e) {
            throw new RuntimeException("API 응답(List<T>)을 읽는데 실패했습니다.", e);
        }
    }

    public <T> ApiResponse<T> readBodyDocument(InputStream body, Class<T> valueType) {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            JavaType wrapperType = objectMapper
                    .getTypeFactory()
                    .constructParametricType(ApiResponse.class, valueType);

            return objectMapper.readValue(responseBody.toString(), wrapperType);

        } catch (IOException e) {
            throw new RuntimeException("API 응답에서 document 목록을 읽는 데 실패했습니다.", e);
        }
    }

    public <T> ApiResponse<T> readBodyDocument(String responseBody, Class<T> valueType) {
        try {
            JavaType wrapperType = objectMapper
                    .getTypeFactory()
                    .constructParametricType(ApiResponse.class, valueType);

            return objectMapper.readValue(responseBody, wrapperType);
        } catch (IOException e) {
            throw new RuntimeException("API 응답에서 document 목록을 읽는 데 실패했습니다.", e);
        }
    }
}
