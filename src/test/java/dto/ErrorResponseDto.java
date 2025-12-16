package Test.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private String message;
    private String errorCode;
    private Long timestamp;
}