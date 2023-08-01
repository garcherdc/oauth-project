package com.example.demo.threadpool;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;


@Service
public class ThreadPoolService {



    /**
     * 导出任务线程池
     */
    private ExecutorService exportExecutorService;

    @PostConstruct
    public void init() {
        exportExecutorService = MonitorThreadPoolFactory.createThreadPool(4,
                4,
                300,
                3000,
                "bawl-list"
                );
    }

    public void submitTask(Runnable runnable) {
        exportExecutorService.submit(runnable);
    }

}
