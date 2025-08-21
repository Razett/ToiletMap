var _search_toggle_timer;
function toggleSearch(el) {

    if (_search_toggle_timer) {
        clearTimeout(_search_toggle_timer);
        _search_toggle_timer = null;
    }
    const searchBox = document.getElementById('search-place');
    const icon = el.querySelector('i');

    if (!searchBox.classList.contains('open')) {
        if (isExpanded) {
            collapseFloatingList();
        }
        // 열기
        searchBox.classList.remove('close');
        searchBox.classList.add('open');
        icon.classList.replace('fa-search', 'fa-close');
    } else {
        // 닫기
        searchBox.classList.remove('open');
        searchBox.classList.add('close');
        icon.classList.replace('fa-close', 'fa-search');

        // 애니메이션이 끝난 뒤 display: none 처리
        searchBox.addEventListener('animationend', function handler() {
            searchBox.style.display = 'none';
            searchBox.classList.remove('close');
            searchBox.removeEventListener('animationend', handler);
        });
    }

    // 보이도록 처리
    if (searchBox.style.display !== 'flex') {
        searchBox.style.display = 'flex';
    }
}

/*
    Index.html Floating Control
 */

const floatingList = document.getElementById('floating-list');
const dragHandle = floatingList.querySelector('.drag-handle');
const floatingInner = document.querySelector('.floating-list-inner');

let startY = 0;
let startHeight = 0;
let isDragging = false;
let isExpanded = false;

const MAX_HEIGHT = window.innerHeight - 56 - 10;
const MAX_HEIGHT_STR = `calc(100% - 56px - 10px)`;
const MIN_HEIGHT = window.innerHeight * 0.35;
const MIN_HEIGHT_STR = '35%';

dragHandle.addEventListener('touchstart', function (e) {
    isDragging = true;
    startY = e.touches[0].clientY;
    startHeight = floatingList.getBoundingClientRect().height;
    floatingList.style.transition = 'none';
}, { passive: true });

dragHandle.addEventListener('touchmove', function (e) {
    if (!isDragging) return;

    currentY = e.touches[0].clientY;
    const diff = startY - currentY;
    let newHeight = startHeight + diff;

    newHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, newHeight));
    floatingList.style.height = `${newHeight}px`;
    toggleLocationRefreshVisibility()
}, { passive: true });

dragHandle.addEventListener('touchend', function () {
    if (!isDragging) return;

    isDragging = false;
    floatingList.style.transition = 'height 0.3s ease';

    const currentHeight = floatingList.getBoundingClientRect().height;

    if (currentHeight > window.innerHeight / 2) {
        floatingList.classList.add('expanded');
        // floatingList.style.height = `${MAX_HEIGHT}px`;
        isExpanded = true;
    } else {
        floatingList.classList.remove('expanded');
        // floatingList.style.height = `${MIN_HEIGHT}px`;
        isExpanded = false;
    }
    toggleLocationRefreshVisibility()
});

floatingInner.addEventListener('wheel', (e) => {
    if (!isExpanded) {
        e.preventDefault(); // 스크롤 방지
        expandFloatingList();
    }
});

dragHandle.addEventListener('click', () => {
    if (isExpanded) {
        collapseFloatingList();
    } else {
        expandFloatingList();
    }
});

let touchStartY = 0;

floatingInner.addEventListener('touchstart', (e) => {
    touchStartY = e.touches[0].clientY;
}, { passive: true });

floatingInner.addEventListener('touchmove', (e) => {
    const touchY = e.touches[0].clientY;
    const deltaY = touchStartY - touchY;

    // 위로 드래그: 확장
    if (!isExpanded && deltaY > 10) {
        e.preventDefault();
        expandFloatingList();
    }

    // 아래로 드래그: 축소 (최상단에서만 작동)
    if (isExpanded && floatingInner.scrollTop === 0 && deltaY < -10) {
        e.preventDefault();
        collapseFloatingList();
    }
}, { passive: false });

function expandFloatingList() {
    floatingList.classList.add('expanded');
    floatingList.style.height = MAX_HEIGHT_STR;
    isExpanded = true;
    locationRefresh.style.opacity = '0';
    locationRefresh.style.pointerEvents = 'none'; // 클릭 막기
}

function collapseFloatingList() {
    floatingList.classList.remove('expanded');
    floatingList.style.height = MIN_HEIGHT_STR;
    isExpanded = false;
    locationRefresh.style.opacity = '1';
    locationRefresh.style.pointerEvents = 'auto';}
//////////

/*
    Update Refresh-Location Button Visiblity.
 */
const locationRefresh = document.querySelector('.location-refresh');

function toggleLocationRefreshVisibility() {
    const currentHeight = floatingList.getBoundingClientRect().height;
    if (currentHeight > MIN_HEIGHT + 5) {
        locationRefresh.style.opacity = '0';
        locationRefresh.style.pointerEvents = 'none'; // 클릭 막기
    } else {
        locationRefresh.style.opacity = '1';
        locationRefresh.style.pointerEvents = 'auto';
    }
}

function updateMarkers(map, markers) {

    var mapBounds = map.getBounds();
    var marker, position;

    for (var i = 0; i < markers.length; i++) {

        marker = markers[i]
        position = marker.getPosition();

        if (mapBounds.hasLatLng(position)) {
            showMarker(map, marker);
        } else {
            hideMarker(map, marker);
        }
    }
}

function showMarker(map, marker) {

    if (marker.getMap()) return;
    marker.setMap(map);
}

function hideMarker(map, marker) {

    if (!marker.getMap()) return;
    marker.setMap(null);
}