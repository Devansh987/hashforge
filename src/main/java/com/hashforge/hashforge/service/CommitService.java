package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.Commit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

@Service
public class CommitService {

    private final HashService hashService;

    public CommitService(HashService hashService){
        this.hashService = hashService;
    }

    private byte[]  serializeCommit(Commit commit){

        StringBuilder content = new StringBuilder();
        content.append("tree ").append(commit.getTreeHash()).append("\n");
        content.append("parent ");
        if (commit.getParentCommit() != null) {
            content.append(commit.getParentCommit());
        }

        content.append("\n");

        content.append("message ")
                .append(commit.getMessage())
                .append("\n");

        return content.toString()
                .getBytes(StandardCharsets.UTF_8);
    }

    public String generateHash(Commit commit) throws NoSuchAlgorithmException {
        byte[] bytes = serializeCommit(commit);
        return hashService.generateHash(bytes);
    }

    public String storeCommit(Commit commit,String repositoryPath) throws NoSuchAlgorithmException, IOException {

        byte[] bytes = serializeCommit(commit);

        String hash = hashService.generateHash(bytes);
        Path projectPath = Path.of(repositoryPath).resolve(".hashforge").resolve("objects");
        Path objectPath = projectPath.resolve(hash);
        if(Files.exists(objectPath)){
            return hash;
        }
        Files.write(objectPath, bytes);
        return hash;
    }
}


