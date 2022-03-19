/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	Java-Daily-Interview 文件名：	ThreadLocalTest.java 模块说明： 修改历史： 2022/3/11 - liuyang - 创建。
 */
package com.ly.interview.JavaSE;

/**
 * @author liuyang
 */
public class ThreadLocalTest {

  static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
  public static void main(String[] args) {

    Thread t1 = new Thread(() -> {
      System.out.println("t1:" + threadLocal.get());
      threadLocal.set(0);
      System.out.println("t1:" + threadLocal.get());
    });

    Thread t2 = new Thread(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("t2:" + threadLocal.get());
      threadLocal.set(1);
      System.out.println("t2:" + threadLocal.get());
    });

    t1.start();
    t2.start();

  }
}