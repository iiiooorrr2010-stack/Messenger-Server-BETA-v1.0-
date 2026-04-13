package com.example.demo.UserManagement.Controller;

import com.example.demo.Service.Account;
import com.example.demo.UnitModel.UserModel.User;
import com.example.demo.UserManagement.UserRepository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserNameController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private Account account;
    @GetMapping("/find/{name}")
  public User httpGetAccount(@PathVariable String name) {
           return account.getUserByNick(name);
    }
}
