package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.Tree;
import com.hashforge.hashforge.model.TreeEntry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class TreeService {

    private final HashService hashService;

    public TreeService(HashService hashService) {
        this.hashService = hashService;
    }

    public String generateTreeHash(Tree tree)
            throws NoSuchAlgorithmException {

        byte[] bytes = serializeTree(tree);

        return hashService.generateHash(bytes);
    }

    public String storeTree(Tree tree, String repositoryPath)
            throws NoSuchAlgorithmException, IOException {

        byte[] bytes = serializeTree(tree);

        String hash = hashService.generateHash(bytes);

        Path projectPath = Path.of(repositoryPath);

        Path objectsPath = projectPath
                .resolve(".hashforge")
                .resolve("objects");

        Path objectPath = objectsPath.resolve(hash);

        if (Files.exists(objectPath)) {
            return hash;
        }

        Files.write(objectPath, bytes);

        return hash;
    }

    private byte[] serializeTree(Tree tree) {

        List<TreeEntry> entries = tree.getEntries();

        entries.sort((a, b) ->
                a.getName().compareTo(b.getName())
        );

        StringBuilder content = new StringBuilder();

        for (TreeEntry entry : entries) {

            content.append(entry.getType())
                    .append(" ")
                    .append(entry.getName())
                    .append(" ")
                    .append(entry.getHash())
                    .append("\n");
        }

        return content.toString()
                .getBytes(StandardCharsets.UTF_8);
    }
}