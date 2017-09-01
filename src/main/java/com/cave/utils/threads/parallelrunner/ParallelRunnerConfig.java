package com.cave.utils.threads.parallelrunner;

class ParallelRunnerConfig {

    private int numberOfThreads = Runtime.getRuntime().availableProcessors() * 2;
    private int queueSize = 5000;

    int getNumberOfThreads() {
        return numberOfThreads;
    }

    void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    int getQueueSize() {
        return queueSize;
    }

    void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
