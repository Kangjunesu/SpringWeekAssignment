package com.sparta.springweekassignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto { //댓글 생성 요청에 필요한 최소한의 데이터를 담는 DTO
    private String contents;  // 댓글내용

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }
}
