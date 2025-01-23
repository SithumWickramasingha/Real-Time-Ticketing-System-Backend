package com.OOP.Reat_Time.Event.Ticketing.System.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

@Entity
@Component
public class Configuration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int activeVendors;
    private int activeCustomers;

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, int activeVendors, int activeCustomers){
        this.setTotalTickets(totalTickets);
        this.setTicketReleaseRate(ticketReleaseRate);
        this.setCustomerRetrievalRate(customerRetrievalRate);
        this.setMaxTicketCapacity(maxTicketCapacity);
        this.setActiveVendors(activeVendors);
        this.setActiveCustomers(activeCustomers);

    }

    public Configuration(){

    }


    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getActiveVendors() {
        return activeVendors;
    }

    public void setActiveVendors(int activeVendors) {
        this.activeVendors = activeVendors;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public void setActiveCustomers(int activeCustomers) {
        this.activeCustomers = activeCustomers;
    }
    public void saveToJson(String filePath){
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            objectMapper.writeValue(new File(filePath), this);
            System.out.println("Data saved to "+ filePath);
        }catch(IOException e){
            System.out.println(e);
        }

    }
}
