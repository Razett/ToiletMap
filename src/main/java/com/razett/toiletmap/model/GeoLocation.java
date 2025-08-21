package com.razett.toiletmap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * GeoLocation 객체
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
public class GeoLocation {
    private String lat;
    @JsonProperty("long")
    private String lng;
}
