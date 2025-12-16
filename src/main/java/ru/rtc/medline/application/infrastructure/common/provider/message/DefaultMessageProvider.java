package ru.rtc.medline.application.infrastructure.common.provider.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DefaultMessageProvider implements MessageProvider {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String key, Object... args) {
        Locale locale = getLocale();

        return getMessageSource().getMessage(
                key,
                args,
                locale
        );
    }

    @Override
    public MessageSource getMessageSource() {
        return this.messageSource;
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

}
