package com.example.demo.ServerConfiguration.Security;

import com.example.demo.Service.Account;
import com.example.demo.UnitModel.UserModel.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetails implements UserDetailsService {
    @Autowired
    private Account account;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        log.info("user found with id {}", id);
        User user = account.getUser(id);
        return user;
    }
}