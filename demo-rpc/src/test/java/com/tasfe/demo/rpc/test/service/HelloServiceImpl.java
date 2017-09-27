package com.tasfe.demo.rpc.test.service;

/**
 * @author lait.zhang@gmail.com
 * @github http://github.com/geeker-lait
 * @create 9/27/2017.
 * @phone 15801818092
 * @blog http://zhserver.cn
 * @description 服务接口实现类
 **/
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
