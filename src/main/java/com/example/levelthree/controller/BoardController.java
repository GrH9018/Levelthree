package com.example.levelthree.controller;

import com.example.levelthree.dto.BoardRequestDto;
import com.example.levelthree.dto.BoardResponseDto;
import com.example.levelthree.jwt.JwtUtil;
import com.example.levelthree.service.BoardService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    // 게시글 작성
    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest req) {

        return boardService.createBoard(requestDto, req);
    }

    // 전체 게시글 조회
    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    //  선택 게시글 조회
    @GetMapping("/boards/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 선택 게시글 수정
    @PutMapping("/boards/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest req) {
        return boardService.updateBoard(id, requestDto, req);
    }

    // 선택 게시글 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id, HttpServletRequest req) {
        boardService.deleteBoard(id, req);
        return ResponseEntity.ok()
                .body("게시글 삭제 성공");
    }

}