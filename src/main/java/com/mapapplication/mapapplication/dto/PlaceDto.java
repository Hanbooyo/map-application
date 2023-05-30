package com.mapapplication.mapapplication.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ApiModel(description = "Place Model")
@Getter
@Setter
@RequiredArgsConstructor
public class PlaceDto {
    @ApiModelProperty(value = "여행장소 식별 아이디", dataType = "String", required = true, example = "ChIJKwjLMvOifDURqPAMQqxwK-k")
    private String placeId;

    @ApiModelProperty(value = "여행장소 위도", dataType = "Double", required = true, example = "37.5666612")
    private Double latitude;

    @ApiModelProperty(value = "여행장소 경도", dataType = "Double", required = true, example = "126.9783785")
    private Double longitude;

    @ApiModelProperty(value = "여행장소 이름", dataType = "String", required = true, example = "서울특별시청")
    private String name;

    @ApiModelProperty(value = "여행장소 평점", dataType = "Double", required = false, example = "4.3")
    private Double rating;

    @ApiModelProperty(value = "여행장소 전화번호", dataType = "String", required = false, example = "02-731-2120")
    private String phoneNumber;

    @ApiModelProperty(value = "여행장소 주소", dataType = "String", required = true, example = "대한민국 서울특별시 중구 세종대로 110")
    private String address;

    @ApiModelProperty(value = "여행일정 아이디", dataType = "Long", required = true, example = "1")
    private Long parentId;

    // 생성자, getter, setter, 기타 메서드
}
