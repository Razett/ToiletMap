package com.razett.toiletmap.model.request;

import com.razett.toiletmap.entity.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 화장실 등록 요청 DTO
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddToiletREQ {
    private String id;
    private String place_id;
    private String title;
    private String roadAddress;
    private String latitude;
    private String longitude;

    private String manPW;
    private Boolean manNeedKey;
    private String womanPW;
    private Boolean womanNeedKey;
    private Integer floor;
    private Integer score;
    private String memo;
}
