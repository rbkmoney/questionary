package com.rbkmoney.questionary.exception;

public class QuestionaryNotValidException extends RuntimeException {

    public QuestionaryNotValidException() {
    }

    public QuestionaryNotValidException(String message) {
        super(message);
    }

    public QuestionaryNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionaryNotValidException(Throwable cause) {
        super(cause);
    }
}
