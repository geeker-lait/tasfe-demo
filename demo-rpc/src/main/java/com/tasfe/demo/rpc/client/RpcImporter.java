package com.tasfe.demo.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lait.zhang@gmail.com
 * @github http://github.com/geeker-lait
 * @create 9/27/2017.
 * @phone 15801818092
 * @blog http://zhserver.cn
 * @description 本地代理，用于描述接口类，并且将描述的接口类信息通过网络传输给远端服务
 **/
public class RpcImporter<T> {

    /**
     * 生成代理对象
     *
     * @param serviceInterface:服务接口的类
     * @param address：远程服务的地址         ip:port  127.0.0.1:21880
     * @return
     */
    @SuppressWarnings("unchecked")
    public T importer(final Class<?> serviceInterface, final InetSocketAddress address) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream out = null;
                ObjectInputStream in = null;

                try {
                    //1.创建连接
                    socket = new Socket();
                    //2.设置连接服务的地址
                    socket.connect(address);
                    //3.获取输出流
                    out = new ObjectOutputStream(socket.getOutputStream());
                    //4.获取服务接口的实现类全类名
                    out.writeUTF(serviceInterface.getName());
                    //5.服务接口内的方法名
                    out.writeUTF(method.getName());
                    //6.方法的参数类型
                    out.writeObject(method.getParameterTypes());
                    //7.方法的参数值
                    out.writeObject(args);
                    //8.发送给远程服务端
                    in = new ObjectInputStream(socket.getInputStream());
                    //9.获取远端服务器返回的对象
                    return in.readObject();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
            }
        });
    }

}
