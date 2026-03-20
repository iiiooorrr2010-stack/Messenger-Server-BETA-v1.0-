package com.example.demo.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SendToNullUser extends RuntimeException {
   public SendToNullUser(String id) {
        super("NOT FOUND RECIPIENT \n ERROR 404");
     log.error("Пользователь {} отправил сообщение несуществующему пользователю", id);
    }
}
