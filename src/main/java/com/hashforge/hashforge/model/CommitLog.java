package com.hashforge.hashforge.model;

public class CommitLog {

    private String currentHash;
    private String Message;

    public CommitLog(String currentHash,String Message) {
        this.currentHash = currentHash;
        this.Message = Message;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public void setCurrentHash(String currentHash) {
        this.currentHash = currentHash;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
