package ru.rtc.medline.application.domain.model.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilter {

    private String categoryId;

    private String name;

    private Integer minPrice = 0;

    private Integer maxPrice = 10000000;

}
