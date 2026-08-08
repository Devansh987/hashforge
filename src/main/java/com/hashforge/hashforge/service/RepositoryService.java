package com.hashforge.hashforge.service;


import com.hashforge.hashforge.model.RepositoryStatus;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class RepositoryService {


    public RepositoryStatus initRepository(String path) {
        Path projectpath = Path.of(path);

        if(!Files.exists(projectpath)){
            System.out.println("Project path does not exist");
            return RepositoryStatus.PROJECT_NOT_FOUND;
        }

        if(!Files.isDirectory(projectpath)){
            System.out.println("Given Project path is not a directory");
            return RepositoryStatus.NOT_A_DIRECTORY;
        }
        Path repositorypath = projectpath.resolve(".hashforge");

        if(!Files.exists(repositorypath)){
            try {
                Files.createDirectory(repositorypath);
            } catch (IOException e) {
                System.out.println("Failed to create .hashforge directory: " + e.getMessage());
                return RepositoryStatus.FAILED;
            }
            return RepositoryStatus.CREATED;
        }
        return RepositoryStatus.ALREADY_EXISTS;
    }
}
