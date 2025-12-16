package dto;

import lombok.Data;

@Data
public class RequestLoginDto {
    private String login;
    private String password;
}