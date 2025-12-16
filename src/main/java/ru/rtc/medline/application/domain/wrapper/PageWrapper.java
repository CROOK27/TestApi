package ru.rtc.medline.application.domain.wrapper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageWrapper<T> {

    private List<T> elements;

    private Integer pageSize;

    private Integer pageNumber;

    private Long totalElementsCount;

}
