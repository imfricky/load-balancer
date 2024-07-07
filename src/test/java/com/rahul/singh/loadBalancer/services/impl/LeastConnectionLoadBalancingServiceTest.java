package com.rahul.singh.loadBalancer.services.impl;

import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.enums.ServerStatus;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.services.ServerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeastConnectionLoadBalancingServiceTest {

    @Mock
    private ServerService serverService;

    @InjectMocks
    private LeastConnectionLoadBalancingService loadBalancingService;

    @Test
    void testGetServerWithHealthyServersReturnsServerWithLeastConnections() throws InvalidRequestException {
        Server server1 = Server.builder().code(UUID.randomUUID()).status(ServerStatus.HEALTHY).totalConnectionsCount(10).build();
        Server server2 = Server.builder().code(UUID.randomUUID()).status(ServerStatus.HEALTHY).totalConnectionsCount(5).build();
        when(serverService.getServers()).thenReturn(Arrays.asList(server1, server2));

        Server result = loadBalancingService.getServer();

        assertSame(server2, result);
        assertEquals(5, result.getTotalConnectionsCount());
    }

    @Test
    void testGetServerNoHealthyServersThrowsInvalidRequestException() {
        Server server1 = Server.builder().code(UUID.randomUUID()).status(ServerStatus.UNHEALTHY).totalConnectionsCount(10).build();
        when(serverService.getServers()).thenReturn(Collections.singletonList(server1));

        assertThrows(InvalidRequestException.class, () -> loadBalancingService.getServer());
    }

    @Test
    void testGetServerMultipleServersOneHealthy() throws InvalidRequestException {
        Server server1 = Server.builder().code(UUID.randomUUID()).status(ServerStatus.HEALTHY).totalConnectionsCount(20).build();
        Server server2 = Server.builder().code(UUID.randomUUID()).status(ServerStatus.UNHEALTHY).totalConnectionsCount(5).build();
        when(serverService.getServers()).thenReturn(Arrays.asList(server1, server2));

        Server result = loadBalancingService.getServer();

        assertSame(server1, result);
        assertEquals(20, result.getTotalConnectionsCount());
    }
}