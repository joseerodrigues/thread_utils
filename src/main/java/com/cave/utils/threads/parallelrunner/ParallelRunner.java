package com.cave.utils.threads.parallelrunner;

import com.cave.utils.threads.Producer;

import java.util.concurrent.CountDownLatch;

public class ParallelRunner {
    private ParallelRunnerConfig config = null;
    private Producer<Runnable> producer = null;

    ParallelRunner(ParallelRunnerConfig config){
        this.config = config;
    }

    public static ParallelRunnerBuilder builder(){
        return new ParallelRunnerBuilder();
    }

    public void start(){
        this.producer = Producer.create(Runnable.class, new RunnableConsumer(), this.config.getNumberOfThreads());
    }

    public void stop(){
        this.producer.stop();
    }

    private CountDownLatch submitAndLatch(Runnable ... actions){
        if (actions == null){
            throw new NullPointerException("actions");
        }

        int actionSize = actions.length;
        CountDownLatch latch = new CountDownLatch(actionSize);

        for (Runnable r : actions){
            if (r != null){
                this.producer.submit(new LatchRunnable(latch, r));
            }
        }

        return latch;
    }

    /**
     * async callBack
     * @param onComplete
     * @param actions
     */
    public void run(Runnable onComplete, Runnable ... actions){
        CountDownLatch latch = submitAndLatch(actions);
        this.producer.submit(new OnCompleteRunnable(latch, onComplete));
    }

    public void runSync(Runnable ... actions){
        CountDownLatch latch = submitAndLatch(actions);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
}
