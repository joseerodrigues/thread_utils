package com.cave.utils.threads.parallelrunner;

import com.cave.utils.threads.Consumer;

class RunnableConsumer implements Consumer<Runnable> {

    @Override
    public void consume(Runnable item) {
        item.run();
    }
}
