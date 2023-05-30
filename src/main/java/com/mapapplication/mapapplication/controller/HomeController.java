package com.mapapplication.mapapplication.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;

@Api(tags = {"1. Home"})
@Controller
public class HomeController {

    @ApiOperation(value = "홈, 로그인 양식", notes = "홈 페이지를 표시합니다. 최초 진입 시 로그인 양식을 표시합니다.")
    @GetMapping("/")
    public ModelAndView home(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        if (session.getAttribute("userId") != null) {
            // 세션에 userId가 있는 경우, 리다이렉트할 URL 생성
            String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/schedules").build().toUriString();
            modelAndView.setViewName("redirect:" + redirectUrl);
        } else {
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }

}
