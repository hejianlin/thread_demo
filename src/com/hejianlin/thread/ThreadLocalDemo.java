package com.hejianlin.thread;

/**
 * @Description TODO
 * @Author jianlin
 * @DateTime 2020/9/6 13:57
 **/
public class ThreadLocalDemo {

    //定义ThreadLocal变量
    public static ThreadLocal<String> value = new ThreadLocal<String>();

    public static void main(String[] args) throws InterruptedException {
        //主线程设置值
        value.set("这是主线程的值");
        String s = value.get();
        System.out.println("主线程获取到的值："+s);

        new Thread(() ->{
            String v = value.get();
            System.out.println("子线程获取到的值："+v);
            value.set("这是子线程的值");

            v = value.get();
            System.out.println("重新设置后，子线程获取到的值："+v);
            System.out.println("子线程执行结束");
        }).start();

        //主线程睡眠3秒，等待子线程执行结束
        Thread.sleep(2000L);
        s = value.get();
        System.out.println("重新设置后，主线程获取到的值："+s);
    }
}
