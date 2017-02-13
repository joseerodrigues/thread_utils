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