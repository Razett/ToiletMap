package com.razett.toiletmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 장소 Entity
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
public class Place extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 장소 이름 */
    @Column(nullable = false, length = 150)
    private String title;

    /** 장소 URL */
    @Column(nullable = false, length = 1024)
    private String link;

    /** 장소 카테고리 */
    @Column(nullable = true, length = 255)
    private String category;

    /** 장소 설명 */
    @Column(nullable = true, length = 1024)
    private String description;

    /** 장소 구 주소 */
    @Column(nullable = true, length = 1024)
    private String address;

    /** 장소 도로명 주소 */
    @Column(nullable = false, length = 1024)
    private String roadAddress;

    /** 장소 x좌표 WGS84 좌표계 기준 */
    @Column(nullable = false, length = 10)
    private String mapx;

    /** 장소 y좌표 WGS84 좌표계 기준 */
    @Column(nullable = false, length = 10)
    private String mapy;

    /** 위도 */
    @Column(nullable = false, length = 30)
    private String latitude;

    /** 경도 */
    @Column(nullable = false, length = 30)
    private String longitude;

    /** 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    /** 장소ID */
    @Column(nullable = false, length = 1024, unique = true)
    private String placeId;
}
