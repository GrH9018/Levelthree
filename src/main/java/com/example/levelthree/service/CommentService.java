package com.example.levelthree.service;

import com.example.levelthree.dto.CommentRequestDto;
import com.example.levelthree.dto.CommentResponseDto;
import com.example.levelthree.entity.Board;
import com.example.levelthree.entity.Comment;
import com.example.levelthree.entity.User;
import com.example.levelthree.entity.UserRoleEnum;
import com.example.levelthree.jwt.JwtUtil;
import com.example.levelthree.repository.BoardRepository;
import com.example.levelthree.repository.CommentRepository;
import com.example.levelthree.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor

public class CommentService {

    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, HttpServletRequest req){
        String token = auth(req);
        String username = getUsername(token);
        Board board = findBoard(requestDto.getPostId());
        User user = findUserByUsername(username);

        Comment comment = new Comment(requestDto, username, board, user);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest req) {
        String token = auth(req);
        Comment comment = findComment(id);
        String username = getUsername(token);

        if (findUserByUsername(username).getRole().equals(UserRoleEnum.USER)) {
            checkUsername(comment, token);
        }

        findBoard(requestDto.getPostId());

        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long id, HttpServletRequest req) {
        String token = auth(req);
        Comment comment = findComment(id);
        String username = getUsername(token);

        if (findUserByUsername(username).getRole().equals(UserRoleEnum.USER)) {
            checkUsername(comment, token);
        }

        commentRepository.delete(comment);
    }


    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
    }

    // 댓글 찾는 매서드
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글이 존재하지 않습니다.")
        );
    }

    // 게시글 찾는 매서드
    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다.")
        );
    }


    private String auth(HttpServletRequest req) {
        String tokenValue = req.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        String token = jwtUtil.substringToken(tokenValue);
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰 에러");
        }
        return token;
    }

    private void checkUsername (Comment comment, String token) {
        String username = getUsername(token);
        if (!comment.getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    private String getUsername(String token) {
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();
        return username;
    }
}