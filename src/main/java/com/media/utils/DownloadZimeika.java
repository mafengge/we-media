package com.media.utils;/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.media.bean.ZimeikaBean;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.ba;
import com.teamdev.jxbrowser.chromium.events.DownloadEvent;
import com.teamdev.jxbrowser.chromium.events.DownloadListener;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The sample demonstrates how to handle file download. To cancel download you must return {@code false} from the {@link
 * DownloadHandler#allowDownload(com.teamdev.jxbrowser.chromium.DownloadItem)} method. To listed for download update
 * events you can register your own {@link com.teamdev.jxbrowser.chromium.events.DownloadListener}.
 */
@Slf4j
public class DownloadZimeika {

    static {
        try {
            Field e = ba.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = ba.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public static void getDownloadUrl(ZimeikaBean zimeikaBean, WebDriver driver, String url) {
        try {
            driver.get(url);
            driver.findElement(org.openqa.selenium.By.id("d_parser_video")).click();
            String href = driver.findElement(By.className("btn-primary")).getAttribute("href");
            log.info(href);
            zimeikaBean.setVideoUrl(href);
            MediaFileUtils.writeFile(MediaUtils.zimekaInfoPath, JsonUtil.toJson(zimeikaBean, true) + ",");
            // + "--" + zimeikaBean.getAuthor()
            DownloadZimeika.downloadVideo(MediaUtils.zimeikaVideoPath, zimeikaBean.getVideoTitle(), href);
        } catch (Exception e) {
            log.info("获取自媒咖视频下载地址报错：" + e.getMessage());
        }
    }

    public static void downloadVideo(String videopath, String videoName, String videoUrl) {
        try {
            Browser browser = new Browser();
            browser.setDownloadHandler(new DownloadHandler() {
                @Override
                public boolean allowDownload(DownloadItem download) {
                    download.setDestinationFile(new File(videopath + videoName + ".mp4"));
                    download.addDownloadListener(new DownloadListener() {
                        @Override
                        public void onDownloadUpdated(DownloadEvent event) {
                            DownloadItem download = event.getDownloadItem();
                            if (download.isCompleted()) {
                                log.info("Download is completed!");
                                /*new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        VideoUtils.shearVideo(videopath + videoName + ".mp4");
                                    }
                                }).start();*/
                                browser.dispose();
                            }
                        }
                    });
                    log.info("Destination file: " +
                        download.getDestinationFile().getAbsolutePath());
                    return true;
                }
            });

            browser.loadURL(videoUrl);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        downloadVideo("D:\\", "kkk",
            "http://www.itono.cn/vip/xlyy.php?url=https://v.youku.com/v_show/id_XMzg0MzM2NDM2NA==.html?spm=a2h0k.11417342.soresults.dposter");
        /*WebDriver driver = new EventFiringWebDriver(new ChromeDriver()).register(new DriverListener());
        driver.get("http://zimeika.com/video/detail/xigua.html?id=6860278");//打开指定的网站
        driver.findElement(org.openqa.selenium.By.id("d_parser_video")).click();
        Thread.sleep(4000);
        String href = driver.findElement(org.openqa.selenium.By.className("btn-primary")).getAttribute("href");
        log.info(driver.findElement(org.openqa.selenium.By.className("btn-primary")).getText());
        log.info(driver.findElement(org.openqa.selenium.By.className("btn-primary")).getAttribute("href"));*/
    }
}
