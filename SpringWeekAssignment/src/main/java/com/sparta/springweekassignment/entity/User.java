package com.sparta.springweekassignment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")  //DB의 "users" 테이블과 매핑
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)  //열거형 값을 문자열로 저장
    private UserRoleEnum role;  // 사용자의 권한 정보를 관리하는 열거형(Enum) 타입

//    @OneToMany
//    List<Folder> folders = new ArrayList<>();  //하나의 사용자가 여러개 폴더 가질 수 있음.

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}