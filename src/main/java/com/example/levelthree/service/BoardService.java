package com.example.levelthree.service;

import com.example.levelthree.dto.BoardRequestDto;
import com.example.levelthree.dto.BoardResponseDto;
import com.example.levelthree.entity.Board;
import com.example.levelthree.entity.User;
import com.example.levelthree.entity.UserRoleEnum;
import com.example.levelthree.exception.JwtTokenNotAvailableException;
import com.example.levelthree.jwt.JwtUtil;
import com.example.levelthree.repository.BoardRepository;
import com.example.levelthree.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest req) {
        String token = auth(req);
        String username = getUsername(token);
        User user = findUserByUsername(username);


        Board board = new Board(requestDto, username, user);
        Board saveBoard = boardRepository.save(board);
        return new BoardResponseDto(saveBoard);
    }

    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        return new BoardResponseDto(board);
    }

    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest req) {
        String token = auth(req);
        Board board = findBoard(id);
        String username = getUsername(token);

        if (findUserByUsername(username).getRole().equals(UserRoleEnum.USER)) {
            checkUsername(board, token);
        }

        board.update(requestDto);
        return new BoardResponseDto(board);
    }



    public void deleteBoard(Long id, HttpServletRequest req) {
        String token = auth(req);
        Board board = findBoard(id);
        String username = getUsername(token);

        if (findUserByUsername(username).getRole().equals(UserRoleEnum.USER)) {
            checkUsername(board, token);
        }

        boardRepository.delete(board);
    }


    public Board findBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        return optionalBoard.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다.")
        );
    }

    private String getUsername(String token) {
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();
        return username;
    }

    private void checkUsername (Board board, String token) {
        String username = getUsername(token);
        if (!board.getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
    }

    private String auth(HttpServletRequest req) {
        String tokenValue = req.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        String token = jwtUtil.substringToken(tokenValue);
        if(!jwtUtil.validateToken(token)) {
            throw new JwtTokenNotAvailableException("토큰이 유효하지 않습니다.");
        }

        return token;
    }

}