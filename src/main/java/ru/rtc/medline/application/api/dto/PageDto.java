package ru.rtc.medline.application.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Объект результата запроса с пагинацией")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDto<T> {

    @Schema(description = "Список объектов на странице",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> elements;

    @Schema(description = "Номер страницы",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNumber;

    @Schema(description = "Размер страницы",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    @Schema(description = "Общее количество элементов по запросу",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long totalElementsCount;

}
