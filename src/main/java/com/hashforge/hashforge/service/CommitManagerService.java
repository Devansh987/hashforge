package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.Commit;
import com.hashforge.hashforge.model.CommitLog;
import com.hashforge.hashforge.model.Index;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommitManagerService {

    private final IndexService indexService;
    private final CommitService commitService;
    private final HeadService headService;
    private final TreeBuilderService treeBuilderService;

    public CommitManagerService(IndexService indexService, CommitService commitService, HeadService headService, TreeBuilderService treeBuilderService) {
        this.indexService = indexService;
        this.commitService = commitService;
        this.headService = headService;
        this.treeBuilderService = treeBuilderService;
    }


    public String createCommit(String message, String repositoryPath) throws IOException, NoSuchAlgorithmException {
        Index index = indexService.loadIndex(repositoryPath);
        String treeHash = treeBuilderService.buildRootTree(index,repositoryPath);
        String parentHash = headService.getHead(repositoryPath);
        Commit commit = new Commit(treeHash,parentHash,message);
        String CommitHash = commitService.storeCommit(commit,repositoryPath);
        headService.updateHead(CommitHash,repositoryPath);
        return CommitHash;
    }

    public List<CommitLog> log(String repositoryPath) throws IOException {
        String currentHash = headService.getHead(repositoryPath);
        List<CommitLog> history = new ArrayList<>();
        if (currentHash == null) {
            return history;
        }


        while (currentHash != null) {

            Commit commit = commitService.loadCommit(
                    currentHash,
                    repositoryPath
            );
            CommitLog logs =  new CommitLog(currentHash,commit.getMessage());
            history.add(logs);


            currentHash = commit.getParentCommit();
        }

        return history;
    }
}
