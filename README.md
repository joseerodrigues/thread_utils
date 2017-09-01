# thread_utils
Helpers for Multi-threading development

- Producer/Consumer
````java
Producer<String> producer = Producer.create(String.class, new Consumer<String>() {
    public void consume(String item) {
        System.out.println(Thread.currentThread().getId() + " : item = [" + item + "]");
    }
});

for (int i = 0; i < 10; i++){
    producer.submit("test_" + i);
}

producer.stop();
````

- ParallelRunner

>Builds on Producer/Consumer to provide an easy way to run multiple tasks in parallel. 
When all tasks complete, a callback is called.

````java
private static class SampleRunnable implements Runnable{
    private int id = 0;

    public SampleRunnable(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("run with id : " + id);
    }
}
````

````java
ParallelRunner runner = ParallelRunner.builder().withThreads(3).build();
runner.start();

// async, method returns as soon as all tasks are submitted to execution, 
// but have not necessarily completed.
runner.run(new Runnable() {
                       @Override
                       public void run() {
                           System.out.println("onComplete");
                       }
                   },
                new SampleRunnable(1),
                new SampleRunnable(2),
                new SampleRunnable(3),
                new SampleRunnable(4)
        );

System.out.println("Maybe called before callback");

runner.stop();
````

````java
ParallelRunner runner = ParallelRunner.builder().withThreads(3).build();
runner.start();

// without a callback
runner.runSync(
            new SampleRunnable(1),
            new SampleRunnable(2),
            new SampleRunnable(3),
            new SampleRunnable(4)
        );

System.out.println("called when all tasks complete");

runner.stop();
````