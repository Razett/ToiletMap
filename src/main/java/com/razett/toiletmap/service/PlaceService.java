package com.razett.toiletmap.service;

import com.razett.toiletmap.entity.Place;
import com.razett.toiletmap.entity.PlaceToilet;
import com.razett.toiletmap.model.PlaceInfoDTO;
import com.razett.toiletmap.model.ToiletDTO;
import com.razett.toiletmap.model.mapper.PlaceMapper;
import com.razett.toiletmap.model.mapper.PlaceToiletMapper;
import com.razett.toiletmap.model.request.GetToiletREQ;
import com.razett.toiletmap.repository.PlaceRepository;
import com.razett.toiletmap.repository.PlaceToiletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceToiletRepository placeToiletRepository;
    private final PlaceMapper placeMapper;
    private final PlaceToiletMapper placeToiletMapper;

    public PlaceInfoDTO findByPlaceId(String placeId) {
        return placeMapper.toDTO(placeRepository.findByPlaceId(placeId));
    }

    public boolean isSaved(String placeId) {
        PlaceInfoDTO placeInfoDTO = findByPlaceId(placeId);
        if (placeInfoDTO != null && placeInfoDTO.getId() != null) {
            return true;
        }
        return false;
    }

    public ToiletDTO findToiletbyPlace(PlaceInfoDTO placeInfoDTO) {
        List<ToiletDTO> toiletDTOList = new ArrayList<>();
        placeToiletRepository.findByPlace(Place.builder().id(placeInfoDTO.getId()).build()).forEach(placeToilet -> {
            toiletDTOList.add(placeToiletMapper.toDTO(placeToilet));
        });

        return toiletDTOList.isEmpty() ? null : toiletDTOList.get(0);
    }

    public Long saveToilet(PlaceInfoDTO placeInfoDTO, ToiletDTO toiletDTO) {
        Place savedPlace;

        if (isSaved(placeInfoDTO.getPlaceId())) {
            savedPlace = placeRepository.findByPlaceId(placeInfoDTO.getPlaceId());
        } else {
            savedPlace = placeRepository.save(placeMapper.toEntity(placeInfoDTO));
        }

        PlaceToilet placeToilet = placeToiletMapper.toEntity(toiletDTO);
        placeToilet.setPlace(savedPlace);

        PlaceToilet savedPlaceToilet = placeToiletRepository.save(placeToilet);

        return savedPlaceToilet.getId();
    }

    public List<ToiletDTO> getNearByToilets(GetToiletREQ getToiletREQ) {
        getToiletREQ.setPage(getToiletREQ.getPage() * getToiletREQ.getCount());
        List<PlaceToilet> placeToiletList = placeToiletRepository.findNearByToilets(getToiletREQ.getLat(), getToiletREQ.getLng(), getToiletREQ.getPage(), getToiletREQ.getCount());

        List<ToiletDTO> toiletDTOList = new ArrayList<>();

        placeToiletList.forEach(placeToilet -> {
            ToiletDTO toiletDTO = placeToiletMapper.toDTO(placeToilet);

            toiletDTO.setPlaceInfoDTO(placeMapper.toDTO(placeToilet.getPlace()));

            toiletDTOList.add(toiletDTO);
        });

        return toiletDTOList;
    }
}
