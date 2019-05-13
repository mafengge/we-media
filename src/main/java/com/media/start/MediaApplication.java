package com.media.start;

import com.media.utils.AipOcrUtil;
import com.media.utils.DownloadZimeika;
import com.media.utils.JsoupUtil;
import com.media.utils.MediaFileUtils;
import com.media.utils.MediaUtils;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediaApplication {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(MediaApplication.class, args);
        AipOcrUtil.runPoints();
        //ss();
        //AipOcrUtil.getPoints("mafengge", "maniqiu5");
     /*   ScheduledService scheduledService = new ScheduledService();
        scheduledService.scheduled();
        scheduledService.scheduled1();
        scheduledService.scheduled2();*/
        //获取视频信息，下载地址、标题、作者等，放入TXT
        //JsoupUtil.getZimeikaInfo(1,11);

        //读取txt中视频下载地址，下载视频
        //downLoadZimeikaVideo();

        //将视频上传youtube
        //ScheduledService.uploadYoutube("mafengge", "mafengge", 1);
        //获取youlikehits积分
    }

    public static void ss() throws InterruptedException, IOException {

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
        WebDriverWait wait = new WebDriverWait(driver, 5, 1);
        WebElement until = wait.until(new ExpectedCondition<WebElement>() {

            @Override
            public WebElement apply(WebDriver driver) {
                driver.findElement(By.id("username"));
                System.out.println(111);
                return driver.findElement(By.id("username"));
            }

        });
        System.out.println(until);

   /* Thread.sleep(4000);
    String js2 = "var elem2 = document.getElementById('columns');"
        + "elem2.parentNode.removeChild(elem2);  ";
    ((JavascriptExecutor) driver).executeScript(js2);
*/



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
