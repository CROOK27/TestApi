package ru.rtc.medline.application.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLoginDto {

    private String login;
    private String password;

}
