package ru.rtc.medline.application.infrastructure.store.specification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeType {

    CONTAINS("%{0}%"),
    STARTS_WITH("{0}%"),
    ENDS_WITH("%{0}");

    private final String pattern;

}
