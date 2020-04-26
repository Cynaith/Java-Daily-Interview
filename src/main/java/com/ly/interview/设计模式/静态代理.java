package com.ly.interview.设计模式;

/**
 * @USER: lynn
 * @DATE: 2020/4/26
 **/
public class 静态代理 {
    public static void main(String[] args) {
        数学老师 mathTeacher1 = new 数学老师();
        数学教授 mathTeacher2 = new 数学教授(mathTeacher1);
        mathTeacher2.教书();
    }
}
interface 老师{
    void 教书();
}
class 数学老师 implements 老师{
    @Override
    public void 教书() {
        System.out.println("教数学");
    }
}

class 数学教授 implements 老师{

    private 数学老师 math;

    public 数学教授(数学老师 math) {
        this.math = math;
    }

    @Override
    public void 教书() {
        System.out.println("教物理");
        math.教书();
    }
}

