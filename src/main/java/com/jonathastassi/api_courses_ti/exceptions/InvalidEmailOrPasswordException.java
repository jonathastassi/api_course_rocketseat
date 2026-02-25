package com.jonathastassi.api_courses_ti.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException {
    public InvalidEmailOrPasswordException() {
        super("Invalid email or password");
    }
}
