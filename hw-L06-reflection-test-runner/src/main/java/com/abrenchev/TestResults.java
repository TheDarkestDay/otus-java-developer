package com.abrenchev;

public class TestResults {
    private int totalCount;

    private int failedCount;

    public TestResults(int totalCount, int failedCount) {
        this.totalCount = totalCount;
        this.failedCount = failedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getFailedCount() {
        return failedCount;
    }
}
