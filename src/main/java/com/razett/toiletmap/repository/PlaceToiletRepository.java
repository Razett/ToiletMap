package com.razett.toiletmap.repository;

import com.razett.toiletmap.entity.Place;
import com.razett.toiletmap.entity.PlaceToilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceToiletRepository extends JpaRepository<PlaceToilet, Long> {
    List<PlaceToilet> findByPlace(Place place);

    @Query(value = """
            SELECT pt.*, (
                6371 * acos(
                    cos(radians(:lat)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:lng)) +
                    sin(radians(:lat)) * sin(radians(p.latitude))
                )
            ) AS distance
            FROM place_toilet pt
            JOIN place p ON pt.place_id = p.id
            ORDER BY distance ASC
            LIMIT :page, :count
            """, nativeQuery = true)
    List<PlaceToilet> findNearByToilets(@Param("lat") double lat, @Param("lng") double lng, @Param("page") int page, @Param("count") int count);

}
