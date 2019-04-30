package com.media.start;

import com.media.utils.DownloadZimeika;
import com.media.utils.DriverListener;
import com.media.utils.MediaUtils;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
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

    public static void getIntegral() throws Exception {
        ChromeOptions option = new ChromeOptions();
        //后台运行
        //option.addArguments("headless");
        //取消"Chrome正在受到自动软件的控制"提示
        option.addArguments("disable-infobars");
        option.addArguments("window-size=1024,768");
        WebDriver driver = new EventFiringWebDriver(new ChromeDriver(option)).register(new DriverListener());
//        driver.manage().window().maximize();
        driver.get("https://www.youlikehits.com/login.php");//打开指定的网站
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        username.sendKeys("mafengge");
        password.sendKeys("maniqiu5");
        driver.findElement(By.id("loginform")).submit();
        driver.get("https://www.youlikehits.com/youtubenew2.php");
        WebElement ele1 = driver.findElement(By.id("captcha"));
//        WebElement ele = ele1.findElement(By.tagName("img"));
        WebElement ele = driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[1]/tbody/tr/td/img"));
// Get entire page screenshot
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);
        File screenshotLocation1 = new File("C:\\images\\1.png");
        FileUtils.copyFile(screenshot, screenshotLocation1);
        Iterator<ImageReader> png = ImageIO.getImageReadersByFormatName("png");
        ImageReader next = png.next();
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(new FileInputStream(screenshot));
        next.setInput(imageInputStream, true);
        ImageReadParam defaultReadParam = next.getDefaultReadParam();


// Get the location of element on the page
        Point point = ele.getLocation();

// Get width and height of the element
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();

        Rectangle rectangle = new Rectangle(point.getX(), point.getY(), eleWidth+point.getX(), eleHeight+point.getY());
        defaultReadParam.setSourceRegion(rectangle);
        BufferedImage bufferedImage = next.read(0, defaultReadParam);
        ImageIO.write(bufferedImage, "png", new File("C:\\images\\2.png"));
// Crop the entire page screenshot to get only element screenshot
        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
            eleWidth+point.getX(), eleHeight+point.getY());
        ImageIO.write(eleScreenshot, "png", screenshot);

// Copy the element screenshot to disk
        File screenshotLocation = new File("C:\\images\\GoogleLogo_screenshot.png");

        FileUtils.copyFile(screenshot, screenshotLocation);
    }

    public static void downLoadZimeikaVideo() throws InterruptedException {
        /*String info = FileUtils.readFile(MediaUtils.zimekaInfoPath);
        JSONArray jsonArray = new JSONArray("[" + info + "]");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String author = jsonObject.getString("author");
            String videoTitle = jsonObject.getString("videoTitle") + "--" + author;
            String videoUrl = jsonObject.getString("videoUrl");
            DownloadZimeika.downloadVideo(MediaUtils.zimeikaVideoPath, videoTitle, videoUrl);
            Thread.sleep(1000);
            log.info(jsonObject.getString("publishTime"));
        }*/
    }

    public static void getPoints() throws Exception{
        ChromeOptions option = new ChromeOptions();
        //后台运行
        //option.addArguments("headless");
        //取消"Chrome正在受到自动软件的控制"提示
        option.addArguments("disable-infobars");
        WebDriver driver = new EventFiringWebDriver(new ChromeDriver()).register(new DriverListener());
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30,TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get("https://www.youlikehits.com/login.php");//打开指定的网站
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        username.sendKeys("mafengge");
        password.sendKeys("maniqiu5");
        driver.findElement(By.id("loginform")).submit();
        driver.get("https://www.youlikehits.com/youtubenew2.php");
        for(int i=0;i<100;i++) {
            driver.findElement(By.className("followbutton")).click();
            Thread.sleep(140000);
        }
    }
}
