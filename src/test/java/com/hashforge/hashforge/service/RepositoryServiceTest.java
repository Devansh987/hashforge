package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.RepositoryStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class RepositoryServiceTest {

     RepositoryService repositoryService =  new RepositoryService();

     @TempDir
    Path tempDir;

     @Test
     void ShouldCreate(){

         Path repositoryPath = tempDir;
         RepositoryStatus status =
                 repositoryService.initRepository(repositoryPath.toString());

         assertEquals(RepositoryStatus.CREATED,status);

         assertTrue(Files.exists(repositoryPath.resolve(".hashforge")));
     }

    @Test
    void shouldReturnAlreadyExists() throws IOException {

        // Arrange
        Path projectPath = tempDir;
        Path repositoryPath = projectPath.resolve(".hashforge");

        Files.createDirectory(repositoryPath);

        // Act
        RepositoryStatus status =
                repositoryService.initRepository(projectPath.toString());

        // Assert
        assertEquals(
                RepositoryStatus.ALREADY_EXISTS,
                status
        );
    }

    @Test
    void shouldReturnProjectNotFound() {

        // Arrange
        Path projectPath =
                tempDir.resolve("does-not-exist");

        // Act
        RepositoryStatus status =
                repositoryService.initRepository(projectPath.toString());

        // Assert
        assertEquals(
                RepositoryStatus.PROJECT_NOT_FOUND,
                status
        );
    }

    @Test
    void shouldReturnNotADirectory() throws IOException {


        Path filePath =
                tempDir.resolve("test.txt");

        Files.createFile(filePath);

        RepositoryStatus status =
                repositoryService.initRepository(filePath.toString());


        assertEquals(
                RepositoryStatus.NOT_A_DIRECTORY,
                status
        );
    }


}
