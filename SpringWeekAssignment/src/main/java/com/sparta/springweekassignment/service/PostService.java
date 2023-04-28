package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.PostRequestDto;
import com.sparta.springweekassignment.dto.PostResponseDto;
import com.sparta.springweekassignment.dto.UpdateResponseDto;
import com.sparta.springweekassignment.entity.Post;
import com.sparta.springweekassignment.entity.User;
import com.sparta.springweekassignment.jwt.JwtUtil;
import com.sparta.springweekassignment.repository.PostRepository;
import com.sparta.springweekassignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {

        User user = checkJwtToken(request);

        Post post = new Post(requestDto);
        post.setUsername(user.getUsername());
        postRepository.saveAndFlush(post);
        return new PostResponseDto(post);
    }

    //모든 리스트 가져오기
    @Transactional(readOnly = true)
    public List<PostResponseDto> getposts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            postResponseDtos.add(new PostResponseDto(post));
        }
        return postResponseDtos;
    }

    //id에 해당하는 게시물 반환
    @Transactional(readOnly = true)  //(readOnly = true) 읽기에 특화된 옵션.
    public PostResponseDto getpost(Long id, HttpServletRequest request) {
        User user = checkJwtToken(request);

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );

        // 요청한 사용자의 게시글인지 확인
        if (!post.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("해당 글에 대한 접근 권한이 없습니다.");
        }
        return new PostResponseDto(post);
    }

    //업데이트
    @Transactional
    public UpdateResponseDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        User user = checkJwtToken(request);

        Optional<Post> optionalPost = postRepository.findById(id);

        if (!optionalPost.isPresent()) {
            throw new IllegalArgumentException("해당 글이 존재하지 않습니다.");
        }
        Post post = optionalPost.get();

        // 작성자와 요청한 사용자가 일치하는지 확인
        if (!post.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        post.update(requestDto);
        return new UpdateResponseDto(post);
    }



    //삭제
    @Transactional
    public String deletePost(Long id, HttpServletRequest request) {
        User user = checkJwtToken(request);

        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        // 작성자와 요청한 사용자가 일치하는지 확인
        if (!post.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);  //클라에서 받아온 id값을 파라미터로 -> 어떤 메모를 삭제할지
        return "게시글을 삭제했습니다.";
    }
    
    public User checkJwtToken(HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 접근 가능
        if (token != null) {

            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;
        }
        return null;
    }


}
