package com.mapapplication.mapapplication.controller;

import com.mapapplication.mapapplication.dto.PlaceDto;
import com.mapapplication.mapapplication.entity.Place;
import com.mapapplication.mapapplication.entity.TripDailySchedule;
import com.mapapplication.mapapplication.entity.TripSchedule;
import com.mapapplication.mapapplication.repository.PlaceRepository;
import com.mapapplication.mapapplication.repository.ScheduleRepository;
import com.mapapplication.mapapplication.repository.TripDailyScheduleRepository;
import com.mapapplication.mapapplication.service.PlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"4. Places"})
@RestController
@RequestMapping("/places")
public class PlaceController {
    private final PlaceRepository placeRepository;
    private final PlaceService placeService;
    private final TripDailyScheduleRepository tripDailyScheduleRepository;
    private final ScheduleRepository scheduleRepository;

    public PlaceController(PlaceRepository placeRepository, PlaceService placeService
            , TripDailyScheduleRepository tripDailyScheduleRepository, ScheduleRepository scheduleRepository) {
        this.placeRepository = placeRepository;
        this.placeService = placeService;
        this.tripDailyScheduleRepository = tripDailyScheduleRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @ApiOperation(value = "일일 여행장소 저장", notes = "일일 여행장소를 저장합니다.")
    @PostMapping(value = "/{parentId}", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> savePlaces(@PathVariable("parentId") Long parentId, @RequestBody List<PlaceDto> places,
                                                          HttpSession session) {
        Long scheduleId = tripDailyScheduleRepository.findById(parentId).get().getParent().getId();
        Long userId = (Long) session.getAttribute("userId");
        boolean isMatching = isUserIdMatching(userId, scheduleId);

        if (isMatching) {
            System.out.println("리다이렉트");
            placeService.savePlaces(parentId, places);
            Map<String, Object> response = new HashMap<>();
            response.put("redirectUrl", "/places/" + parentId);
            return ResponseEntity.ok().body(response);
        } else {
            System.out.println("에러페이지");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 변경된 컨트롤러
     * 리스트 조회
     *
     * @param parentId
     * @return
     */
    @ApiOperation(value = "일일 여행장소 목록 조회", notes = "일일 여행장소 목록을 조회합니다.")
    @GetMapping("/{parentId}")
    public ModelAndView getPlacesByParentId(@PathVariable("parentId") Long parentId,
                                            HttpSession session) {
        ModelAndView modelAndView= new ModelAndView();
        Long scheduleId = tripDailyScheduleRepository.findById(parentId).get().getParent().getId();
        System.out.println(scheduleId);
        Long userId = (Long) session.getAttribute("userId");
        boolean isMatching = isUserIdMatching(userId, scheduleId);

        if (isMatching) {
            List<Place> places = placeRepository.findByParentId(parentId);

            TripDailySchedule parent = tripDailyScheduleRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("부모 엔티티를 찾을 수 없음: " + parentId));

            modelAndView.addObject("places", places);
            modelAndView.addObject("daily", parent);
            modelAndView.setViewName("map");
        }else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    /**
     * 리스트 조회 (JSON 데이터)
     *
     * @param parentId
     * @return
     */
    @ApiOperation(value = "일일 여행장소 목록 JSON 데이터 조회", notes = "일일 여행장소 목록의 JSON 데이터를 조회합니다.")
    @GetMapping("/data/{parentId}")
    public ResponseEntity<List<Map<String, Object>>> getPlacesByParentIdData(@PathVariable("parentId") Long parentId) {
        try {
            List<Place> places = placeRepository.findByParentId(parentId);
            List<Map<String, Object>> result = new ArrayList<>();

            for (Place place : places) {
                Map<String, Object> placeMap = new HashMap<>();
                placeMap.put("id", place.getId());
                placeMap.put("name", place.getName());
                placeMap.put("formatted_phone_number", place.getPhoneNumber());
                placeMap.put("rating", place.getRating());
                placeMap.put("place_id", place.getPlaceId());
                placeMap.put("address", place.getAddress());
                placeMap.put("lat", place.getLatitude());
                placeMap.put("lng", place.getLongitude());
                result.add(placeMap);
            }

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //장소 하나 지우기
    @ApiOperation(value = "일일 여행장소 선택 삭제", notes = "일일 여행장소를 선택 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable("id") Long id,
                                              HttpSession session) {
        try {
            // 삭제할 장소 엔티티 가져오기
            Place place = placeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("삭제할 장소를 찾을 수 없음: " + id));

            // 삭제
            placeRepository.delete(place);

            return ResponseEntity.status(HttpStatus.OK).body("장소 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장소 삭제 실패");
        }
    }

    // 부모id를 받아서 연관된 전체 장소 지워서 초기화
    @ApiOperation(value = "일일 여행장소 전체 삭제", notes = "일일 여행장소를 전체 삭제합니다.")
    @DeleteMapping("/reset/{parentId}")
    public ResponseEntity<String> deletePlacesByParentId(@PathVariable("parentId") Long parentId,
                                                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        boolean isMatching = isUserIdMatching(userId, parentId);
        if(isMatching) {
            placeRepository.deleteByParentId(parentId);
            return ResponseEntity.ok("places 삭제 완료");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("places 삭제 실패");
        }
    }

    public boolean isUserIdMatching(Long userId, Long tripScheduleId) {
        TripSchedule tripSchedule = scheduleRepository.findById(tripScheduleId).orElse(null);
        return ((tripSchedule != null) && (tripSchedule.getUserId().equals(userId)));
    }


}

