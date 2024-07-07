package com.rahul.singh.loadBalancer.services.impl;

import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.enums.ServerStatus;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.services.ServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoundRobinLoadBalancingServiceTest {

    @Mock
    private ServerService serverService;

    @InjectMocks
    private RoundRobinLoadBalancingService loadBalancingService;

    private List<Server> servers;

    @BeforeEach
    void setUp() {
        servers = Arrays.asList(
                new Server(UUID.randomUUID(), ServerStatus.HEALTHY, 10),
                new Server(UUID.randomUUID(), ServerStatus.HEALTHY, 20),
                new Server(UUID.randomUUID(), ServerStatus.HEALTHY, 30)
        );
    }

    @Test
    void testGetServerNoServersAvailableThrowsInvalidRequestException() {
        when(serverService.getServers()).thenReturn(Collections.emptyList());
        assertThrows(InvalidRequestException.class, () -> loadBalancingService.getServer(),
                "Expected to throw an exception when no servers are available");
    }

    @Test
    void testGetServerReturnsServersInRoundRobinOrder() throws InvalidRequestException {
        when(serverService.getServers()).thenReturn(servers);
        when(serverService.getServerByIndex(anyInt())).thenAnswer(invocation -> {
            Integer index = invocation.getArgument(0);
            return servers.get(index);
        });

        assertEquals(servers.get(1), loadBalancingService.getServer(), "First call should return the first server");
        assertEquals(servers.get(2), loadBalancingService.getServer(), "Second call should return the second server");
        assertEquals(servers.get(0), loadBalancingService.getServer(), "Third call should return the third server");
        assertEquals(servers.get(1), loadBalancingService.getServer(), "Fourth call should wrap around to the first server");
    }

    @Test
    void testGetServerContinuousCallsWrapAround() throws InvalidRequestException {
        when(serverService.getServers()).thenReturn(servers);
        when(serverService.getServerByIndex(anyInt())).thenAnswer(invocation -> {
            Integer index = invocation.getArgument(0);
            return servers.get(index % servers.size());
        });

        for (int i = 0; i < servers.size() * 3; i++) {
            Server expected = servers.get((i + 1) % servers.size());
            Server actual = loadBalancingService.getServer();
            assertEquals(expected, actual, "Server should follow round-robin order even after multiple cycles");
        }
    }

}