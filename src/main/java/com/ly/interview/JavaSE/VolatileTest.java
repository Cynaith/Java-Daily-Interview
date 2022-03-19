package com.ly.interview.JavaSE;

/**
 * @USER: lynn
 * @DATE: 2020/4/23
 **/
public class VolatileTest {
    public static void main(String[] args) {
        final Counter counter = new Counter();
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    counter.inc();
                }
            }).start();
        }
        System.out.println(counter);
    }
}
class Counter {
    private int count = 0;
    public void inc() {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
    }
    @Override
    public String toString() {
        return "[count=" + count + "]";
    }
}

