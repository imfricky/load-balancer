# Load Balancer Project

## Overview
This project implements a simple load balancer to distribute incoming network traffic across multiple servers to ensure reliability and high availability. The load balancer supports different strategies such as Round Robin, Random Selection and Least Connections to efficiently manage the server load.

## High-Level Design (HLD)
The load balancer system consists of the following components:

- **Client**: Sends requests to the load balancer.
- **Load Balancer**: Routes incoming requests to one of the available servers based on the selected load balancing strategy.
- **Load Balancing Strategy**: Algorithm that determines which server will handle the incoming request.
- **Server Pool**: Consists of multiple servers that handle the requests sent from the load balancer.
- **Scheduled Job**: Regularly checks the health of servers and removes any that are not responding.

## Low-Level Design (LLD)
The load balancer's operation can be detailed as follows:

1. **Request Reception**: The load balancer receives a request from the client.
2. **Strategy Execution**: Based on the configured load balancing strategy, the load balancer selects a server from the pool.
3. **Request Forwarding**: The selected server receives the request for processing.
4. **Health Check**: A scheduled job runs at regular intervals to evaluate the health of each server, removing any that are unresponsive from the pool.

### Load Balancing Strategies
- **Round Robin**: Distributes requests sequentially across all servers.
- **Least Connections**: Routes requests to the server with the fewest active connections.
- **Random Selection**: Routes requests to server in random order.


## Project Setup

### Compile
- Compile the project using
```mvn clean install```

### Run
- Run the project using
```./mvnw spring-boot:run```


## APIs

{baseUrl} here refer to localhost:8099.


### Server

- **GET {baseUrl}/api/servers**
  - Description: Retrieves all the servers present in server pool.
  - Response:
     ```json
    [
    {
        "code": "a866a17e-d4e3-4fc2-827c-c08a680d53ed",
        "status": "HEALTHY",
        "totalConnectionsCount": 4
    },
    {
        "code": "649c1efe-cbfc-471b-8145-e2280ae62835",
        "status": "HEALTHY",
        "totalConnectionsCount": 3
    },
    {
        "code": "8512f431-ffa6-4685-ac80-ad2e1d9a4acf",
        "status": "HEALTHY",
        "totalConnectionsCount": 6
    },
    {
        "code": "d23dbc03-ab2b-4b0c-a2a4-df18436fde7b",
        "status": "HEALTHY",
        "totalConnectionsCount": 3
    },
    {
        "code": "7613ea51-77b2-40d2-a435-ec5637373024",
        "status": "HEALTHY",
        "totalConnectionsCount": 3
    },
    {
        "code": "88edae84-055c-4a51-a087-d1a0094c3840",
        "status": "HEALTHY",
        "totalConnectionsCount": 3
    },
    {
        "code": "824364d9-5943-4e63-9cf6-c797e7edf371",
        "status": "HEALTHY",
        "totalConnectionsCount": 3
    }
    ]
    ```


- **POST {baseUrl}/api/servers**
  - Description: Add a new healthy server.
  - Response:
    ```json
    {
    "code": "824364d9-5943-4e63-9cf6-c797e7edf371",
    "status": "HEALTHY",
    "totalConnectionsCount": 0
    }
    ```

- **DELETE {baseUrl}/api/servers/:serverCode**
  - Description: Delete server by server code.
  - Response:
    ```
    Server removed successfully
    ```

- **PUT {baseUrl}/api/servers/:serverCode/health-status?isHealthy=true**
  - Description: Make a server healthy.
  - Response:
    ```
    Server marked healthy successfully
    ```


- **PUT {baseUrl}/api/servers/:serverCode/health-status?isHealthy=false**
  - Description: Make a server unhealthy.
  - Response:
    ```
    Server marked unhealthy successfully
    ```


- **GET {baseUrl}/api/servers/unhealthy**
  - Description: Get all unhealthy servers.
  - Response:
    ```json
    [
        {
        "code": "824364d9-5943-4e63-9cf6-c797e7edf371",
        "status": "UNHEALTHY",
        "totalConnectionsCount": 3
        }
    ]
    ```

### Load Balancer

- **GET {baseUrl}/api/loadBalancer**
  - Description: Get server response via loadbalancer.
  - Response:
    ```json
    [
        {
            "response": "Dummy response from server 649c1efe-cbfc-471b-8145-e2280ae62835",
            "code": "649c1efe-cbfc-471b-8145-e2280ae62835"
        }
    ]
    ```


- **GET {baseUrl}/api/loadBalancer/loadBalancerType**
  - Description: Get load balancer strategy.
  - Response:
    ```
        Current Load balancer type is: RANDOM_SELECTION
    ```

- **PUT {baseUrl}/api/loadBalancer/loadBalancerType/:loadBalancerType**
  - Description: Update load balancer strategy type.
  - Response:
    ```
        Load balancer type set to LEAST_CONNECTION
    ```

Postman collection link: https://www.postman.com/rahulsingh-ss/workspace/load-balancer/collection/23640880-03da9eb8-6af9-4cb6-954a-eb50f6175b1c?action=share&creator=23640880

