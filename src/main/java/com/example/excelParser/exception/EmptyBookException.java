package com.example.excelParser.exception;

public class EmptyBookException extends RuntimeException {
    public EmptyBookException(String message) {
        super(message);
    }
}
