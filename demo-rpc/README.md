#编写一个RPC框架主要包括的模块
1.通信：socket

2.序列化：java原生 Serializable 序列化方式

3.注册中心：此框架不需要注册中心，但是需要一个保存注册中心的Map来保存服务接口和实现类

4.动态代理：通过动态代理来描述一个服务接口信息，然后通过网络传输传到服务端完成调用，服务端调用之后将结果再通过网络传输返回给客户端

#测试方法：
* 服务端：

1.定义服务接口和实现类
```
package com.tasfe.demo.rpc.test.service;
public interface HelloService {
    String sayHello(String name);
}
```
```
package com.tasfe.demo.rpc.test.service;
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
	        return "Hello " + name;
	    }
}
```
2.创建RPC服务
```
RpcExporter rpcExporter = new RpcExporter();
```

3.将接口注册到RPC服务上
```
rpcExporter.registry(HelloService.class, HelloServiceImpl.class);
```

4.绑定主机和端口号并且启动
```
rpcExporter.start("localhost", 8000);
```
* 客户端

1.创建客户端代理对象，比如刚刚我们创建的服务接口 我们就要创建这个接口的代理对象
```
RpcImporter<HelloService> helloService = new RpcImporter<HelloService>();
```
2.生成远程代理对象
```
HelloService service = helloService.importer(HelloService.class, new InetSocketAddress("127.0.0.1", 8000));
```
3.直接通过代理对象调用服务
```
String sayHello = service.sayHello("World !!!");
System.out.println(sayHello);
```

详情请见deno-rpc测试类

注意：
1.因为这只是一个简单的RPC框架，所以地址和端口号采用傻瓜式自定义填写，如果节点很多的话，我们就要加注册中心，然后通过注册中心获取服务节点的地址和端口号类似于dubbo那样。

2.本项目只是为了练习和测试RPC框架的基本原理，性能很差，因为采用的是I/O阻塞通信方式，序列化采用的java原生序列化方式。

