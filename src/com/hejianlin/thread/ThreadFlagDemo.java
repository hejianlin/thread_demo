package com.hejianlin.thread;

/**
 * @Description TODO
 * @Author jianlin
 * @DateTime 2020/9/5 15:10
 **/
public class ThreadFlagDemo {

    public volatile static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            //为true，在循环执行
            try {
                while (flag) {
                    System.out.println("线程执行中……");
                    //休眠0.5秒
                    Thread.sleep(500L);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();

        //主线程休眠2秒后，将flag修改为false
        Thread.sleep(2000L);
        flag = false;
        System.out.println("主线程运行结束");
        while(thread.isAlive()){
            //空循环，等待子线程运行结束
        }
        System.out.println("子线程运行结束");
    }
}
