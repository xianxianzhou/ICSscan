package com.gon.dashboards.util;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class ThreadPoolTaskExecutorUtils {
    static ThreadPoolExecutor threadPoolExecutor;

    public static void execute(Runnable runnable) {
        getThreadPoolTaskExecutor().execute(runnable);
    }

    private static ThreadPoolExecutor getThreadPoolTaskExecutor() {
        if (threadPoolExecutor == null) {
            int corePoolSize = 100;
            int maximumPoolSize = 200;
            long keepAliveTime = 50;
            TimeUnit unit = TimeUnit.MINUTES;
            BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
             threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }
        return threadPoolExecutor;
    }
}
