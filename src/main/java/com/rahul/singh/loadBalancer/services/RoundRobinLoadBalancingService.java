package com.rahul.singh.loadBalancer.services;

import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundRobinLoadBalancingService implements LoadBalancingFactoryService {

    @Autowired
    ServerService serverService;

    private Integer currentServerIndex = 0;

    @Override
    public Server getServer() throws InvalidRequestException {
        List<Server> servers = serverService.getServers();
        int serverCount = servers.size();
        if (serverCount == 0) {
            throw new InvalidRequestException("No servers available");
        }

        currentServerIndex++;
        return serverService.getServerByIndex((currentServerIndex) % serverCount);
    }

}
