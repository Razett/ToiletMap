package com.razett.toiletmap.model;

import com.razett.toiletmap.entity.Users;
import jakarta.persistence.Column;
import lombok.*;

/**
 * 장소 정보 DTO
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceInfoDTO {
    private Long id;
    private String title;
    private String link;
    private String latitude;
    private String longitude;
    private String placeId;
    private String roadAddress;

    private String mapx;
    private String mapy;

    private Users user;
    private Double distance;
}
