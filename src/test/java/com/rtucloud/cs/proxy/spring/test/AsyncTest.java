package com.rtucloud.cs.proxy.spring.test;

import com.rtucloud.cs.proxy.StartProgram;
import com.rtucloud.cs.proxy.server.ExecutorsServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = StartProgram.class) // 指定我们SpringBoot工程的Application启动类
public class AsyncTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTest.class);

    @Autowired
    ExecutorsServer executorsServer;

    @Test
    public void test1() throws Exception{

        Future future2 =executorsServer.taskOne();
        Thread.sleep(5000);
        future2.cancel(true);


        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
