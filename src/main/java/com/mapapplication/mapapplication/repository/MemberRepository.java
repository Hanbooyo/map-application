package com.mapapplication.mapapplication.repository;


import com.mapapplication.mapapplication.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원정보 조회
    // SELECT * FROM Member WHERE member_email = :memberEmail
    Optional<Member> findByMemberEmail(String memberEmail);


}
