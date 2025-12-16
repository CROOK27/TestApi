package ru.rtc.medline.application.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateProductDto {

    private UUID id;

    private String name;

    private String characteristics;

    private Long price;

    private Long count;

    private UUID categoryId;

}
