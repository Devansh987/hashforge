package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.Index;
import com.hashforge.hashforge.model.IndexEntry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class IndexService {

    public void saveIndex(Index index , String repositoryPath) throws IOException {

        Path indexPath = Path.of(repositoryPath).resolve(".hashforge").resolve("index");

        StringBuilder content = new StringBuilder();

        for(IndexEntry in : index.getEntries()){
            String s = in.getPath() + " " + in.getHash() + "\n";
            content.append(s);
        }

        Files.writeString(
                indexPath,
                content.toString()
        );

    }
}
