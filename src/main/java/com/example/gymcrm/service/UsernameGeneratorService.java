package com.example.gymcrm.service;


public interface UsernameGeneratorService {

    String generateUsername(String firstName, String lastName) throws IllegalArgumentException;
}
