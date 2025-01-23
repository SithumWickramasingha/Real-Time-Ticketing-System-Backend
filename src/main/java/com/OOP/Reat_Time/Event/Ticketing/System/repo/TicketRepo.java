package com.OOP.Reat_Time.Event.Ticketing.System.repo;

import com.OOP.Reat_Time.Event.Ticketing.System.model.Configuration;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories

public interface TicketRepo extends JpaRepository<Configuration, Integer> {

}
