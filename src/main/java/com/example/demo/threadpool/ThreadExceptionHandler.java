package com.example.demo.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("uncaughtException,thread:{},error:{}", t.getName(),e);
    }
}
