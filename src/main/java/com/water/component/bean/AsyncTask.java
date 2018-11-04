package com.water.component.bean;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步线程
 */
@Component
public class AsyncTask {

    @Async
    public void execute(Runnable task) throws InterruptedException{
        task.run();
    }
}
