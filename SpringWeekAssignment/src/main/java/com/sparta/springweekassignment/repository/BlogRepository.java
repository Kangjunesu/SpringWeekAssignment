package com.sparta.springweekassignment.repository;

import com.sparta.springweekassignment.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
