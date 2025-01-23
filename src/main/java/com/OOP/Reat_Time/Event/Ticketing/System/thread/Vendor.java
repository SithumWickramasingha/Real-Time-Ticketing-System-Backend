package com.OOP.Reat_Time.Event.Ticketing.System.thread;

import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.CustomWebSocketHandler;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.LogWebSocketHandler;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.WebSocketConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public class Vendor implements Runnable{
    private TicketPool ticketPool;
    private int maxTicketCapacity;
    private int ticketReleaseRate;
    private int totalTicket;
    private int releasedTicketCount = 0;

    private final CustomWebSocketHandler webSocketHandler;

    private final LogWebSocketHandler logWebSocketHandler;

    private WebSocketConfig webSocketConfig;



    public Vendor(TicketPool ticketPool, int maxTicketCapacity, int ticketReleaseRate, int totalTicket, CustomWebSocketHandler customWebSocketHandler, LogWebSocketHandler logWebSocketHandler){
        this.ticketPool = ticketPool;
        this.maxTicketCapacity = maxTicketCapacity;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTicket = totalTicket;
        this.webSocketHandler = customWebSocketHandler;
        this.logWebSocketHandler = logWebSocketHandler;
    }



    @Override
    public void run() {
       // System.out.println("Starting with max tickets: " + maxTicketCapacity);
        while (maxTicketCapacity > 0) { // Continue until all tickets are released
            synchronized (ticketPool) {
                 //Check if the pool is full and wait if necessary
                while (ticketPool.getPool().size() == totalTicket) {
                    String logMessage = Thread.currentThread().getName() + " - Pool is full. Waiting...";
                    notifyLogWebSocket(logMessage, "Vendor");
                    try {
                        ticketPool.wait(); // Wait until space is available
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Vendor interrupted: " + e.getMessage());
                        return;
                    }
                }

                if(maxTicketCapacity > 0){
                    int ticketsToAdd = Math.min(ticketReleaseRate, maxTicketCapacity);

                    for (int i = 0; i < ticketsToAdd; i++) {

                        Ticket ticket = new Ticket(1000 + maxTicketCapacity); // Unique ticket ID
                        ticketPool.addTicket(ticket); // Add ticket to the pool
                        maxTicketCapacity--;

                        releasedTicketCount++; // to count how many tickets released

                    }

                    String logMessage = Thread.currentThread().getName() +
                            " Released " +
                            ticketsToAdd +
                            " tickets. Current pool size: " +
                            ticketPool.getPool().size() +
                            ",\nRemaining tickets: " +
                            maxTicketCapacity;
                    System.out.println(logMessage);
                    notifyLogWebSocket(logMessage, "Vendor");

                    System.out.println();



                    try {
                        notifyWebSocket();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    ticketPool.notifyAll(); // Notify waiting threads that tickets are added

                }else{
                    break;
                }


            }

            // Simulate time between ticket releases
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " interrupted: " + e.getMessage());
                break;
            }
        }

        String logMessage = Thread.currentThread().getName() + " - No more tickets to release. Stopping.";
        System.out.println(logMessage);
        notifyLogWebSocket(logMessage, "Vendor");
        System.out.println();
    }

    public void count(){
        System.out.println("Total released tickets: "+ releasedTicketCount);
    }

    private void notifyWebSocket() throws JsonProcessingException {
        if(webSocketHandler != null){
            Map<String, Object> payLoad = new HashMap<>();
            payLoad.put("maxTicketCapacity", maxTicketCapacity);
            webSocketHandler.broadcast(new ObjectMapper().writeValueAsString(payLoad));
            System.out.println("websocket is working");
        }else{
            System.out.println("WebSocketHandler is not initialized. Cannot send updates.");
        }
    }


//    private void notifyLogWebSocket(String longMessage, String type){
//        String logMessageJson = String.format(
//                "{\"type\": \"%s\", \"message\": \"%s\"}",
//                type,
//                longMessage.replace("\"","\\\"")
//        );
//        logWebSocketHandler.broadcast(logMessageJson);
//    }

    private void notifyLogWebSocket(String longMessage, String type) {
        try {
            String escapedMessage = longMessage
                    .replace("\\", "\\\\")  // Escape backslashes
                    .replace("\"", "\\\"")  // Escape double quotes
                    .replace("\n", "\\n")   // Escape newlines
                    .replace("\r", "\\r")   // Escape carriage returns
                    .replace("\t", "\\t");  // Escape tabs

            String logMessageJson = String.format(
                    "{\"type\": \"%s\", \"message\": \"%s\"}",
                    type,
                    escapedMessage
            );

            logWebSocketHandler.broadcast(logMessageJson);
            System.out.println("Log message sent: " + logMessageJson);
        } catch (Exception e) {
            System.err.println("Error while notifying log WebSocket: " + e.getMessage());
        }
    }

}





