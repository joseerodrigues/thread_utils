package com.cave.utils.threads;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Producer<T> {

    private static final int DEFAULT_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_QUEUE_SIZE = 10000;
    private static final int DEFAULT_SLEEP_TIME = -1;

    private static final AtomicLong producerID = new AtomicLong(0);
    private LinkedBlockingQueue<Object> queue = null;
    private Object poison = new Object();
    private ExecutorService executor = null;
    private int nThreads = DEFAULT_THREADS;

    private Producer(Class<T> type, int nThreads, int queueSize, int sleepMs, Consumer<T> consumer) {

        if (queueSize <= 0) {
            queueSize = DEFAULT_QUEUE_SIZE;
        }

        if (nThreads > 0) {
            this.nThreads = nThreads;
        }

        this.queue = new LinkedBlockingQueue<Object>(queueSize);
        this.executor = Executors.newFixedThreadPool(nThreads);

        ConsumerRunnable<T> runnableConsumer = new ConsumerRunnable<T>(this.queue, poison, sleepMs, consumer);
        setupJMX(runnableConsumer, type, nThreads, queueSize);

        for (int i = 0; i < nThreads; i++) {
            this.executor.submit(runnableConsumer);
        }
    }

    public static <T> Producer<T> create(Class<T> type, Consumer<T> consumer, int nThreads, int queueSize, int sleepMs) {
        return new Producer<T>(type, nThreads, queueSize, sleepMs, consumer);
    }

    public static <T> Producer<T> create(Class<T> type, Consumer<T> consumer, int nThreads) {
        return new Producer<T>(type, nThreads, DEFAULT_QUEUE_SIZE, DEFAULT_SLEEP_TIME, consumer);
    }

    public static <T> Producer<T> create(Class<T> type, Consumer<T> consumer, int nThreads, int queueSize) {
        return new Producer<T>(type, nThreads, queueSize, DEFAULT_SLEEP_TIME, consumer);
    }

    public static <T> Producer<T> create(Class<T> type, Consumer<T> consumer) {
        return new Producer<T>(type, DEFAULT_THREADS, DEFAULT_QUEUE_SIZE, DEFAULT_SLEEP_TIME, consumer);
    }

    public boolean isQueueEmpty() {
        return this.queue.isEmpty();
    }

    /**
     * Submits a new item to be consumed. This method blocks if the queue is full.
     *
     */
    public void submit(T item) {
        try {
            this.queue.put(item);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    @SuppressWarnings("unchecked")
    public void stop() {

        for (int i = 0; i < this.nThreads; i++) {
            try {
                this.queue.put(this.poison);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }

        this.executor.shutdown();
        try {
            this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            //
        }
    }

    private void setupJMX(ConsumerRunnable<T> runnableConsumer, Class<T> type,
                          int nThreads, int queueSize) {

        String objName = "com.cave.utils.threads.Producer:type=" + type.getSimpleName()
                + ",threads=" + nThreads +",queueSize="
                + queueSize + ",id=" + producerID.incrementAndGet();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try{
            ObjectName objectName = new ObjectName(objName);
            mbs.registerMBean(runnableConsumer, objectName);
        } catch (MalformedObjectNameException e){
            e.printStackTrace(System.err);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace(System.err);
        } catch (MBeanRegistrationException e) {
            e.printStackTrace(System.err);
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace(System.err);
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
        }
    }
}
