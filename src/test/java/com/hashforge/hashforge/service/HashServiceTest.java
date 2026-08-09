package com.hashforge.hashforge.service;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HashServiceTest {

    HashService hash = new HashService();


    @Test
    void shouldGenerateSameHash() throws NoSuchAlgorithmException {
        byte[] content1 =
                "Hello HashForge".getBytes(StandardCharsets.UTF_8);

        byte[] content2 =
                "Hello HashForge".getBytes(StandardCharsets.UTF_8);

        String a = hash.generateHash(content1);
        String b = hash.generateHash(content2);

        assertEquals(a, b);
    }

    @Test
    void shouldGenerateDifferentHashForDifferentContent()
            throws NoSuchAlgorithmException {

        byte[] content1 =
                "Hello HashForge".getBytes(StandardCharsets.UTF_8);

        byte[] content2 =
                "Hello hashforge".getBytes(StandardCharsets.UTF_8);

        String hash1 = hash.generateHash(content1);
        String hash2 = hash.generateHash(content2);

        assertNotEquals(hash1, hash2);
    }


    @Test
    void shouldGenerate64CharacterHash()
            throws NoSuchAlgorithmException {

        byte[] content =
                "Hello HashForge".getBytes(StandardCharsets.UTF_8);

        String has = hash.generateHash(content);

        assertEquals(64, has.length());
    }
}
