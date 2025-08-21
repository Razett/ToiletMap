package com.razett.toiletmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 Entity
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    /** Username (아이디) */
    @Column(nullable = false, length = 15)
    private String username;

    /** 패스워드 */
    @Column(nullable = false, length = 2048)
    private String password;

    /** 이름 */
    @Column(nullable = true, length = 15)
    private String firstName;

    /** 성 */
    @Column(nullable = false, length = 15)
    private String lastName;

    /** 이메일 주소 */
    @Column(nullable = false, length = 30)
    private String email;
}
