package ru.rtc.medline.application.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Product {

    private UUID id;

    private String name;

    private String characteristics;

    private Long price;

    private Long count;

    private Double rating;

    private Category category;

}
