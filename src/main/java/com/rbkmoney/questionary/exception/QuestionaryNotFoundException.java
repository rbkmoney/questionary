package com.rbkmoney.questionary.exception;

public class QuestionaryNotFoundException extends RuntimeException {

    public QuestionaryNotFoundException() {
    }

    public QuestionaryNotFoundException(String message) {
        super(message);
    }

    public QuestionaryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionaryNotFoundException(Throwable cause) {
        super(cause);
    }
}
