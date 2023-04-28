package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.LoginRequestDto;
import com.sparta.springweekassignment.dto.SignupRequestDto;
import com.sparta.springweekassignment.entity.User;
import com.sparta.springweekassignment.entity.UserRoleEnum;
import com.sparta.springweekassignment.jwt.JwtUtil;
import com.sparta.springweekassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;  	//의존성주입

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional   //회원가입 처리 메서드.
    public String signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다."+ HttpStatus.BAD_REQUEST);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {  //이부분 isAdmin, getAdminToken 메서드가 DTO에 없는데 어떻게 작동?
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

            User user = new User(username, password ,role);
            userRepository.save(user);
            return "회원가입 성공";
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        //LoginRequestDto : username, password

        // 사용자 확인 -> DB에서 사용자를 조회하고
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."+ HttpStatus.BAD_REQUEST)
        );
        //-> 사용자가 있다면 비밀번호가 일치하는지 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다."+ HttpStatus.BAD_REQUEST);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
    // 비밀번호가 일치하면 JwtUtil 클래스를 사용하여 JWT 토큰을 생성하고, HTTP 응답 헤더에 인증 정보를 담아 클라이언트에게 전달
    // response.addHeader()를 사용하여 HTTP 응답 헤더에 JWT 토큰을 추가 -> 토큰값은 클라에서 인증정보로 사용.
}
