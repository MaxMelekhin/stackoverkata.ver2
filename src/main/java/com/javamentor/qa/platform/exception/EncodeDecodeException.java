package com.javamentor.qa.platform.exception;

public class EncodeDecodeException extends RuntimeException {
    public EncodeDecodeException() {
    }
    public EncodeDecodeException(String message) {
        super(message);
    }

    public EncodeDecodeException(Throwable cause) {
        super(cause);
    }
}
