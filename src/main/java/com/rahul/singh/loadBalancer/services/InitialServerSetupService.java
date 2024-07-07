package com.rahul.singh.loadBalancer.services;

import com.rahul.singh.loadBalancer.entities.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@Slf4j
public class InitialServerSetupService {

    @Value("${initial.server.count:5}")
    Integer serverCount;

    @Autowired
    ServerService serverService;

    @PostConstruct
    public void init() {
        log.info("Setting up initial servers");
        for (int i = 0; i < serverCount; i++) {
            UUID serverCode = UUID.randomUUID();
            serverService.addServer(serverCode);
        }

        // Remove later
        log.info("Initial servers setup complete");
        for (Server server : serverService.getServers()) {
            log.info("Server {}", server);
        }
    }

}
