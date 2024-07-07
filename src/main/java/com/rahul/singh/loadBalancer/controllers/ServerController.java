package com.rahul.singh.loadBalancer.controllers;

import com.rahul.singh.loadBalancer.dtos.ServerDTO;
import com.rahul.singh.loadBalancer.entities.Server;
import com.rahul.singh.loadBalancer.exceptions.ResourceNotFoundException;
import com.rahul.singh.loadBalancer.services.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/servers")
@Slf4j
public class ServerController {

    @Autowired
    ServerService serverService;

    @GetMapping
    public ResponseEntity getAllServers() {
        try {
            List<ServerDTO> servers = serverService.getServerDTOs();
            return ResponseEntity.ok(servers);
        } catch (Exception e) {
            log.error("Server error occurred while fetching servers", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity addServer() {
        try {
            UUID serverCode = UUID.randomUUID();
            Server server = serverService.addServer(serverCode);
            ServerDTO serverDTO = ServerDTO.builder()
                    .code(server.getCode().toString())
                    .status(server.getStatus().toString())
                    .totalConnectionsCount(server.getTotalConnectionsCount())
                    .build();
            return ResponseEntity.ok(serverDTO);
        } catch (Exception e) {
            log.error("Server error occurred while adding server", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{serverCode}")
    public ResponseEntity removeServer(@PathVariable UUID serverCode) {
        try {
            serverService.removeServer(serverCode);
            return ResponseEntity.ok("Server removed successfully");
        } catch (ResourceNotFoundException e) {
            log.error("Server error occurred while removing server", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Server error occurred while removing server", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{serverCode}/health-status")
    public ResponseEntity toggleServerStatus(@PathVariable UUID serverCode,
                                             @QueryParam("isHealthy") boolean isHealthy) {
        try {
            String response = serverService.toggleServerStatus(serverCode, isHealthy);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            log.error("Server error occurred while toggling server status", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Server error occurred while toggling server status", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/unhealthy")
    public ResponseEntity getUnhealthyServers() {
        try {
            List<ServerDTO> servers = serverService.getUnhealthyServerDTOs();
            return ResponseEntity.ok(servers);
        } catch (Exception e) {
            log.error("Server error occurred while fetching unhealthy servers", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
