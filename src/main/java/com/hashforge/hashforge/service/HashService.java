package com.hashforge.hashforge.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashService {

    public String generateHash(byte[] content) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] ans = messageDigest.digest(content);

        StringBuilder s = new StringBuilder();

        for (byte b : ans) {
            s.append(String.format("%02x", b & 0xff));
        }

        return s.toString();
    }
}
