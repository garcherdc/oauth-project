package com.example.demo.threadpool;


import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


public class MonitorRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorRejectedExecutionHandler.class);

    RejectedExecutionHandler handler;

    String poolName;
    public MonitorRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler, String poolName) {

        this.handler=rejectedExecutionHandler;
        this.poolName=poolName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
     handler.rejectedExecution(r,executor);
    }


}
