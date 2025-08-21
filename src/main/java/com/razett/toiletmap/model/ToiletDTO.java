package com.razett.toiletmap.model;

import com.razett.toiletmap.entity.Users;
import lombok.*;

/**
 * 화장실 정보 DTO
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
public class ToiletDTO {
    private Long id;
    private String manPW;
    private Boolean manNeedKey;
    private String womanPW;
    private Boolean womanNeedKey;
    private Integer floor;
    private Integer score;
    private String memo;

    private Users users;

    private PlaceInfoDTO placeInfoDTO;
}
