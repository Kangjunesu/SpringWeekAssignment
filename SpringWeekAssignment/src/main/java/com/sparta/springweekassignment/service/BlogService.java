package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.BlogRequestDto;
import com.sparta.springweekassignment.dto.BlogResponseDto;
import com.sparta.springweekassignment.dto.UpdateResponseDto;
import com.sparta.springweekassignment.entity.Blog;
import com.sparta.springweekassignment.entity.User;
import com.sparta.springweekassignment.jwt.JwtUtil;
import com.sparta.springweekassignment.repository.BlogRepository;
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
public class BlogService {


    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto requestDto,  HttpServletRequest request) {

        User user = checkJwtToken(request);

        Blog blog = new Blog(requestDto);
        blog.setUsername(user.getUsername());
        blogRepository.saveAndFlush(blog);
        return new BlogResponseDto(blog);
    }

    //모든 리스트 가져오기
    @Transactional(readOnly = true)
    public List<BlogResponseDto> getblogs() {
        List<Blog> blogs = blogRepository.findAllByOrderByModifiedAtDesc();
        List<BlogResponseDto> blogResponseDtos = new ArrayList<>();
        for (Blog blog : blogs) {
            blogResponseDtos.add(new BlogResponseDto(blog));
        }
        return blogResponseDtos;
    }

    //id에 해당하는 게시물 반환
    @Transactional(readOnly = true)  //(readOnly = true) 읽기에 특화된 옵션.
    public BlogResponseDto getblog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재하지 않습니다.")
        );
        return new BlogResponseDto(blog);
    }

    //업데이트
    @Transactional
    public UpdateResponseDto update(Long id, BlogRequestDto requestDto, HttpServletRequest request) {
        User user = checkJwtToken(request);

        Optional<Blog> optionalBlog = blogRepository.findById(id);

        if (!optionalBlog.isPresent()) {
            throw new IllegalArgumentException("해당 글이 존재하지 않습니다.");
        }
        Blog blog = optionalBlog.get();

        blog.update(requestDto);
        return new UpdateResponseDto(blog);
    }

    @Transactional
    public String deleteBlog(Long id, HttpServletRequest request) {
        User user = checkJwtToken(request);

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글이 존재하지 않습니다.")
        );

        blogRepository.delete(blog);  //클라에서 받아온 id값을 파라미터로 -> 어떤 메모를 삭제할지
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
