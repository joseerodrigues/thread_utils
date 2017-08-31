package com.cave.utils.threads.parallelrunner;

public class ParallelRunnerExample {

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

    public static void main(String[] args) {
        ParallelRunner runner = ParallelRunner.builder().withThreads(3).build();

        System.out.println("starting...");
        runner.start();
        System.out.println("started");

        runner.run(new Runnable() {
                       @Override
                       public void run() {
                           System.out.println("onComplete first round");
                       }
                   },
                new SampleRunnable(1),
                new SampleRunnable(2),
                new SampleRunnable(3),
                new SampleRunnable(4)
        );

        System.out.println("run completed. stopping...");

        System.out.println("run second round");
        runner.runSync(
                new SampleRunnable(1),
                new SampleRunnable(2),
                new SampleRunnable(3),
                new SampleRunnable(4)
        );
        System.out.println("complete second round");
        System.out.println("run completed. stopping...");

        runner.stop();

        System.out.println("done");
    }
}
