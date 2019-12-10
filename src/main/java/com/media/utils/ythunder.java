package com.media.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;

@Slf4j
public class ythunder {

    public static void runPoints() {
        startThread("mafengge6","maniqiu5");
        startThread("mafengge1", "maniqiu5");
        startThread("mafengge", "maniqiu5");
        /*startThread("505877502", "lp123456");
        startThread("guodongbin", "guodongbin1987");


        startThread("mafengge2", "maniqiu5");
        startThread("mafengge10", "maniqiu5");

        startThread("xingyu", "123456");
        startThread("mafengge3", "maniqiu5");
        startThread("mafengge4", "maniqiu5");
        startThread("mafengge5", "maniqiu5");
        startThread("mafengge7", "maniqiu5");
        startThread("mafengge8", "maniqiu5");
        startThread("mafengge9", "maniqiu5");
        startThread("mafengge11", "maniqiu5");
        startThread("mafengge12", "maniqiu5");
        startThread("mafengge13", "maniqiu5");
        startThread("mafengge14", "maniqiu5");
        startThread("mafengge15", "maniqiu5");*/
    }

    public static void startThread(String userName, String passWord) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPoints(userName, passWord);
                //getCurrPoints(userName,passWord);
            }
        }).start();
        /*try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 刷积分
     */
    public static void getPoints(String userName, String passWord) {
        WebDriver driver = driverHandler(userName, passWord);
        try {
            while (true) {
                try {
                    WebElement currentPionts = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]"));
                    String oldPoints = currentPionts.getText();
                    System.out.println(userName + " 当前分数：" + oldPoints);
                    WebElement followbutton = driver.findElement(By.className("followbutton"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", followbutton);
                    Set<String> winHandels = driver.getWindowHandles();
                    List<String> it = new ArrayList<>(winHandels);
                    if (null != it && it.size() > 2) {
                        throw new Exception(userName + "窗口大于2! v" + it.size());
                    }

                    driver.switchTo().window(it.get(1));
                    String js1 = "window.stop(); ";
                    ((JavascriptExecutor) driver).executeScript(js1);
                    driver.switchTo().window(it.get(0));
                    //根据分数sleep时长
                    sleepTimes(driver,userName);
                    String newPoints = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]")).getText();
                    if (oldPoints.equals(newPoints)) {
                        driver.navigate().refresh();
                        log.info("分数相同刷新当前页面");
                    }
                } catch (Exception e1) {
                    log.error(userName + "循环中发生异常", e1);
                    driver.quit();
                    //Thread.sleep(960000);
                    Thread.sleep(10000);
                    driver = driverHandler(userName, passWord);
                }
            }
        } catch (Exception e) {
            log.error("getPoints::" + userName, e);
            driver.quit();
            getPoints(userName, passWord);
        }
    }

    public static void main(String[] args){
        //driverHandler("","");
        ChromeOptions options = new ChromeOptions();
            /*options.addArguments("start-maximized");
            options.addArguments("start-fullscreen");
            options.addArguments("disable-dev-shm-usage");
            options.addArguments("incognito");
            options.addArguments("ignore-certificate-errors");*/
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--headless");
        //System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
        //System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
        WebDriver driver = new EventFiringWebDriver(new ChromeDriver(options));
        driver.get("http://v.douyin.com/9qE38J/");
        System.out.println(1);
    }

    public static WebDriver driverHandler(String userName, String passWord) {
        ChromeOptions options = new ChromeOptions();
            /*options.addArguments("start-maximized");
            options.addArguments("start-fullscreen");
            options.addArguments("disable-dev-shm-usage");
            options.addArguments("incognito");
            options.addArguments("ignore-certificate-errors");*/
        /*options.addArguments("--disable-plugins");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--headless");*/
        //System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
        //System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
            WebDriver driver = new EventFiringWebDriver(new ChromeDriver(options))
                .register(new DriverListener(userName, passWord,1));
        /*WebDriver driver = new ChromeDriver(options);*/
        try {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.get("https://ythunder.com/");
            driver.findElement(By.xpath("/html/body/section/section/header/div/ul/li[2]/span")).click();
            WebElement loginUserName = driver.findElement(By.xpath("//*[@id=\"loginUsername\"]"));
            WebElement loginPassword = driver.findElement(By.xpath("//*[@id=\"loginPassword\"]"));
            loginUserName.sendKeys("413942291@qq.com");
            loginPassword.sendKeys("maniqiu5");
            driver.findElement(By.xpath("//*[@id=\"loginBtn\"]")).click();
            driver.findElement(By.className("play_video")).click();
            driver.findElement(By.className("add_icon")).click();
            Set<String> winHandels = driver.getWindowHandles();
            List<String> it = new ArrayList<>(winHandels);
            driver.switchTo().window(it.get(1));
            driver.findElement(By.xpath("//*[@id=\"startBtn\"]")).click();
            Thread.sleep(5000);
            driver.switchTo().window(it.get(0));
            driver.findElement(By.className("add_icon")).click();
            Set<String> winHandels1 = driver.getWindowHandles();
            List<String> it1 = new ArrayList<>(winHandels1);
            driver.switchTo().window(it1.get(3));
            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id=\"startBtn\"]")).click();
            Thread.sleep(5000);
            driver.switchTo().window(it.get(0));
            driver.findElement(By.className("add_icon")).click();
            Set<String> winHandels2 = driver.getWindowHandles();
            List<String> it2 = new ArrayList<>(winHandels2);
            driver.switchTo().window(it2.get(5));
            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id=\"startBtn\"]")).click();
            Thread.sleep(5000);
            while (true) {
                Set<String> winHandels4 = driver.getWindowHandles();
                List<String> it4 = new ArrayList<>(winHandels4);
                if (it1.size()<1) {
                    continue;
                }
                for (int i=0;i<it4.size();i++) {
                    WebDriver window = driver.switchTo().window(it4.get(i));
                    System.out.println("111111111：： "+window.getCurrentUrl());
                    if (window.getCurrentUrl().contains("www.youtube.com")) {
                        try {
                            String js1 = "var elem2 = document.getElementById('columns');"
                                + "elem2.parentNode.removeChild(elem2);  ";
                            ((JavascriptExecutor) window).executeScript(js1);
                        } catch (Exception e) {
                            //Thread.sleep(20000);
                        }
                    }
                }
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            log.error("登录页报错" + userName, e);
            driver.quit();
        }

        return driver;
    }

    public static WebDriver getCurrPoints(String userName, String passWord) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--headless");
        //System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
        System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver(options);
        try {
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
            WebElement currentPionts = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]"));
            String oldPoints = currentPionts.getText();
            System.out.println(userName + " : " + oldPoints);
            for (int i=0;i<1;i++){
                Thread.sleep(3000);
                driver.findElement(By.xpath("//*[@id=\"bodybg\"]/table[1]/tbody/tr[1]/td/table/tbody/tr/td[2]/a[2]"))
                    .click();
                Thread.sleep(3000);
                driver.findElement(By.xpath(
                    "//*[@id=\"bodybg\"]/table[2]/tbody/tr/td/table[1]/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/center/a/font"))
                    .click();
            }
        } catch (Exception e) {
            log.error("登录页报错" + userName, e);
            driver.quit();
            driver = new ChromeDriver(options);
        }

        return driver;
    }


    /**
     * 截图
     */
    public static void screenShots(WebDriver driver, String userName) throws Exception {
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
            //       eleWidth + point.getX(), eleHeight + point.getY());
         eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);

        // Copy the element screenshot to disk
        File screenshotLocation = new File(MediaUtils.parseImagePath + userName + ".png");
        FileUtils.copyFile(screenshot, screenshotLocation);
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

    public static void sleepTimes(WebDriver driver,String userName) throws Exception {
        WebElement listAll = driver.findElement(By.xpath("//*[@id=\"listall\"]/center"));
        String addPoints = listAll.getText();
        log.info(userName + "添加分数+等待时长：：" + addPoints);
        addPoints = addPoints.substring(addPoints.lastIndexOf("Points:") + 7, addPoints.lastIndexOf("View"))
            .replace(" ", "").trim();
        log.info(addPoints);
        if (addPoints.contains("Timer:")) {
            addPoints = addPoints.split("/")[1];
            int anInt = Integer.parseInt(addPoints + "000") + 4000;
            log.info(userName + "sleep时长：：" + anInt);
            Thread.sleep(anInt);
        } else {
            sleepTimes(addPoints);
        }
    }

    public static void sleepTimes(String points) throws Exception {
        int anInt = Integer.parseInt(points);
        int sec = 45000;
        for (int i = 3; i < 36; i++) {
            sec = sec + 4000;
            if (anInt == i) {
                log.info("sleep : " + sec);
                Thread.sleep(sec);
                break;
            }
        }

    }
}