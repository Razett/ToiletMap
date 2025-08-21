package com.razett.toiletmap.repository;

import com.razett.toiletmap.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByPlaceId(String placeId);

}
