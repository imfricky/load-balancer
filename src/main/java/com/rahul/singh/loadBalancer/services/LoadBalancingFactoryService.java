package com.rahul.singh.loadBalancer.services;

import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;

public interface LoadBalancingFactoryService {

    Server getServer() throws InvalidRequestException;

}
