package com.razett.toiletmap.repository;

import com.razett.toiletmap.entity.Place;
import com.razett.toiletmap.entity.PlaceToilet;
import com.razett.toiletmap.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PlaceToiletRepositoryTest {

    @Autowired
    private PlaceToiletRepository placeToiletRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveToilet() {
        Place place = placeRepository.findByPlaceId("26805340");
        Users user = userRepository.findByUsername("jiwonj");

        placeToiletRepository.save(PlaceToilet.builder().place(place).user(user).manPW("1234").womanPW("1234").manNeedKey(false).womanNeedKey(false).floor(1).score(5).build());
    }
}
