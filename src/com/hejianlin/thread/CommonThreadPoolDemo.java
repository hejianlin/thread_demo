package com.hejianlin.thread;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 标准线程池demo
 * @Author jianlin
 * @DateTime 2020/9/8 22:34
 **/
public class CommonThreadPoolDemo {

    public static void main(String[] args) throws Exception {

        CommonThreadPoolDemo demo = new CommonThreadPoolDemo();
        //demo.test();
        demo.test1();
    }

    /**
     * 测试提交15个执行时间需要3秒的任务
     * 由于我们使用的是无界队列，所以我们这里最大线程数10的配置其实是无效的，因为线程没有空闲时，优先将任务放在任务队列之中，而不是新建线程执行
     * 所以我们可以看到，同一时间，最多只有5个线程在执行
     *
     * @param
     */
    private void test() throws Exception {

        //使用标准的线程池构造方法构造线程池
        //核心线程数5，最大线程数10，空闲线程存活时间5秒，任务队列为没有指定数量的LinkedBlockingDeque队列，即无界队列，使用默认的拒绝策略
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>());
        execute(threadPoolExecutor);
    }


    /**
     * 测试提交15个执行时间需要3秒的任务
     * 由于我们使用的是无界队列，所以我们这里最大线程数10的配置其实是无效的，因为线程没有空闲时，优先将任务放在任务队列之中，而不是新建线程执行
     * 所以我们可以看到，同一时间，最多只有5个线程在执行
     *
     * @param
     */
    private void test1() throws Exception {

        //使用标准的线程池构造方法构造线程池
        //核心线程数5，最大线程数10，空闲线程存活时间5秒，任务队列为没有指定数量为3的LinkedBlockingDeque队列，即有界队列，使用自定义的拒绝策略
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3), (r, executor) -> {
            System.err.println("有任务被拒绝执行了");
        });
        execute(threadPoolExecutor);
    }

    private void execute(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            int n = i;
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println("开始执行：" + n);
                    Thread.sleep(3000L);
                    System.out.println("执行结束：" + n);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("任务提交成功:" + n);
        }

        //查看线程数量，查看队列等待数量
        Thread.sleep(500L);
        System.out.println("当前线程池数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待的数量：" + threadPoolExecutor.getQueue().size());

        //等待15秒，这时候任务应该全部执行完了，查看依然存活的线程数量是否等于核心线程数量
        Thread.sleep(15000L);
        System.out.println("当前线程池数量：" + threadPoolExecutor.getPoolSize());
        System.out.println("当前线程池等待的数量：" + threadPoolExecutor.getQueue().size());
    }
}
