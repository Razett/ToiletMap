$(document).ready(function () {
    geoLocationByGPS();
})

function geoLocationByGPS() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(success, error);
    } else {
        geoLocationByIP();
    }

    function success(position) {
        _geo_lat = position.coords.latitude;
        _geo_lng = position.coords.longitude;
        _geo_type = "gps";
    }

    function error() {
        geoLocationByIP();
    }
}

function getGeoLocation() {
    return new Promise((resolve, reject) => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                position => {
                    resolve({
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                        type: "gps"
                    });
                },
                () => {
                    // 실패 시 IP 기반 위치로 fallback
                    getGeoLocationByIP().then(resolve).catch(reject);
                }
            );
        } else {
            getGeoLocationByIP().then(resolve).catch(reject);
        }
    });
}

function geoLocationByIP() {
    $.ajax({
        url: '/rest/location/location',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            _geo_lat = Number(data.lat);
            _geo_lng = Number(data.lng);
            _geo_type = "ip";
        }
    });
}

function getGeoLocationByIP() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/rest/location/location',
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                resolve({
                    lat: Number(data.lat),
                    lng: Number(data.lng),
                    type: "ip"
                });
            }
        });
    });
}

function resetCenter() {
    getGeoLocation().then(location => {
        _geo_lat = location.lat;
        _geo_lng = location.lng;
        _geo_type = location.type;

        if (map) {
            map.setCenter(new naver.maps.LatLng(_geo_lat, _geo_lng));
        }

        reverseGeocoding(_geo_lat, _geo_lng);
    });
}

function reverseGeocoding(lat, lng, callback) {
    $.ajax({
        url: '/rest/location/reverse-geocoding',
        method: 'POST',
        data: { coords: `${lng},${lat}` },
        success: function (data) {
            _current_location_address = data;
            console.log(data)

            if (callback) {
                callback(data);
            }
        }
    });
}