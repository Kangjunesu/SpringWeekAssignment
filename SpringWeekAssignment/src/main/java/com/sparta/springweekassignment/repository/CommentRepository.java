package com.sparta.springweekassignment.repository;

import com.sparta.springweekassignment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
