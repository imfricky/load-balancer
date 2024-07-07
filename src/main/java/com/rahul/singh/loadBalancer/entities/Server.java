package com.rahul.singh.loadBalancer.entities;

import com.rahul.singh.loadBalancer.enums.ServerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Server {

    private UUID code;

    private ServerStatus status;

    private Integer totalConnectionsCount;

    public boolean isHealthy() {
        return status == ServerStatus.HEALTHY;
    }

    public void incrementTotalConnectionsCount() {
        totalConnectionsCount++;
    }

    public String getResponse() {
        return String.format("Dummy response from server %s", code.toString());
    }

}
