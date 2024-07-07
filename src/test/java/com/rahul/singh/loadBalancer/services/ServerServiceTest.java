package com.rahul.singh.loadBalancer.services;

import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.enums.ServerStatus;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

    private ServerService serverService;

    @BeforeEach
    void setUp() {
        serverService = new ServerService();
    }

    @Test
    void addServerShouldAddHealthyServer() {
        UUID serverCode = UUID.randomUUID();
        Server server = serverService.addServer(serverCode);
        assertNotNull(server);
        assertEquals(ServerStatus.HEALTHY, server.getStatus());
        assertTrue(serverService.getServers().contains(server));
    }

    @Test
    void removeServerShouldRemoveServer() throws ResourceNotFoundException {
        UUID serverCode = UUID.randomUUID();
        Server server = serverService.addServer(serverCode);
        serverService.removeServer(serverCode);
        assertFalse(serverService.getServers().contains(server));
    }

    @Test
    void removeServerThrowsWhenServerNotFound() {
        UUID serverCode = UUID.randomUUID();
        assertThrows(ResourceNotFoundException.class, () -> serverService.removeServer(serverCode));
    }

    @Test
    void removeUnhealthyServersShouldRemoveOnlyUnhealthyServers() throws ResourceNotFoundException {
        Server healthyServer = serverService.addServer(UUID.randomUUID());
        Server unhealthyServer = serverService.addServer(UUID.randomUUID());
        serverService.markServerUnhealthy(unhealthyServer.getCode());

        serverService.removeUnhealthyServers();

        assertTrue(serverService.getServers().contains(healthyServer));
        assertFalse(serverService.getServers().contains(unhealthyServer));
    }

    @Test
    void moveHealthyServersBackShouldMoveHealthyServers() throws ResourceNotFoundException {
        UUID serverCode = UUID.randomUUID();
        Server server = serverService.addServer(serverCode);
        serverService.markServerUnhealthy(serverCode);

        assertFalse(serverService.getUnhealthyServerDTOs().contains(server));
    }

    @Test
    void toggleServerStatusTogglesStatusCorrectly() throws ResourceNotFoundException {
        UUID serverCode = UUID.randomUUID();
        serverService.addServer(serverCode);
        String response = serverService.toggleServerStatus(serverCode, false);
        assertEquals(ServerService.SERVER_MARKED_UNHEALTHY_SUCCESSFULLY, response);

    }

    @Test
    void getServerByIndexThrowsInvalidRequestException() {
        serverService.addServer(UUID.randomUUID());
        assertThrows(InvalidRequestException.class, () -> serverService.getServerByIndex(10));
    }

}