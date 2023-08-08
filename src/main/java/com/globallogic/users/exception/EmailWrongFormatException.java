package com.globallogic.users.exception;

public class EmailWrongFormatException extends Exception {

    private String message;

    public EmailWrongFormatException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
