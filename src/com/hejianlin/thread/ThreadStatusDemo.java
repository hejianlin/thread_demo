package com.hejianlin.thread;

/**
 * @Description 测试线程状态切换
 * @Author jianlin
 * @DateTime 2020/9/2 15:21
 **/
public class ThreadStatusDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("1. new -> runnable -> terminated");
        test1();
        System.out.println();

        System.out.println("2. new -> runnable -> waiting -> runnable -> terminated");
        test2();
        System.out.println();

        System.out.println("3. new -> runnable -> blocked -> runnable -> terminated");
        test3();
        System.out.println();
    }

    public static void test1() throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println("thread1当前的状态：" + Thread.currentThread().getState().toString());
            System.out.println("thread1执行了");
        });

        System.out.println("还没有调用start方法，thread1的状态："+thread.getState().toString());
        thread.start();
        System.out.println("调用了start方法，thread1的状态："+thread.getState().toString());
        //这里睡眠2秒，注意，睡眠的是主线程，thread这个线程不受影响，主要是为了等待thread执行结束
        Thread.sleep(2000L);
        System.out.println("等待两秒后，thread1的状态："+thread.getState().toString());
    }

    public static void test2() throws InterruptedException {

        Thread thread = new Thread(() -> {
            try {
                //当前线程先休眠1.5秒，1.5秒之后自动唤醒
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread2当前状态："+Thread.currentThread().getState().toString());
            System.out.println("thread2执行了");
        });

        System.out.println("还没有调用start方法，thread2的状态："+thread.getState().toString());
        thread.start();
        System.out.println("调用了start方法，thread2的状态："+thread.getState().toString());
        Thread.sleep(200L);
        System.out.println("等待200毫秒后，thread2的状态："+thread.getState().toString());
        Thread.sleep(3000L); //等待3000毫秒，确保thread2线程已经执行完了
        System.out.println("等待3000毫秒后，thread2的状态："+thread.getState().toString());
    }

    public static void test3() throws InterruptedException {

        Thread thread = new Thread(() -> {
            synchronized (ThreadStatusDemo.class) {
                System.out.println("thread3当前状态：" + Thread.currentThread().getState().toString());
                System.out.println("thread3执行了");
            }
        });

        synchronized (ThreadStatusDemo.class){
            System.out.println("还没有调用start方法，thread3的状态："+thread.getState().toString());
            thread.start();
            System.out.println("调用了start方法，thread3的状态："+thread.getState().toString());
            Thread.sleep(200L);
            System.out.println("等待200毫秒后，thread3的状态："+thread.getState().toString());
        }
        Thread.sleep(3000L);
        System.out.println("等待3000毫秒后，thread3的状态："+thread.getState().toString());

    }
}


