package ru.rtc.medline.application.infrastructure.common.holder;

public class ClientTimeZoneHolder {

    public static final String TIME_ZONE_HEADER = "TimeZone";

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static String get() {
        return THREAD_LOCAL.get();
    }

    public static void set(String timeZone) {
        THREAD_LOCAL.set(timeZone);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}

