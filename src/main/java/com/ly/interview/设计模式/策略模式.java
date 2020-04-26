package com.ly.interview.设计模式;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 策略模式 {
    public static void main(String[] args) {
        Context context = new Context((int money)->{
            System.out.println("微信付钱"+money);
        });
        context.付钱(100);

        Integer[] integers = {
                new Integer(1),
                new Integer(3),
                new Integer(5)
        } ;
        Arrays.sort(integers,new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) o2).intValue()-((Integer) o1).intValue();
            }
        });
        System.out.println(Arrays.toString(integers));
    }
}
@FunctionalInterface
interface 支付{
    void 付钱(int money);

}
class 微信支付 implements 支付{

    @Override
    public void 付钱(int money) {
        System.out.println("微信付钱"+money);
    }

}
class 支付宝支付 implements 支付{

    @Override
    public void 付钱(int money) {
        System.out.println("支付宝付钱"+money);
    }
}
class Context{
    private 支付 pay;

    public Context(支付 payWay) {
        this.pay = payWay;
    }

    public void 付钱(int money) {
        pay.付钱(money);
    }
}
