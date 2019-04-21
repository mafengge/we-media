package com.media.utils;/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ba;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;

/**
 * This sample demonstrates how to load custom HTML string into
 * Browser component and display it.
 */
public class GetHTMLSample {
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
    public static void main(String[] args) {
        DownloadZimeika.downloadVideo("D:\\","kk","https://www.ixigua.com/a6681142479619621379/#mid=65105036458");
        /*Browser browser = new Browser();

        browser.loadURL("http://zimeika.com/video/detail/xigua.html?id=6772810");
        System.out.println(browser.getHTML());
        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                        System.out.println(0);
                    Browser browser1 = event.getBrowser();
                    DOMDocument document = browser1.getDocument();
                        DOMElement d_parser_video = document.findElement(By.id("source_url"));
                        String xiUrl = d_parser_video.getInnerHTML();
                        browser1.loadURL(xiUrl);
                        browser1.addLoadListener(new LoadAdapter() {
                            @Override
                            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                                DOMElement element = event.getBrowser().getDocument()
                                    .findElement(By.tagName("source"));
                                String src = element.getAttribute("src");
                                System.out.println(src);
                            }
                        });
                }
            }
        });*/
    }
}
