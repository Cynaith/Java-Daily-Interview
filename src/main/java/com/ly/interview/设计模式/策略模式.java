package com.ly.interview.设计模式;

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
