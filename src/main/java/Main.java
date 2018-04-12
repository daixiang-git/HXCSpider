import net.sf.json.JSONObject;
import util.MainThread;
import util.Spider;
import util.ThreadPool;

import java.io.IOException;

/**
 * Created by 祥少 on 2017/7/26.
 */
public class Main {
    static ThreadPool threadPool = new MainThread();

    public static void main(String[] args) throws IOException {
        System.out.println("请输入好学成账号密码------");

        String username = "15280901938";
        String password = "19951115abcd";

        if (username.equals("") || password.equals("")) {
            System.out.println("账号或者密码为空！！！");
            return;
        }
        Spider.login(username, password);
        JSONObject json = Spider.getYuYueJson("魏健");
//        for (int i = 0; i < 4; i++) {
//            threadPool.addJob(Spider.getorder(json, i));
//        }
        threadPool.addJob(Spider.getorder(json, 0));

        System.out.println("开始执行！！！！！！！！！！！！！！！");
        //开始执行任务
        threadPool.start();
    }

}
