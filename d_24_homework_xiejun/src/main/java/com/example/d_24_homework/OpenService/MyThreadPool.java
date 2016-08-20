package com.example.d_24_homework.OpenService;

import java.util.LinkedList;

public class MyThreadPool extends ThreadGroup{
    private boolean isClose = false;  //线程池是否关闭
    private LinkedList workQueue;     //工作队列
    private static int threadPollID = 1;  //线程池ID

    public MyThreadPool(int poolSize) {
        super("threadPoolId" + "");      //调用父类构造方法，指定ThreadGroup的名称
        setDaemon(true);                 //继承的方法，设置是否设置守护线程池
        workQueue = new LinkedList();     //创建工作队列
        for (int i = 0; i < poolSize; i++) {
            new WorkThread(i).start();    //创建并启动工作线程，创建线程池最大容量数的工作线程
        }
    }

    //向工作线程中添加一个任务，由工作线程来执行该任务

    public synchronized void execute(Runnable task){
//        if (isClose) {
//            throw new IllegalStateException();
//        }
        if (task != null) {
            workQueue.add(task);    //向消息队列中添加一个任务
            notify();               //唤醒一个正在getTask()方法中等待任务的工作线程
        }
    }

    //从工作队列中取出一个任务，工作线程会调用该方法
    private synchronized Runnable getTask(int threadId) throws InterruptedException{
        while (workQueue.size() == 0) {
            if (isClose) {return null;}
            System.out.println("工作线程" + threadId + "等待任务");
            wait();                 //如果工作线程中没有任务，那么就等待着
        }
        System.out.println("工作线程" + threadId + "开始执行任务...");
        return (Runnable) workQueue.removeFirst();          //返回队列中的第一个元素，并从队列中删除
    }

    //等待工作线程把任务执行完成
    public void waitFinish(){
        synchronized (this) {
            isClose = true;
            notifyAll();        //唤醒所有还在getTask()方法中等待任务的工作线程
        }
        Thread[] threads = new Thread[activeCount()];   //activeCount()返回该线程组中活动线程的估计值
        int count = enumerate(threads);  //enumerate方法继承自ThreadGroup，根据活动的线程的估计值获得该线程组中当前所有活动的工作线程
        for (int i = 0; i < count; i++) {   //等待所有工作线程结束
            try {
                threads[i].join();    //等待工作线程结束
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //关闭线程池
    public synchronized void closePool(){
        if (!isClose) {
            waitFinish();   //等待工作线程执行完毕
            isClose = true;
            workQueue.clear();  //清空工作队列
            interrupt();   //中断线程池所有的工作线程
        }
    }

    //工作线程，负责从工作队列中取出任务，并执行
    private class WorkThread extends Thread{
        private int id;
        public WorkThread(int id){
            //父类构造，将线程加入到当前ThreadPool线程组
            super(MyThreadPool.this, id + "");
            this.id = id;
        }
        public void run(){
            while (! isInterrupted()) { //继承自Thread，判断线程是否被中断
                Runnable task = null;
                try {
                    task = getTask(id);    //取出任务
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程
                if (task == null) {
                    return;
                }
                task.run();   //运行任务
            }
        }
    }
}