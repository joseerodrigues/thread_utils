package com.cave.utils.threads.parallelrunner;

class ParallelRunnerConfig {

    private int numberOfThreads = Runtime.getRuntime().availableProcessors();

    int getNumberOfThreads() {
        return numberOfThreads;
    }

    void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }
}
