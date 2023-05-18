package com.mapapplication.mapapplication.controller;

import com.mapapplication.mapapplication.dto.PlaceDto;
import com.mapapplication.mapapplication.entity.Place;
import com.mapapplication.mapapplication.entity.TripDailySchedule;
import com.mapapplication.mapapplication.repository.PlaceRepository;
import com.mapapplication.mapapplication.repository.TripDailyScheduleRepository;
import com.mapapplication.mapapplication.service.PlaceService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {
    private final PlaceRepository placeRepository;
    private final PlaceService placeService;

    public PlaceController(PlaceRepository placeRepository, PlaceService placeService) {
        this.placeRepository = placeRepository;
        this.placeService = placeService;
    }

    @PostMapping("/save/{parentId}")
    public String savePlaces(@PathVariable("parentId") Long parentId, @RequestBody List<PlaceDto> places) {
        System.out.println("savePlaces 진입");
        try {
            placeService.savePlaces(parentId, places);
            return "redirect:/places/lists/" + parentId;  // 저장 성공 시 리다이렉트
        } catch (EntityNotFoundException e) {
            // Parent가 존재하지 않을 경우 처리할 예외 핸들링 로직
            // 예: 에러 메시지 출력, 다른 경로로 리다이렉트 등
            return "error-page";
        }
    }

    @GetMapping("/lists/{parentId}")
    public ModelAndView getPlacesByParentId(@PathVariable("parentId") Long parentId) {
        List<Place> places = placeRepository.findByParentId(parentId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("places", places);
        modelAndView.addObject("parentId", parentId);
        modelAndView.setViewName("index");

        return modelAndView;
    }

    //장소 하나 지우기
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable("id") Long id) {
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
    @DeleteMapping("/refresh/{parentId}")
    public ResponseEntity<String> deletePlacesByParentId(@PathVariable("parentId") Long parentId) {
        try {
            placeRepository.deleteByParentId(parentId);
            return ResponseEntity.ok("places 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("places 삭제 실패");
        }
    }



    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("calendar");

        return modelAndView;
    }

}

