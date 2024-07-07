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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomSelectionLoadBalancingServiceTest {

    @Mock
    private ServerService serverService;

    @InjectMocks
    private RandomSelectionLoadBalancingService loadBalancingService;

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
    void testGetServerWithSingleServerReturnsTheServer() throws InvalidRequestException {
        when(serverService.getServers()).thenReturn(Collections.singletonList(servers.get(0)));
        when(serverService.getServerByIndex(0)).thenReturn(servers.get(0));

        Server result = loadBalancingService.getServer();

        assertEquals(servers.get(0), result);
    }

    @Test
    void testGetServerMultipleServersCheckRandomness() throws InvalidRequestException {
        when(serverService.getServers()).thenReturn(servers);
        when(serverService.getServerByIndex(anyInt())).thenAnswer(invocation -> {
            Integer index = invocation.getArgument(0);
            return servers.get(index);
        });

        boolean differentSelections = false;
        Server firstSelected = loadBalancingService.getServer();
        for (int i = 0; i < 10; i++) {
            Server selected = loadBalancingService.getServer();
            if (!selected.equals(firstSelected)) {
                differentSelections = true;
                break;
            }
        }

        assertTrue(differentSelections);
    }

    @Test
    void testGetServerNoServersAvailableThrowsInvalidRequestException() {
        when(serverService.getServers()).thenReturn(Collections.emptyList());

        assertThrows(InvalidRequestException.class, () -> loadBalancingService.getServer());
    }

}