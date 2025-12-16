package ru.rtc.medline.application.api.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры пагинации")
public class PageFilterDto {

    @Schema(description = "Номер запрашиваемой страницы",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer pageNumber = 0;

    @Schema(description = "Количество записей на странице",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer pageSize = 10;

}
