package com.rahul.singh.loadBalancer.cron;

import com.rahul.singh.loadBalancer.services.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServerMaintenanceJob {

    @Autowired
    ServerService serverService;

    @Scheduled(cron = "*/30 * * * * *")
    public void removeUnhealthyServers() {
        log.info("Starting scheduled task to remove unhealthy servers...");
        serverService.removeUnhealthyServers();
        log.info("Unhealthy servers removed.");

        log.info("Starting scheduled task to move healthy servers...");
        serverService.moveHealthyServersBack();
        log.info("Healthy servers moved back.");
    }

}
