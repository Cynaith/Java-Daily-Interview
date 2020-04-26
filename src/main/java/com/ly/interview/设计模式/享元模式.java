package com.ly.interview.设计模式;

import java.util.HashMap;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 享元模式 {
    public static void main(String[] args) {
        字符串 string1 = 对象工厂.getChars("a");
        string1.print();
        字符串 string2 = 对象工厂.getChars("a");
        string2.print();
        字符串 string3 = 对象工厂.getChars("b");
        string3.print();
    }
}
interface 对象{
    public void print();
}
class 字符串 implements 对象{
    String chars;

    public 字符串(String chars) {
        this.chars = chars;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    @Override
    public void print() {
        System.out.println(chars);
    }
}

class 对象工厂{
    private static final HashMap<String,字符串> 字符串对象池 = new HashMap<>();

    public static 字符串 getChars(String chars){
        字符串 字符串对象 = 字符串对象池.get(chars);

        if (字符串对象 == null){
            字符串对象 = new 字符串(chars);
            字符串对象池.put(chars,字符串对象);
            System.out.println("向字符串对象池插入一个对象:"+字符串对象.toString());
        }
        return 字符串对象;
    }
}
