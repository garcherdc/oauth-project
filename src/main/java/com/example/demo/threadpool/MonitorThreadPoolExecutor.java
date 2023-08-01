package com.example.demo.threadpool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


public class MonitorThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorThreadPoolExecutor.class);

    /**
     * measureMent
     */

    /**
     * 线程池名称
     */
    String threadPoolName;


    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,handler);
        this.threadPoolName = threadPoolName;
        this.setRejectedExecutionHandler(new MonitorRejectedExecutionHandler(handler,threadPoolName));
    }

    /**
     *
     */


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
//        setThreadLocalTimer();
//        queueMonitor();
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
//        stopThreadLocalTimer();
        super.afterExecute(runnable, throwable);
//        if (throwable == null && runnable instanceof Future && ((Future) runnable).isDone()) {
//            try {
//                ((Future) runnable).get();
//            } catch (CancellationException ce) {
//                throwable = ce;
//            } catch (ExecutionException ee) {
//                throwable = ee.getCause();
//            } catch (InterruptedException ie) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        if (throwable != null) {
//            incrErrorNum();
//        }

    }


    /**
     * task错误数量
     */




}
