package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.BlogRequestDto;
import com.sparta.springweekassignment.entity.Blog;
import com.sparta.springweekassignment.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)  //(readOnly = true) 읽기에 특화된 옵션.
    public List<Blog> getblogs() {
        return blogRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public Long update(Long id, BlogRequestDto requestDto) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);

        if (!optionalBlog.isPresent()) {
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }
        Blog blog = optionalBlog.get();
        // 비밀번호 확인
        String inputPassword = requestDto.getPassword();
        String storedPassword = blog.getPassword();

        if (!storedPassword.equals(inputPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        blog.update(requestDto);
        return blog.getId();
    }

    @Transactional
    public Long deleteBlog(Long id,  BlogRequestDto requestDto) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        Blog blog = optionalBlog.get();

        String inputPassword = requestDto.getPassword();
        String storedPassword = blog.getPassword();

        if (!storedPassword.equals(inputPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        blogRepository.deleteById(id);  //클라에서 받아온 id값을 파라미터로 -> 어떤 메모를 삭제할지
        return id;
    }


}
