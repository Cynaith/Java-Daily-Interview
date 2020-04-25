package com.ly.interview.设计模式;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 对象适配器 {
    public static void main(String[] args) {
        充电器 phone = new 手机对象(new 高压电());
        System.out.println(phone.高压转低压());
    }
}

class 手机对象 extends 高压电 implements 充电器{

    private 高压电 伏220高压电;

    public 手机对象(高压电 伏220高压电) {
        this.伏220高压电 = 伏220高压电;
    }

    @Override
    public int 高压转低压() {
        int 高压电 = 伏220高压电.插座();
        //适配细节
        int 低压电 = 高压电/44;
        return 低压电;
    }
}

