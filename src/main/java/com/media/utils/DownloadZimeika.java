package com.media.utils;/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import static com.media.youtube.consumer.UploadVideo.proxySwitch;

import com.media.bean.ZimeikaBean;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.ba;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.DownloadEvent;
import com.teamdev.jxbrowser.chromium.events.DownloadListener;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * The sample demonstrates how to handle file download. To cancel download you must return {@code false} from the {@link
 * DownloadHandler#allowDownload(com.teamdev.jxbrowser.chromium.DownloadItem)} method. To listed for download update
 * events you can register your own {@link com.teamdev.jxbrowser.chromium.events.DownloadListener}.
 */
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

    public static void getDownloadUrl(ZimeikaBean zimeikaBean, String url) {
        try {
            Browser browser = new Browser();
            browser.addLoadListener(new LoadAdapter() {
                @Override
                public void onFinishLoadingFrame(FinishLoadingEvent event) {
                    DOMElement element = event.getBrowser().getDocument()
                        .findElement(By.tagName("source"));
                    if (null != element) {
                        String src = element.getAttribute("src");
                        zimeikaBean.setVideoUrl(src);
                        System.out.println(src);
                        FileUtils.writeFile(MediaUtils.zimekaInfoPath, JsonUtil.toJson(zimeikaBean, true) + ",");
                        browser.dispose();
                    }
                }
            });
            browser.loadURL(url);
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("获取自媒咖视频下载地址报错：" + e.getMessage());
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
                                System.out.println("Download is completed!");
                            }
                        }
                    });
                    System.out.println("Destination file: " +
                        download.getDestinationFile().getAbsolutePath());
                    return true;
                }
            });

            browser.loadURL(videoUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
