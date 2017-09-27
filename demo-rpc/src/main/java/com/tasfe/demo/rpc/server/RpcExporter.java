package com.tasfe.demo.rpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * @author lait.zhang@gmail.com
 * @github http://github.com/geeker-lait
 * @create 9/27/2017.
 * @phone 15801818092
 * @blog http://zhserver.cn
 * @description RpcExporter类，用于接收远程客户端发送过来的请求
 **/
public class RpcExporter {

    /**
     * 创建任务线程池用来处理接收RPC客户端发送过来的请求任务
     */
    private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    /**
     * 用来保存注册的服务接口以及对应的实现类 Map的key:value 格式  eg: key: com.tasfe.demo.rpc.test.service.HelloService  value:HelloServiceImpl.class
     * key：接口的全类名 value:接口实现类的Class类
     */
    private static final Map<String, Class<?>> serverRegistry = new HashMap<String, Class<?>>();

    /**
     * 接口注册
     *
     * @param serviceInterface     ：自定的接口
     * @param serviceInterfaceImpl ：实现自定接口的类
     */
    public void registry(Class<?> serviceInterface, Class<?> serviceInterfaceImpl) {
        serverRegistry.put(serviceInterface.getName(), serviceInterfaceImpl);
    }

    /**
     * 开启RPC服务
     */
    public void start(String hostName, int port) throws IOException {
        ServerSocket server = new ServerSocket();
        /*
		 * 绑定主机和端口号
		 */
        server.bind(new InetSocketAddress(hostName, port));
        System.out.println("RPC service start ... ...");
        int count = 0;
        try {
            while (true) {
                System.out.println("调用次数为： " + count++);
                executor.execute(new ExporterTask(server.accept()));
            }
        } finally {
            server.close();
        }

    }


    /**
     * 定义任务类，执行客户端发送过来的请求
     *
     * @author lait.zhang@gmail.com
     * @github http://github.com/geeker-lait
     * @phone 15801818092
     * @create 9/27/2017.
     * @description
     **/
    private class ExporterTask implements Runnable {

        private Socket client = null;

        public ExporterTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {

            ObjectInputStream in = null;
            ObjectOutputStream out = null;

            try {
                // 1.从Socket中获取输入流
                in = new ObjectInputStream(client.getInputStream());
                // 2.获取客户端发送过来的服务接口实现类的全类名
                String interfaceName = in.readUTF();
                // 3.通过服务端接口实现类的全类名实例化
                if (serverRegistry.get(interfaceName) == null) {
                    throw new Exception("没有提供任何服务接口~~~");
                }
                Class<?> service = serverRegistry.get(interfaceName);
                if (service == null) {
                    throw new Exception("服务接口没有进行实现~~~");
                }
                // 4.获取客户端服务类方法名
                String methodName = in.readUTF();
                // 5.获取客户端服务类方法参数类型
                Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
                // 6.获取客户端服务类方法参数值
                Object[] arguments = (Object[]) in.readObject();
                // 7.通过方法名和参数列表确定一个方法
                Method method = service.getMethod(methodName, parameterTypes);
                // 8.调用该方法和参数值返回结果
                Object result = method.invoke(service.newInstance(), arguments);
                // 9.写进输出流
                out = new ObjectOutputStream(client.getOutputStream());
                // 10.将结果写回给客户端
                out.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

}
