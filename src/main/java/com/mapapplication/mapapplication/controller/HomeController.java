package com.mapapplication.mapapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

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
