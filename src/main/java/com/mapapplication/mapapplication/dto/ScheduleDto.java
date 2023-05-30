package com.mapapplication.mapapplication.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@ApiModel(description = "Schedule Model")
@Getter @Setter
@RequiredArgsConstructor
public class ScheduleDto {
    @ApiModelProperty(value = "여행일정 아이디", dataType = "Long", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "여행일정 제목", dataType = "String", required = true, example = "title")
    private String title;

    @ApiModelProperty(value = "여행일정 시작날짜", dataType = "LocalDate", required = true, example = "2023-06-01")
    private LocalDate startDate;

    @ApiModelProperty(value = "여행일정 종료날짜", dataType = "LocalDate", required = true, example = "2023-06-02")
    private LocalDate endDate;

    // Ddte대신에 LocalDate를 사용하면 불필요한 시간 및 타임존 정보를 제외하고 간결하게 날짜를 다룰 수 있습니다.

}

