package com.example.levelthree.entity;

import com.example.levelthree.dto.CommentRequestDto;
import jakarta.persistence.*;
import jdk.jfr.Timespan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "contents", nullable = false,  length = 500)
    private String contents;

    public  Comment(CommentRequestDto requestDto, String username){
        this.username = username;
        this.contents = requestDto.getContents();
    }

    public void update(CommentRequestDto commentRequestDto){

    }

    //public Board(BoardRequestDto requestDto, String username) {
    //        this.title = requestDto.getTitle();
    //        this.username = username;
    //        this.contents = requestDto.getContents();
    //    }
    //
    //    public void update(BoardRequestDto requestDto) {
    //        this.title = requestDto.getTitle();;
    //        this.contents = requestDto.getContents();
    //    }
}
