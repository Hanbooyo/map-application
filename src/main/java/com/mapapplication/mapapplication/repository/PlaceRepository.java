package com.mapapplication.mapapplication.repository;

import com.mapapplication.mapapplication.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    // 부모 ID로 장소 정보 조회
    // SELECT * FROM Place WHERE parent_id = :parentId
    List<Place> findByParentId(Long parentId);

    // 부모 ID로 장소 정보 삭제
    // DELETE FROM Place WHERE parent_id = :parentId
    void deleteByParentId(Long parentId);

    // 여러 부모 ID로 장소 정보 삭제
    // DELETE FROM Place WHERE parent_id IN (:parentIds)
    void deleteByParentIdIn(List<Long> parentIds);

}
