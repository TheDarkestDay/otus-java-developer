package com.abrenchev.cachehw;

public enum CacheEvent {
    CACHE_MISSED("CACHE_MISSED"),
    VALUE_READ("VALUE_READ"),
    VALUE_ADDED("VALUE_ADDED"),
    VALUE_REMOVED("VALUE_REMOVED");

    private String description;

    CacheEvent(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CacheEvent{" +
                "description='" + description + '\'' +
                '}';
    }
}
