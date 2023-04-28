package com.sparta.springweekassignment.controller;

import com.sparta.springweekassignment.dto.LoginRequestDto;
import com.sparta.springweekassignment.dto.SignupRequestDto;
import com.sparta.springweekassignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @ResponseBody
    @PostMapping("/login")
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {  //리스폰스바디와 리퀘스트 바디가 다 적용
        userService.login(loginRequestDto, response);
        return "success";
    } //LoginRequestDto : username, pw
    // HttpServletResponse 객체를 사용하여 JWT 토큰 값을 클라이언트에게 전달
}



