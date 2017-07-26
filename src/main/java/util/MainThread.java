package util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 祥少 on 2017/6/24.
 */
public class MainThread implements ThreadPool {
    public static volatile boolean flag = false;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    @Override
    public void start() {
        flag = true;
        for (int i = 0; i < 10; i++) {
            new MessageWork(queue).start();
        }
    }

    @Override
    public void stop() {
        flag = false;
    }

    @Override
    public void addJob(String message) {
        try {
            System.out.println("收到一条任务："+message);
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
