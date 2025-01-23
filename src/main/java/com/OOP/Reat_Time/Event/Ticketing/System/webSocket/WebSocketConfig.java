package com.OOP.Reat_Time.Event.Ticketing.System.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Autowired
    private final CustomWebSocketHandler customWebSocketHandler;

    @Autowired
    private final LogWebSocketHandler logWebSocketHandler;


    public WebSocketConfig(CustomWebSocketHandler customWebSocketHandler, LogWebSocketHandler logWebSocketHandler){
        this.customWebSocketHandler = customWebSocketHandler;
        this.logWebSocketHandler = logWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(customWebSocketHandler, "/ws").setAllowedOrigins("*");
        registry.addHandler(logWebSocketHandler, "/log-updates").setAllowedOrigins("*");

    }


}
