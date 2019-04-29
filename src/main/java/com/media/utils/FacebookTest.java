package com.media.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FacebookTest {

    public static void main(String[] args) {
        ChromeOptions option = new ChromeOptions();
        //后台运行
        option.addArguments("headless");
        //取消"Chrome正在受到自动软件的控制"提示
        option.addArguments("disable-infobars");
        System.setProperty("webdriver.chrome.driver", "/Users/guodongbin/Work/chromedriver");
        WebDriver webDriver = new ChromeDriver(option);
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        List<String> ids = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        try {
            String url = "https://www.facebook.com/pg/djy.news/posts/?ref=page_internal";
            webDriver.get(url);
            boolean run = true;
            int size = 0;
            long page = 10000;
            while (run) {
                page = page + page;
                WebElement div = ((ChromeDriver) webDriver).findElementById("pagelet_timeline_main_column");
                List<WebElement> divElements = div.findElements(By.cssSelector("._5pcr.userContentWrapper"));
                if (size == divElements.size()) {
                    break;
                }
                for (WebElement webElement : divElements) {
                    WebElement element = webElement.findElement(By.tagName("abbr"));
                    String date = element.getAttribute("title");
                    if (!dates.contains(date)) {
                        dates.add(date);
                        System.out.print("发帖时间：" + date);
                    }
                    WebElement messageElement = webElement.findElement(By.className("userContent"));
                    String id = messageElement.getAttribute("id");
                    if (!ids.contains(id)) {
                        ids.add(id);
                        System.out.print("帖子内容：" + messageElement.getText());
                        System.out.println("");
                    }
                }
                ((JavascriptExecutor) webDriver).executeScript("scroll(0," + page + ")");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        webDriver.close();
    }
}