package com.mapapplication.mapapplication.dto;

import com.mapapplication.mapapplication.entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String memberEmail;
    private String memberName;
    private String memberPassword;

    public static MemberDto toMemberDTO(Member member) {
        MemberDto memberDTO = new MemberDto();
        memberDTO.setId(member.getId());
        memberDTO.setMemberEmail(member.getMemberEmail());
        memberDTO.setMemberName(member.getMemberName());
        memberDTO.setMemberPassword(member.getMemberPassword());
        return memberDTO;
    }

}
