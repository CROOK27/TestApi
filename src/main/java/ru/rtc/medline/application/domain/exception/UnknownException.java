package ru.rtc.medline.application.domain.exception;

/**
 * Выбрасывается при неизвестной ошибке
 */
public class UnknownException extends BaseException {

    public UnknownException(Object... args) {
        super(ErrorMessageKeys.UNKNOWN_ERROR_KEY, args);
    }

}
