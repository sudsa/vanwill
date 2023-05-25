package com.qifeng.will.srv.impl;

import cn.hutool.http.HttpStatus;
import com.qifeng.will.base.bean.Student;
import com.qifeng.will.base.util.ComUtils;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestService {


    public static Student student = null;


    public static void main(String[] args) throws InterruptedException {
        //waitTest();
        //reentrantLock();
        System.out.println("==============================");
        //funcTest("success", 100);

        semaphore();
    }

    public static void waitTest() throws InterruptedException {
        student = new Student();
        Thread t1 = new Thread(() -> {
            synchronized (student) {
                System.out.println("t1 wait!");
                try {
                    student.wait();
                    System.out.println("t1 开始重新run!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        Thread.sleep(3000);

        Thread t2 = new Thread(() -> {
            synchronized (student) {
                System.out.println("t2 我通知了 notify!");
                student.notify();
            }
        });
        t2.start();

    }


    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    private static void reentrantLock() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("我进入了T1！");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("释放T1锁!");
            }

        });
        t1.start();

        Thread.sleep(2000);

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("我进入了T2！");
                condition.signal();
                System.out.println("我是T2,我唤醒了T1,T1进入等待状态！");
                //condition.signalAll();
            } finally {
                lock.unlock();
                System.out.println("释放T2锁!");
            }
        });
        t2.start();

    }

    public static void funcTest(String message, int status) {

        ComUtils.isTrue(status != HttpStatus.HTTP_OK)
                .throwException("创建发布计划任务失败-调用更新xxl-job api接口异常:" + message);


        ComUtils.isTrueOrFalse(message.equals("success"))
                .trueOfFalseHandle(() -> {
                    System.out.println("true,开始操作");
                }, () -> {
                    System.out.println("false,开始操作");
                });

        ComUtils.isNotBlankOrNull(message).presentOrElseHandler(System.out::println, () -> {
            System.out.println("hello world");
        });


    }


    public static class OneToHundred {
        public static void main(String[] args) throws InterruptedException {
            Task t = new Task();
            Thread t1 = new Thread(t, "A");
            Thread t2 = new Thread(t, "B");
            t1.start();
            t2.start();
        }
    }
//假设A线程先获得锁，B会阻塞(同步队列)，A第一次调用signal(),但此时条件队列为空，firstWaiter为null，
//然后A调用await(),让出锁，同时自己进入条件等待队列，用一个变量记录自己持有锁的次数(saveState)
//然后释放锁的时候唤醒同步队列的第一个阻塞线程，即B，B醒了以后，尝试获取锁
//假设只有这两个线程，则B获取锁成功，number++，然后signal()，将A加入到同步队列，然后await(),
//释放锁，唤醒A，将自己阻塞。
//A醒来后，判断自己是否在同步队列中，在的话跳出自旋（这个地方肯定是在同步队列中的，因为唤醒只会唤醒
//同步队列上的线程。此处while循环是怕线程在别的情况下醒来)
//继续执行await()方法，没执行完，在await()方法内部阻塞了
//然后调用acquire(node,saveState)尝试获得锁，A获取成功，然后number++，在signal()，再调用await()
//这样A和B可以一直输出到100
//但是缺少了unlock()操作，ReentrantLock被A和B分别重入50次，A和B可以相互交替运行完全是因为await()和
//signal()的机制，所以未改正前，当A退出时，锁被A持有，且state = 50，表示重入了50次。
//调用了signal()也只是把B线程从条件队列移动到同步队列，没有调用await()，没让出锁。
//即便在最后调用一次unlock()，也只是将state - 1，变为49，依然未释放锁。
//所以，在每次lock()之后一定要unlock().

    //改正
    public static class Task implements Runnable {
        private int number = 0;
        //private ReentranLock lock = new ReentranLock();
        private Condition condition = lock.newCondition();

        @Override
        public void run() {
            while (number < 100) {
                lock.lock();
                try {
                    number++;
                    condition.signal();

                    if (number < 100){
                        condition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    //停车场同时容纳的车辆10
    private  static  Semaphore semaphore=new Semaphore(10);
    public static void semaphore(){
        //模拟100辆车进入停车场
        for(int i=0;i<100;i++){
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("===="+Thread.currentThread().getName()+"来到停车场");
                        if(semaphore.availablePermits()==0){
                            System.out.println("车位不足，请耐心等待");
                        }
                        semaphore.acquire();//获取令牌尝试进入停车场
                        System.out.println(Thread.currentThread().getName()+"成功进入停车场");
                        Thread.sleep(new Random().nextInt(10000));//模拟车辆在停车场停留的时间
                        System.out.println(Thread.currentThread().getName()+"驶出停车场");
                        semaphore.release();//释放令牌，腾出停车场车位
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },i+"号车");
            thread.start();
        }
    }
}
