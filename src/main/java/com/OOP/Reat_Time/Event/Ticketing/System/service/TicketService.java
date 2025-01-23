package com.OOP.Reat_Time.Event.Ticketing.System.service;

import com.OOP.Reat_Time.Event.Ticketing.System.model.Configuration;
import com.OOP.Reat_Time.Event.Ticketing.System.repo.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepo;

    public Configuration setAllData(Configuration config){

        Configuration configuration = new Configuration(
          config.getTotalTickets(),
          config.getTicketReleaseRate(),
          config.getCustomerRetrievalRate(),
          config.getMaxTicketCapacity(),
          config.getActiveVendors(),
          config.getActiveCustomers()
        );
        ticketRepo.save(configuration);
        return configuration;
    }


    public List<Configuration> getAllConfiguration(){
        return ticketRepo.findAll();
    }

}
