package com.OOP.Reat_Time.Event.Ticketing.System.webSocket;

import com.OOP.Reat_Time.Event.Ticketing.System.model.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Autowired
    private Configuration config;

    //private TicketPool ticketPool;

    public CustomWebSocketHandler(Configuration config){
        this.config = config;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessions.add(session);
        System.out.println("Connection Established with Front end");

        String message = "{\"message\": \"Message from backend\"}";
        session.sendMessage(new TextMessage(message));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        for(WebSocketSession s: sessions){
            s.sendMessage(new TextMessage("Backend Update: "+ message.getPayload()));
        }
    }

    public void broadcast(Object payLoad){
        try{
            String message = new ObjectMapper().writeValueAsString(payLoad);

            for(WebSocketSession session: sessions){
                if(session.isOpen()){
                    try{
                        session.sendMessage(new TextMessage(message));
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Closed or sending websocket message");
                }
            }

        }catch(Exception e){
            System.out.println("Error serializing payload: "+ e.getMessage());
            e.printStackTrace();
        }
    }


}


