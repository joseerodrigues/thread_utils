package com.cave.utils.threads.parallelrunner;

class ParallelRunnerBuilder {

    private final ParallelRunnerConfig config = new ParallelRunnerConfig();

    ParallelRunnerBuilder() {
    }

    private void checkExpr(boolean expr, String msg){
        if (!expr){
            throw new IllegalArgumentException(msg);
        }
    }

    public ParallelRunnerBuilder withThreads(int numberOfThreads){
        checkExpr(numberOfThreads > 0, "numberOfThreads : " + numberOfThreads);
        config.setNumberOfThreads(numberOfThreads);
        return this;
    }

    public ParallelRunnerBuilder queueSize(int queueSize){
        checkExpr(queueSize > 0, "queueSize : " + queueSize);
        config.setQueueSize(queueSize);
        return this;
    }

    public ParallelRunner build(){
        return new ParallelRunner(config);
    }
}
