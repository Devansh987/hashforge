package com.hashforge.hashforge.service;


import com.hashforge.hashforge.Dto.RepositoryStatusResponse;
import com.hashforge.hashforge.model.Index;
import com.hashforge.hashforge.model.IndexEntry;
import com.hashforge.hashforge.model.RepositoryStatus;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class RepositoryService {

    private final IndexService indexService;
    private final HashService hashService;

    public RepositoryService(IndexService indexService,HashService hashService) {
        this.indexService = indexService;
        this.hashService = hashService;
    }

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
                Path objectsPath = repositorypath.resolve("objects");
                Files.createDirectory(objectsPath);

            } catch (IOException e) {
                System.out.println("Failed to create .hashforge directory: " + e.getMessage());
                return RepositoryStatus.FAILED;
            }
            return RepositoryStatus.CREATED;
        }
        return RepositoryStatus.ALREADY_EXISTS;
    }


    public RepositoryStatusResponse getStatus(String repositoryPath)
            throws IOException, NoSuchAlgorithmException {
        List<String> staged = new ArrayList<>();
        List<String> modified = new ArrayList<>();
        List<String> untracked = new ArrayList<>();

        Index index;


        Path projectPath = Path.of(repositoryPath);
        Path hashForgePath = projectPath.resolve(".hashforge");
        Path indexPath = hashForgePath.resolve("index");


        if (Files.exists(indexPath)) {
            index = indexService.loadIndex(repositoryPath);
        } else {
            index = new Index();
        }

        try(Stream<Path> paths  = Files.walk(projectPath)){

            List<Path> files = paths.filter(Files::isRegularFile).filter(path -> !path.startsWith(hashForgePath))
                    .toList();

            for (Path file : files) {

                Path relativePath = projectPath.relativize(file);
                String relativeFilePath = relativePath.toString();
                IndexEntry indexedEntry = null;

                for (IndexEntry entry : index.getEntries()) {

                    if (entry.getPath().equals(relativeFilePath)) {
                        indexedEntry = entry;
                        break;
                    }
                }

                if (indexedEntry != null) {

                    byte[] content = Files.readAllBytes(file);

                    String currentHash =
                            hashService.generateHash(content);

                    if (currentHash.equals(indexedEntry.getHash())) {
                        staged.add(relativeFilePath);
                    }else{
                        modified.add(relativeFilePath);
                    }
                }else{
                    untracked.add(relativeFilePath);
                }

            }
        }

        return new RepositoryStatusResponse(staged,modified,untracked);

    }
}
