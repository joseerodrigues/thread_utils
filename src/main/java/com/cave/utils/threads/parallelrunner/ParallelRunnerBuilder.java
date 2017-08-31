package com.cave.utils.threads.parallelrunner;

class ParallelRunnerBuilder {

    private ParallelRunnerConfig config = new ParallelRunnerConfig();

    ParallelRunnerBuilder() {
    }

    public ParallelRunnerBuilder withThreads(int numberOfThreads){
        config.setNumberOfThreads(numberOfThreads);
        return this;
    }

    public ParallelRunner build(){
        return new ParallelRunner(config);
    }
}
