package ru.rtc.medline.application.domain.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String messageKey;

    private final Object[] args;

    protected BaseException(String messageKey, Object... args) {
        this.messageKey = messageKey;
        this.args = args;
    }

}
