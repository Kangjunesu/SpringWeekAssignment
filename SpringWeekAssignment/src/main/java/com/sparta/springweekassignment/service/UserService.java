package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.LoginRequestDto;
import com.sparta.springweekassignment.dto.SignupRequestDto;
import com.sparta.springweekassignment.entity.User;
import com.sparta.springweekassignment.entity.UserRoleEnum;
import com.sparta.springweekassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final com.sparta.springweekassignment.jwt.JwtUtil jwtUtil;  	//의존성주입
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    //UserService 클래스 내에서만 사용하는 상수, 관리자 권한을 부여하기 위한 토큰 값

    @Transactional   //회원가입 처리 메서드.
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
        String email = signupRequestDto.getEmail();
        //dto : 유저네임, pw, email, 관리자 확인 boolean, 관리자인경우 토큰값

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;  //UserRoleEnum에는 User와 Admain
        if (signupRequestDto.isAdmin()) {  //isAdmin() 메서드를 사용 관리자 권한 여부를 확인
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                //.getAdminToken()을 사용하여 관리자 암호가져와서 ADMIN_TOKEN과 비교
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
        //회원가입 성공시 UserRepository의 save함수 호출하여 DB저장

    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        //LoginRequestDto : username, pw

        // 사용자 확인 -> DB에서 사용자를 조회하고
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호가 일치하는지 확인
        if(!user.getPassword().equals(password)){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(com.sparta.springweekassignment.jwt.JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
    // 비밀번호가 일치하면 JwtUtil 클래스를 사용하여 JWT 토큰을 생성하고, HTTP 응답 헤더에 인증 정보를 담아 클라이언트에게 전달
    // response.addHeader()를 사용하여 HTTP 응답 헤더에 JWT 토큰을 추가 -> 토큰값은 클라에서 인증정보로 사용.
}
