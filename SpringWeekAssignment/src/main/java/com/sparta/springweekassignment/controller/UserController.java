package com.sparta.springweekassignment.controller;

import com.sparta.springweekassignment.dto.LoginRequestDto;
import com.sparta.springweekassignment.dto.SignupRequestDto;
import com.sparta.springweekassignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public String signup(SignupRequestDto signupRequestDto) {
        String userName = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        System.out.println(userName);
        System.out.println(password);
        return userService.signup(signupRequestDto);
//        return "success";
    }//dto : 유저네임, pw, email, 관리자 확인 boolean, 관리자인경우 토큰값

    @ResponseBody
    @PostMapping("/login")
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {  //리스폰스바디와 리퀘스트 바디가 다 적용
        userService.login(loginRequestDto, response);
        return "success";
    } //LoginRequestDto : username, pw
    // HttpServletResponse 객체를 사용하여 JWT 토큰 값을 클라이언트에게 전달
}



