package com.tasfe.demo.rpc.test;


import com.tasfe.demo.rpc.server.RpcExporter;
import com.tasfe.demo.rpc.test.service.HelloService;
import com.tasfe.demo.rpc.test.service.HelloServiceImpl;

import java.io.IOException;

/**
 * @author lait.zhang@gmail.com
 * @github http://github.com/geeker-lait
 * @create 9/27/2017.
 * @phone 15801818092
 * @blog http://zhserver.cn
 * @description 启动服务端
 **/
public class SimRpcServiceTest {

    public static void main(String[] args) {
        /*
         * 1.创建RPC服务
		 */
        RpcExporter rpcExporter = new RpcExporter();
		/*
		 * 2.注册服务
		 */
        rpcExporter.registry(HelloService.class, HelloServiceImpl.class);

        try {
			/*
			 * 3.绑定主机和端口号并启动RPC服务
			 */
            rpcExporter.start("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
