package com.media.utils;

import com.baidu.aip.ocr.AipOcr;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
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
                getPoints("mafengge", "maniqiu5", 1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("505877502", "lp123456", 1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("xingyu", "123456", 1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("mafengge1", "123456", 1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("guodongbin", "guodongbin1987", 1);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints("mafengge2", "maniqiu5", 1);
            }
        }).start();
    }

    public static Integer getAipOcr(String userName) throws Exception {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        String val = "";
        JSONObject res = client
            .basicGeneral(MediaUtils.parseImagePath + userName + ".png", new HashMap<String, String>());
        System.out.println(res.toString(2));
        String s = res.toString(2);
        Pattern p = Pattern.compile("[0-9]");
        JSONArray words_result = res.getJSONArray("words_result");
        for (int i = 0; i < words_result.length(); i++) {
            Object words = words_result.get(i);
            Matcher m = p.matcher(words.toString());
            if (m.find()) {
                String s1 = words.toString();
                val = s1.split(":")[1].replace("}", "").replace(",", "").replace("\"", "");
            }
        }
        val = val.replace("×", "*").replace("÷", "/").replace("÷", "/");
        log.info("计算数值：" + countVal(val));
        return countVal(val);
    }

    /**
     * 刷积分
     */
    public static void getPoints(String userName, String passWord, int err) {
        try {
            ChromeOptions options = new ChromeOptions();
            /*options.addArguments("start-maximized");
            options.addArguments("start-fullscreen");
            options.addArguments("disable-infobars");*/
            /*options.addArguments("--host-resolver-rules=\"MAP *.youtube.com 127.0.0.1\"");
            //options.addArguments("headless");
            options.addArguments("disable-plugins");
            options.addArguments("disable-gpu");
            options.addArguments("no-sandbox");
            options.addArguments("disable-dev-shm-usage");
            options.addArguments("incognito");
            options.addArguments("ignore-certificate-errors");*/
            //System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
            WebDriver driver = new EventFiringWebDriver(new ChromeDriver(options))
                .register(new DriverListener(userName, passWord, err));
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.get("https://www.youlikehits.com/login.php");
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            username.sendKeys(userName);
            password.sendKeys(passWord);
            driver.findElement(By.id("loginform")).submit();
            driver.get("https://www.youlikehits.com/youtubenew2.php");
            while (true) {
                try {
                    /*if (!AipOcrUtil.doesWebElementExist(driver, "//*[@id=\"listall\"]/center")) {
                        String text = driver.findElement(By.xpath("//*[@id=\"listall\"]/center")).getText();
                        if (text.contains("YouTube Limit")) {
                            System.out.println(1);
                            Thread.sleep(600000);
                        }
                    }*/
                    String oldPoints = "";
                    try {
                        oldPoints = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]")).getText();
                    } catch (Exception e) {
                        driver.navigate().refresh();
                        driver.get("https://www.youlikehits.com/youtubenew2.php");
                        log.error("获取总积分异常");
                    }

                    if (!doesWebElementExist(driver, "//*[@id=\"captcha\"]/table[1]/tbody/tr/td/img")) {
                        //截图
                        screenShots(driver, userName);
                        //百度识图 + 计算数值
                        Integer aipOcr = getAipOcr(userName);
                        driver.findElement(By.name("answer")).sendKeys(String.valueOf(aipOcr));
                        driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[2]/tbody/tr/td/input[2]")).click();
                        Thread.sleep(2000);
                    }
                    driver.findElement(By.className("followbutton")).click();
                    Set<String> winHandels = driver.getWindowHandles();
                    List<String> it = new ArrayList<>(winHandels);
                    for (String list : it) {
                        System.out.println(list);
                    }
                   /* driver.switchTo().window(it.get(1));
                    ((EventFiringWebDriver) driver).executeScript("window.stop()");
                    ((EventFiringWebDriver) driver).executeScript("alert(1111);");*/
                    String addPoints = driver.findElement(By.xpath("//*[@id=\"listall\"]/center")).getText();
                    addPoints = addPoints.substring(addPoints.indexOf("Points:") + 7,addPoints.indexOf("View")).replace(" ","").trim();
                    System.out.println("添加分数：：" + addPoints);
                    if (StringUtils.isNotBlank(addPoints) && StringUtils.isNumeric(addPoints)
                        && Integer.parseInt(addPoints) >= 3 && Integer.parseInt(addPoints) <= 35) {
                        sleepTimes(addPoints);
                    } else {
                        log.info("获取添加分数失败");
                        Thread.sleep(140000);
                    }
                    String newPoints = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]")).getText();
                    log.info(userName + ": 当前分数：" + newPoints);
                    if (oldPoints.equals(newPoints)) {
                        driver.navigate().refresh();
                    }
                } catch (Exception e1) {
                    log.error(userName + "循环中发生异常" + e1);
                }
            }
        } catch (Exception e) {
            log.error("getPoints::", e);
        }
    }

    public static void sleepTimes(String points) throws Exception {
        int anInt = Integer.parseInt(points);
        int sec = 45000;
        for (int i = 3; i < 36; i++) {
            sec = sec + 3000;
            if (anInt == i) {
                log.info("sleep : " + sec);
                Thread.sleep(sec);
                break;
            }
        }

    }

    /**
     * 判断元素是否存在
     */
    public static boolean doesWebElementExist(WebDriver driver, String str) {
        try {
            driver.findElement(By.xpath(str));
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    /**
     * 截图
     */
    public static void screenShots(WebDriver driver, String userName) {
        try {
            WebElement ele = driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[1]/tbody/tr/td/img"));
            // Get entire page screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImg = ImageIO.read(screenshot);

            // Get the location of element on the page
            Point point = ele.getLocation();

            // Get width and height of the element
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();

            // Crop the entire page screenshot to get only element screenshot
            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(),
                eleWidth+point.getX(), eleHeight+point.getY());
            ImageIO.write(eleScreenshot, "png", screenshot);

            // Copy the element screenshot to disk
            File screenshotLocation = new File(MediaUtils.parseImagePath + userName + ".png");
            FileUtils.copyFile(screenshot, screenshotLocation);
        } catch (Exception e) {
            log.error("获取积分截图错误", e);
        }
    }

    /**
     * 图片数字计算
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