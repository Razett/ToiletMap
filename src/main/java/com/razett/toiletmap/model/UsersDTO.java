package com.razett.toiletmap.model;

import lombok.Data;

/**
 * 사용자 DTO
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Data
public class UsersDTO {
    private Long idx;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
