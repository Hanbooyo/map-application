package com.mapapplication.mapapplication.dto;

import com.mapapplication.mapapplication.entity.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(description = "Member Model")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @ApiModelProperty(value = "회원 아이디", dataType = "Long", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "회원 이메일", dataType = "String", required = true, example = "test@email.com")
    private String memberEmail;

    @ApiModelProperty(value = "회원 이름", dataType = "String", required = true, example = "test")
    private String memberName;

    @ApiModelProperty(value = "회원 비밀번호", dataType = "String", required = true, example = "test1234")
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
