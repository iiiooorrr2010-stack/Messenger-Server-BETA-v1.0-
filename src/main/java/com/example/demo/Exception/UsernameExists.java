package com.example.demo.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsernameExists extends RuntimeException {
    public UsernameExists(String name) {
        super("The username exists " + name);
            log.info("The username exists: {}", name);
    }
}
