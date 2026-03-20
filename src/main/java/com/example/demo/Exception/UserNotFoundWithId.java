package com.example.demo.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundWithId extends RuntimeException {
    public UserNotFoundWithId(String id) {
            super("User not Found\n ERROR 404");
        log.error("User Not Found With Id {}", id);
    }
}
