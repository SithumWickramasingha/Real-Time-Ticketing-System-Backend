package com.OOP.Reat_Time.Event.Ticketing.System.thread;

import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.CustomWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {

    private int totalTicket;


    //Constructor
    public TicketPool(int totalTicket){
        this.totalTicket = totalTicket;

    }

    private List<Ticket> pool = Collections.synchronizedList(new ArrayList<>());

    public synchronized List<Ticket> getPool(){
        return pool;
    }

    public synchronized void addTicket(Ticket ticket){
        while(pool.size() == totalTicket){
            try{
                System.out.println("Ticket pool reached the total capacity. The vendor have to wait until customer buy.");
                wait();

            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(e);
            }
        }

        pool.add(ticket);
        notifyAll();
        //notifyWebSocket();
    }

    public synchronized void removeTicket(){
        while(pool.isEmpty()){
            try{
                wait();
                System.out.println("Ticket pool is empty, Customer has to wait until the vendor adds the tickets.");
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(e);
            }
        }


        Ticket removedTicket;
        removedTicket = pool.remove(0);
        notifyAll();
        //notifyWebSocket();

    }



}
