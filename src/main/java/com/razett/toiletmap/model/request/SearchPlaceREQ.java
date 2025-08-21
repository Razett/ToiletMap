package com.razett.toiletmap.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 장소 검색 요청 DTO
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPlaceREQ {
    private String keyword;
    private String latitude;
    private String longitude;
    private Integer radius;
    private Integer start;
    private Integer count;
}
