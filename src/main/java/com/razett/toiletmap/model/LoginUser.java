package com.razett.toiletmap.model;

import com.razett.toiletmap.entity.Users;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * 현재 로그인 한 사용자 객체
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class LoginUser extends User {
    private UsersDTO usersDTO;

    public LoginUser(UsersDTO usersDTO) {
        super(String.valueOf(usersDTO.getUsername()), usersDTO.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));

        this.usersDTO = usersDTO;
    }
}
