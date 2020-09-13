package com.hejianlin.thread;

/**
 * @Description 演示wait/notify进行线程等待与唤醒
 * @Author jianlin
 * @DateTime 2020/9/5 21:56
 **/
public class ThreadWaitNotifyDemo {

    //包子数量
    public volatile int sum = 0;
    public static void main(String[] args) throws Exception {
        ThreadWaitNotifyDemo demo = new ThreadWaitNotifyDemo();
        demo.waitNotifyTest();
    }

    private  void waitNotifyTest() throws  Exception{
        new Thread(() ->{
            while (sum <= 0){
                synchronized (this){
                    System.out.println("1.没有包子，进入等待");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2.买到包子了，回家");
        }).start();

        //3秒之后，生产一个包子
        Thread.sleep(3000L);
        sum++;
        synchronized (this){
            this.notifyAll();
            System.out.println("3.通知消费者");
        }
    }
}

