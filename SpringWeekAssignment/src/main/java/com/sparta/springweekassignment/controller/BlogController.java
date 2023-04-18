package com.sparta.springweekassignment.controller;

import com.sparta.springweekassignment.dto.BlogRequestDto;
import com.sparta.springweekassignment.entity.Blog;
import com.sparta.springweekassignment.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    //Create
    @PostMapping("/api/blogs")
    public Blog createBlog(@RequestBody BlogRequestDto requestDto) {
        return blogService.createBlog(requestDto);
    }



}
