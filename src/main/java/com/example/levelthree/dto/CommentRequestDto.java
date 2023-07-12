package com.example.levelthree.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String contents;
    private Long postId;
}
