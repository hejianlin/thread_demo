package com.hejianlin.thread;

import java.util.List;
import java.util.concurrent.*;

/**
 * @Description TODO
 * @Author jianlin
 * @DateTime 2020/9/12 10:40
 **/
public class ExecutorsDemo {

    //newFiexedThreadPool(int nThreads) 创建一个固定大小，任务队列无界的线程池，及核心线程数等于最大线程数
    //newCachedThreadPool() 创建一个大小无界的缓冲线程池，即核心线程数为0，最大线程数等于Integer.MAX_VALUE，
    // 如果有空闲线程，则执行，反之，则新建空闲线程执行，线程空闲时间为60秒，适用于执行耗时较小的异步任务
    //newSingleThreadExecutor() 创建一个只有一个线程的无界任务队列的单一线程池。确保任务能够按照加入的顺序依次执行，当唯一的线程因为异常中止时
    // ，将创建新的线程执行后续的任务
    //newScheduledThreadPool(int cordPoolSize) 能够定时执行任务的线程池，该线程池的核心线程数由参数指定，最大线程数=Integer.MAX_VALUE

    public static void main(String[] args) throws Exception {
//        fixedThreadPoolTest();
//        cachedThreadPoolTest();
//        singleThreadTest();
//        scheduledThreadPoolTest();
//        scheduledThreadPoolRateOrDelayTest();
//        testShutdown();
          testShutdownNow();
    }


    private static void testShutdownNow() throws InterruptedException {
        ThreadPoolExecutor threadPool = getThreadPoolExecutor();
        //1秒后终止线程池
        Thread.sleep(1000L);
        //返回的是处于等待队列的任务数
        List<Runnable> shutdownNow = threadPool.shutdownNow();
        // 再次提交提示失败
        threadPool.submit(() ->{
            System.out.println("追加一个任务");
        });
        System.out.println("等待队列中的任务数："+shutdownNow.size());
        //结果分析：
        /**
         * 1、10个任务被执行，3个任务进入等待队列，2个任务被拒绝执行
         * 2、调用shutdownNow后，不接受新的任务，已执行的任务会被中止
         * 3、追加的任务在线程池关闭后，无法再提交，会被拒绝执行
         */
    }

    private static void testShutdown() throws InterruptedException {
        ThreadPoolExecutor threadPool = getThreadPoolExecutor();
        //1秒后终止线程池
        Thread.sleep(1000L);
        threadPool.shutdown();
        // 再次提交提示失败
        threadPool.submit(() ->{
            System.out.println("追加一个任务");
        });
        //结果分析：
        /**
         * 1、10个任务被执行，3个任务进入等待队列，2个任务被拒绝执行
         * 2、调用shutdown后，不接受新的任务，等待13个任务执行结束
         * 3、追加的任务在线程池关闭后，无法再提交，会被拒绝执行
         */
    }

    private static ThreadPoolExecutor getThreadPoolExecutor() {
        //核心线程数5，最大线程数10，空闲时间5秒，大小为3的有界队列，所以同一时间，最多容纳13个任务
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3),
                (r, executor) -> System.err.println("有任务被拒绝执行了"));
        //测试：提交15个执行时间需要3秒的任务，1秒后终止线程池，看超过大小的2个，对应的处理情况
        for(int i=0;i<15;i++){
            int n=i;
            threadPool.submit(() ->{
                try {
                    System.out.println("开始执行任务："+n);
                    Thread.sleep(3000L);
                    System.out.println("执行结束："+n);
                } catch (InterruptedException e) {
                    System.out.println("异常信息："+e.getMessage());
                }
            });
            System.out.println("任务提交成功:"+i);
        }
        return threadPool;
    }


    private static void scheduledThreadPoolTest() throws Exception {
        // 和Executors.newScheduledThreadPool()一样的
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        threadPoolExecutor.schedule(() -> {
            System.out.println("任务被执行，现在时间：" + System.currentTimeMillis());
        }, 3000, TimeUnit.MILLISECONDS);
        System.out.println(
                "定时任务，提交成功，时间是：" + System.currentTimeMillis() + ", 当前线程池中线程数量：" + threadPoolExecutor.getPoolSize());
        // 预计结果：任务在3秒后被执行一次
    }


    private static void scheduledThreadPoolRateOrDelayTest() {
        //周期性地执行任务，有两种调度方式
        //方式1：延迟若干秒之后开始执行，之后间隔指定时间，执行一次，如果这时候发现上次没有执行完，则等待上次执行完之后，立即执行
        //方式2：延迟若干秒之后开始执行，之后间隔指定时间，执行一次，如果这时候发现上次没有执行完，则等待上次执行完之后，重新开始计时，之后才开始执行

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);

        //方式1的演示: 使用scheduleAtFixedRate方法
        //提交后，2秒之后开始执行，之后每间隔1秒固定执行一次，但是这个任务要执行3秒
        //也就是说变成了3秒执行一次任务，执行完之后，下个任务立即执行
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务-1被执行,现在时间：" + System.currentTimeMillis());
        }, 2000, 1000, TimeUnit.MILLISECONDS);


        //方式2的演示：使用scheduleWithFixedDelay方法
        //提交后，2秒之后开始执行，之后每间隔1秒执行一次，但是这个任务要执行3秒,所以需要等待当前任务执行完之后，再重新计时去执行
        //也就是说变成了4秒执行一次任务
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务-2被执行,现在时间：" + System.currentTimeMillis());
        }, 2000, 1000, TimeUnit.MILLISECONDS);

    }


    private static void fixedThreadPoolTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            System.out.println("fixedThreadPool执行线程啦");
        });
    }

    private static void cachedThreadPoolTest() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("cachedThreadPool执行线程啦");
            });
        }
        int poolSize = executorService.getPoolSize();
        System.out.println("cachedThreadPool线程池个数：" + poolSize);


    }

    private static void singleThreadTest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            System.out.println("singleThread执行线程啦");
        });
    }


}
