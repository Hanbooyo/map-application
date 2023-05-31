package com.mapapplication.mapapplication.repository;

import com.mapapplication.mapapplication.entity.TripDailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripDailyScheduleRepository extends JpaRepository<TripDailySchedule, Long> {

    // 부모 ID로 여행 일정 조회 후 날짜(오름차순)로 정렬
    // SELECT * FROM TripDailySchedule WHERE parent_id = :parentId ORDER BY date ASC
    List<TripDailySchedule> findByParentIdOrderByDateAsc(Long parentId);

    // 부모 ID로 여행 일정 조회
    // SELECT * FROM TripDailySchedule WHERE parent_id = :parentId
    List<TripDailySchedule> findByParentId(Long parentId);

}

