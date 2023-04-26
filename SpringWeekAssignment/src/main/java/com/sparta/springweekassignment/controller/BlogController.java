package com.sparta.springweekassignment.controller;

import com.sparta.springweekassignment.dto.BlogRequestDto;
import com.sparta.springweekassignment.entity.Blog;
import com.sparta.springweekassignment.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    //Create
    @PostMapping("/blogs")
    public Blog createBlog(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.createBlog(requestDto, request);
    }

    @GetMapping("/blogs")
    public List<Blog> getblogs() {
        return blogService.getblogs();
    }

    //업데이트 = id, 비밀번호 확인하여 ->제목 내용 이름 수정
    @PutMapping("/blogs/{id}")
    public Long updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.update(id, requestDto);
    }

    @DeleteMapping("/blogs/{id}")
    public Long deleteBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.deleteBlog(id, requestDto);
    }

}
