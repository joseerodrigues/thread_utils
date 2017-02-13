package com.cave.utils.threads;

import java.util.concurrent.LinkedBlockingQueue;

class ConsumerRunnable<T> implements Runnable {

    private LinkedBlockingQueue<Object> queue = null;
    private Object poison = null;
    private Consumer<T> consumer = null;
    private int sleepMs = -1;

    ConsumerRunnable(LinkedBlockingQueue<Object> queue, Object poison, int sleepMs, Consumer<T> consumer) {
        super();
        this.queue = queue;
        this.poison = poison;
        this.consumer = consumer;
        this.sleepMs = sleepMs;
    }

    @SuppressWarnings("unchecked")
    public void run() {

        while (true) {

            try {
                Object obj = this.queue.take();

                if (obj == this.poison) {
                    break;
                }

                if (this.sleepMs > 0) {
                    Thread.sleep(this.sleepMs);
                }

                this.consumer.consume((T) obj);

            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            } catch (Throwable t) {
                t.printStackTrace(System.err);
            }
        }
    }

}
