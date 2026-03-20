package com.example.demo.WebSocketManagement.EventManagement;

import com.example.demo.UserManagemeng.Methods.UserStatusChange;
import com.example.demo.UnitModel.UserModel.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class ConnectingEvent {
    @Autowired
    private UserStatusChange userStatusChange;
    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        Thread.startVirtualThread(() -> {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String id = headerAccessor.getFirstNativeHeader("id");
            if (id == null) {
                System.out.println("Подключился пользователь без id!!!");
                System.out.println("Место подключения:" + headerAccessor.getHost());
                System.out.println("Устройство: " + headerAccessor.getMessage());
            } else {

                userStatusChange.setUserStatus(id, UserStatus.ONLINE);
            }
        });
    }
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        Thread.startVirtualThread(() -> {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            String id = headerAccessor.getFirstNativeHeader("id");
            if (id == null) {

                System.out.println("Отключился пользователь без id");
                System.out.println("Место подключения:" + headerAccessor.getHost());
                System.out.println("Все данные подключения: " + headerAccessor.getMessage());
            } else {
                userStatusChange.setUserStatus(id, UserStatus.OFFLINE);
            }
        });
    }
}
