package com.OOP.Reat_Time.Event.Ticketing.System.thread;

public class Ticket {
    private int ticketId;


    Ticket(int ticketId){
        this.ticketId = ticketId;
    }

    public int getTicketId(){
        return ticketId;
    }

    public void setTicketId(int ticketId){
        this.ticketId = ticketId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                '}';
    }
}
