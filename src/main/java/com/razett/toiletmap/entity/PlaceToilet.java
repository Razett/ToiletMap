package com.razett.toiletmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 장소 내 화장실 정보 Entity
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceToilet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    /** 남자 화장실 패스워드 */
    @Column(length = 10)
    private String manPW;

    /** 남자 화장실 별도 열쇠 필요 여부 */
    @Column(nullable = false)
    private Boolean manNeedKey;

    /** 여자 화장실 패스워드 */
    @Column(length = 10)
    private String womanPW;

    /** 여자 화장실 별도 열쇠 필요 여부 */
    @Column(nullable = false)
    private Boolean womanNeedKey;

    /** 층 수 (빌딩의 경우) */
    private Integer floor;

    /** 점수 */
    private Integer score;

    /** 메모 */
    @Column(length = 2048)
    private String memo;

    /** 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

}
