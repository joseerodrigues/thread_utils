package com.cave.utils.threads.parallelrunner;

import java.util.concurrent.CountDownLatch;

class LatchRunnable implements Runnable {

    private CountDownLatch latch = null;
    private Runnable runnable = null;

    LatchRunnable(CountDownLatch latch, Runnable runnable) {
        this.latch = latch;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try{
            runnable.run();
        }finally {
            latch.countDown();
        }
    }
}
