package ru.rtc.medline.application.domain.model.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageFilter {

    private Integer pageNumber;

    private Integer pageSize;

}
