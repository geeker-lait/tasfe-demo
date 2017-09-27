package com.tasfe.demo.rpc.test;


import com.tasfe.demo.rpc.client.RpcImporter;
import com.tasfe.demo.rpc.test.service.HelloService;

import java.net.InetSocketAddress;

/**
 * @author lait.zhang@gmail.com
 * @github http://github.com/geeker-lait
 * @create 9/27/2017.
 * @phone 15801818092
 * @blog http://zhserver.cn
 * @description 客户端测试代码
 **/
public class SimRpcClientTest {


    public static void main(String[] args) throws InterruptedException {

		/*
         * 创建客户端对象
		 */
        RpcImporter<HelloService> helloService = new RpcImporter<HelloService>();

        /**
         *生成远程代理对象
         */
        HelloService service = helloService.importer(HelloService.class, new InetSocketAddress("127.0.0.1", 8000));

        /**
         * 为了测试所以写了一个循环调用代理服务
         */
        while (true) {

            String sayHello = service.sayHello("World !!!");

            System.out.println(sayHello);

            Thread.sleep(1000);

        }
    }
}
