package util;

/**
 * Created by 祥少 on 2017/6/24.
 */
public interface ThreadPool {
    void start();
    void stop();
    void addJob(String message);
}
