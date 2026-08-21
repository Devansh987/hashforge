package com.hashforge.hashforge.model;

public class IndexEntry {

    private String Path;
    private String hash;

    public IndexEntry(String blob, String hash) {
        this.Path = blob;
        this.hash = hash;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        this.Path = path;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
