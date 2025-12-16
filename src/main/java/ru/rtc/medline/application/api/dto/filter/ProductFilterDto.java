package ru.rtc.medline.application.api.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры фильтрации списка продуктов")
public class ProductFilterDto {

    @Schema(description = "Идентификатор категории", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String categoryId;

    @Schema(description = "Наименование товара", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(description = "", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer minPrice = 0;

    @Schema(description = "Дата созданя с", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer maxPrice = 100000000;

}
