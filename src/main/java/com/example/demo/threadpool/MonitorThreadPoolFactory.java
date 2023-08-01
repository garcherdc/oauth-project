
package com.example.demo.threadpool;



import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class MonitorThreadPoolFactory {

    public static ExecutorService createThreadPool(int corePoolSize, int maxPoolSize, long keepAliveSeconds, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler, String threadPoolName) {
        return new MonitorThreadPoolExecutor(
                corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                workQueue, threadFactory, rejectedExecutionHandler, threadPoolName);
    }

    public static ExecutorService createThreadPool(int corePoolSize, int maxPoolSize, long keepAliveSeconds, int queueCapacity, String threadPoolName) {
        BlockingQueue<Runnable> queue = createQueue(queueCapacity);
        ThreadFactory customThreadFactory = new ThreadFactoryBuilder().setNameFormat(threadPoolName + "-thread-%d").setUncaughtExceptionHandler(new ThreadExceptionHandler()).build();
        return createThreadPool(corePoolSize, maxPoolSize, keepAliveSeconds,
                queue, customThreadFactory, new ThreadPoolExecutor.AbortPolicy(), threadPoolName);
    }

    /**
     * Create the BlockingQueue to use for the ThreadPoolExecutor.
     * <p>A LinkedBlockingQueue instance will be created for a positive
     * capacity value; a SynchronousQueue else.
     * @param queueCapacity the specified queue capacity
     * @return the BlockingQueue instance
     * @see LinkedBlockingQueue
     * @see SynchronousQueue
     */
    protected static BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else {
            return new SynchronousQueue<>();
        }
    }

}
