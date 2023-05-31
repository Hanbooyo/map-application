package com.mapapplication.mapapplication.repository;

import com.mapapplication.mapapplication.entity.TripDailySchedule;
import com.mapapplication.mapapplication.entity.TripSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<TripSchedule, Long> {
    // 모든 여행 일정 조회
    // SELECT * FROM TripSchedule
    List<TripSchedule> findAll();

    // 부모 ID로 여행 일정 조회
    @Query("SELECT tds FROM TripDailySchedule tds WHERE tds.parent.id = :parentId")
    List<TripDailySchedule> findByParentId(@Param("parentId") Long parentId);

    // 사용자 ID로 여행 일정 조회
    // SELECT * FROM TripSchedule WHERE user_id = :userId
    List<TripSchedule> findByUserId(Long userId);

}

