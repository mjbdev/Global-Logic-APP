package com.globallogic.users.exception;

public class PasswordWrongFormatException extends Exception {

    private String message;

    public PasswordWrongFormatException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
