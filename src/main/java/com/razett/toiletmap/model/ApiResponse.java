package com.razett.toiletmap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * API 응답 객체
 *
 * @since 2025-06-06 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private List<T> documents;
    private String returnCode;
    private GeoLocation geoLocation;
}
