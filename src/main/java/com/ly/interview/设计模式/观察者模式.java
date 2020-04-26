package com.ly.interview.设计模式;

import java.util.ArrayList;
import java.util.List;

/**
 * @USER: lynn
 * @DATE: 2020/4/27
 **/
public class 观察者模式 {
    public static void main(String[] args) {
        运营商 电信运营商 = new 电信运营商();
        电信运营商.增加用户((String message)->{
            System.out.println("安卓电信短信:"+message);
        });
        电信运营商.增加用户((String message)->{
            System.out.println("苹果电信短信:"+message);
        });
        电信运营商.发送短信("开会");
    }
}
@FunctionalInterface
interface 电信用户{ //Observe
    void 接受短信(String message);
}
interface 运营商{
    void 增加用户(电信用户 user);
    void 发送短信(String message);
}

class 电信运营商 implements 运营商{

    private final List<电信用户> 用户们 = new ArrayList<>();

    @Override
    public void 增加用户(电信用户 user) {
        用户们.add(user);
    }

    @Override
    public void 发送短信(String message) {
        用户们.forEach(电信用户 -> {
            电信用户.接受短信(message);
        });
    }
}
