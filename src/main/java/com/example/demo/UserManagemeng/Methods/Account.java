package com.example.demo.UserManagemeng.Methods;

import com.example.demo.Exception.UserNotCreated;
import com.example.demo.Exception.UserNotFoundWithId;
import com.example.demo.Exception.UsernameExists;
import com.example.demo.UnitModel.UserModel.NameIndex;
import com.example.demo.UnitModel.UserModel.User;
import com.example.demo.UnitModel.UserModel.UserStatus;
import com.example.demo.UserManagemeng.UserRepository.UsersNameIndex;
import com.example.demo.UserManagemeng.UserRepository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Random;


@Component
public class Account {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersNameIndex usersNameIndex;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    Random rand = new Random();
    @Transactional
    public User create(@Valid User user) {
    String id = DigestUtils.md5DigestAsHex(user.getName().getBytes(StandardCharsets.UTF_8));
           if (usersRepository.existsById(id)) {

        int x = rand.nextInt(0, 1000);
        user.setId(id + "salt" + x);
    }
           if (usersNameIndex.existsById(user.getName())) {
        throw new UsernameExists(user.getName());
    }
    NameIndex lowUser = new NameIndex(user.getName(), user.getId());
            usersNameIndex.save(lowUser);
            user.setUserStatus(UserStatus.OFFLINE);
           return usersRepository.save(user);
    }
    @Transactional
    public User changeUser(@Valid User changeUser) {
        User oldUser = usersRepository.findById(changeUser.getId())
                .orElseThrow(() -> {
                    throw new UserNotCreated(changeUser);
                });
        if (!oldUser.getName().equals(changeUser.getName())) {
            if(usersNameIndex.existsById(changeUser.getName())) {
                throw new UsernameExists(changeUser.getName());
            }
        }
        usersNameIndex.deleteById(oldUser.getName());
        NameIndex lowUser = new NameIndex(changeUser.getName(), changeUser.getId());
        usersNameIndex.save(lowUser);
        return usersRepository.save(changeUser);
    }
    public User getUser( String id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundWithId(id));
    }

}
