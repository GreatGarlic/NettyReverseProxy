package com.rtucloud.cs.proxy;

import cn.hutool.socket.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
@Slf4j
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
    @Test
    public void test4()throws Exception{
        Socket socket= SocketUtil.connect("ns1.dnspod.net",6666);

        InputStream is=socket.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String reply=br.readLine();
        br.close();
        is.close();
        socket.close();
        log.debug(reply);
    }
}
