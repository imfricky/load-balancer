package com.rahul.singh.loadBalancer.factories;

import com.rahul.singh.loadBalancer.enums.LoadBalancingType;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.services.LoadBalancingFactoryService;
import com.rahul.singh.loadBalancer.services.impl.LeastConnectionLoadBalancingService;
import com.rahul.singh.loadBalancer.services.impl.RandomSelectionLoadBalancingService;
import com.rahul.singh.loadBalancer.services.impl.RoundRobinLoadBalancingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadBalancingFactory {

    @Autowired
    RoundRobinLoadBalancingService roundRobinLoadBalancingService;

    @Autowired
    RandomSelectionLoadBalancingService randomSelectionLoadBalancingService;

    @Autowired
    LeastConnectionLoadBalancingService leastConnectionLoadBalancingService;

    public LoadBalancingFactoryService getLoadBalancingService(LoadBalancingType loadBalancingType) throws InvalidRequestException {
        switch (loadBalancingType) {
            case ROUND_ROBIN:
                return roundRobinLoadBalancingService;
            case RANDOM_SELECTION:
                return randomSelectionLoadBalancingService;
            case LEAST_CONNECTION:
                return leastConnectionLoadBalancingService;
            default:
                throw new InvalidRequestException("Invalid Load Balancing Type");
        }
    }

}
