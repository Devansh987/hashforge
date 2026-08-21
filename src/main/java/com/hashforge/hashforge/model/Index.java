package com.hashforge.hashforge.model;

import java.util.ArrayList;
import java.util.List;

public class Index {

    public List<IndexEntry> entries = new ArrayList<>();

    public void addEntry(IndexEntry entry){
        int n = entries.size();
        for(int i = 0;i<n;i++){
            if (entries.get(i).getPath().equals(entry.getPath())) {
                entries.set(i, entry);
                return;
            }

        }
        entries.add(entry);

    }

    public void removeEntry(String path) {

        entries.removeIf(entry ->
                entry.getPath().equals(path)
        );
    }

    public List<IndexEntry> getEntries() {
        return entries;
    }


}



