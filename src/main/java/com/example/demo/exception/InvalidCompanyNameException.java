package com.example.demo.exception;

public class InvalidCompanyNameException extends RuntimeException {
    public InvalidCompanyNameException(String message) {
        super(message);
    }
}
