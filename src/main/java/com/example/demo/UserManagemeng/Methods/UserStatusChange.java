package com.example.demo.UserManagemeng.Methods;

import com.example.demo.Exception.UserNotFoundWithId;
import com.example.demo.UserManagemeng.UserRepository.UsersRepository;
import com.example.demo.UnitModel.UserModel.User;
import com.example.demo.UnitModel.UserModel.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class UserStatusChange {
    @Autowired
    private UsersRepository usersRepository;

    public void setUserStatus(String id, UserStatus ConnectionType) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Пользователь " + id + "не найден");
                    return new UserNotFoundWithId(id);
                });
        if (ConnectionType.equals(UserStatus.ONLINE)) {
            user.setUserStatus(UserStatus.ONLINE);
        } else {
            user.setUserStatus(UserStatus.OFFLINE);
        }
        usersRepository.save(user);
    }
}
