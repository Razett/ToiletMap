package com.razett.toiletmap.model.mapper;

import com.razett.toiletmap.entity.Users;
import com.razett.toiletmap.model.UsersDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Users Entity와 DTO 사이간 변환 Component
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UsersDTO toDTO(Users users) {
        return Optional.ofNullable(users)
                .map(u -> modelMapper.map(u, UsersDTO.class))
                .orElse(null);
    }

    public Users toEntity(UsersDTO usersDTO) {
        return Optional.ofNullable(usersDTO)
                .map(u -> modelMapper.map(u, Users.class))
                .orElse(null);
    }
}
