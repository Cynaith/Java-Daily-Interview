package com.ly.interview.JavaSE;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @USER: lynn
 * @DATE: 2020/4/24
 **/
public class ProxyTest {
    public static void main(String[] args) {
        final ArrayList<String> list = new ArrayList<>();
        List<String> listProxy = (List<String>) Proxy.newProxyInstance(list.getClass().getClassLoader(),
                list.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("method name:"+method.getName());
                        System.out.println("before");
                        Object result = method.invoke(list,args);
                        System.out.println("adding");
                        System.out.println("after");
                        return result;
                    }
                });
        listProxy.add("hello world");
        listProxy.get(0);
        System.out.println(list);
    }
}
