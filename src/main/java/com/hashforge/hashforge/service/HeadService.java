package com.hashforge.hashforge.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class HeadService {

    private CommitService commitService;

    public HeadService(CommitService commitService) {
        this.commitService = commitService;
    }

    public String getHead(String repositoryPath) throws IOException {

        Path headPath = Path.of(repositoryPath)
                .resolve(".hashforge")
                .resolve("HEAD");

        if (!Files.exists(headPath)) {
            return null;
        }

        return Files.readString(headPath).trim();

    }

    public void updateHead(String commitHash, String repositoryPath) throws IOException {

        Path headPath = Path.of(repositoryPath).resolve(".hashforge").resolve("HEAD");
        Files.writeString(headPath, commitHash);
    }
}
