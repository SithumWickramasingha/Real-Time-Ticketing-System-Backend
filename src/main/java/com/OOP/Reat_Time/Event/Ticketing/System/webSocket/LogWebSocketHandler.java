package com.OOP.Reat_Time.Event.Ticketing.System.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LogWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status){
        sessions.remove(session);
    }

    public synchronized void broadcast(String message){
        for(WebSocketSession session : sessions){
            if(session.isOpen()){
                try{
                    session.sendMessage(new TextMessage(message));
                }catch (IOException e){
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }
    }


}
