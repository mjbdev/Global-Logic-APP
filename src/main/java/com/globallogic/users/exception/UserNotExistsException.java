package com.globallogic.users.exception;

public class UserNotExistsException extends Exception {

    private String message;

    public UserNotExistsException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
