let _search_page = 0;
let _search_keyword = '';
let _search_per_count = 10;
let _search_total_count = 0;
let _search_get_continue = true;
let _search_is_fetching = false;

let _nearby_toilet_page = 0;
let _nearby_toilet_count = 10;
let _nearby_toilet_get_continue = true;
let _nearby_toilet_is_fetching = false;

function search_place() {
    clearBeforeSearchList();
    clearNearByBeforeSearch();
    _search_keyword = $('.search-place input').val();


    if (_search_toggle_timer) {
        clearTimeout(_search_toggle_timer);
        _search_toggle_timer = null;
    }
    _search_toggle_timer = setTimeout(function () {
        toggleSearch($('#h-search-btn')[0]);
    }, 500);

    search_place_ajax();
}

function search_place_ajax() {
    _search_is_fetching = true;

    $.ajax({
        url: '/rest/place/search',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({
            keyword: _search_keyword,
            start: _search_page,
            count: _search_per_count,
            ...(_geo_type !== "none" ? {
                latitude: _geo_lat,
                longitude: _geo_lng
            } : {})
        }),
        success: function (data) {
            if (data.length > 0) {
                console.log(data)
                if (data[0]._end) {
                    _search_is_fetching = false;
                    _search_get_continue = false;
                }
                _search_page++;
                _search_total_count = _search_total_count + data.length;
                drawSearchResult(data);
            } else {
                _search_get_continue = false;
            }
            _search_is_fetching = false;
        },
        error: function (xhr, status, error) {
            _search_is_fetching = false;
            _search_get_continue = false;
            console.log(xhr.responseText);
        }
    });
}

function drawSearchResult(data) {
    $('.floating-list .title-area .title').text(`"${_search_keyword}" 검색 결과`);
    $('.floating-list .search-count').text(`총 ${_search_total_count}개 검색 결과`);

    $('.floating-list .title-area .close').removeClass('d-none');

    $('.floating-list #tab-list').addClass('d-none');
    $('.floating-list #tab-search').removeClass('d-none');
    for (const r of data) {
        const _search_result_html = `
            <div class="item" data-lat="${r.y}" data-lng="${r.x}" data-place-id="${r.id}" tabindex="0">
                <div class="place-info">
                  <span class="place-distance c-blue-500">${formatDistanceMeters(r.distance)}</span>
                  <h4 class="place-name">${r.place_name}</h4>
                  <span class="place-address c-grey-800">${r.road_address_name}</span>
                </div>
              <div class="toilet-info">
              </div>
            </div>
            <hr class="item-hr">`;

        $('.floating-list #tab-search').append(_search_result_html);
        $('.floating-list #tab-search .item').last().focus(function () {
            $(this).addClass('bgc-blue-50');
            // $(this).removeClass('bgc-grey-50');


            const _r_id = $(this).data('place-id');
            const _r_lat = $(this).data('lat');
            const _r_lng = $(this).data('lng');

            collapseFloatingList()

            setTimeout(() => {
                this.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }, 300);

            markers.forEach(marker => {
                if (marker.customData.id == _r_id) {
                    marker.setVisible(true);
                } else {
                    marker.setVisible(false);
                }
            });

            if (map) {
                map.setCenter(new naver.maps.LatLng(_r_lat, _r_lng));
            }
        });
        $('.floating-list #tab-search .item').last().blur(function () {
            // $('.floating-list #tab-search .item').addClass(('bgc-grey-50'));
            $('.floating-list #tab-search .item').removeClass(('bgc-blue-50'));
            markers.forEach(marker => {
                marker.setVisible(true);
            });
        });
        let _toilet_info_html;
        let marker;
        if (r.isSaved && r.toiletDTO) {
            _toilet_info_html = `
                <table>
                  <tr class="man">
                    <td><i class="fa-solid fa-mars c-blue-400"></i></td>
                    <td><span class="c-blue-500">${r.toiletDTO.manNeedKey ? "열쇠필요" : r.toiletDTO.manPW}</span></td>
                  </tr>
                  <tr class="woman">
                    <td><i class="fa-solid fa-venus c-red-400"></i></td>
                    <td><span class="c-red-500">${r.toiletDTO.womanNeedKey ? "열쇠필요" : r.toiletDTO.womanPW}</span></td>
                  </tr>
                </table>`

            marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(r.y, r.x),
                map: map,
                icon: {
                    url: './asset/image/marker/marker-blue.png',
                    size: new naver.maps.Size(25, 34),
                    scaledSize: new naver.maps.Size(25, 34),
                    origin: new naver.maps.Point(0, 0),
                    anchor: new naver.maps.Point(12, 34)
                }
            });
            marker.customData = {
                id: r.id
            }
            markers.push(marker);

            $('.floating-list #tab-search .item').last().addClass('is-saved');
        } else {
            _toilet_info_html = `
                <button class="c-grey-800" onclick="showAddToiletModal(this)">
                  <i class="fa-solid fa-plus"></i>
                </button>`

            marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(r.y, r.x),
                map: map,
                icon: {
                    url: './asset/image/marker/marker-grey.png',
                    size: new naver.maps.Size(25, 34),
                    scaledSize: new naver.maps.Size(25, 34),
                    origin: new naver.maps.Point(0, 0),
                    anchor: new naver.maps.Point(12, 34)
                }
            });
            marker.customData = {
                id: r.id
            }
            markers.push(marker);
        }
        if (map) {
            naver.maps.Event.addListener(marker, 'click', function (e) {
                const _marker_id = marker.customData.id;
                console.log(_marker_id);

                $('.floating-list #tab-search .item').each(function () {
                    const _r_id = $(this).data('place-id');

                    if (_marker_id == _r_id) {
                        setTimeout(() => {
                            $(this).focus();
                        }, 100);
                    }
                });
            });
        }

        $('.floating-list #tab-search .item').last().find('.toilet-info').append(_toilet_info_html);
    }
}

function clearSearchList() {
    _search_page = 0;
    _search_keyword = '';
    _search_total_count = 0;
    _search_is_fetching = false;
    _search_get_continue = true;
    $('.floating-list .title-area .title').text(`저장된 주변 장소`);
    $('.floating-list .search-count').text(_current_location_address);

    $('.floating-list .title-area .close').addClass('d-none');

    $('.floating-list #tab-list').removeClass('d-none');
    $('.floating-list #tab-search').addClass('d-none');
    $('.floating-list #tab-search').html('');
    $('.search-place input').val(_search_keyword);
    markers.forEach(m => {
        m.setMap(null);
    })
    markers.length = 0;
    refreshNearByList()
}

function clearBeforeSearchList() {
    _search_page = 0;
    _search_total_count = 0;
    _search_is_fetching = false;
    _search_get_continue = true;
    $('.floating-list #tab-search').html('');

    markers.forEach(m => {
        m.setMap(null);
    })

    markers.length = 0;
    resetCenter();
}

function setAddToiletModal(el) {
    const item = el.closest('.item');
    const place_name = item.querySelector('.place-name')
    const place_id = item.getAttribute('data-place-id');
    const place_address = item.querySelector('.place-address');
    const place_lat = item.getAttribute('data-lat');
    const place_lng = item.getAttribute('data-lng');

    $('#add-toilet-modal .place-name').text(place_name.innerText);
    $('#add-toilet-modal #add-toilet-place-id').val(place_id);
    $('#add-toilet-modal #add-toilet-place-address').val(place_address.innerText);
    $('#add-toilet-modal #add-toilet-place-lat').val(place_lat);
    $('#add-toilet-modal #add-toilet-place-lng').val(place_lng);

}

function showAddToiletModal(el) {
    const add_toilet_modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('add-toilet-modal'));
    setAddToiletModal(el);
    add_toilet_modal.show();
}

function clearAddToiletModal() {
    $('#add-toilet-modal .place-name').text('');

    $('#add-toilet-modal #add-toilet-man-password').val('');
    $('#add-toilet-modal #add-toilet-man-needkey').prop('checked', false);

    $('#add-toilet-modal #add-toilet-woman-password').val('');
    $('#add-toilet-modal #add-toilet-woman-needkey').prop('checked', false);

    $('#add-toilet-modal #add-toilet-floor').val('');
    $('#add-toilet-modal #add-toilet-memo').val('');

    $('#add-toilet-modal #add-toilet-place-id').val('');
    $('#add-toilet-modal #add-toilet-place-address').val('');
    $('#add-toilet-modal #add-toilet-place-lat').val('');
    $('#add-toilet-modal #add-toilet-place-lng').val('');
}

function toggleAddToiletPassword(el) {
    const section = el.closest('.section');
    const input = section.querySelector('.toilet-key');

    if (!el.checked) {
        input.disabled = false;
    } else {
        input.disabled = true;
    }
}

function addToilet() {

    let place_id = $('#add-toilet-modal #add-toilet-place-id').val();
    let title = $('#add-toilet-modal .place-name').text();
    let roadAddress = $('#add-toilet-modal #add-toilet-place-address').val();
    let latitude = $('#add-toilet-modal #add-toilet-place-lat').val();
    let longitude = $('#add-toilet-modal #add-toilet-place-lng').val();

    let manPW = $('#add-toilet-modal #add-toilet-man-password').val();
    let manNeedKey = $('#add-toilet-modal #add-toilet-man-needkey').prop('checked');
    let womanPW = $('#add-toilet-modal #add-toilet-woman-password').val();
    let womanNeedKey = $('#add-toilet-modal #add-toilet-woman-needkey').prop('checked');
    let floor = $('#add-toilet-modal #add-toilet-floor').val();
    let score = '';
    let memo = $('#add-toilet-modal #add-toilet-memo').val();

    if ([place_id, title, roadAddress, latitude, longitude].every(isEmpty)) {
        alert('나중에 다시 시도해주세요.');
        return;
    }

    if (!manNeedKey && isEmpty(manPW)) {
        alert('남자 화장실 비밀번호를 입력해 주세요.');
        return;
    }

    if (!womanNeedKey && isEmpty(womanPW)) {
        alert('여자 화장실 비밀번호를 입력해 주세요.');
        return;
    }
    let req_object = {
        place_id: place_id,
        title: title,
        roadAddress: roadAddress,
        latitude: latitude,
        longitude: longitude,
        manPW: manPW,
        manNeedKey: manNeedKey,
        womanPW: womanPW,
        womanNeedKey: womanNeedKey,
        floor: floor,
        score: score,
        memo: memo
    }
    addToiletAjax(req_object, function (data) {
        const add_toilet_modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('add-toilet-modal'));
        console.log(add_toilet_modal);
        add_toilet_modal.hide();
        ToastService.printToast(title + "의 화장실 정보를 저장하였습니다.");
        clearSearchList();
    })
}

function isEmpty(str) {
    return !str || String(str).trim() === '';
}

function addToiletAjax(obj, success, err) {

    $.ajax({
        url: '/rest/place/add',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(obj),
        success: function (data) {
            success(data);
        },
        error: function (xhr, status, error) {
            if (err) {
                err(error)
            }
        }
    });
}

function getNearbyToilet() {
    let req_object = {
        lat: _geo_lat,
        lng: _geo_lng,
        page: _nearby_toilet_page,
        count: _nearby_toilet_count
    }

    getNearbyToiletAjax(req_object, function (data) {
        if (data) {
            for (const toilet of data) {
                const _nearby_toilet_html = `
                    <div class="item" data-lat="${toilet.placeInfoDTO.latitude}" data-lng="${toilet.placeInfoDTO.longitude}" data-toilet-id="${toilet.id}" tabindex="0">
                        <div class="place-info">
                          <span class="place-distance c-blue-500">${formatDistance(toilet.placeInfoDTO.latitude, toilet.placeInfoDTO.longitude)}</span>
                          <h4 class="place-name">${toilet.placeInfoDTO.title}</h4>
                          <span class="place-address c-grey-800">${toilet.placeInfoDTO.roadAddress}</span>
                        </div>
                      <div class="toilet-info">
                      </div>
                    </div>
                    <hr class="item-hr">`;

                $('.floating-list #tab-list').append(_nearby_toilet_html);
                $('.floating-list #tab-list .item').last().focus(function () {
                    $(this).addClass('bgc-blue-50');
                    // $(this).removeClass('bgc-grey-50');


                    const _toilet_id = $(this).data('toilet-id');
                    const _toilet_lat = $(this).data('lat');
                    const _toilet_lng = $(this).data('lng');

                    collapseFloatingList()

                    setTimeout(() => {
                        this.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }, 300);

                    markers_saved.forEach(marker => {
                        if (marker.customData.id == _toilet_id) {
                            marker.setVisible(true);
                        } else {
                            marker.setVisible(false);
                        }
                    });

                    if (map) {
                        map.setCenter(new naver.maps.LatLng(_toilet_lat, _toilet_lng));
                    }
                });
                $('.floating-list #tab-list .item').last().blur(function () {
                    // $('.floating-list #tab-list .item').addClass(('bgc-grey-50'));
                    $('.floating-list #tab-list .item').removeClass(('bgc-blue-50'));
                    markers_saved.forEach(marker => {
                        marker.setVisible(true);
                    });
                });

                let _toilet_info_html = `
                <table>
                  <tr class="man">
                    <td><i class="fa-solid fa-mars c-blue-400"></i></td>
                    <td><span class="c-blue-500">${toilet.manNeedKey ? "열쇠필요" : toilet.manPW}</span></td>
                  </tr>
                  <tr class="woman">
                    <td><i class="fa-solid fa-venus c-red-400"></i></td>
                    <td><span class="c-red-500">${toilet.womanNeedKey ? "열쇠필요" : toilet.womanPW}</span></td>
                  </tr>
                </table>`

                let marker = new naver.maps.Marker({
                        position: new naver.maps.LatLng(toilet.placeInfoDTO.latitude, toilet.placeInfoDTO.longitude),
                        map: map,
                        icon: {
                            url: './asset/image/marker/marker-blue.png',
                            size: new naver.maps.Size(25, 34),
                            scaledSize: new naver.maps.Size(25, 34),
                            origin: new naver.maps.Point(0, 0),
                            anchor: new naver.maps.Point(12, 34)
                        }
                    });
                    marker.customData = {
                        id: toilet.id
                    }
                    markers_saved.push(marker);

                if (map) {
                    naver.maps.Event.addListener(marker, 'click', function (e) {
                        const _marker_id = marker.customData.id;

                        $('.floating-list #tab-list .item').each(function () {
                            const _toilet_id = $(this).data('toilet-id');

                            if (_marker_id == _toilet_id) {
                                setTimeout(() => {
                                    $(this).focus();
                                }, 100);
                            }
                        });
                    });
                }
                $('.floating-list #tab-list .item').last().find('.toilet-info').append(_toilet_info_html);
            }
        }
    })
}

function formatDistanceMeters(meters) {
    return meters < 1000
        ? `${Math.round(meters)}m`
        : `${(meters / 1000).toFixed(2)}km`;
}

function formatDistance(lat, lng) {
    const meters = calculateDistanceInMeters(lat, lng);
    return meters < 1000
        ? `${Math.round(meters)}m`
        : `${(meters / 1000).toFixed(2)}km`;
}

function calculateDistanceInMeters(lat, lng) {
    const R = 6371000;
    const toRad = angle => angle * Math.PI / 180;

    const dLat = toRad(lat - _geo_lat);
    const dLng = toRad(lng - _geo_lng);

    const a = Math.sin(dLat / 2) ** 2 +
        Math.cos(toRad(_geo_lat)) * Math.cos(toRad(lat)) *
        Math.sin(dLng / 2) ** 2;

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}

function getNearbyToiletAjax(obj, success, err) {
    _nearby_toilet_is_fetching = true;

    $.ajax({
        url: '/rest/place/nearby',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(obj),
        success: function (data) {
            _nearby_toilet_is_fetching = false;
            if (data.length > 0) {
                _nearby_toilet_get_continue = true;
                _nearby_toilet_page++;
                success(data);
            } else {
                _nearby_toilet_get_continue = false;
            }
        },
        error: function (xhr, status, error) {
            if (err) {
                _nearby_toilet_is_fetching = false;
                _nearby_toilet_get_continue = false;
                err(error);
            }
        }
    });
}

function refreshNearByList() {
    resetCenter();

    _nearby_toilet_page = 0;

    $('.floating-list .title-area .title').text(`저장된 주변 장소`);
    $('.floating-list .search-count').text(_current_location_address);

    $('.floating-list #tab-list').html('');
    markers_saved.forEach(m => {
        m.setMap(null);
    })
    markers_saved.length = 0;

    getNearbyToilet();
}

function clearNearByBeforeSearch() {
    _nearby_toilet_page = 0;

    $('.floating-list #tab-list').html('');

    markers_saved.forEach(m => {
        m.setMap(null);
    })

    markers_saved.length = 0;
}