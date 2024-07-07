package com.rahul.singh.loadBalancer.controllers;

import com.rahul.singh.loadBalancer.dtos.ServerResponseDTO;
import com.rahul.singh.loadBalancer.exceptions.InvalidRequestException;
import com.rahul.singh.loadBalancer.services.LoadBalancerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/loadBalancer")
@Slf4j
public class LoadBalancerController {

    @Autowired
    LoadBalancerService loadBalancerService;

    @GetMapping
    public ResponseEntity getServerResponse() {
        try {
            ServerResponseDTO serverResponse = loadBalancerService.getServerResponse();
            return ResponseEntity.ok(serverResponse);
        } catch (InvalidRequestException e) {
            log.error("Error occurred while getting server response", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Server error occurred while getting server response", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/loadBalancerType/{loadBalancerType}")
    public ResponseEntity setLoadBalancerType(@PathVariable("loadBalancerType") String loadBalancerType) {
        try {
            loadBalancerService.setLoadBalancerType(loadBalancerType);
            String response = "Load balancer type set to " + loadBalancerType;
            return ResponseEntity.ok(response);
        } catch (InvalidRequestException e) {
            log.error("Error occurred while changing load balancer type", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Server error occurred while changing load balancer type", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/loadBalancerType")
    public ResponseEntity getLoadBalancerType() {
        try {
            String loadBalancerType = loadBalancerService.getLoadBalancerType();
            String response = "Current Load balancer type is: " + loadBalancerType;
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Server error occurred while fetching load balancer type", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
