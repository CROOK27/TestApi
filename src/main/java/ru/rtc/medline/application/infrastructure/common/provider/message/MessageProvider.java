package ru.rtc.medline.application.infrastructure.common.provider.message;

import org.springframework.context.MessageSource;

public interface MessageProvider {

    String getMessage(String key, Object... args);

    default String getMessage(String prefix, Enum<?> key, Object... args) {
        return getMessage(toMessageKey(prefix, key), args);
    }

    MessageSource getMessageSource();

    default String toMessageKey(String prefix, Enum<?> key) {
        return prefix + key.name().toLowerCase().replace("_", ".");
    }

}
