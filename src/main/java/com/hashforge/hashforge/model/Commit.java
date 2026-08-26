package com.hashforge.hashforge.model;

public class Commit {

    private String treeHash;
    private String parentCommit;
    private String message;

    public Commit(String treeHash, String parentCommit, String message) {
        this.treeHash = treeHash;
        this.parentCommit = parentCommit;
        this.message = message;
    }

    public String getTreeHash() {
        return treeHash;
    }

    public void setTreeHash(String treeHash) {
        this.treeHash = treeHash;
    }

    public String getParentCommit() {
        return parentCommit;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
