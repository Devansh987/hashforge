package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.ObjectType;
import com.hashforge.hashforge.model.Tree;
import com.hashforge.hashforge.model.TreeEntry;
import net.bytebuddy.ClassFileVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class TreeServiceTest {

    private final TreeService treeService;

    TreeServiceTest() {
        HashService hashService = new HashService();
        treeService = new TreeService(hashService);
    }

    @Test
    void shouldGenerateTreeHash() throws NoSuchAlgorithmException {

        Tree tree = new Tree();

        tree.addEntry(
                new TreeEntry(
                        "Main.java",
                        ObjectType.BLOB,
                        "ABC123"
                )
        );

        tree.addEntry(
                new TreeEntry(
                        "User.java",
                        ObjectType.BLOB,
                        "DEF456"
                )
        );

        String hash = treeService.generateTreeHash(tree);

        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @TempDir
    Path tempDir;

    @Test
    void shouldStoreTree() throws Exception {


        Files.createDirectories(
                tempDir.resolve(".hashforge").resolve("objects")
        );

        Tree tree = new Tree();

        tree.addEntry(
                new TreeEntry(
                        "Main.java",
                        ObjectType.BLOB,
                        "ABC123"
                )
        );

        String hash = treeService.storeTree(
                tree,
                tempDir.toString()
        );

        Path objectPath = tempDir
                .resolve(".hashforge")
                .resolve("objects")
                .resolve(hash);

        assertTrue(Files.exists(objectPath));
    }
}