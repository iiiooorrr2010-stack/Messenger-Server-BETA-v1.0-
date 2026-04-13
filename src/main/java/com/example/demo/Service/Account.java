package com.example.demo.Service;

import com.example.demo.Exception.PasswordIsIncorrected;
import com.example.demo.Exception.UserNameIndexNotFound;
import com.example.demo.Exception.UserNotFoundWithId;
import com.example.demo.Exception.UsernameExists;
import com.example.demo.ServerConfiguration.Security.JwtService;
import com.example.demo.ServerConfiguration.Security.PasswordManager;
import com.example.demo.UnitModel.UserModel.User;
import com.example.demo.UnitModel.UserModel.UserStatus;
import com.example.demo.UserManagement.UserRepository.UsersRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Slf4j
public class Account {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordManager passwordManager;
    private User create(User user) {
    user.setId(UUID.randomUUID().toString());
    user.setPassword(passwordManager.create(user.getPassword()));
    user.setUserStatus(UserStatus.OFFLINE);
    validateUser(user);
    usersRepository.save(user);
           return getUser(user.getId());
    }

    @Transactional
    public User changeUser(@Valid User changeUser) {
        User oldUser = getUser(changeUser.getId());

        if (!passwordManager.compare(changeUser.getPassword(), oldUser.getPassword())) {
        throw new PasswordIsIncorrected(changeUser.getId());
        }

        if (!oldUser.getNickname().equals(changeUser.getNickname())) {
            User haveName = usersRepository.findByNickname(changeUser.getNickname());
            throw new UsernameExists(changeUser.getNickname());
        }
        changeUser.setPassword(passwordManager.create(changeUser.getPassword()));
        return usersRepository.save(changeUser);
    }


    public User getUser(String id) {
        log.info("Looking for user with id: {}", id);
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundWithId(id));
    }

    public User getUserByNick(String name) {
        log.info("get user, nick {}", name);
        return usersRepository.findByNickname(name);
    }


    public void changeUserStatus(String id, UserStatus сonnectionType) {
        User user = getUser(id);
        if (сonnectionType.equals(UserStatus.ONLINE)) {
            user.setUserStatus(UserStatus.ONLINE);
        } else {
            user.setUserStatus(UserStatus.OFFLINE);
        }
        usersRepository.save(user);
    }

    private User loginUser(String name, String password) {
        log.info("LOGGGG USER ");
        User user = usersRepository.findByNickname(name);
        log.info("Found user: id={}, nickname={}, password={}", user.getId(), user.getNickname(), user.getPassword());
        if (!passwordManager.compare(password, user.getId())) {
            throw new PasswordIsIncorrected(name);
        }
        return user;
    }
    @Transactional
    public Map<String, String> loginWithToken(String name, String password) {
        User user = loginUser(name, password);
        log.info("Found user: id={}, nickname={}, password={}", user.getId(), user.getNickname(), user.getPassword());
        String token = jwtService.generateToken(user.getId(), user.getNickname());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
    @Transactional
    public Map<String, String> authWithToken(User user) {
        user = create(user);
        String token = jwtService.generateToken(user.getId(), user.getNickname());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    private void validateUser(@Valid User user) {

    }
}
