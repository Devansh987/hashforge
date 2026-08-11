package com.hashforge.hashforge.model;

public class TreeEntry {

    private String name;
    private ObjectType type;
    private String hash;

    public TreeEntry(String name, ObjectType type, String hash) {
        this.name = name;
        this.type = type;
        this.hash = hash;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
