let map; // 지도
let service; // 서비스
let infowindow; // 인포윈도우
let openInfowindow = null; // 열린 인포윈도우
let currentMarkerId = null; // 현재 선택된 마커의 ID
let markerIdCounter = 0; // 마커 ID 카운트
let markers = []; // 마커의 값들을 담은 배열

// 지도 초기화
function initMap() {
    const seoul = new google.maps.LatLng(37.555, 126.9725);

    // 지도
    map = new google.maps.Map(document.getElementById("map"), {
        center: seoul,
        zoom: 15,
        gestureHandling: "greedy", // 더블 클릭, 스크롤 시 확대/축소 가능
    });

    // [이벤트] 지도 클릭
    map.addListener("click", (event) => {
        addMarker(event.latLng);
    });

    // 인포윈도우
    infowindow = new google.maps.InfoWindow();

    // 검색창 자동완성
    searchAutocomplete(map); // 함수 호출
}

// [함수] 마커 추가
function addMarker(position) {
    // 마커 생성
    const marker = new google.maps.Marker({
        position: position,
        map: map,
        id: markerIdCounter++, // 마커 ID 증가
    });

    // Reverse Geocoding API 호출
    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ location: position }, (results, status) => {
        if (status === "OK") {
            // placeId 추출
            const placeId = results[0].place_id;
            marker.set("placeId", placeId);

            // 장소 정보 가져오기
            const request = {
                placeId: placeId,
                fields: ['name', 'rating', 'formatted_phone_number', 'geometry'],
            }

            // 장소 정보
            const placesService = new google.maps.places.PlacesService(map);
            placesService.getDetails(request, (place, status) => {
                const lat = place.geometry.location.lat();
                const lng = place.geometry.location.lng();
                const placeRating = place.rating === undefined ? 0 : place.rating;
                const placeName = place.name === undefined ? "이름 없음" : place.name;
                const placePhoneNumber = place.formatted_phone_number === undefined ? "전화번호 없음" : place.formatted_phone_number;

                // 마커에 장소 정보 설정
                marker.place = place;

                /*
                console.log("lat : " + lat);
                console.log("lng : " + lng);
                console.log("name : " + place.name);
                console.log("rating : " + place.rating);
                console.log("place : " + place);
                console.log("placeRating : " + placeRating);
                console.log("placeName : " + placeName);
                */

                // 인포윈도우 내용
                // addInfowindow() 함수 내 content에 들어가는 내용
                const content = "<form id='infowindow'>" + `
                    <label for='name'>이름</label><input name='name' id='name' value='${placeName}' readonly /></br>
                    <label for='phone'>전화번호</label><input name='phoneNumber' id='phone' value='${placePhoneNumber}' readonly /></br>
                    <label for='rating'>별점</label><input name='rating' id='rating' value='${placeRating}' readonly />
                    <label for='placeId'></label><input type='hidden' name='placeId' id='placeId' value='${placeId}' readonly />
                    <label for='lat'></label><input type='hidden' name='latitude' id='lat' value='${lat}' readonly />
                    <label for='longitude'></label><input type='hidden' name='longitude' id='longitude' value='${lng}' readonly />` +
                    "<div id='infowindow_btn_group'>" +
                    "<input type='button' value='장소 추가' onclick='addPlace()' />" +
                    "<input type='button' value='마커 삭제' onclick='deleteMarker()' />" +
                    "<input type='button' value='창 닫기' onclick='closeInfowindow()' />" +
                    "</div>" + "</form>"

                // 인포윈도우 추가
                addInfowindow(marker, content); // 함수 호출
            });
        }
    });

    // makers에 marker 값 담기
    markers.push(marker);
}

// [함수] 인포윈도우 추가
// marker, content를 받아 해당 마커에 인포윈도우 추가
function addInfowindow(marker, content) {
    // 인포윈도우 생성
    const infowindow = new google.maps.InfoWindow({
        content: content,
    });

    // 마커에 인포윈도우 속성을 설정
    // 마커와 인포윈도우를 연결 -> 마커 삭제, 인포윈도우 조작 가능
    marker.infowindow = infowindow;

    // [이벤트] 마커 클릭 시 인포윈도우 열기, 닫기
    marker.addListener("click", () => {
        // 이미 열린 인포윈도우가 있을 경우 열린 인포윈도우 닫기
        if (openInfowindow && openInfowindow.getMap()) {
            openInfowindow.close();
        }

        // getMap() : 인포윈도우가 지도에 연결되었는지 유무 확인
        // 인포윈도우 표시 시 연결된 지도를 반환, 미표시 시 Null 반환
        if (infowindow.getMap()) {
            // 지도에 인포윈도우 표시 시 인포윈도우 닫기
            infowindow.close();
        } else {
            // 지도에 인포윈도우 미표시 시 인포윈도우 열기
            // map에서 인포윈도우를 열기, 특정 marker와 연결
            infowindow.open(map, marker);
            openInfowindow = infowindow;
        }

        // 현재 선택된 마커의 ID 설정
        // 설정 이유 : 마커 선택 시 ID 필요
        currentMarkerId = marker.id;
    });

    // [이벤트] 지도 클릭 시 인포윈도우 닫기
    map.addListener("click", () => {
        infowindow.close();

        // 현재 선택된 마커의 ID 초기화
        // 초기화 이유 : 미선택 시 초기화 필요 (마커 선택 시만 ID 필요)
        currentMarkerId = null;
    });
}

// [함수] 인포윈도우 닫기
function closeInfowindow() {
    // find() : 배열의 요소에 대해 조건 함수를 실행 후, true를 반환하는 첫 번째 요소를 반환 (조건 미부합 undefined 반환)
    // currentMarkerId와 일치하는 ID를 가진 마커를 markers에서 찾기
    const marker = markers.find((marker) => marker.id === currentMarkerId);

    // 마커 표시 중 인포윈도우 닫기, 마커 ID 초기화
    if (marker) {
        // 인포윈도우 생성, 마커와 인포윈도우를 연결
        const infowindow = marker.infowindow;

        // 인포윈도우 표시 중 인포윈도우 닫기
        if (infowindow) {
            infowindow.close();
        }

        // 현재 선택된 마커의 ID 초기화
        currentMarkerId = null;
    }
}

// [함수] 전체 장소 초기화
function initPlace(map) {
    const q = confirm("초기화를 하시겠습니까?");

    if (q) {
        // 확인 버튼 클릭 시 로직
        // 전체 markers 값 초기화
        if (markers && markers.length > 0) {
            for (let i = 0; i < markers.length; i++) {
                markers[i].setMap(null);
            }

            markers = [];
            alert("초기화를 성공했습니다.");
        } else {
            alert("초기화할 장소가 없습니다.");
        }
    } else {
        // 취소 버튼 클릭 시 로직
        alert("초기화를 실패했습니다.");
    }
}

// [함수] 장소 추가
// function addPlace() {
//     // currentMarkerId와 일치하는 ID를 가진 마커를 markers에서 찾기
//     const marker = markers.find((marker) => marker.id === currentMarkerId);
//
//     // 장소 정보 가져오기
//     const place = marker.place;
//     const placeId = marker.get("placeId");
//     const lat = marker.getPosition().lat();
//     const lng = marker.getPosition().lng();
//     const placeRating = place.rating === undefined ? 0 : place.rating;
//     const placeName = place.name === undefined ? "이름 없음" : place.name;
//     const placePhoneNumber = place.formatted_phone_number === undefined ? "전화번호 없음" : place.formatted_phone_number;
//
//     // 장소 정보 리스트 생성하기
//     const list = document.getElementById("list_place");
//     const listItem = document.createElement("li");
//
//     listItem.innerHTML = `
//         <label for='name'>이름</label><input name='name' id='name' value='${placeName}' readonly /></br>
//         <label for='phone'>전화번호</label><input name='phoneNumber' id='phone' value='${placePhoneNumber}' readonly /></br>
//         <label for='rating'>별점</label><input name='rating' id='rating' value='${placeRating}' readonly />
//         <label for='placeId'></label><input type='hidden' name='placeId' id='placeId' value='${placeId}' readonly />
//         <label for='latitude'></label><input type='hidden' name='latitude' id='latitude' value='${lat}' readonly />
//         <label for='longitude'></label><input type='hidden' name='longitude' id='longitude' value='${lng}' readonly />
//         <input type='button' value='삭제' onclick='deletePlace()' />
//     `;
//
//     // 장소 정보 리스트 추가
//     list.appendChild(listItem);
//     alert("장소를 추가했습니다.");
//
//     // 인포윈도우 닫기
//     closeInfowindow() // 함수 호출
// }

// [함수] 마커 삭제
function deleteMarker() {
    // currentMarkerId와 일치하는 ID를 가진 마커를 markers에서 찾기
    const marker = markers.find((marker) => marker.id === currentMarkerId);

    // 마커 표시 중 인포윈도우 닫기, 마커 ID 초기화
    if (marker) {
        // marker, markers 값 초기화
        marker.setMap(null);
        markers[marker] = null;

        // 인포윈도우 생성, 마커와 인포윈도우를 연결
        const infowindow = marker.infowindow;

        // 인포윈도우 표시 중 인포윈도우 닫기
        if (infowindow) {
            infowindow.close();
        }

        // filter() : 배열의 요소에 대해 조건 함수를 실행 후, true를 반환하는 요소들로 새로운 배열을 구성
        // currentMarkerId와 일치하지 않는 마커들로 이루어진 새로운 배열 생성 (마커를 삭제한 효과와 동일)
        markers = markers.filter((m) => m.id !== currentMarkerId);
        alert("마커를 삭제했습니다.");

        // 현재 선택된 마커의 ID 초기화
        currentMarkerId = null;
    }
}

// [함수] 장소 검색
function searchPlace() {
    const query = document.getElementById("map_search_input").value;

    const request = {
        query: query,
        fields: ['ALL'],
    }

    service = new google.maps.places.PlacesService(map);
    service.findPlaceFromQuery(request, (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK && results) {
            const result = results[0];
            const position = result.geometry.location;
            addSearchMarker(position, result); // 함수 호출
            map.setCenter(result.geometry.location);
        }
    });
}

// [함수] 마커 추가 (검색)
function addSearchMarker(position, place) {
    addMarker(position); // 함수 호출
}

// [함수] 검색창 자동완성
// 검색창 자동완성
function searchAutocomplete(map) {
    // 검색창
    const input = document.getElementById("map_search_input");

    const options = {
        fields: ["name", "geometry"],
        strictBounds: false, // 넓은 범위 검색 (true : 설정된 지도 범위 내)
        // types: ["establishment"], // 검색할 장소의 유형을 제한 (establishment : 사업체나 기관과 같은 공공장소)
    }

    // 자동완성
    autocomplete = new google.maps.places.Autocomplete(input, options);

    // 지도의 경계에 바인딩
    // 검색 결과가 지도의 가시 영역으로 제한 -> 사용자가 특정 지도 영역 내의 장소를 검색할 때 유용
    autocomplete.bindTo("bounds", map);

    autocomplete.addListener("place_changed", () => {
        // 선택한 장소의 세부 정보 반환
        const place = autocomplete.getPlace();

        // 지도에서 보여줄 영역
        if (place.geometry.viewport) {
          // 선택한 장소의 위치 좌표를 지도의 확대/축소 수준과 지도의 중심으로 자동 설정
          map.fitBounds(place.geometry.viewport);
        } else {
          // 선택한 장소의 위치 좌표를 지도의 중심으로 설정
          map.setCenter(place.geometry.location);
          map.setZoom(15);
        }

        addMarker(place.geometry.location); // 함수 호출
    });
}

window.initMap = initMap;
