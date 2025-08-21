package com.razett.toiletmap.model.mapper;

import com.razett.toiletmap.entity.Place;
import com.razett.toiletmap.entity.PlaceToilet;
import com.razett.toiletmap.model.PlaceInfoDTO;
import com.razett.toiletmap.model.ToiletDTO;
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
public class PlaceToiletMapper {
    private final ModelMapper modelMapper;

    public ToiletDTO toDTO(PlaceToilet placeToilet) {
        return Optional.ofNullable(placeToilet)
                .map(u -> modelMapper.map(u, ToiletDTO.class))
                .orElse(null);
    }

    public PlaceToilet toEntity(ToiletDTO toiletDTO) {
        return Optional.ofNullable(toiletDTO)
                .map(u -> modelMapper.map(u, PlaceToilet.class))
                .orElse(null);
    }
}
