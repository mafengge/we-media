package com.media.utils;

import com.baidu.aip.ocr.AipOcr;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
@Slf4j
public class AipOcrUtil {
    //设置APPID/AK/SK
    public static final String APP_ID = "16104677";
    public static final String API_KEY = "tvrCZ6GUi1kaEtiIHSm76kbQ";
    public static final String SECRET_KEY = "Dtd0dL5tvjUgHA3XEykHsDL22MVVrAkg";

    public static void runPoints() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("mafengge", "maniqiu5",1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("505877502", "lp123456",1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("xingyu", "123456",1);
            }
        }).start();

        //getPoints("maniqiu", "maniqiu5");
    }

    public static Integer getAipOcr() throws Exception {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        String val = "";
        JSONObject res = client.basicGeneral(MediaUtils.parseImagePath, new HashMap<String, String>());
        System.out.println(res.toString(2));
        String s = res.toString(2);
        Pattern p = Pattern.compile("[0-9]");
        JSONArray words_result = res.getJSONArray("words_result");
        for (int i=0;i<words_result.length();i++) {
            Object words = words_result.get(i);
            Matcher m = p.matcher(words.toString());
            if (m.find()) {
                String s1 = words.toString();
                val = s1.split(":")[1].replace("}","").replace(",","").replace("\"","");
            }
        }
        val = val.replace("×","*").replace("÷","/");
        log.info("计算数值："+countVal(val));
        return countVal(val);
    }

    /**
     * 刷积分
     */
    public static void getPoints(String userName, String passWord,int err) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            options.addArguments("start-fullscreen");
            options.addArguments("disable-infobars");
            WebDriver driver = new EventFiringWebDriver(new ChromeDriver()).register(new DriverListener(userName, passWord, err));
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(30,TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.get("https://www.youlikehits.com/login.php");
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys(userName);
            password.sendKeys(passWord);
            driver.findElement(By.id("loginform")).submit();
            driver.get("https://www.youlikehits.com/youtubenew2.php");
            /*if (err==10) {
                driver.quit();
            }*/
            for(int i=0;i<50000000;i++) {
                if (doesWebElementExist(driver)) {
                    //截图
                    screenShots(driver);
                    //百度识图 + 计算数值
                    Integer aipOcr = getAipOcr();
                    driver.findElement(By.name("answer")).sendKeys(String.valueOf(aipOcr));
                    driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[2]/tbody/tr/td/input[2]")).click();
                    Thread.sleep(2000);
                }
                driver.findElement(By.className("followbutton")).click();
                String oldPoints = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]")).getText();
                //String addPoints = driver.findElement(By.xpath("//*[@id=\"listall\"]/center/b[2]")).getText();
                Thread.sleep(140000);
                String newPoints = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]")).getText();
                log.info("当前分数：" + newPoints);
                if (oldPoints.equals(newPoints)) {
                    driver.navigate().refresh();
                }
            }
        } catch (Exception e) {
            log.error("：",e);
        }
    }

    /**
     * 判断元素是否存在
     * @param driver
     * @return
     */
    public static boolean doesWebElementExist(WebDriver driver) {
        try {
            driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[1]/tbody/tr/td/img"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * 截图
     * @param driver
     */
    public static void screenShots(WebDriver driver) {
        try {
            WebElement ele = driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[1]/tbody/tr/td/img"));
            // Get entire page screenshot
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            BufferedImage  fullImg = ImageIO.read(screenshot);

            // Get the location of element on the page
            Point point = ele.getLocation();

            // Get width and height of the element
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();

            // Crop the entire page screenshot to get only element screenshot
            BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
                eleWidth+point.getX(), eleHeight+point.getY());
            ImageIO.write(eleScreenshot, "png", screenshot);

            // Copy the element screenshot to disk
            File screenshotLocation = new File(MediaUtils.parseImagePath);
            FileUtils.copyFile(screenshot, screenshotLocation);
        } catch (Exception e) {
            log.error("获取积分截图错误",e);
        }
    }

    /**
     * 图片数字计算
     * @param val
     * @return
     */
    public static Integer countVal(String val) {
        if (val.contains("+")) {
            String[] split = val.split("\\+");
            int result = Integer.parseInt(split[0]) + Integer.parseInt(split[1]);
            return result;
        }
        if (val.contains("*")) {
            String[] split = val.split("\\*");
            int result = Integer.parseInt(split[0]) * Integer.parseInt(split[1]);
            return result;
        }
        if (val.contains("/")) {
            String[] split = val.split("\\/");
            int result = Integer.parseInt(split[0]) / Integer.parseInt(split[1]);
            return result;
        }
        if (val.contains("-")) {
            String[] split = val.split("\\-");
            int result = Integer.parseInt(split[0]) - Integer.parseInt(split[1]);
            return result;
        }
        return 1;
    }
}