package ru.rtc.medline.application.infrastructure.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import ru.rtc.medline.application.infrastructure.common.holder.ClientTimeZoneHolder;
import ru.rtc.medline.application.infrastructure.common.interfaces.AutoHandlerInterceptor;

@Component
public class TimeZoneInterceptor implements AutoHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ClientTimeZoneHolder.set(request.getHeader(ClientTimeZoneHolder.TIME_ZONE_HEADER));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        ClientTimeZoneHolder.remove();
    }

}
