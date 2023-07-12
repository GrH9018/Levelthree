package com.example.levelthree.controller;

import com.example.levelthree.dto.CommentRequestDto;
import com.example.levelthree.dto.CommentResponseDto;
import com.example.levelthree.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    //댓글 생성
    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest req){
        return commentService.createComment(requestDto, req);
    }


    //댓글 수정
    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest req) {
        return  commentService.updateComment(id, requestDto, req);
    }


    //댓글 삭제
//    @DeleteMapping("/comment/{id}")
//    public Long deleteComment(@PathVariable Long id, HttpServletRequest req) {
//        return commentService.deleteComment(id, req);
//    }


}
