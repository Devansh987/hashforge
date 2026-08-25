package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TreeBuilderService {

    private final TreeService treeService;

    public TreeBuilderService(TreeService treeService) {
        this.treeService = treeService;
    }

    public String buildRootTree(
            Index index,
            String repositoryPath
    ) throws IOException, NoSuchAlgorithmException {

        return buildTree(
                index.getEntries(),
                repositoryPath
        );
    }

    private String buildTree(
            List<IndexEntry> entries,
            String repositoryPath
    ) throws IOException, NoSuchAlgorithmException {

        Tree tree = new Tree();

        Map<String, List<IndexEntry>> groups = new HashMap<>();

        // First: process all entries
        for (IndexEntry entry : entries) {

            String path = entry.getPath();

            if (!path.contains("/")) {

                TreeEntry treeEntry = new TreeEntry(
                        path,
                        ObjectType.BLOB,
                        entry.getHash()
                );

                tree.addEntry(treeEntry);

            } else {

                int slashIndex = path.indexOf("/");

                String directory = path.substring(0, slashIndex);

                String remainingPath =
                        path.substring(slashIndex + 1);

                IndexEntry childEntry = new IndexEntry(
                        remainingPath,
                        entry.getHash()
                );

                if (!groups.containsKey(directory)) {
                    groups.put(directory, new ArrayList<>());
                }

                groups.get(directory).add(childEntry);
            }
        }

        // Second: build child Trees
        for (Map.Entry<String, List<IndexEntry>> group
                : groups.entrySet()) {

            String childTreeHash = buildTree(
                    group.getValue(),
                    repositoryPath
            );

            TreeEntry treeEntry = new TreeEntry(
                    group.getKey(),
                    ObjectType.TREE,
                    childTreeHash
            );

            tree.addEntry(treeEntry);
        }

        return treeService.storeTree(
                tree,
                repositoryPath
        );
    }

}
