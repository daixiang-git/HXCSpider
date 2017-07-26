import model.Info;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.MainThread;
import util.Spider;
import util.ThreadPool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 祥少 on 2017/7/26.
 */
public class Main {
    static ThreadPool threadPool = new MainThread();

    public static void main(String[] args) throws IOException {
        System.out.println("请输入好学成账号密码------");

        String username="";
        String password="";
        if(username.equals("")||password.equals("")){
            System.out.println("账号或者密码为空！！！");
            return;
        }
        Spider.login(username, password);
        JSONObject json = Spider.getYuYueJson("魏健");
        for (int i = 0; i < 4; i++) {
            threadPool.addJob(Spider.getorder(json, i));
        }
        //开始执行任务
        threadPool.start();
    }

}
