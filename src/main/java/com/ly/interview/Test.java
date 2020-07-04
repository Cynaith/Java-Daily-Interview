package com.ly.interview;

import com.github.cynaith.jedis.Ledis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @autohr: Cynaith
 **/
public class Test {

    public static void main(String[] args) {


        ExecutorService executorService =  Executors.newCachedThreadPool();
        Future<?> future =  executorService.submit(new Runnable() {

            @Override
            public void run() {
                int i =0;
            }

        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
