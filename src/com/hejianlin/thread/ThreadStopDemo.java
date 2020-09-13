package com.hejianlin.thread;

/**
 * @Description 错误stop线程的demo示例
 * @Author jianlin
 * @DateTime 2020/9/2 17:17
 **/
public class ThreadStopDemo {

    public static void main(String[] args) throws InterruptedException {
        StopThread stopThread = new StopThread();
        stopThread.start();
        //休眠一秒，确保i自增成功
        Thread.sleep(1000);
        //使用错误的方式中止线程
        stopThread.stop();;
        while(stopThread.isAlive()){
            //空循环，主要用于确保线程已经终止
        }
        //输出结果
        stopThread.print();
    }

}

class StopThread extends Thread{
    private int i,j =0;

    @Override
    public void run() {
        //同步锁，确保线程安全
        synchronized (this){
            ++i;
            try {
                //休眠10秒，模拟操作
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++j;
        }
    }

    public void print(){
        System.out.println("i="+i+",j="+j);
    }
}
