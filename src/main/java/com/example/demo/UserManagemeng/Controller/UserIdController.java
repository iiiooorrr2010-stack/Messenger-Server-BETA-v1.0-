package com.example.demo.UserManagemeng.Controller;

import com.example.demo.UserManagemeng.UserRepository.UsersNameIndex;


import com.example.demo.UserManagemeng.UserRepository.UsersRepository;
import com.example.demo.UnitModel.UserModel.User;
import com.example.demo.UserManagemeng.Methods.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
public class UserIdController {
    @Autowired
    private Account account;
    @GetMapping("/get-id-user/{id}")
 public User httpGetAccount(@PathVariable String id) {
   return account.getUser(id);
 }

    @PatchMapping("/update-user/{id}")
 public User httpUserChange(@PathVariable String id, @RequestBody @Valid User changeUser) {
        return account.changeUser(changeUser);
    }


   @PostMapping("/add/user")
 public User httpCreateAccount(@RequestBody @Valid User user) {
        return account.create(user);
   }
}