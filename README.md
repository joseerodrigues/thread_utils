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
    
ParallelRunner runner = ParallelRunner.builder().withThreads(3).build();
runner.start();

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

runner.stop();
````