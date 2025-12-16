package ru.rtc.medline.application.infrastructure.common.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.rtc.medline.application.infrastructure.common.interfaces.AutoHandlerInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final List<AutoHandlerInterceptor> interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (AutoHandlerInterceptor interceptor : interceptors) {
            registry.addInterceptor(interceptor);
        }
    }

}

