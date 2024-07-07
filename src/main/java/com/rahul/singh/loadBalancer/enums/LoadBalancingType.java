package com.rahul.singh.loadBalancer.enums;

public enum LoadBalancingType {

    ROUND_ROBIN,
    RANDOM_SELECTION,
    LEAST_CONNECTION;

    public static LoadBalancingType fromString(String type) {
        for (LoadBalancingType loadBalancingType : LoadBalancingType.values()) {
            if (loadBalancingType.toString().equalsIgnoreCase(type)) {
                return loadBalancingType;
            }
        }
        return null;
    }

}
