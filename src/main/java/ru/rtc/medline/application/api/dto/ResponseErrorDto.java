package ru.rtc.medline.application.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Информация об ошибке")
public class ResponseErrorDto {

    @Schema(description = "Сообщение об ошибке", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Поле, вызвавшее ошибку", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String field;

    public ResponseErrorDto(String message) {
        this.message = message;
    }

}
