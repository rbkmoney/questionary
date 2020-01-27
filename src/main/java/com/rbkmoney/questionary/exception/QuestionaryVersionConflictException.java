package com.rbkmoney.questionary.exception;

public class QuestionaryVersionConflictException extends RuntimeException {

    public QuestionaryVersionConflictException() {
    }

    public QuestionaryVersionConflictException(String message) {
        super(message);
    }

    public QuestionaryVersionConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionaryVersionConflictException(Throwable cause) {
        super(cause);
    }
}
