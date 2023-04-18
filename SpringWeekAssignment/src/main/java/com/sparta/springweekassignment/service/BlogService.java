package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.BlogRequestDto;
import com.sparta.springweekassignment.entity.Blog;
import com.sparta.springweekassignment.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    @Transactional
    public Blog createBlog(BlogRequestDto requestDto) {
        Blog blog = new Blog(requestDto);
        blogRepository.save(blog);
        return blog;
    }


}
