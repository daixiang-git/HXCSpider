package util;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 祥少 on 2017/6/24.
 */
public class MessageWork extends Thread {
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    public MessageWork(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (MainThread.flag) {
            try {
                String message = queue.take();
                if(Spider.YuYue(message)==0){
                    MainThread.flag=false;
                }else if(Spider.YuYue(message)==2){
                    queue.add(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
