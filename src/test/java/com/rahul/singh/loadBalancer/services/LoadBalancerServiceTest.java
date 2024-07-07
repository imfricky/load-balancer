package com.rahul.singh.loadBalancer.services;

import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.factories.LoadBalancingFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadBalancerServiceTest {

    @Mock
    private LoadBalancingFactory loadBalancingFactory;

    @Mock
    private LoadBalancingFactoryService loadBalancingService;

    @InjectMocks
    private LoadBalancerService loadBalancerService;

    @BeforeEach
    void setup() throws InvalidRequestException {
        loadBalancerService.maxNumberOfRetries = 3;
        loadBalancerService.backOffTimeInMs = 1000;
        lenient().when(loadBalancingFactory.getLoadBalancingService(any())).thenReturn(loadBalancingService);
    }

    @Test
    void getServerResponseExceedsMaxRetries() throws InvalidRequestException {
        when(loadBalancingService.getServer()).thenThrow(new InvalidRequestException("Server error"));
        assertThrows(InvalidRequestException.class, () -> loadBalancerService.getServerResponse());
        verify(loadBalancingService, times(3)).getServer();
    }

    @Test
    void setLoadBalancerTypeInvalidThrowsException() {
        Exception exception = assertThrows(InvalidRequestException.class, () -> loadBalancerService.setLoadBalancerType("invalid"));
        assertEquals("Invalid Load Balancing Type", exception.getMessage());
    }

    @Test
    void setLoadBalancerTypeValid() throws InvalidRequestException {
        loadBalancerService.setLoadBalancerType("ROUND_ROBIN");
        assertEquals("ROUND_ROBIN", loadBalancerService.getLoadBalancerType());
    }

}