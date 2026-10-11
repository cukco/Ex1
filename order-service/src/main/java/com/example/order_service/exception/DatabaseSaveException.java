package com.example.order_service.exception;

public class DatabaseSaveException extends RuntimeException {
    public DatabaseSaveException(String message) {
        super(message);
    }

    public DatabaseSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
