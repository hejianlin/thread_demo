package com.hejianlin.thread;

/**
 * @Description TODO
 * @Author jianlin
 * @DateTime 2020/9/5 14:09
 **/
public class ThreadInterruptStatusDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("线程启动了");
            while (!Thread.currentThread().isInterrupted()){
                System.out.println("线程中断状态："+Thread.currentThread().isInterrupted());
            }

            Thread.interrupted();
            System.out.println("调用Thread.interrupted()之后的线程状态："+Thread.currentThread().isInterrupted());
            Thread.interrupted();
            System.out.println("再次调用Thread.interrupted()之后的线程状态："+Thread.currentThread().isInterrupted());
            Thread.interrupted();
            System.out.println("第三次调用Thread.interrupted()之后的线程状态："+Thread.currentThread().isInterrupted());
            System.out.println("线程结束了");
        });
        thread.start();


        try {
            //主线程休眠0.5秒，先等待线程thread打印自己的线程状态
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //中断线程thread
        thread.interrupt();
        System.out.println("thread线程是否被中断："+thread.isInterrupted());
    }
}
