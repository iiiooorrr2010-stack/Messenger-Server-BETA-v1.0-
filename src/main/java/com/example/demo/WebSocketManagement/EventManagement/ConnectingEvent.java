package com.example.demo.WebSocketManagement.EventManagement;

import com.example.demo.Exception.ConnectedNullUser;
import com.example.demo.ServerConfiguration.Security.JwtService;
import com.example.demo.UnitModel.UserModel.UserStatus;
import com.example.demo.Service.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class ConnectingEvent {
    @Autowired
    private Account account;
    @Autowired
    private JwtService jwtService;
    private final Map<String, String> sessionMap = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> sessionLock = new ConcurrentHashMap<>();

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        log.info("Connect create");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
            String token = (String) headerAccessor.getSessionAttributes().get("token");
            ReentrantLock thread = sessionLock.computeIfAbsent(sessionId, k -> new ReentrantLock());
            thread.lock();
            try {
                if (token == null) {
                    log.error("подключен пользователь без token \n {}", headerAccessor);
                    return;
                }
                sessionMap.putIfAbsent(sessionId, token);
                String id = jwtService.extractUserId(token);
                account.changeUserStatus(id, UserStatus.ONLINE);
                log.info("изменен статус пользователя на онлайн {}", token);
            } catch (Exception e) {
                log.error("Ошибка подключения пользователя {}, сессия {}", token, sessionId);
                throw new ConnectedNullUser(headerAccessor);
            } finally {
                thread.unlock();
            }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
                ReentrantLock thread = sessionLock.get(sessionId);
                if (thread != null) {
                thread.lock();
                try {
                    String token = sessionMap.remove(sessionId);
                    if (token == null) {
                        return;
                    }
                    String id = jwtService.extractUserId(token);
                        account.changeUserStatus(id, UserStatus.OFFLINE);
                        log.info("изменен статус пользователя на оффлайн {}", token);
                } catch(NullPointerException id){
                        log.error("Отключен пользовательн без id \n {}", headerAccessor);
                    } finally {
                        thread.unlock();
                        sessionLock.remove(sessionId);
                    }
                } else {
                    String token = sessionMap.remove(sessionId);
                    if (token != null) {
                        String id = jwtService.extractUserId(token);
                        account.changeUserStatus(id, UserStatus.OFFLINE);
                        log.warn("Пользователь перешел в оффлайн без лока {}", sessionId);
                    }
                }
        }
    }
