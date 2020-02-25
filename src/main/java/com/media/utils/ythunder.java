package com.media.utils;

import static com.media.youtube.consumer.UploadVideo.proxySwitch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

@Slf4j
public class ythunder {

    public static void main(String[] args) throws InterruptedException {
        //driverHandlerYTH("413942291@qq.com","maniqiu5");
        //runYTH();
        addTask("mafengge5@163.com","maniqiu5","wYFDjeUcHeQ", "[抖音比基尼]美女跳舞、热门街拍、福利视频合集|亚洲TV.Ep.179");
    }

    public static void runYTH() throws InterruptedException {
        /*startThread("mafengge3@163.com", "maniqiu5", "Thread1");
        Thread.sleep(120000);
        startThread("mafengge4@163.com", "maniqiu5", "Thread2");
        Thread.sleep(120000);*/
        startThread("mafengge5@163.com", "maniqiu5", "Thread3");
        Thread.sleep(120000);
        startThread("mafengge6@163.com", "maniqiu5", "Thread4");
        Thread.sleep(120000);
        startThread("mafengge7@163.com", "maniqiu5", "Thread5");
        Thread.sleep(120000);
        startThread("mafengge8@163.com", "maniqiu5", "Thread6");
        Thread.sleep(120000);
        startThread("mafengge9@163.com", "maniqiu5", "Thread7");
        Thread.sleep(120000);
        startThread("mafengge12@163.com", "maniqiu", "Thread8");
    }

    public static void startThread(String userName, String passWord, String threadName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebDriver driver = null;
                try {
                    driver = driverHandlerYTH(userName, passWord, threadName);
                } catch (InterruptedException e) {
                    System.out.println(("线程页报错" + userName + e));
                    driver.quit();
                    try {
                        Thread.sleep(6000);
                        driverHandlerYTH(userName, passWord, threadName);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static WebDriver driverHandlerYTH(String userName, String passWord, String threadName)
        throws InterruptedException {
        System.out.println(userName + " ChromeDriver Start!");

        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
        /*options.addArguments("--disable-plugins");
        */
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            /*options.addArguments("--disable-plugins");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            //options.addArguments("no-sandbox");
            options.addArguments("--disable-infobars");
            options.addArguments("disable-dev-shm-usage");
            options.addArguments("start-maximized");
            options.addArguments("start-fullscreen");

            options.addArguments("incognito");
            options.addArguments("ignore-certificate-errors");*/
            //options.addArguments("--headless");
            //System.setProperty("webdriver.chrome.driver", "/opt/google/chromedriver/chromedriver");
            //System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
            //driver = new EventFiringWebDriver(new ChromeDriver(options)).register(new DriverListener(userName, passWord, 1));
            System.out.println(00);
            driver = new ChromeDriver(options);

            System.out.println(11);
            driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
            driver.get("https://ythunder.com/");
            System.out.println(1010);
            Thread.sleep(10000);
            driver.findElement(By.xpath("/html/body/section/section/header/div/ul/li[2]/span")).click();
            WebElement loginUserName = driver.findElement(By.xpath("//*[@id=\"loginUsername\"]"));
            WebElement loginPassword = driver.findElement(By.xpath("//*[@id=\"loginPassword\"]"));
            loginUserName.sendKeys(userName);
            loginPassword.sendKeys(passWord);
            driver.findElement(By.xpath("//*[@id=\"loginBtn\"]")).click();
            System.out.println(1100);
            //System.out.println(driver.getPageSource());
            Thread.sleep(10000);
            driver.findElement(By.className("play_video")).click();
            //*[@id="nav"]/section/section/div/nav/ul/li[3]/a
            //driver.findElement(By.className("btn-rounded")).click();
            System.out.println(1101);
            Thread.sleep(10000);
            driver.findElement(By.className("add_icon")).click();
            Thread.sleep(10000);
            Set<String> winHandels = driver.getWindowHandles();
            List<String> it = new ArrayList<>(winHandels);
            driver.switchTo().window(it.get(1));
            driver.findElement(By.xpath("//*[@id=\"startBtn\"]")).click();
            System.out.println(123);
            Thread.sleep(10000);
            driver.switchTo().window(it.get(0));
            System.out.println(22);
            driver.findElement(By.className("add_icon")).click();
            Set<String> winHandels1 = driver.getWindowHandles();
            List<String> it1 = new ArrayList<>(winHandels1);
            driver.switchTo().window(it1.get(3));
            Thread.sleep(10000);
            driver.findElement(By.xpath("//*[@id=\"startBtn\"]")).click();
            Thread.sleep(10000);
            driver.switchTo().window(it.get(0));
            driver.findElement(By.className("add_icon")).click();
            Thread.sleep(10000);
            Set<String> winHandels2 = driver.getWindowHandles();
            List<String> it2 = new ArrayList<>(winHandels2);
            driver.switchTo().window(it2.get(5));
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"startBtn\"]")).click();
            Thread.sleep(1000);
            System.out.println(33);
            int aa = 1;
            while (true) {
                //https://ythunder.com/task/taskfinish
                //https://ythunder.com/task/taskstart
                Set<String> winHandels4 = driver.getWindowHandles();
                List<String> it4 = new ArrayList<>(winHandels4);
                if (it1.size() < 1) {
                    continue;
                }
                for (int i = 0; i < it4.size(); i++) {
                    WebDriver window = driver.switchTo().window(it4.get(i));
                    System.out.println(threadName + " ：： " + window.getCurrentUrl());
                    if (window.getCurrentUrl().contains("www.youtube.com")) {
                        try {
                            String js1 = "var elem2 = document.getElementById('content');"
                                + "elem2.parentNode.removeChild(elem2);  ";
                            //String js1 = "window.stop(); ";
                            ((JavascriptExecutor) window).executeScript(js1);
                        } catch (Exception e) {
                            //System.out.println(e);
                            //Thread.sleep(13000);
                        }
                    }
                }
                Thread.sleep(5000);
                aa++;
                System.out.println(threadName + "  " + aa);
                if (aa == 5000) {
                    driver.quit();
                    Thread.sleep(7200000);
                    driverHandlerYTH(userName, passWord, threadName);
                    aa = 1;
                }
            }
        } catch (Exception e) {
            System.out.println(("登录页报错" + userName + e));
            driver.quit();
            Thread.sleep(6000);
            driverHandlerYTH(userName, passWord, threadName);
        }
        return driver;
    }

    public static void addTask(String userName, String passWord, String url,String title)
        throws InterruptedException {
        System.out.println(userName + " ChromeDriver Start!");

        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            /*options.addArguments("--disable-plugins");
             */
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            System.setProperty("webdriver.chrome.driver", "/opt/google/chromedriver/chromedriver");
            //System.setProperty("webdriver.chrome.driver", "C:\\ChromedDriver\\chromedriver.exe");
            //driver = new EventFiringWebDriver(new ChromeDriver(options)).register(new DriverListener(userName, passWord, 1));
            System.out.println(00);
            driver = new ChromeDriver(options);

            System.out.println(11);
            driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
            driver.get("https://ythunder.com/");
            System.out.println(1010);
            Thread.sleep(10000);
            driver.findElement(By.xpath("/html/body/section/section/header/div/ul/li[2]/span")).click();
            WebElement loginUserName = driver.findElement(By.xpath("//*[@id=\"loginUsername\"]"));
            WebElement loginPassword = driver.findElement(By.xpath("//*[@id=\"loginPassword\"]"));
            loginUserName.sendKeys(userName);
            loginPassword.sendKeys(passWord);
            driver.findElement(By.xpath("//*[@id=\"loginBtn\"]")).click();
            System.out.println(1100);
            Thread.sleep(10000);
            //driver.findElement(By.xpath("//*[@id=\"nav\"]/section/section/div/div[1]/nav/ul/li[3]/a")).click();
            driver.get("https://ythunder.com/user/addtask");
            WebElement videoSrc = driver.findElement(By.xpath("//*[@id=\"videoSrc\"]"));
            WebElement second = driver.findElement(By.xpath("//*[@id=\"second\"]"));
            WebElement times = driver.findElement(By.xpath("//*[@id=\"times\"]"));
            url = "https://www.youtube.com/watch?v=" + url;
            int temp = (int) (100 * Math.random() + 50);
            videoSrc.sendKeys(url);
            WebElement desc = driver.findElement(By.xpath("//*[@id=\"desc\"]"));
            String jsString = "document.getElementById(\"desc\").removeAttribute(\"readonly\");";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(jsString);
            desc.sendKeys(title);
            Thread.sleep(10000);
            second.sendKeys(temp + "");
            times.sendKeys(temp + "");
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"videoMsg\"]/div[10]/div/p[2]")).click();
            System.out.println(1101);
            Thread.sleep(10000);
            driver.quit();
        } catch (Exception e) {
            System.out.println(("登录页报错" + userName + e));
            driver.quit();
        }
    }
}