package com.razett.toiletmap.controller.place;

import com.razett.toiletmap.model.GeoLocation;
import com.razett.toiletmap.util.GeoLocationService;
import com.razett.toiletmap.util.GeocodingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/location")
@RequiredArgsConstructor
public class LocationRestController {

    private final GeoLocationService geoLocationService;
    private final GeocodingService geocodingService;

    @PostMapping("/location")
    public ResponseEntity<GeoLocation> geoLocationByIP(HttpServletRequest request) {
        GeoLocation geoLocation = geoLocationService.geoLocationByIp(request);

        if (geoLocation != null) {
            return ResponseEntity.ok(geoLocation);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/reverse-geocoding")
    public ResponseEntity<String> reverseGeocoding(HttpServletRequest request, @RequestParam String coords) {

        return ResponseEntity.ok(geocodingService.reverseGeocoding(request, coords));

    }
}
