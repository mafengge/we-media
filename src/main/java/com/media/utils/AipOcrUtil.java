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
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Slf4j
public class AipOcrUtil {

    //设置APPID/AK/SK guo
    /*public static final String APP_ID = "16104677";
    public static final String API_KEY = "tvrCZ6GUi1kaEtiIHSm76kbQ";
    public static final String SECRET_KEY = "Dtd0dL5tvjUgHA3XEykHsDL22MVVrAkg";*/

    //设置APPID/AK/SK ma
    public static final String APP_ID = "16237345";
    public static final String API_KEY = "gbNsTDiYQxTQGNtRg4WPcLTt";
    public static final String SECRET_KEY = "4DnQFRVmtEHhFtUk0cRDDsPxPldNKKoV";
    public static void runPoints() {
        startThread("505877502", "lp123456");
        startThread("guodongbin", "guodongbin1987");
        startThread("mafengge", "maniqiu5");
        startThread("mafengge1", "123456");
        startThread("mafengge2", "maniqiu5");
        startThread("mafengge10", "maniqiu5");

        startThread("xingyu", "123456");
        startThread("mafengge3", "maniqiu5");
        startThread("mafengge4", "maniqiu5");
        startThread("mafengge5", "maniqiu5");
        //startThread("mafengge6","maniqiu5");
        startThread("mafengge7", "maniqiu5");
        startThread("mafengge8", "maniqiu5");
        startThread("mafengge9", "maniqiu5");
        startThread("mafengge11", "maniqiu5");
        startThread("mafengge12", "maniqiu5");
        startThread("mafengge13", "maniqiu5");
        startThread("mafengge14", "maniqiu5");
        startThread("mafengge15", "maniqiu5");
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

    public static Integer getAipOcr(String userName) {
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
        val = val.replace("×", "*").replace("x", "*").replace("÷", "/").replace("÷", "/");
        log.info("计算数值：" + countVal(val));
        return countVal(val);
    }

    /**
     * 刷积分
     */
    public static void getPoints(String userName, String passWord) {
        try {
            WebDriver driver = driverHandler(userName, passWord);
            while (true) {
                try {
                    WebElement currentPionts = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]"));
                    String oldPoints = currentPionts.getText();
                    WebElement followbutton = driver.findElement(By.className("followbutton"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", followbutton);
                    Set<String> winHandels = driver.getWindowHandles();
                    List<String> it = new ArrayList<>(winHandels);
                    if (null != it && it.size() > 2) {
                        throw new Exception("窗口大于2");
                    }
                    driver.switchTo().window(it.get(1));
                    String js1 = "window.stop(); ";
                    ((JavascriptExecutor) driver).executeScript(js1);
                    driver.switchTo().window(it.get(0));
                    //根据分数sleep时长
                    sleepTimes(driver);
                    String newPoints = driver.findElement(By.xpath("//*[@id=\"currentpoints\"]")).getText();
                    log.info(userName + ": 当前分数：" + newPoints);
                    if (oldPoints.equals(newPoints)) {
                        driver.navigate().refresh();
                    }
                } catch (Exception e1) {
                    log.error(userName + "循环中发生异常", e1);
                    driver.quit();
                    driver = driverHandler(userName, passWord);
                    Thread.sleep(10000);
                }
            }
        } catch (Exception e) {
            log.error("getPoints::" + userName, e);
            getPoints(userName, passWord);
        }
    }

    public static WebDriver driverHandler(String userName, String passWord) {
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
        System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
            /*WebDriver driver = new EventFiringWebDriver(new ChromeDriver(options))
                .register(new DriverListener(userName, passWord,1));*/
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
            codeSubmit(driver, userName, passWord);
        } catch (Exception e) {
            log.error("登录页报错" + userName, e);
            driver.quit();
            driver = new ChromeDriver(options);
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
            for (int i=0;i<2;i++){
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

    public static void codeSubmit(WebDriver driver, String userName, String passWord) throws Exception {
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"captcha\"]/table[1]/tbody/tr/td/img"));
        //截图
        screenShots(driver, userName);
        Thread.sleep(1000);
        //百度识图 + 计算数值
        Integer aipOcr = getAipOcr(userName);
        driver.findElement(By.name("answer")).sendKeys(String.valueOf(aipOcr));
        WebElement element = driver
            .findElement(By.xpath("//*[@id=\"captcha\"]/table[2]/tbody/tr/td/input[2]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
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
            //    eleWidth + point.getX(), eleHeight + point.getY());
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


    public static void sleepTimes(WebDriver driver) throws Exception {
        WebElement listAll = driver.findElement(By.xpath("//*[@id=\"listall\"]/center"));
        String addPoints = listAll.getText();
        log.info("添加分数+等待时长：：" + addPoints);
        addPoints = addPoints.substring(addPoints.indexOf("Points:") + 7, addPoints.indexOf("View"))
            .replace(" ", "").trim();
        log.info(addPoints);
        if (addPoints.contains("Timer:")) {
            addPoints = addPoints.split("/")[1];
            int anInt = Integer.parseInt(addPoints + "000") + 4000;
            log.info("sleep时长：：" + anInt);
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