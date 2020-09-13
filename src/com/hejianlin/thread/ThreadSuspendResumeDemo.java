package com.hejianlin.thread;

/**
 * @Description 演示suspend/resume，该方法已被弃用
 * @Author jianlin
 * @DateTime 2020/9/5 20:04
 **/
public class ThreadSuspendResumeDemo {

    //包子数量
    public static volatile int sum = 0;
    public static void main(String[] args) throws InterruptedException {
        //测试生产者消费者
        //suspendResumeTest();
        //suspendResumeDeadLockTest();
        suspendResumeTest2();
    }

    //测试线程等待与唤醒
    private static void suspendResumeTest() throws InterruptedException {
        Thread consumerThread = new Thread(() -> {
            while (sum <= 0) {
                System.out.println("1.没有包子，进入等待");
                Thread.currentThread().suspend();
            }
            System.out.println("2.买到包子，回家");
        });
        //启动消费者线程
        consumerThread.start();

        //3秒之后，生产一个包子
        Thread.sleep(3000L);
        sum++;
        //唤醒消费者线程
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }


    //测试锁没有被释放
    private static void suspendResumeDeadLockTest() throws InterruptedException {
        Thread consumerThread = new Thread(() -> {
            while (sum <= 0) {
                System.out.println("1.没有包子，进入等待");
                synchronized (ThreadSuspendResumeDemo.class){
                    Thread.currentThread().suspend();
                }

            }
            System.out.println("2.买到包子，回家");
        });
        //启动消费者线程
        consumerThread.start();

        //3秒之后，生产一个包子
        Thread.sleep(3000L);
        sum++;
        //唤醒消费者线程
        synchronized (ThreadSuspendResumeDemo.class){
            consumerThread.resume();
            System.out.println("3.通知消费者");
        }
    }


    //测试线程唤醒比等待先执行造成的问题：程序永远挂起
    private static void suspendResumeTest2() throws InterruptedException {
        Thread consumerThread = new Thread(() -> {
            while (sum <= 0) {
                System.out.println("1.没有包子，进入等待");
                try {
                    //延迟5秒
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //执行挂起操作
                Thread.currentThread().suspend();
            }
            System.out.println("2.买到包子，回家");
        });
        //启动消费者线程
        consumerThread.start();

        //3秒之后，生产一个包子
        Thread.sleep(3000L);
        sum++;
        //唤醒消费者线程
        consumerThread.resume();
        System.out.println("3.通知消费者");
    }




}
