package com.example.gccoffee.exception;

public class InvalidEmailPatternException extends RuntimeException {
    public InvalidEmailPatternException(String message) {
        super(message);
    }
}
