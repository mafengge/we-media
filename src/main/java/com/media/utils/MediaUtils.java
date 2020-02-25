package com.media.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MediaUtils {

    public static final String zimekaInfoPath = "D:" + File.separator + "zimeika" + File.separator + "info.txt";
    //MediaUtils.getCurrDate()
    public static final String zimeikaVideoPath = /*"/root/youtube/zimeika/";*/
        "D:" + File.separator + "zimeika" + File.separator + "video" + File.separator + "2019-04-28"
            + File.separator;

    //public static final String parseImagePath = "/root/youtube/";
    public static final String parseImagePath = "D://";
    public static final String ffmpegPath = "D:/ffmpeg-4.1.1-win64-static/bin/ffmpeg.exe";

    public static String getCurrDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(now);
        return date;
    }

    public static void main(String[] args) {
        final Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                log.info("t1");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        final Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("t2");
            }
        });
        Thread t3 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("t3");
            }
        });
        t3.start();
        t2.start();
        t1.start();
    }

}
