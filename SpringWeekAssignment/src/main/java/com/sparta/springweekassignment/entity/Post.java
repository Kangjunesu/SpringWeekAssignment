package com.sparta.springweekassignment.entity;

import com.sparta.springweekassignment.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //PK=ID
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    @ManyToOne  //User와 다대일 관계
    @JoinColumn(name = "user_id")  //@JoinColumn : 외래키 설정
    private User user;

    //하나의 게시물에 여러 댓글
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) //cascade : 게시글 삭제시 관련 댓글도 삭제
    @OrderBy("createdAt DESC")    // 댓글 목록 생성 시간 내림 차순
    private List<Comment> commentList = new ArrayList<>();



    public Post(PostRequestDto requestDto, User user) {
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = requestDto.getUsername();
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = requestDto.getUsername();
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);  // 이 코드의 용도?
    }
}
