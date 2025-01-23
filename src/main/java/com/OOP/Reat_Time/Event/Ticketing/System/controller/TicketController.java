package com.OOP.Reat_Time.Event.Ticketing.System.controller;

import com.OOP.Reat_Time.Event.Ticketing.System.main.ThreadManager;
import com.OOP.Reat_Time.Event.Ticketing.System.model.Configuration;
import com.OOP.Reat_Time.Event.Ticketing.System.service.TicketService;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.CustomWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TicketController {


    @Autowired
    private TicketService ticketService;

    @Autowired
    private ThreadManager threadManager;

    @Autowired
    private CustomWebSocketHandler customWebSocketHandler;

    @PostMapping("/getParam")
    public Configuration setAllData(@RequestBody Configuration config){
        threadManager.threadRun(config);
        return ticketService.setAllData(config);
    }

    @GetMapping("/getAllConfig")
    public List<Configuration> getAllConfiguration(){
        return ticketService.getAllConfiguration();
    }

}
