package com.sparta.springweekassignment.controller;

import com.sparta.springweekassignment.dto.CommentRequestDto;
import com.sparta.springweekassignment.dto.CommentResponseDto;
import com.sparta.springweekassignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/{postId}")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request){
        return commentService.createComment(postId, commentRequestDto, request);
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return CommentService.updateComment(commentId, requestDto, request);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId, HttpServletRequest request){
        return commentService.deleteComment(commentId, request);
    }

}
