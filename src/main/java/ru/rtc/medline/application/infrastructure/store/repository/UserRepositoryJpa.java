package ru.rtc.medline.application.infrastructure.store.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.rtc.medline.application.domain.model.User;
import ru.rtc.medline.application.domain.repository.UserRepository;
import ru.rtc.medline.application.domain.wrapper.ResultWrapper;
import ru.rtc.medline.application.infrastructure.store.dao.UserDao;

@Repository
@RequiredArgsConstructor
public class UserRepositoryJpa implements UserRepository {

    private final UserDao userDao;

    @Override
    public ResultWrapper<User> login(String login, String password) {
        return null;
    }

}
