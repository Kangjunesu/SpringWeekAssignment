package com.sparta.springweekassignment.service;

import com.sparta.springweekassignment.dto.CommentRequestDto;
import com.sparta.springweekassignment.dto.CommentResponseDto;
import com.sparta.springweekassignment.entity.Comment;
import com.sparta.springweekassignment.entity.Post;
import com.sparta.springweekassignment.entity.User;
import com.sparta.springweekassignment.entity.UserRoleEnum;
import com.sparta.springweekassignment.jwt.JwtUtil;
import com.sparta.springweekassignment.repository.CommentRepository;
import com.sparta.springweekassignment.repository.PostRepository;
import com.sparta.springweekassignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

      //Create
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Claims claims = validateAndGetClaims(request);
        User user = getUserFromClaims(claims);
        //HttpServletRequest로부터 토큰을 검증하고, 토큰에 포함된 Claims 객체를 반환
        //Claims 객체를 사용하여 사용자 정보를 조회하고 User 객체를 반환

        Post post = postRepository.findById(postId).orElseThrow( // 게시글 ID로 게시글(Post 객체)을 조회
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Comment comment = new Comment(commentRequestDto, user, post);//생성자에 Cmt~Dto, User, Post 객체를 전달-> 댓글의 내용, 작성자, 게시글 정보를 초기화
        post.addComment(comment);  // Post 객체에 Comment 객체를 추가. 게시글과 댓글 간의 관계가 설정
        comment.setUser(user);  //Comment 객체에 작성자 정보(User 객체)를 설정 + 연관관계 설정

        commentRepository.save(comment);  // Comment 객체를 저장. 데이터베이스에 새로운 댓글이 저장
        return new CommentResponseDto(comment); //클라이언트에게 새로 생성된 댓글에 대한 응답을 제공
    }

    //Update
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = validateAndGetClaims(request);
        User user = getUserFromClaims(claims);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        checkPermission(claims, comment);
        //Claims 객체와 Comment 객체를 인자로 받아 사용자가 해당 댓글에 권한이 있는지 확인, 권한이 없을때 예외

        comment.updateComment(requestDto, user);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public String deleteComment(Long commentId, HttpServletRequest request){
        Claims claims = validateAndGetClaims(request);
        User user = getUserFromClaims(claims);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        checkPermission(claims, comment);
        commentRepository.deleteById(commentId);
        return "삭제 완료";
    }


    //HttpServletRequest를 인자로 받아 토큰을 검증하고,
    //토큰에 포함된 Claims 객체를 반환. 토큰이 없거나 유효하지 않은 경우 예외를 발생
    private Claims validateAndGetClaims(HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        if (token != null && jwtUtil.validateToken(token)) {
            return jwtUtil.getUserInfoFromToken(token);
        }
        throw new IllegalArgumentException("Token Error");
    }

    //Claims 객체를 인자로 받아 사용자 정보를 조회하고, User 객체를 반환. 사용자가 존재하지 않는 경우 예외를 발생
    private User getUserFromClaims(Claims claims) {
        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
    }

    //Claims 객체와 Comment 객체를 인자로 받아 사용자가 해당 댓글에 대한 권한이 있는지 확인, 권한이 없을때 예외
    private void checkPermission(Claims claims, Comment comment) {
        String name = claims.getSubject();
        UserRoleEnum role = comment.getUser().getRole();

        if (!name.equals(comment.getUsername()) && role != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("권한이 없습니다." + HttpStatus.BAD_REQUEST);
        }
    }
}
