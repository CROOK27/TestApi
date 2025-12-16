package ru.rtc.medline.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rtc.medline.application.domain.model.User;
import ru.rtc.medline.application.domain.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(String login, String password) {
        return userRepository.login(login, password).orElseThrow(RuntimeException::new);
    }

}
