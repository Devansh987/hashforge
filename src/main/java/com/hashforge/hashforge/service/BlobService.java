package com.hashforge.hashforge.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@Service
public class BlobService {

   private final HashService hashService;

   public BlobService(HashService hashService){
       this.hashService = hashService;
   }

    public String storeBlob(byte[] content, String repositoryPath) throws NoSuchAlgorithmException, IOException {
        String hash = hashService.generateHash(content);
        Path projectPath = Path.of(repositoryPath);
        Path hashForgePath = projectPath.resolve(".hashforge");
        Path objectsPath = hashForgePath.resolve("objects");
        Path object = objectsPath.resolve(hash);

        if(Files.exists(object)) return hash;
        Files.write(object,content);
        return hash;
    }
}
