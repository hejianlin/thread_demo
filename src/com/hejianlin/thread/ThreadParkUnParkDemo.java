package com.hejianlin.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @Description TODO
 * @Author jianlin
 * @DateTime 2020/9/5 23:28
 **/
public class ThreadParkUnParkDemo {

    //包子数量
    public volatile int sum = 0;
    public static void main(String[] args) throws Exception {
        ThreadParkUnParkDemo demo = new ThreadParkUnParkDemo();
        demo.parkUnParkTest();
    }

    private  void parkUnParkTest() throws  Exception{

        Thread thread = new Thread(() -> {
            while (sum <= 0) {
                System.out.println("1.没有包子，进入等待");
                LockSupport.park();
            }
            System.out.println("2.买到包子了，回家");
        });
        thread.start();

        //3秒之后，生产一个包子
        Thread.sleep(3000L);
        sum++;
        LockSupport.unpark(thread);
        System.out.println("3.通知消费者");
    }
}
