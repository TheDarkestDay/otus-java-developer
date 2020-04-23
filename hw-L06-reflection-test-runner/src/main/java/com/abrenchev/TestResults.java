package com.abrenchev;

import java.util.Map;

public class TestResults {
    private int totalCount;

    private int failedCount;

    private Map<String, String> testErrors;

    public TestResults(int totalCount, int failedCount, Map<String, String> testErrors) {
        this.totalCount = totalCount;
        this.failedCount = failedCount;
        this.testErrors = testErrors;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public Map<String, String> getTestErrors() {
        return testErrors;
    }
}
