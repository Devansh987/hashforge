package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.Commit;
import com.hashforge.hashforge.model.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommitManagerServiceTest {

    private IndexService indexService;
    private TreeBuilderService treeBuilderService;
    private CommitService commitService;
    private HeadService headService;

    private CommitManagerService commitManagerService;

    private Path repositoryPath;

    @BeforeEach
    void setUp() throws IOException {

        indexService = mock(IndexService.class);
        treeBuilderService = mock(TreeBuilderService.class);
        commitService = mock(CommitService.class);
        headService = mock(HeadService.class);

        commitManagerService = new CommitManagerService(
                indexService,
                commitService,
                headService,
                treeBuilderService
        );

        repositoryPath = Files.createTempDirectory("hashforge-test");

        Files.createDirectories(
                repositoryPath
                        .resolve(".hashforge")
                        .resolve("objects")
        );
    }

    @Test
    void shouldCreateFirstCommit() throws IOException, NoSuchAlgorithmException {

        // Arrange

        Index index = new Index();

        when(indexService.loadIndex(repositoryPath.toString()))
                .thenReturn(index);

        when(treeBuilderService.buildRootTree(
                index,
                repositoryPath.toString()
        )).thenReturn("TREE123");

        when(headService.getHead(repositoryPath.toString()))
                .thenReturn(null);

        when(commitService.storeCommit(
                any(Commit.class),
                eq(repositoryPath.toString())
        )).thenReturn("COMMIT123");


        // Act

        String result = commitManagerService.createCommit(
                "Initial commit",
                repositoryPath.toString()
        );


        // Assert

        assertEquals("COMMIT123", result);

        verify(indexService)
                .loadIndex(repositoryPath.toString());

        verify(treeBuilderService)
                .buildRootTree(
                        index,
                        repositoryPath.toString()
                );

        verify(headService)
                .getHead(repositoryPath.toString());

        verify(commitService)
                .storeCommit(
                        any(Commit.class),
                        eq(repositoryPath.toString())
                );

        verify(headService)
                .updateHead(
                        "COMMIT123",
                        repositoryPath.toString()
                );
    }


    @Test
    void shouldCreateSecondCommitWithPreviousCommitAsParent()
            throws IOException, NoSuchAlgorithmException {

        // Arrange

        Index index = new Index();

        when(indexService.loadIndex(repositoryPath.toString()))
                .thenReturn(index);

        when(treeBuilderService.buildRootTree(
                index,
                repositoryPath.toString()
        )).thenReturn("TREE456");

        when(headService.getHead(repositoryPath.toString()))
                .thenReturn("COMMIT123");

        when(commitService.storeCommit(
                any(Commit.class),
                eq(repositoryPath.toString())
        )).thenReturn("COMMIT456");


        // Act

        String result = commitManagerService.createCommit(
                "Updated Main",
                repositoryPath.toString()
        );


        // Assert

        assertEquals("COMMIT456", result);

        verify(headService)
                .getHead(repositoryPath.toString());

        verify(commitService)
                .storeCommit(
                        any(Commit.class),
                        eq(repositoryPath.toString())
                );

        verify(headService)
                .updateHead(
                        "COMMIT456",
                        repositoryPath.toString()
                );
    }

    @Test
    void shouldTraverseCommitHistory()
            throws IOException, NoSuchAlgorithmException {

        when(headService.getHead(repositoryPath.toString()))
                .thenReturn("COMMIT003");

        Commit commit3 = new Commit(
                "TREE003",
                "COMMIT002",
                "Third commit"
        );

        Commit commit2 = new Commit(
                "TREE002",
                "COMMIT001",
                "Second commit"
        );

        Commit commit1 = new Commit(
                "TREE001",
                null,
                "Initial commit"
        );

        when(commitService.loadCommit(
                "COMMIT003",
                repositoryPath.toString()
        )).thenReturn(commit3);

        when(commitService.loadCommit(
                "COMMIT002",
                repositoryPath.toString()
        )).thenReturn(commit2);

        when(commitService.loadCommit(
                "COMMIT001",
                repositoryPath.toString()
        )).thenReturn(commit1);

        commitManagerService.log(repositoryPath.toString());

        verify(commitService).loadCommit(
                "COMMIT003",
                repositoryPath.toString()
        );

        verify(commitService).loadCommit(
                "COMMIT002",
                repositoryPath.toString()
        );

        verify(commitService).loadCommit(
                "COMMIT001",
                repositoryPath.toString()
        );
    }
}
