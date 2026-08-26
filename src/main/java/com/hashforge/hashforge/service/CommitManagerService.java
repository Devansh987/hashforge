package com.hashforge.hashforge.service;

import com.hashforge.hashforge.model.Commit;
import com.hashforge.hashforge.model.Index;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
}
