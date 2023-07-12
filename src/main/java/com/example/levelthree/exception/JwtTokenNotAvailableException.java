package com.example.levelthree.exception;

public class JwtTokenNotAvailableException extends RuntimeException{
    public JwtTokenNotAvailableException(String message) {
        super(message);
    }
}
