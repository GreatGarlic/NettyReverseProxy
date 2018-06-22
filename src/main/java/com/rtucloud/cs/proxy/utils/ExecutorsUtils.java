package com.rtucloud.cs.proxy.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorsUtils {

    public static ExecutorService newSingleThreadExecutor(String threadName) {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1),
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName + "-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
