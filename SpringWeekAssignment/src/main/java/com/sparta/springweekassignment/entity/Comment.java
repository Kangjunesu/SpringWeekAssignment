package com.sparta.springweekassignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springweekassignment.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "Comments")
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @ManyToOne
    private User user;
//username 2번 들어감.

    @JsonIgnore
    @ManyToOne
    private Post post;

    public Comment(CommentRequestDto commentRequestDto, User user, Post post) {
        this.contents = commentRequestDto.getContents();
        this.username = user.getUsername();
        this.user = user;
        this.post = post;
    }

    public void updateComment(CommentRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }
}
