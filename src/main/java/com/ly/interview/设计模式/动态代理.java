package com.ly.interview.设计模式;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 动态代理 {
    public static void main(String[] args) {
        DynamicProxy dynamicProxy = new DynamicProxy(new 董事长(),new 助理());
        职工 worker = (职工)dynamicProxy.newInstance();
        worker.work();
    }


}
interface 职工{
    void work();
}
class 董事长 implements 职工{
    @Override
    public void work() {
        System.out.println("做董事长该干的事");
    }
}
class 助理 {
    public void beforeWork(){
        System.out.println("work前");
    }
    public void afterWork(){
        System.out.println("work后");
    }
}

class DynamicProxy{
    private Object object;
    private 助理 helper;

    public DynamicProxy(Object object, 助理 helper) {
        this.object = object;
        this.helper = helper;
    }

    public Object newInstance(){
        Class<?> clas = this.helper.getClass();
        return Proxy.newProxyInstance(clas.getClassLoader(), clas.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                helper.beforeWork();
                Object obj = method.invoke(object,args);
                helper.afterWork();
                return obj;
            }
        });
    }

}
