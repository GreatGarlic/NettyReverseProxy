package com.rtucloud.cs.proxy;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BaseTest {

    @Test
    public void test1() throws UnknownHostException {
        InetAddress[] addresses=InetAddress.getAllByName("nandcloud.cn");
        for(InetAddress addr:addresses)
        {
            System.out.println(addr);
        }
//        InetAddress   inetAddress=  InetAddress.getByName("");
//
//
//        System.out.println(inetAddress.getHostAddress());

    }
}
