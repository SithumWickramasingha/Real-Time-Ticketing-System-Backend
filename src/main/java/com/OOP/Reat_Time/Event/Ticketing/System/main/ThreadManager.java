package com.OOP.Reat_Time.Event.Ticketing.System.main;

import com.OOP.Reat_Time.Event.Ticketing.System.thread.Customer;
import com.OOP.Reat_Time.Event.Ticketing.System.thread.TicketPool;
import com.OOP.Reat_Time.Event.Ticketing.System.thread.Vendor;
import com.OOP.Reat_Time.Event.Ticketing.System.model.Configuration;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.CustomWebSocketHandler;
import com.OOP.Reat_Time.Event.Ticketing.System.webSocket.LogWebSocketHandler;
import org.springframework.stereotype.Service;

@Service
public class ThreadManager {
    private final CustomWebSocketHandler webSocketHandler;

    private final LogWebSocketHandler logWebSocketHandler;

    public ThreadManager(CustomWebSocketHandler webSocketHandler, LogWebSocketHandler logWebSocketHandler) {
        this.webSocketHandler = webSocketHandler;
        this.logWebSocketHandler = logWebSocketHandler;
    }

    public void threadRun(Configuration config) {
        while (config.getTotalTickets() == 0) {
            try {
                wait();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (config.getTotalTickets() < config.getMaxTicketCapacity()) {
            System.out.println();
            config.saveToJson("Data.json");


            TicketPool ticketPool = new TicketPool(config.getTotalTickets());

            Vendor vendor = new Vendor(ticketPool, config.getMaxTicketCapacity(), config.getTicketReleaseRate(), config.getTotalTickets(), webSocketHandler, logWebSocketHandler);
            Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(), logWebSocketHandler);

            System.out.println("Start with tickets: " + config.getMaxTicketCapacity());
            for (int i = 1; i <= config.getActiveVendors(); i++) {
                Thread vendorThread = new Thread(vendor, "Vendor Thread " + "(" + i + ")" + ": ");
                vendorThread.start();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (int i = 1; i <= config.getActiveCustomers(); i++) {
                Thread customerThread = new Thread(customer, "Customer Thread " + "(" + i + ")" + ": ");
                customerThread.start();
            }

        }else{
            System.out.println("Invalid Input.");
        }
    }
}
