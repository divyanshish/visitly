package com.visitly.myproject.exception;


public class DuplicateRoleException extends RuntimeException {
    public DuplicateRoleException(String message) {
        super(message);
    }

    public DuplicateRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}