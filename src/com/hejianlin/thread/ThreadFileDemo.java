package com.hejianlin.thread;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Description TODO
 * @Author jianlin
 * @DateTime 2020/9/5 19:31
 **/
public class ThreadFileDemo {

    public static void main(String[] args) {

        //线程1，写入数据
        new Thread(() ->{
            try{
                while(true){
                    Files.write(Paths.get("Demo.log"),("当前时间："+String.valueOf(System.currentTimeMillis())).getBytes());
                    Thread.sleep(1000L);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }).start();

        //线程2，读取数据
        new Thread(() ->{
            try{
                while (true){
                    Thread.sleep(1000L);
                    byte[] bytes = Files.readAllBytes(Paths.get("Demo.log"));
                    System.out.println(new String(bytes));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }).start();
    }
}
