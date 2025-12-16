package ru.rtc.medline.application.infrastructure.common.actuator;

import org.springframework.boot.actuate.endpoint.SanitizableData;
import org.springframework.boot.actuate.endpoint.SanitizingFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Класс для сокрытия значений в env актуатора
 * Источник <a href="https://github.com/spring-projects/spring-boot/issues/32156">...</a>
 * Spring Sources <a href="https://github.com/spring-projects/spring-boot/blob/2.7.x/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/endpoint/Sanitizer.java">...</a>
 */
@Component
public class ActuatorSanitizer implements SanitizingFunction {

    private static final String[] REGEX_PARTS = {"*", "$", "^", "+"};

    private final List<Pattern> keysToSanitize = new ArrayList<>();

    public ActuatorSanitizer(List<String> keysToSanitize) {
        addKeysToSanitize(keysToSanitize);
    }

    @Override
    public SanitizableData apply(SanitizableData data) {
        if (ObjectUtils.isEmpty(data.getValue())) {
            return data;
        }

        for (Pattern pattern : keysToSanitize) {
            if (pattern.matcher(data.getKey()).matches()) {
                return data.withValue(SanitizableData.SANITIZED_VALUE);
            }
        }

        return data;
    }

    private void addKeysToSanitize(Collection<String> keysToSanitize) {
        for (String key : keysToSanitize) {
            this.keysToSanitize.add(getPattern(key));
        }
    }

    private Pattern getPattern(String value) {
        if (isRegex(value)) {
            return Pattern.compile(value, Pattern.CASE_INSENSITIVE);
        }
        return Pattern.compile(".*" + value + "$", Pattern.CASE_INSENSITIVE);
    }

    private boolean isRegex(String value) {
        for (String part : REGEX_PARTS) {
            if (value.contains(part)) {
                return true;
            }
        }
        return false;
    }

}
