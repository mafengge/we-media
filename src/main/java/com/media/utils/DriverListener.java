package com.media.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

@Slf4j
@Data
public class DriverListener implements WebDriverEventListener {

    private String userName;
    private String passWord;
    private Integer err;

    public DriverListener(String userName, String passWord, Integer err) {
        this.userName = userName;
        this.passWord = passWord;
        this.err = err;
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        System.out.println("转向前URL: '" + url + "'");
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        System.out.println("转向后URL:'" + url + "'");
    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        //before any changes made
        System.out.println("值: " + element.toString()
            + " 在任何更改之前");
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver) {
        System.out.println("元素值更改为: " + element.toString());
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        System.out.println("试图单击对象: " + element.toString());
    }
    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        System.out.println("单击对象: " + element.toString());
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        //Navigating back to previous page
        System.out.println("返回到前一页");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        //Navigated back to previous page
        System.out.println("返回到后一页");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        //Navigating forward to next page
        System.out.println("导航到前一页");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        //Navigated forward to next page
        System.out.println("导航到下一页");
    }

    @Override
    public void onException(Throwable error, WebDriver driver) {
        //Exception occured
        try {
            if (error instanceof NoSuchElementException) {
                System.out.println("异常类中NoSuch用户名密码：" + userName + "  " + passWord);
            } else {
                //driver.quit();
                //driver.close();
                /*err ++;
                if (err == 10) {
                    driver.quit();
                } else {
                    AipOcrUtil.getPoints(userName, passWord,err);
                }*/
                //AipOcrUtil.getPoints(userName, passWord);
                System.out.println("异常类中用户名密码：" + userName + "  " + passWord);
            }
        } catch (Exception e) {
            log.error("异常监听中发生异常");
            e.printStackTrace();
        }
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println("试图寻找对象 : " + by.toString());
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println("找到对象 : " + by.toString());
    }

    /*
     * non overridden methods of WebListener class
     */
    @Override
    public void beforeScript(String script, WebDriver driver) {
        System.out.println("执行脚本前");
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        System.out.println("执行脚本后");
    }

    @Override
    public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
        // TODO Auto-generated method stub
        System.out.println(arg0 + "变值后： " + arg2);
    }

    @Override
    public void afterNavigateRefresh(WebDriver arg0) {
        // TODO Auto-generated method stub
        System.out.println("导航后刷新");
    }

    @Override
    public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
        // TODO Auto-generated method stub
        System.out.println("在更改值之前：" + arg0 + " 更改值之后: " + arg2);
    }

    @Override
    public void beforeNavigateRefresh(WebDriver arg0) {
        // TODO Auto-generated method stub
        System.out.println("导航刷新前：");
    }
}