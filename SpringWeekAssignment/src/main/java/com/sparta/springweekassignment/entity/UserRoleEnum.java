package com.sparta.springweekassignment.entity;

public enum UserRoleEnum {
    USER,  // 사용자 권한
    ADMIN  // 관리자 권한
}// 각각 로그인 할때는 USER, ADMIN이라고 표시가 되고 회원가입시 미리 서버쪽에 정의되있는 가지고 있는 코드를
//입력해야 ADMIN 으로 등록.
