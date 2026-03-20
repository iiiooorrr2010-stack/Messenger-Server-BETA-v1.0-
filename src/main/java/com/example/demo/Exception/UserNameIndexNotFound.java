package com.example.demo.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNameIndexNotFound extends RuntimeException{
    public UserNameIndexNotFound(String num) {
        super("User Not Found \n ERROR 404");
        log.error("User not Found With Num: {}", num);
    }
}
