package com.media.start;

import com.media.utils.AipOcrUtil;
import com.media.utils.DownloadZimeika;
import com.media.utils.DriverListener;
import com.media.utils.MediaFileUtils;
import com.media.utils.MediaUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediaApplication {

    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(MediaApplication.class, args);
        AipOcrUtil.runPoints();
        //ss();
        //AipOcrUtil.getPoints("mafengge", "maniqiu5", 1);
     /*   ScheduledService scheduledService = new ScheduledService();
        scheduledService.scheduled();
        scheduledService.scheduled1();
        scheduledService.scheduled2();*/
        //获取视频信息，下载地址、标题、作者等，放入TXT
        //JsoupUtil.getZimeikaInfo(17,21);

        //读取txt中视频下载地址，下载视频
        //downLoadZimeikaVideo();

        //将视频上传youtube
        //ScheduledService.uploadYoutube("mafengge", "mafengge", 6);
        //获取youlikehits积分
        //getPoints();
    }
public static void ss () throws InterruptedException, IOException {

    ChromeOptions options = new ChromeOptions();
    options.addArguments("disable-infobars");
    //options.addArguments("headless");
    WebDriver driver = new ChromeDriver(options);
    /*WebDriver driver = new EventFiringWebDriver(new ChromeDriver())
        .register(new DriverListener("mafengge1", "123456", 1));
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);*/
    driver.get("https://www.youlikehits.com/login.php");
    WebElement username = driver.findElement(By.id("username"));
    WebElement password = driver.findElement(By.id("password"));
    username.sendKeys("mafengge1");
    password.sendKeys("123456");
    driver.findElement(By.id("loginform")).submit();
    ((JavascriptExecutor) driver).executeScript("window.open(\"https://www.youlikehits.com/youtuberender.php?id=2824551\")");
    Thread.sleep(10000);
    Cookie lbsessionid = driver.manage().getCookieNamed("LBSESSIONID");
    Set<Cookie> cookies = driver.manage().getCookies();
    StringBuffer cook = new StringBuffer();

    CookieStore cookieStore = new BasicCookieStore();

    HttpGet get = new HttpGet("https://www.youlikehits.com/playyoutubenew.php?id=2824551&step=points&x=43cbee73c07809518e7149877543602b02a747a673da032924435fd50dde541f&rand=0.8267959001314654");//这里发送get请求
    for(Cookie cookie :cookies) {
        BasicClientCookie getCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
        getCookie.setDomain(cookie.getDomain());
        getCookie.setPath(cookie.getPath());
        getCookie.setExpiryDate(cookie.getExpiry());
        getCookie.setSecure(cookie.isSecure());
        cookieStore.addCookie(getCookie);
    }
    CloseableHttpClient httpClient = HttpClients.custom()
        .setDefaultCookieStore(cookieStore)
        .build();

    get.setHeader("Accept","*/*");
    get.setHeader("Accept-Encoding","gzip, deflate, br");
    get.setHeader("Connection","keep-alive");
    get.setHeader("Host","www.youlikehits.com");
    get.setHeader("Referer","https://www.youlikehits.com/youtubenew2.php");
    get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
    get.setHeader("X-Requested-With","XMLHttpRequest");
    // 通过请求对象获取响应对象
    HttpResponse response = httpClient.execute(get);
    // 判断网络连接状态码是否正常(0--200都数正常)
    String result = EntityUtils.toString(response.getEntity(), "utf-8");
    System.out.println(result);



    /*Set<String> windowHandles = driver.getWindowHandles();
    List<String> it = new ArrayList<>(windowHandles);
    System.out.println(it.get(0));
    System.out.println(it.get(1));
    driver.switchTo().window(it.get(1));*/
   // ((JavascriptExecutor) driver).executeScript("window.open(\"https://www.youlikehits.com/playyoutubenew.php?id=2823849&step=points&x=74bc1d3bd3088b0871b2d7e2ca100040fb535185817edf6898a47d1a33cee257&rand=0.8267939001314654\")");

}



    public static void downLoadZimeikaVideo() throws InterruptedException {
        String info = MediaFileUtils.readFile(MediaUtils.zimekaInfoPath);
        JSONArray jsonArray = new JSONArray("[" + info + "]");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String author = jsonObject.getString("author");
            String videoTitle = jsonObject.getString("videoTitle") + "--" + author;
            String videoUrl = jsonObject.getString("videoUrl");
            DownloadZimeika.downloadVideo(MediaUtils.zimeikaVideoPath, videoTitle, videoUrl);
            Thread.sleep(1000);
            System.out.println((jsonObject.getString("publishTime")));
        }
    }

}
