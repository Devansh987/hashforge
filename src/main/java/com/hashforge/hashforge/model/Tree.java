package com.hashforge.hashforge.model;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    private List<TreeEntry> entries = new ArrayList<>();

    public void addEntry(TreeEntry entry){
        entries.add(entry);
    }
    public List<TreeEntry> getEntries() {
        return entries;
    }
}
