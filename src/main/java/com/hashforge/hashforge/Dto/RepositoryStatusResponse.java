package com.hashforge.hashforge.Dto;

import java.util.List;

public class RepositoryStatusResponse {

    private List<String> staged;
    private List<String> modified;
    private List<String> untracked;

    public RepositoryStatusResponse(List<String> staged, List<String> modified, List<String> untracked) {
        this.staged = staged;
        this.modified = modified;
        this.untracked = untracked;
    }

    public List<String> getStaged() {
        return staged;
    }

    public List<String> getModified() {
        return modified;
    }

    public List<String> getUntracked() {
        return untracked;
    }
}
