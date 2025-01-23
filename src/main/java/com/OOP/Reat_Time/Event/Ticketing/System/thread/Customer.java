package com.OOP.Reat_Time.Event.Ticketing.System.thread;

import com.OOP.Reat_Time.Event.Ticketing.System.thread.TicketPool;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.LogWebSocketHandler;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



public class Customer implements Runnable{
    private TicketPool ticketPool;
    private int customerRetrievalRate;

    private WebSocketConfig webSocketConfig;
    private LogWebSocketHandler logWebSocketHandler;

    public Customer(TicketPool ticketPool, int customerRetrievalRate, LogWebSocketHandler logWebSocketHandler){
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.logWebSocketHandler = logWebSocketHandler;
    }


    @Override
    public void run(){

        //System.out.println(ticketPool.getPool().size());
        while(!ticketPool.getPool().isEmpty()){
            synchronized (ticketPool) {

                int ticketToAdd = Math.min(customerRetrievalRate, ticketPool.getPool().size());

                for (int i = 0; i < ticketToAdd; i++) {
                    if (ticketPool.getPool().isEmpty()) {
                        break;
                    }
                    ticketPool.removeTicket();
                }

                String logMessage = Thread.currentThread().getName() +
                        " purchased " +
                        ticketToAdd +
                        " tickets, Current pool size " +
                        ticketPool.getPool().size();
                System.out.println();
                notifyLogWebSocket(logMessage, "Customer");

            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }



        }
        String logMessage = Thread.currentThread().getName() + " - No more tickets to purchased.Ticket pool is empty. Stopping.";
        notifyLogWebSocket(logMessage, "Customer");
    }

    private void notifyLogWebSocket(String longMessage, String type){
        String logMessageJson = String.format(
                "{\"type\": \"%s\", \"message\": \"%s\"}",
                type,
                longMessage.replace("\"","\\\"")
        );
        logWebSocketHandler.broadcast(logMessageJson);
    }

}
