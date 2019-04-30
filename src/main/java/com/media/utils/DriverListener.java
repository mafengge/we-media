package com.media.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
@Slf4j
public class DriverListener implements WebDriverEventListener {

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        log.info("转向前URL: '" + url + "'");
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        log.info("转向后URL:'" + url + "'");
    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        //before any changes made
        log.info("值: " + element.toString()
            + " 在任何更改之前");
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver) {
        log.info("元素值更改为: " + element.toString());
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        log.info("试图单击对象: " + element.toString());
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        log.info("单击对象: " + element.toString());
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        //Navigating back to previous page
        log.info("返回到前一页");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        //Navigated back to previous page
        log.info("返回到后一页");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        //Navigating forward to next page
        log.info("导航到前一页");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        //Navigated forward to next page
        log.info("导航到下一页");
    }

    @Override
    public void onException(Throwable error, WebDriver driver) {
        //Exception occured
        log.info("发生异常: " + error);
        try {
            driver.get("https://www.youlikehits.com/youtubenew2.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        log.info("试图寻找对象 : " + by.toString());
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        log.info("找到对象 : " + by.toString());
    }

    /*
     * non overridden methods of WebListener class
     */
    @Override
    public void beforeScript(String script, WebDriver driver) {
        log.info("执行脚本前");
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        log.info("执行脚本后");
    }

    @Override
    public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
        // TODO Auto-generated method stub
        log.info(arg0 + "变值后： " + arg2);
    }

    @Override
    public void afterNavigateRefresh(WebDriver arg0) {
        // TODO Auto-generated method stub
        log.info("导航后刷新");
    }

    @Override
    public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
        // TODO Auto-generated method stub
        log.info("在更改值之前：" + arg0 + " 更改值之后: " + arg2);
    }

    @Override
    public void beforeNavigateRefresh(WebDriver arg0) {
        // TODO Auto-generated method stub
        log.info("导航刷新前：");
    }
}