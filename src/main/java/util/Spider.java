package util;

import model.Info;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 祥少 on 2017/7/26.
 */
public class Spider {
    static String LOGINURL = "http://www.haoxueche.com/auth/login?service=http%3A%2F%2Fwww.haoxueche.com%3A80%2Fprotal%2Findex";
    static String YUYUEURL = "http://www.haoxueche.com/s/order/home";
    static String POSTURL = "http://www.haoxueche.com/auth/login?service=http://www.haoxueche.com:80/protal/index";
    static String TIJIAOORDERURL = "http://new.haoxueche.com/Orders/AddOrders";
    static String ORDERURL = "http://new.haoxueche.com/Orders/QueryCoachesAndOrders";

    static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    static String acceptEncoding = "gzip, deflate";
    static Map<String, String> cookie = new HashMap<String, String>();

    public static void login(String username, String password) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        Connection.Response res = Jsoup.connect(LOGINURL).method(Connection.Method.GET).timeout(5000).userAgent(userAgent).header("Accept-Encoding", acceptEncoding).validateTLSCertificates(false).execute();
        cookie.putAll(res.cookies());
        Document document = res.parse();
        Element element = document.getElementById("fm1");
        Elements element1 = element.getElementsByTag("input");
        int key = 0;
        for (Element e : element1) {
            if (key < 2) {
                key++;
                continue;
            }
            map.put(e.attr("name"), e.attr("value"));
        }
        map.put("username", username);
        map.put("password", password);
        map.put("submit", "登录");
        Connection.Response res1 = Jsoup.connect(POSTURL).data(map).cookies(cookie).method(Connection.Method.POST).timeout(5000).userAgent(userAgent).header("Accept-Encoding", acceptEncoding).validateTLSCertificates(false).execute();
        cookie.putAll(res1.cookies());
    }

    public static JSONObject getYuYueJson(String teacherName) throws IOException {
        Connection.Response res = Jsoup.connect(YUYUEURL).method(Connection.Method.GET).cookies(cookie).timeout(5000).userAgent(userAgent).header("Accept-Encoding", acceptEncoding).validateTLSCertificates(false).execute();
        Document document = res.parse();
        Element element = document.getElementById("iisjfj");
        String url = element.attr("src");

        Connection.Response res1 = Jsoup.connect(url).method(Connection.Method.GET).cookies(cookie).timeout(5000).userAgent(userAgent).header("Accept-Encoding", acceptEncoding).validateTLSCertificates(false).execute();
        cookie.putAll(res1.cookies());
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Encoding", acceptEncoding);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        String dateNowStr = sdf.format(date);
        String string = "STAGE=2&MID=1000317&STUID=1159799&GROUPID=0&SHOOLCODE=510700080&TID=&ISMODE=1&ISTHEDAY=1&PAGEINDEX=1&PAGESIZE=3&DATE=" + dateNowStr + "&TEACHERNAME=" + teacherName;
        HashMap<String, String> data = new HashMap<String, String>();
        String[] strings = string.split("&");
        for (String s : strings) {
            String[] string1 = s.split("=");
            if (string1.length == 1) {
                data.put(string1[0], "");
            } else {
                data.put(string1[0], string1[1]);
            }
        }
        Connection.Response res3 = Jsoup.connect(ORDERURL).method(Connection.Method.POST).data(data).cookies(cookie).timeout(5000).headers(headers).userAgent(userAgent).ignoreContentType(true).validateTLSCertificates(false).execute();
        cookie.putAll(res3.cookies());
        String body = res3.body();
        Pattern pattern = Pattern.compile("\\\\u0027");
        Matcher m = pattern.matcher(body);
        body = m.replaceAll("'");
        JSONObject json = JSONObject.fromObject(body.substring(2, body.length() - 2));
        return json;
    }

    public static String getorder(JSONObject json, int index) {
        Info o = new Info();
        for (Object str : json.keySet()) {
            if (str.equals("timeData")) {
                JSONArray jsonTime = (JSONArray) json.get(str);
                o.setBe((String) jsonTime.getJSONObject(index).get("be"));
                o.setEn((String) jsonTime.getJSONObject(index).get("en"));
            }
            if (str.equals("teacherId")) {
                o.setTeacherId((String) json.get("teacherId"));
            }
            if (str.equals("teacherName")) {
                o.setTeacherName((String) json.get("teacherName"));
            }
            if (str.equals("phone")) {
                o.setPhone((String) json.get("phone"));
            }
            if (str.equals("carNumber")) {
                o.setCarNumber((String) json.get("carNumber"));
            }
        }
        return getOrderString(o);
    }

    public static String getOrderString(Info o) {
        return "1000317,0,1159799,510700080,"+o.getTeacherId()+"," + o.getCarNumber() + "," + o.getBe().split(" ")[0] + "," + o.getBe() + "," + o.getEn() + ",2,0.00,1,0,1," + o.getTeacherName() + ",20170727_1003053080010001," + o.getPhone() + ",1.0,1.0";
    }

    public static int YuYue(String orderString) throws IOException {

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("OrderString", orderString);
        Connection.Response res3 = Jsoup.connect(TIJIAOORDERURL).method(Connection.Method.POST).data(data).cookies(cookie).timeout(5000).header("Accept-Encoding", acceptEncoding).userAgent(userAgent).ignoreContentType(true).validateTLSCertificates(false).execute();
        if (res3.body().contains("成功预约1条")) {
            System.out.println("-------------成功预约到一条-------------");
            System.out.println("程序结束！！");
            return 0;
        }
        if(res3.body().contains("今日预约学时数已超上限")) {
            System.out.println("-------------成功预约到一条-------------");
            System.out.println("预约成功:" + res3.body());
            return 0;
        }
        System.out.println("预约失败:" + res3.body());
        return 2;
    }
}
