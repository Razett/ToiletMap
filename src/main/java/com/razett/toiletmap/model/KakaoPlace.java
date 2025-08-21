package com.razett.toiletmap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * 카카오 API 장소 객체
 *
 * @since 2025-06-06 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoPlace {
    private String id;
    private String place_name;
    private String category_name;
    private String phone;
    private String address_name;
    private String road_address_name;
    private String x;
    private String y;
    private String distance;

    // Custom Field
    private Boolean isSaved;
    private ToiletDTO toiletDTO;
    private boolean is_end;
}
