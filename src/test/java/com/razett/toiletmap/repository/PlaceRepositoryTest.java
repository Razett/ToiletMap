package com.razett.toiletmap.repository;

import com.razett.toiletmap.entity.Place;
import com.razett.toiletmap.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void savePlace() {
        Users user = userRepository.findByUsername("jiwonj");
        placeRepository.save(Place.builder().title("스타벅스 평촌역사거리점").link("").roadAddress("경기 안양시 동안구 시민대로 311").mapx("").mapy("").latitude("37.3959517911724").longitude("126.964936529833").users(user).placeId("26805340").build());
    }
}
