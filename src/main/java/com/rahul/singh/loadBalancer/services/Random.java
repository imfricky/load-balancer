package com.rahul.singh.loadBalancer.services;

import org.springframework.stereotype.Service;

@Service
public class Random {

    public int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

}
