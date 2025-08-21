package com.razett.toiletmap.controller.place;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razett.toiletmap.entity.Users;
import com.razett.toiletmap.model.KakaoPlace;
import com.razett.toiletmap.model.LoginUser;
import com.razett.toiletmap.model.PlaceInfoDTO;
import com.razett.toiletmap.model.ToiletDTO;
import com.razett.toiletmap.model.request.AddToiletREQ;
import com.razett.toiletmap.model.request.GetToiletREQ;
import com.razett.toiletmap.model.request.SearchPlaceREQ;
import com.razett.toiletmap.properties.KakaoAppKey;
import com.razett.toiletmap.service.PlaceService;
import com.razett.toiletmap.util.GeoLocationService;
import com.razett.toiletmap.util.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 장소 관련 Rest Controller
 *
 * @since 2025-06-03 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@RestController
@RequestMapping("/rest/place")
@RequiredArgsConstructor
public class PlaceRestController {

    private final KakaoAppKey kakaoAppKey;
    private final HttpService httpService;
    private final PlaceService placeService;

    @PostMapping("/search")
    public ResponseEntity<List<KakaoPlace>> searchPlace(@RequestBody SearchPlaceREQ searchPlaceREQ) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
        if (searchPlaceREQ.getKeyword() != null) {
            String encodedKeyword = URLEncoder.encode(searchPlaceREQ.getKeyword(), StandardCharsets.UTF_8);
            url = url + "?query=" + encodedKeyword;

            if (searchPlaceREQ.getLatitude() != null && !searchPlaceREQ.getLatitude().isEmpty()) {
                url = url + "&y=" + searchPlaceREQ.getLatitude() + "&x=" + searchPlaceREQ.getLongitude() + "&sort=distance";
            }

            url = url + "&page=" + (searchPlaceREQ.getStart() + 1) + "&size=" + searchPlaceREQ.getCount();

            Map<String, String> header = new HashMap<>();

            header.put("Authorization", "KakaoAK " + kakaoAppKey.getRest());
            HttpService.HttpResponse response = httpService.get(url, header);

            List<KakaoPlace> placeList = new ArrayList<>();

            boolean isEnd = false;
            try (InputStream body = response.body()) {
                String responseBody = new String(body.readAllBytes(), StandardCharsets.UTF_8);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody); // JSON 파싱

                // meta → is_end 접근
                isEnd = root.path("meta").path("is_end").asBoolean();

                // JSON → DTO로 변환
                placeList = httpService.readBodyDocument(responseBody, KakaoPlace.class).getDocuments();
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            } finally {
                response.connection().disconnect();
            }

            if (!placeList.isEmpty()) {
                boolean finalIsEnd = isEnd;
                placeList.forEach(kakaoPlace -> {
                    kakaoPlace.set_end(finalIsEnd);
                    PlaceInfoDTO placeInfoDTO = placeService.findByPlaceId(kakaoPlace.getId());

                    if (placeInfoDTO != null) {
                        kakaoPlace.setToiletDTO(placeService.findToiletbyPlace(placeInfoDTO));
                        kakaoPlace.setIsSaved(placeService.isSaved(kakaoPlace.getId()));
                    }
                });
            }

            return ResponseEntity.ok(placeList);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToilet(@RequestBody AddToiletREQ addToiletREQ, @AuthenticationPrincipal LoginUser loginUser) {
        PlaceInfoDTO placeInfoDTO = new PlaceInfoDTO();
        ToiletDTO toiletDTO = new ToiletDTO();

        if (loginUser != null) {
            placeInfoDTO.setUser(Users.builder().idx(loginUser.getUsersDTO().getIdx()).build());
            toiletDTO.setUsers(Users.builder().idx(loginUser.getUsersDTO().getIdx()).build());
        } else {
            return ResponseEntity.badRequest().build();
        }

        placeInfoDTO.setTitle(addToiletREQ.getTitle());
        placeInfoDTO.setRoadAddress(addToiletREQ.getRoadAddress());
        placeInfoDTO.setPlaceId(addToiletREQ.getPlace_id());
        placeInfoDTO.setLink("null");
        placeInfoDTO.setMapx("null");
        placeInfoDTO.setMapy("null");

        placeInfoDTO.setLatitude(addToiletREQ.getLatitude());
        placeInfoDTO.setLongitude(addToiletREQ.getLongitude());

        if (addToiletREQ.getId() == null || addToiletREQ.getId().isEmpty()) {
            toiletDTO.setId(null);
        } else {
            try {
                toiletDTO.setId(Long.parseLong(addToiletREQ.getId()));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        toiletDTO.setManPW(addToiletREQ.getManPW());
        toiletDTO.setManNeedKey(addToiletREQ.getManNeedKey());


        toiletDTO.setWomanPW(addToiletREQ.getWomanPW());
        toiletDTO.setWomanNeedKey(addToiletREQ.getWomanNeedKey());
        toiletDTO.setFloor(addToiletREQ.getFloor());
        toiletDTO.setScore(addToiletREQ.getScore());

        if (addToiletREQ.getMemo().length() > 512) {
            toiletDTO.setMemo(addToiletREQ.getMemo().substring(0, 512));
        } else {
            toiletDTO.setMemo(addToiletREQ.getMemo());
        }

        Long savedToiletId = placeService.saveToilet(placeInfoDTO, toiletDTO);

        return savedToiletId == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(savedToiletId.toString());
    }

    @PostMapping("/nearby")
    public ResponseEntity<List<ToiletDTO>> getNearByToilet(@RequestBody GetToiletREQ getToiletREQ) {
        List<ToiletDTO> toiletDTOList;
        try {
            toiletDTOList = placeService.getNearByToilets(getToiletREQ);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(toiletDTOList);
    }
}
