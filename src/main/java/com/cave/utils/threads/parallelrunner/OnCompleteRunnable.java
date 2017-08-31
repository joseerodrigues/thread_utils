package com.cave.utils.threads.parallelrunner;

import java.util.concurrent.CountDownLatch;

class OnCompleteRunnable implements Runnable{
    private CountDownLatch latch = null;
    private Runnable onComplete = null;


    OnCompleteRunnable(CountDownLatch latch, Runnable onComplete) {
        this.latch = latch;
        this.onComplete = onComplete;
    }

    @Override
    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            onComplete.run();
        }
    }
}
