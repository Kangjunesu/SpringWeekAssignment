package com.sparta.springweekassignment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private boolean admin = false;  //관리자인지 아닌지 확인
    private String adminToken = "";// 관리자인경우 토큰값
}