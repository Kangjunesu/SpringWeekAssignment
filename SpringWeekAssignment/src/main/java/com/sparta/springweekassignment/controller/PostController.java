package com.sparta.springweekassignment.controller;

import com.sparta.springweekassignment.dto.PostRequestDto;
import com.sparta.springweekassignment.dto.PostResponseDto;
import com.sparta.springweekassignment.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //Create
    @PostMapping("/posts")
    public PostResponseDto createBlog(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }
    //전체 게시물 목록 READ.
    //제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    //작성 날짜 기준 내림차순으로 정렬하기
    @GetMapping("/posts")
    public List<PostResponseDto> getposts() { // Entity였는데 Dto로
        return postService.getposts();
    }

    //선택한 게시글 조회 API
    //선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기
    @GetMapping("/post/{id}")
    public PostResponseDto getpost(@PathVariable Long id, HttpServletRequest request) {
        return postService.getpost(id, request);
    }


    //업데이트 = id, 비밀번호 확인하여 ->제목 내용 이름 수정
    //토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
    //제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        return postService.update(id, requestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

}
