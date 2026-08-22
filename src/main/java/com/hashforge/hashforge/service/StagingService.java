package com.hashforge.hashforge.service;


import com.hashforge.hashforge.model.Index;
import com.hashforge.hashforge.model.IndexEntry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

@Service
public class StagingService {

    private final BlobService blobService;
    private final IndexService indexService;

    public StagingService(BlobService blobService,IndexService indexService){
        this.blobService = blobService;
        this.indexService = indexService;
    }

    public void stageFiles(String filepath , String repositoryPath) throws IOException, NoSuchAlgorithmException {

        Path path = Path.of(filepath);
        byte[] content = Files.readAllBytes(path);
        String hash = blobService.storeBlob(content,repositoryPath);
        Path projectPath = Path.of(repositoryPath);

        Path relativePath = projectPath.relativize(path);

        String relativeFilePath = relativePath.toString();

        IndexEntry entry = new IndexEntry(
                relativeFilePath,
                hash
        );

        Index index;

        Path indexPath = projectPath
                .resolve(".hashforge")
                .resolve("index");

        if (Files.exists(indexPath)) {
            index = indexService.loadIndex(repositoryPath);
        } else {
            index = new Index();
        }

        index.addEntry(entry);

        indexService.saveIndex(
                index,
                repositoryPath
        );
    }


    public void stageDirectory(String directoryPath, String repositoryPath)
            throws IOException, NoSuchAlgorithmException {

        Path path = Path.of(directoryPath);

        Path hashForgePath = Path.of(repositoryPath)
                .resolve(".hashforge");

        try (Stream<Path> paths = Files.walk(path)) {

            for (Path file : paths
                    .filter(Files::isRegularFile)
                    .toList()) {

                if (!file.startsWith(hashForgePath)) {
                    stageFiles(
                            file.toString(),
                            repositoryPath
                    );
                }
            }
        }
    }
}
