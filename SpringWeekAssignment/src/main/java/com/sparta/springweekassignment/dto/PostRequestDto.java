package com.sparta.springweekassignment.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {  //객체로 이동하는 데이터 - 제목, 내용, 작성자명, 비밀번호..?, (작성 날짜),
    private Long id;
    private String title;
    private String content;
    private String username;
//    private String password;
}
