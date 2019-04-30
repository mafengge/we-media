package com.media.start;

import com.media.utils.DownloadZimeika;
import com.media.utils.DriverListener;
import com.media.utils.MediaFileUtils;
import com.media.utils.MediaUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
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
@Slf4j
public class MediaApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MediaApplication.class, args);
     /*   ScheduledService scheduledService = new ScheduledService();
        scheduledService.scheduled();
        scheduledService.scheduled1();
        scheduledService.scheduled2();*/
        //获取视频信息，下载地址、标题、作者等，放入TXT
        //JsoupUtil.getZimeikaInfo(17,21);

        //读取txt中视频下载地址，下载视频
        //downLoadZimeikaVideo();

        //将视频上传youtube
        //ScheduledService.uploadYoutube("mafengge", "mafengge", 1);
        //获取youlikehits积分
        //getPoints();
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
            log.info(jsonObject.getString("publishTime"));
        }
    }

}
