package com.rahul.singh.loadBalancer.services;

import com.rahul.singh.loadBalancer.dtos.ServerDTO;
import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.enums.ServerStatus;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ServerService {

    public static final String SERVER_MARKED_HEALTHY_SUCCESSFULLY = "Server marked healthy successfully";
    public static final String SERVER_MARKED_UNHEALTHY_SUCCESSFULLY = "Server marked unhealthy successfully";
    List<Server> servers = new ArrayList<>();

    List<Server> unhealthyServers = new ArrayList<>();

    // add a healthy server to the list of servers
    public Server addServer(UUID serverCode) {
        Server server = Server.builder()
                .code(serverCode)
                .status(ServerStatus.HEALTHY)
                .totalConnectionsCount(0)
                .build();
        servers.add(server);
        return server;
    }

    // remove a server from the list of servers
    public void removeServer(UUID serverCode) throws ResourceNotFoundException {
        // remove the server with the given id else throw error if server not found
        Server concernedServer = servers.stream().filter(server -> server.getCode().equals(serverCode)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Server with given id %s not found", serverCode.toString())));
        servers.remove(concernedServer);

    }

    // remove unhealthy servers from the list of servers
    public void removeUnhealthyServers() {
        servers.stream().filter(server -> !server.isHealthy()).forEach(server -> unhealthyServers.add(server));
        servers.removeIf(server -> !server.isHealthy());
    }

    public void moveHealthyServersBack() {
        unhealthyServers.stream().filter(Server::isHealthy).forEach(server -> servers.add(server));
        unhealthyServers.removeIf(Server::isHealthy);
    }

    // Get all servers
    public List<Server> getServers() {
        return servers;
    }

    public List<ServerDTO> getServerDTOs() {
        List<ServerDTO> serverDTOs = new ArrayList<>();
        for (Server server : servers) {
            serverDTOs.add(ServerDTO.builder()
                    .code(server.getCode().toString())
                    .status(server.getStatus().toString())
                    .totalConnectionsCount(server.getTotalConnectionsCount())
                    .build());
        }
        return serverDTOs;
    }

    public Server getServerByIndex(Integer index) throws InvalidRequestException {
        if (index > servers.size()) {
            throw new InvalidRequestException("Invalid index for serverList");
        }

        return servers.get(index);
    }

    public String toggleServerStatus(UUID serverCode, boolean isHealthy) throws ResourceNotFoundException {
        if (isHealthy) {
            markServerHealthy(serverCode);
            return SERVER_MARKED_HEALTHY_SUCCESSFULLY;
        } else {
            markServerUnhealthy(serverCode);
            return SERVER_MARKED_UNHEALTHY_SUCCESSFULLY;
        }
    }

    public void markServerUnhealthy(UUID serverCode) throws ResourceNotFoundException {
        //update server health or throw exception
        Server concernedServer = servers.stream().filter(server -> server.getCode().equals(serverCode)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Server with given id %s not found", serverCode.toString())));
        concernedServer.setStatus(ServerStatus.UNHEALTHY);
    }

    public List<ServerDTO> getUnhealthyServerDTOs() {
        List<ServerDTO> serverDTOs = new ArrayList<>();
        for (Server server : unhealthyServers) {
            serverDTOs.add(ServerDTO.builder()
                    .code(server.getCode().toString())
                    .status(server.getStatus().toString())
                    .totalConnectionsCount(server.getTotalConnectionsCount())
                    .build());
        }
        return serverDTOs;
    }

    public void markServerHealthy(UUID serverCode) throws ResourceNotFoundException {
        //update server health or throw exception
        Server concernedServer = unhealthyServers.stream().filter(server -> server.getCode().equals(serverCode)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Server with given id %s not found", serverCode.toString())));
        concernedServer.setStatus(ServerStatus.HEALTHY);
    }
}
