package com.ly.interview.设计模式;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class Cglib代理 {
    public static void main(String[] args) {
        CrudDaoInterceptor interceptor = new CrudDaoInterceptor(new CrudDaoImpl(),new Transaction());
        CrudDaoImpl proxy = (CrudDaoImpl) interceptor.getObject();
        proxy.delete();
    }

}
class CrudDaoImpl{
    public void select(){
        System.out.println("select * from table");
    }
    public void insert(){
        System.out.println("insert into table values()");
    }
    public void delete(){
        System.out.println("delete from table where id = ?");
    }
    public void update(){
        System.out.println("update table set id = ? where name = ?");
    }
}

class Transaction{
    public void openTransaction(){
        System.out.println("开启事务");
    }
    public void commitTransaction(){
        System.out.println("提交事务");
    }
}

class CrudDaoInterceptor implements MethodInterceptor{

    private Object object;
    private Transaction transaction; //切面

    public CrudDaoInterceptor(Object object, Transaction transaction) {
        this.object = object;
        this.transaction = transaction;
    }

    public Object getObject() {
        Enhancer enhancer = new Enhancer();
        Class<?> clazz = this.object.getClass();
        enhancer.setCallback(this);
        enhancer.setClassLoader(clazz.getClassLoader());
        enhancer.setInterfaces(clazz.getInterfaces());
        enhancer.setSuperclass(clazz);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        System.out.println("执行的方法名为:"+methodName);
        if ("delete".equals(methodName)){
            transaction.openTransaction();
            method.invoke(object,objects);
            transaction.commitTransaction();
        }

        return null;
    }
}
