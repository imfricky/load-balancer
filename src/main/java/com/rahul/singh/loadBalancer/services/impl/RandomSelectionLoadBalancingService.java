package com.rahul.singh.loadBalancer.services.impl;

import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.services.LoadBalancingFactoryService;
import com.rahul.singh.loadBalancer.services.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RandomSelectionLoadBalancingService implements LoadBalancingFactoryService {

    @Autowired
    ServerService serverService;

    @Override
    public Server getServer() throws InvalidRequestException {
        int min = 0;
        int serverCount = serverService.getServers().size();
        if (serverCount == 0) {
            throw new InvalidRequestException("No servers available");
        }

        int randomIndex = (int) (Math.random() * (serverCount - 1)) + min;
        return serverService.getServerByIndex(randomIndex);
    }

}
