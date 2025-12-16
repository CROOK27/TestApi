package ru.rtc.medline.application.domain.repository;

import ru.rtc.medline.application.domain.model.User;
import ru.rtc.medline.application.domain.wrapper.ResultWrapper;

public interface UserRepository {

    ResultWrapper<User> login(String login, String password);

}
