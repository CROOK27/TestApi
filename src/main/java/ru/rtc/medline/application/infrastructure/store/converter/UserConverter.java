package ru.rtc.medline.application.infrastructure.store.converter;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import ru.rtc.medline.application.domain.model.User;
import ru.rtc.medline.application.infrastructure.store.entity.UserEntity;

import java.util.UUID;

public class UserConverter {

    public static User toUser(UserEntity entity) {
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }

        User user = new User();

        user.setId(entity.getId().toString());
        user.setPassword(entity.getPassword());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setMiddleName(entity.getMiddleName());
        user.setBirthDate(entity.getBirthDate());
        user.setPhone(entity.getPhone());
        user.setEmail(entity.getEmail());

        return user;
    }

    public static UserEntity toUserEntity(User user) {
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }

        UserEntity entity = new UserEntity();

        String reportId = user.getId();
        if (StringUtils.hasText(reportId)) {
            entity.setId(UUID.fromString(reportId));
        }

        entity.setPassword(user.getPassword());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setMiddleName(user.getMiddleName());
        entity.setBirthDate(user.getBirthDate());
        entity.setPhone(user.getPhone());
        entity.setEmail(user.getEmail());

        return entity;
    }

}
