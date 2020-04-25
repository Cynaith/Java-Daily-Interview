package com.ly.interview.设计模式;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 接口适配器 {
    public static void main(String[] args) {
        手机输入 phone = new 手机适配器(new 工业插座());
        System.out.println("输出后电压"+phone.输入电压());
        phone = new 手机适配器(new 家庭插座());
        System.out.println("输出后电压"+phone.输入电压());
    }
}

abstract class 插座{
    public int 输出电压(){
        return 200;
    }
}
class 家庭插座 extends 插座{
    @Override
    public int 输出电压() {
        return 220;
    }
}
class 工业插座 extends 插座{
    @Override
    public int 输出电压() {
        return 440;
    }
}
interface 手机输入{
    int 输入电压();
}

class 手机适配器 implements 手机输入{

    private 插座 chazuo;

    public 手机适配器(家庭插座 jiatingchazuo) {
        this.chazuo = jiatingchazuo;
    }

    public 手机适配器(工业插座 gongyechazuo) {
        this.chazuo = gongyechazuo;
    }

    @Override
    public int 输入电压() {
        if (chazuo !=null){
            System.out.println("输出前电压:"+chazuo.输出电压());
        }
        //适配逻辑
        return 5;
    }
}

