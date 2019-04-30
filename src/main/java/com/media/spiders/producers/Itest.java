
package com.media.spiders.producers;

import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
@Slf4j
public class Itest {

    public static void main(String[] args) throws Exception {
        /*System.setProperty("webdriver.chrome.driver","C:\\ChromedDriver\\chromedriver.exe");//chromedriver服务地址
        WebDriver driver =new ChromeDriver(); //新建一个WebDriver 的对象，但是new 的是FirefoxDriver的驱动
        driver.get("http://www.baidu.com");//打开指定的网站

        driver.close();*/
        test("i6639932541157507597");
    }

    //只需传入video_id即可

    public static String test(String videoId) throws Exception {
        String link = "/video/urls/v/1/toutiao/mp4/";
        String uriAPI = "https://www.365yg.com/a6681773825517945358/#mid=95420576271";// Post方式没有参数在这里
        link = link + videoId;
        Map<String, String> map = excuteJs(link);
        uriAPI = uriAPI + videoId + "?r=" + map.get("r") + "&s=" + map.get("s");
        HttpGet httpGet = new HttpGet(uriAPI);
        log.info(uriAPI);
        HttpResponse httpResponse = HttpClients.createDefault().execute(httpGet);
        String result = "";
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);// 取出应答字符串
            JSONObject jsonobject = JSONObject.fromObject(result);
            //String posterUrl = jsonobject.getJSONObject("data").getString("poster_url");
            String mainUrl = jsonobject.getJSONObject("data").getJSONObject("video_list").getJSONObject("video_1")
                .getString("main_url");
            String video = new String(Base64.getDecoder().decode(mainUrl.getBytes()));
            return video;
        }
        return null;
    }

    //得出r和s

    private static Map<String, String> excuteJs(String link) throws ScriptException,
        FileNotFoundException, NoSuchMethodException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript"); // 得到脚本引擎
        String script = "function test(a) {" +
            "var c = function() {" +
            "for(var d = 0,f = new Array(256), g = 0; 256 != g; ++g) {" +
            "d = g," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "d = 1 & d ? -306674912 ^ d >>> 1 : d >>> 1," +
            "f[g] = d" +
            "}" +
            "return \"undefined\" != typeof Int32Array ? new Int32Array(f) : f" +
            "} ()," +
            "b = function(g) {" +
            "for (var j, k, h = -1,f = 0,d = g.length; f < d;) {" +
            "j = g.charCodeAt(f++)," +
            "j < 128 ? h = h >>> 8 ^ c[255 & (h ^ j)] : j < 2048 ? (h = h >>> 8 ^ c[255 & (h ^ (192 | j >> 6 & 31))], h = h >>> 8 ^ c[255 & (h ^ (128 | 63 & j))]) : j >= 55296 && j < 57344 ? (j = (1023 & j) + 64, k = 1023 & g.charCodeAt(f++), h = h >>> 8 ^ c[255 & (h ^ (240 | j >> 8 & 7))], h = h >>> 8 ^ c[255 & (h ^ (128 | j >> 2 & 63))], h = h >>> 8 ^ c[255 & (h ^ (128 | k >> 6 & 15 | (3 & j) << 4))], h = h >>> 8 ^ c[255 & (h ^ (128 | 63 & k))]) : (h = h >>> 8 ^ c[255 & (h ^ (224 | j >> 12 & 15))], h = h >>> 8 ^ c[255 & (h ^ (128 | j >> 6 & 63))], h = h >>> 8 ^ c[255 & (h ^ (128 | 63 & j))])"
            +
            "}" +
            "return h ^ -1" +
            "};" +
            "var f = b(a) >>> 0;" +
            "return f.toString();" +
            "}" +
            "function test2(){" +
            "return Math.random().toString(10).substring(2);" +
            "}";
        engine.eval(script);
        Invocable inv = (Invocable) engine;
        Object test2 = inv.invokeFunction("test2");
        String param = link + "?r=" + test2;
        Object a = inv.invokeFunction("test", param);
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("r", test2.toString());
        hashMap.put("s", a.toString());
        return hashMap;
    }

    public static String Intercept(String str) {
        if ("".equals(str) || null == str) {
            return "";
        }
        int start = str.indexOf("\"");
        int last = str.lastIndexOf("\"");
        if (start >= 0 && last > start) {
            str = str.substring(start + 1, last);
        }
        return str;
    }

    public static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }
}