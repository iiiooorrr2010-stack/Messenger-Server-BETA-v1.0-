package com.example.demo.Exception;

import com.example.demo.UnitModel.UserModel.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotCreated extends RuntimeException {
    public UserNotCreated(User user) {
        super();
      log.error("Попытка изменить несуществующего пользователя! {}", user);
    }
}
