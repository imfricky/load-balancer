package com.rahul.singh.loadBalancer.services;

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
    }

}
