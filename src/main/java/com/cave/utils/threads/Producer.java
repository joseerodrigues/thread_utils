package com.cave.utils.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Producer<T> {

	private static final int DEFAULT_THREADS = Runtime.getRuntime().availableProcessors();
	private static final int DEFAULT_QUEUE_SIZE = 10000;
	private static final int DEFAULT_SLEEP_TIME = -1;

	private LinkedBlockingQueue<Object> queue = null;
	private Object poison = new Object();
	private ExecutorService executor = null;
	private int nThreads = DEFAULT_THREADS;
	
	private Producer(Class<T> type, int nThreads, int queueSize, int sleepMs, Consumer<T> consumer){
		
		if (queueSize <= 0){
			queueSize = DEFAULT_QUEUE_SIZE;
		}
		
		if (nThreads > 0){
			this.nThreads = nThreads;
		}				
		
		this.queue = new LinkedBlockingQueue<Object>(queueSize);
		this.executor = Executors.newFixedThreadPool(nThreads);

		for (int i = 0; i < nThreads; i++){
			this.executor.submit(new ConsumerRunnable<T>(this.queue, poison, sleepMs, consumer));
		}
	}
	
	public static<T> Producer<T> create(Class<T> type, int nThreads, int queueSize, int sleepMs, Consumer<T> consumer){
		return new Producer<T>(type, nThreads, queueSize, sleepMs, consumer);
	}
	
	public static<T> Producer<T> create(Class<T> type, Consumer<T> consumer){					
		return new Producer<T>(type, DEFAULT_THREADS, DEFAULT_QUEUE_SIZE, DEFAULT_SLEEP_TIME, consumer);
	}

	public boolean isQueueEmpty(){
		return this.queue.isEmpty();
	}

	/**
	 * Submits a new item to be consumed. This method blocks if the queue is full.
	 * @param item
	 */
	public void submit(T item){
		try {
			this.queue.put(item);
		} catch (InterruptedException e) {			
			e.printStackTrace(System.err);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void stop(){
		
		for (int i = 0; i < this.nThreads; i++){
			try {
				this.queue.put((T) this.poison);
			} catch (InterruptedException e) {				
				e.printStackTrace(System.err);
			}
		}
		
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		}
	}
}
