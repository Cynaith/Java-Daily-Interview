package com.ly.interview.设计模式;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 类适配器 {
    public static void main(String[] args) {
        充电器 phone = new 手机();
        System.out.println(phone.高压转低压());
    }
}

class 高压电{
    public int 插座(){
        int output = 220;
        return output;
    }
}
interface 充电器{
    int 高压转低压();
}
class 手机 extends 高压电 implements 充电器{

    @Override
    public int 高压转低压() {
        int 高压电 = 插座();
        //适配细节
        int 低压电 = 高压电/44;
        return 低压电;
    }
}