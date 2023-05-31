package com.mapapplication.mapapplication.controller;


import com.mapapplication.mapapplication.dto.MemberDto;
import com.mapapplication.mapapplication.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Api(tags = {"2. Member"})
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @GetMapping("/join")
    @ApiOperation(value = "회원가입 양식", notes = "회원가입 양식을 표시합니다.")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    @ApiOperation(value = "회원가입", notes = "회원가입을 합니다.")
    public String join(@ModelAttribute MemberDto memberDTO) {
        memberService.join(memberDTO);
        return "login";
    }

    // 회원탈퇴
    @DeleteMapping ("/delete/{id}")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴를 합니다.")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/login";
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 합니다.")
    public String login(@ModelAttribute MemberDto memberDTO, HttpSession session, HttpServletResponse response, Model model) {
        MemberDto loginResult = memberService.login(memberDTO);

        if (loginResult != null) {
            // 로그인 성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("userId", loginResult.getId());

            // 리다이렉트할 URL 생성
            String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/schedules").build().toUriString();

            // 클라이언트에게 리다이렉션 응답을 보낼 때 세션 ID가 노출되지 않도록 설정
            response.setHeader("Location", redirectUrl);
            response.setStatus(HttpStatus.SEE_OTHER.value());

            return null; // null을 반환하여 뷰를 렌더링하지 않도록 설정
        } else {
            // 로그인 실패
            model.addAttribute("message", "이메일 또는 패스워드를 확인해주세요");
            return "login"; // 실패 시에는 로그인 페이지를 다시 렌더링하도록 설정
        }
    }



    // 로그아웃
    @GetMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃을 합니다.")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 이메일 중복체크
    @PostMapping("/email-check")
    @ApiOperation(value = "이메일 중복체크", notes = "이메일 중복체크를 합니다.")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }

}
