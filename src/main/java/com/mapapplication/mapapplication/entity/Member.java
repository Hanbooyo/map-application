package com.mapapplication.mapapplication.entity;


import com.mapapplication.mapapplication.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberEmail;

    @Column
    private String memberName;

    @Column
    private String memberPassword;

    public static Member toMemberEntity(MemberDto memberDTO) {
        Member member = new Member();
        member.setMemberEmail(memberDTO.getMemberEmail());
        member.setMemberName(memberDTO.getMemberName());
        member.setMemberPassword(memberDTO.getMemberPassword());
        return member;
    }


}
