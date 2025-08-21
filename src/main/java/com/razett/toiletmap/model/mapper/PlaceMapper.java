package com.razett.toiletmap.model.mapper;

import com.razett.toiletmap.entity.Place;
import com.razett.toiletmap.entity.Users;
import com.razett.toiletmap.model.PlaceInfoDTO;
import com.razett.toiletmap.model.UsersDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Place Entity와 DTO 사이간 변환 Component
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class PlaceMapper {
    private final ModelMapper modelMapper;

    public PlaceInfoDTO toDTO(Place place) {
        return Optional.ofNullable(place)
                .map(u -> modelMapper.map(u, PlaceInfoDTO.class))
                .orElse(null);
    }

    public Place toEntity(PlaceInfoDTO placeInfoDTO) {
        return Optional.ofNullable(placeInfoDTO)
                .map(u -> modelMapper.map(u, Place.class))
                .orElse(null);
    }
}
